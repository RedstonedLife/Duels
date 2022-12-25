package com.redstonedlife.duels.plugin.teleport;

import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.plugin.IDuels;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.redstonedlife.duels.plugin.I18n.tl;

public class AsyncTimedTeleport implements Runnable {
    private static final double MOVE_CONSTANT = 0.3;
    private final IUser teleportOwner;
    private final IDuels duels;
    private final AsyncTeleport teleport;
    private final UUID timer_teleportee;
    private final long timer_started; // time this task was initiated
    private final long timer_delay; // how long to delay the teleportPlayer
    private final CompletableFuture<Boolean> parentFuture;
    // note that I initially stored a clone of the location for reference, but...
    // when comparing locations, I got incorrect mismatches (rounding errors, looked like)
    // so, the X/Y/Z values are stored instead and rounded off
    private final long timer_initX;
    private final long timer_initY;
    private final long timer_initZ;
    private final ITarget timer_teleportTarget;
    private final boolean timer_respawn;
    private final boolean timer_canMove;
    private final TeleportCause timer_cause;
    private int timer_task;
    private double timer_health;

    AsyncTimedTeleport(final IUser user, final IDuels duels, final AsyncTeleport teleport, final long delay, final IUser teleportUser, final ITarget target, final TeleportCause cause, final boolean respawn) {
        this(user, duels, teleport, delay, null, teleportUser, target, cause, respawn);
    }

    AsyncTimedTeleport(final IUser user, final IDuels duels, final AsyncTeleport teleport, final long delay, final CompletableFuture<Boolean> future, final IUser teleportUser, final ITarget target, final TeleportCause cause, final boolean respawn) {
        this.teleportOwner = user;
        this.duels = duels;
        this.teleport = teleport;
        this.timer_started = System.currentTimeMillis();
        this.timer_delay = delay;
        this.timer_health = teleportUser.getBase().getHealth();
        this.timer_initX = Math.round(teleportUser.getBase().getLocation().getX() * MOVE_CONSTANT);
        this.timer_initY = Math.round(teleportUser.getBase().getLocation().getY() * MOVE_CONSTANT);
        this.timer_initZ = Math.round(teleportUser.getBase().getLocation().getZ() * MOVE_CONSTANT);
        this.timer_teleportee = teleportUser.getBase().getUniqueId();
        this.timer_teleportTarget = target;
        this.timer_cause = cause;
        this.timer_respawn = respawn;
        this.timer_canMove = user.isAuthorized("essentials.teleport.timer.move");

        timer_task = duels.runTaskTimerAsynchronously(this, 20, 20).getTaskId();

        if (future != null) {
            this.parentFuture = future;
            return;
        }

        final CompletableFuture<Boolean> cFuture = new CompletableFuture<>();
        cFuture.exceptionally(e -> {
            duels.showError(teleportOwner.getSource(), e, "\\ teleport");
            return false;
        });
        this.parentFuture = cFuture;
    }

    @Override
    public void run() {

        if (teleportOwner == null || !teleportOwner.getBase().isOnline() || teleportOwner.getBase().getLocation() == null) {
            cancelTimer(false);
            return;
        }

        final IUser teleportUser = duels.getUser(this.timer_teleportee);

        if (teleportUser == null || !teleportUser.getBase().isOnline()) {
            cancelTimer(false);
            return;
        }

        final Location currLocation = teleportUser.getBase().getLocation();
        if (currLocation == null) {
            cancelTimer(false);
            return;
        }

        if (!timer_canMove && (Math.round(currLocation.getX() * MOVE_CONSTANT) != timer_initX || Math.round(currLocation.getY() * MOVE_CONSTANT) != timer_initY || Math.round(currLocation.getZ() * MOVE_CONSTANT) != timer_initZ || teleportUser.getBase().getHealth() < timer_health)) {
            // user moved, cancelTimer teleportPlayer
            cancelTimer(true);
            return;
        }

        class DelayedTeleportTask implements Runnable {
            @Override
            public void run() {

                timer_health = teleportUser.getBase().getHealth(); // in case user healed, then later gets injured
                final long now = System.currentTimeMillis();
                if (now > timer_started + timer_delay) {
                    try {
                        teleport.cooldown(false);
                    } catch (final Throwable ex) {
                        teleportOwner.sendMessage(tl(null,"cooldownWithMessage", ex.getMessage()));
                        if (teleportOwner != teleportUser) {
                            teleportUser.sendMessage(tl(null,"cooldownWithMessage", ex.getMessage()));
                        }
                    }
                    try {
                        cancelTimer(false);
                        teleportUser.sendMessage(tl(null,"teleportationCommencing"));

                        if (timer_respawn) {
                            teleport.respawnNow(teleportUser, timer_cause, parentFuture);
                        } else {
                            teleport.nowAsync(teleportUser, timer_teleportTarget, timer_cause, parentFuture);
                        }
                    } catch (final Exception ex) {
                        duels.showError(teleportOwner.getSource(), ex, "\\ teleport");
                    }
                }
            }
        }

        duels.scheduleSyncDelayedTask(new DelayedTeleportTask());
    }

    //If we need to cancelTimer a pending teleportPlayer call this method
    void cancelTimer(final boolean notifyUser) {
        if (timer_task == -1) {
            return;
        }
        try {
            duels.getServer().getScheduler().cancelTask(timer_task);
            if (notifyUser) {
                teleportOwner.sendMessage(tl(null,"pendingTeleportCancelled"));
                if (timer_teleportee != null && !timer_teleportee.equals(teleportOwner.getBase().getUniqueId())) {
                    duels.getUser(timer_teleportee).sendMessage(tl(null,"pendingTeleportCancelled"));
                }
            }
        } finally {
            timer_task = -1;
        }
    }
}
