package com.redstonedlife.duels.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

public interface IAsyncTeleport {
    /**
     * Used to skip teleportPlayer delay when teleporting someone to a location or player.
     *
     * @param loc      - Where should the player end up
     * @param cooldown - If cooldown should be enforced
     * @param cause    - The reported teleportPlayer cause
     * @param future   - Future which is completed with the success status of the execution
     */
    void now(Location loc, boolean cooldown, PlayerTeleportEvent.TeleportCause cause, CompletableFuture<Boolean> future);

    /**
     * Used to skip teleportPlayer delay when teleporting someone to a location or player.
     *
     * @param entity   - Where should the player end up
     * @param cooldown - If cooldown should be enforced
     * @param cause    - The reported teleportPlayer cause
     * @param future   - Future which is completed with the success status of the execution
     */
    void now(Player entity, boolean cooldown, PlayerTeleportEvent.TeleportCause cause, CompletableFuture<Boolean> future);

    /**
     * Used to skip all safety checks while teleporting a player asynchronously.
     *
     * @param loc    - Where should the player end up
     * @param cause  - The reported teleportPlayer cause
     * @param future - Future which is completed with the success status of the execution
     */
    void nowUnsafe(Location loc, PlayerTeleportEvent.TeleportCause cause, CompletableFuture<Boolean> future);
    
    /**
     * Teleport wrapper used to handle throwing user home after a jail sentence
     *
     * @param future - Future which is completed with the success status of the execution
     */
    void back(CompletableFuture<Boolean> future);
}
