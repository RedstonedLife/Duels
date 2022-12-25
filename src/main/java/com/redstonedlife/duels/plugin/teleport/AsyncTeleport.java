package com.redstonedlife.duels.plugin.teleport;

import com.redstonedlife.duels.api.IAsyncTeleport;
import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.api.events.teleport.PreTeleportEvent;
import com.redstonedlife.duels.plugin.IDuels;
import com.redstonedlife.duels.plugin.utils.DateUtil;
import com.redstonedlife.duels.plugin.utils.LocationUtil;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.redstonedlife.duels.plugin.I18n.tl;

public class AsyncTeleport implements IAsyncTeleport {
    private final IUser teleportOwner;
    private final IDuels duels;
    private AsyncTimedTeleport timedTeleport;

    private TeleportType tpType;

    public AsyncTeleport(final IUser user, final IDuels duels) {
        this.teleportOwner = user;
        this.duels = duels;
        tpType = TeleportType.NORMAL;
    }

    public void cooldown(final boolean check) throws Throwable {
        final CompletableFuture<Boolean> exceptionFuture = new CompletableFuture<>();
        if (cooldown(check, exceptionFuture)) {
            try {
                exceptionFuture.get();
            } catch (final ExecutionException e) {
                throw e.getCause();
            }
        }
    }

    public boolean cooldown(final boolean check, final CompletableFuture<Boolean> future) {
        final Calendar time = new GregorianCalendar();
        if (teleportOwner.getLastTeleportTimestamp() > 0) {
            // Take the current time, and remove the delay from it.
            final double cooldown = duels.getSettings().getTeleportCooldown();
            final Calendar earliestTime = new GregorianCalendar();
            earliestTime.add(Calendar.SECOND, -(int) cooldown);
            earliestTime.add(Calendar.MILLISECOND, -(int) ((cooldown * 1000.0) % 1000.0));
            // This value contains the most recent time a teleportPlayer could have been used that would allow another use.
            final long earliestLong = earliestTime.getTimeInMillis();

            // When was the last teleportPlayer used?
            final long lastTime = teleportOwner.getLastTeleportTimestamp();

            if (lastTime > time.getTimeInMillis()) {
                // This is to make sure time didn't get messed up on last teleportPlayer use.
                // If this happens, let's give the user the benifit of the doubt.
                teleportOwner.setLastTeleportTimestamp(time.getTimeInMillis());
                return false;
            } else if (lastTime > earliestLong
                    && cooldownApplies()) {
                time.setTimeInMillis(lastTime);
                time.add(Calendar.SECOND, (int) cooldown);
                time.add(Calendar.MILLISECOND, (int) ((cooldown * 1000.0) % 1000.0));
                future.completeExceptionally(new Exception(tl(null, "timeBeforeTeleport", DateUtil.formatDateDiff(time.getTimeInMillis()))));
                return true;
            }
        }
        // if justCheck is set, don't update lastTeleport; we're just checking
        if (!check) {
            teleportOwner.setLastTeleportTimestamp(time.getTimeInMillis());
        }
        return false;
    }

    private boolean cooldownApplies() {
        boolean applies = true;
        final String globalBypassPerm = "duels.teleport.cooldown.bypass";
        switch (tpType) {
            case NORMAL:
                applies = !teleportOwner.isAuthorized(globalBypassPerm);
                break;
            case BACK:
                applies = !(teleportOwner.isAuthorized(globalBypassPerm) &&
                        teleportOwner.isAuthorized("duels.teleport.cooldown.bypass.back"));
                break;
            case TPA:
                applies = !(teleportOwner.isAuthorized(globalBypassPerm) &&
                        teleportOwner.isAuthorized("duels.teleport.cooldown.bypass.tpa"));
                break;
        }
        return applies;
    }

    private void warnUser(final IUser user, final double delay) {
        final Calendar c = new GregorianCalendar();
        c.add(Calendar.SECOND, (int) delay);
        c.add(Calendar.MILLISECOND, (int) ((delay * 1000.0) % 1000.0));
        user.sendMessage(tl(null,"dontMoveMessage", DateUtil.formatDateDiff(c.getTimeInMillis())));
    }

    void respawnNow(final IUser teleportee, final PlayerTeleportEvent.TeleportCause cause, final CompletableFuture<Boolean> future) {
        final Player player = teleportee.getBase();
        PaperLib.getBedSpawnLocationAsync(player, true).thenAccept(location -> {
            if (location != null) {
                nowAsync(teleportee, new LocationTarget(location), cause, future);
            } else {
                if (duels.getSettings().isDebug()) {
                    duels.getLogger().info("Could not find bed spawn, forcing respawn event.");
                }
                final PlayerRespawnEvent pre = new PlayerRespawnEvent(player, player.getWorld().getSpawnLocation(), false);
                duels.getServer().getPluginManager().callEvent(pre);
                nowAsync(teleportee, new LocationTarget(pre.getRespawnLocation()), cause, future);
            }
        }).exceptionally(th -> {
            future.completeExceptionally(th);
            return null;
        });
    }

    protected void nowAsync(final IUser teleportee, final ITarget target, final PlayerTeleportEvent.TeleportCause cause, final CompletableFuture<Boolean> future) {
        future.cancel(false);

        final PreTeleportEvent event = new PreTeleportEvent(teleportee, cause, target);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            future.complete(false);
            return;
        }
        teleportee.setLastLocation();
        final Location targetLoc = target.getLocation();
        if (duels.getSettings().isTeleportSafetyEnabled() && LocationUtil.isBlockOutsideWorldBorder(targetLoc.getWorld(), targetLoc.getBlockX(), targetLoc.getBlockZ())) {
            targetLoc.setX(LocationUtil.getXInsideWorldBorder(targetLoc.getWorld(), targetLoc.getBlockX()));
            targetLoc.setZ(LocationUtil.getZInsideWorldBorder(targetLoc.getWorld(), targetLoc.getBlockZ()));
        }
        PaperLib.getChunkAtAsync(targetLoc.getWorld(), targetLoc.getBlockX() >> 4, targetLoc.getBlockZ() >> 4, true, true).thenAccept(chunk -> {
            Location loc = targetLoc;
            if (LocationUtil.isBlockUnsafeForUser(duels, teleportee, chunk.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                if (duels.getSettings().isTeleportSafetyEnabled()) {
                    if (duels.getSettings().isForceDisableTeleportSafety()) {
                        //The chunk we're teleporting to is 100% going to be loaded here, no need to teleport async.
                        teleportee.getBase().teleport(loc, cause);
                    } else {
                        try {
                            //There's a chance the safer location is outside the loaded chunk so still teleport async here.
                            PaperLib.teleportAsync(teleportee.getBase(), LocationUtil.getSafeDestination(duels, teleportee, loc), cause);
                        } catch (final Exception e) {
                            future.completeExceptionally(e);
                            return;
                        }
                    }
                } else {
                    future.completeExceptionally(new Exception(tl(null,"unsafeTeleportDestination", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
                    return;
                }
            } else {
                if (duels.getSettings().isForceDisableTeleportSafety()) {
                    //The chunk we're teleporting to is 100% going to be loaded here, no need to teleport async.
                    teleportee.getBase().teleport(loc, cause);
                } else {
                    if (duels.getSettings().isTeleportToCenterLocation()) {
                        loc = LocationUtil.getRoundedDestination(loc);
                    }
                    //There's a *small* chance the rounded destination produces a location outside the loaded chunk so still teleport async here.
                    PaperLib.teleportAsync(teleportee.getBase(), loc, cause);
                }
            }
            future.complete(true);
        }).exceptionally(th -> {
            future.completeExceptionally(th);
            return null;
        });
    }


    @Override
    public void now(final Location loc, final boolean cooldown, final PlayerTeleportEvent.TeleportCause cause, final CompletableFuture<Boolean> future) {
        if (cooldown && cooldown(false, future)) {
            return;
        }
        final ITarget target = new LocationTarget(loc);
        nowAsync(teleportOwner, target, cause, future);
    }

    @Override
    public void now(final Player entity, final boolean cooldown, final PlayerTeleportEvent.TeleportCause cause, final CompletableFuture<Boolean> future) {
        if (cooldown && cooldown(false, future)) {
            future.complete(false);
            return;
        }
        final ITarget target = new PlayerTarget(entity);
        nowAsync(teleportOwner, target, cause, future);
        future.thenAccept(success -> {
            if (success) {
                teleportOwner.sendMessage(tl(null,"teleporting", target.getLocation().getWorld().getName(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
            }
        });
    }

    @Override
    public void nowUnsafe(Location loc, PlayerTeleportEvent.TeleportCause cause, CompletableFuture<Boolean> future) {

    }

    @Override
    public void back(CompletableFuture<Boolean> future) {

    }
}
