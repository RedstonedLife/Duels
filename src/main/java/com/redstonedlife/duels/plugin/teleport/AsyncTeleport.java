package com.redstonedlife.duels.plugin.teleport;

import com.redstonedlife.duels.api.IAsyncTeleport;
import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.plugin.IDuels;
import com.redstonedlife.duels.plugin.utils.DateUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
                teleportOwner.sendMessage(tl("teleporting", target.getLocation().getWorld().getName(), target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ()));
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
