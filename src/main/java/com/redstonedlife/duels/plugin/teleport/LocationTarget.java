package com.redstonedlife.duels.plugin.teleport;

import org.bukkit.Location;

public class LocationTarget implements ITarget {
    private final Location location;

    LocationTarget(final Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
