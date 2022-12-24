package com.redstonedlife.duels.plugin.config.holders;

import com.redstonedlife.duels.plugin.config.annotations.DeleteIfIncomplete;
import com.redstonedlife.duels.plugin.config.annotations.DeleteOnEmpty;
import com.redstonedlife.duels.plugin.config.entities.CommandCooldown;
import com.redstonedlife.duels.plugin.config.entities.LazyLocation;
import org.bukkit.Location;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class UserConfigHolder {
    private @MonotonicNonNull BigDecimal money;

    public BigDecimal money() {return money;}
    public void money(final BigDecimal value) {this.money = value;}

    private @MonotonicNonNull LazyLocation lastlocation;

    public LazyLocation lastLocation() {return this.lastlocation;}

    public void lastLocation(final Location value) {
        if (value == null || value.getWorld() == null) return;
        this.lastlocation = LazyLocation.fromLocation(value);
    }

    private @MonotonicNonNull LazyLocation logoutlocation;

    public LazyLocation logoutLocation() {return this.logoutlocation;}

    public void logoutLocation(final Location value) {
        if (value == null || value.getWorld() == null) return;
        this.logoutlocation = LazyLocation.fromLocation(value);
    }

    private @NonNull Timestamps timestamps = new Timestamps();

    public Timestamps timestamps() {
        return this.timestamps;
    }

    @ConfigSerializable
    public static class Timestamps {
        private long lastteleport = 0L;

        public long lastTeleport() {
            return this.lastteleport;
        }

        public void lastTeleport(final long value) {
            this.lastteleport = value;
        }

        private long lastheal = 0L;

        public long lastHeal() {
            return this.lastheal;
        }

        public void lastHeal(final long value) {
            this.lastheal = value;
        }

        private long mute = 0L;

        public long mute() {
            return this.mute;
        }

        public void mute(final long value) {
            this.mute = value;
        }

        private long jail = 0L;

        public long jail() {
            return this.jail;
        }

        public void jail(final long value) {
            this.jail = value;
        }

        private long onlinejail = 0L;

        public long onlineJail() {
            return this.onlinejail;
        }

        public void onlineJail(final long value) {
            this.onlinejail = value;
        }

        private long logout = 0L;

        public long logout() {
            return this.logout;
        }

        public void logout(final long value) {
            this.logout = value;
        }

        private long login = 0L;

        public long login() {
            return this.login;
        }

        public void login(final long value) {
            this.login = value;
        }

        @DeleteOnEmpty
        private @MonotonicNonNull Map<String, Long> kits;

        public Map<String, Long> kits() {
            if (this.kits == null) {
                this.kits = new HashMap<>();
            }
            return this.kits;
        }

        public void kits(final Map<String, Long> value) {
            this.kits = value;
        }

        @DeleteOnEmpty
        @DeleteIfIncomplete
        private @MonotonicNonNull List<CommandCooldown> commandCooldowns;

        public List<CommandCooldown> commandCooldowns() {
            if (this.commandCooldowns == null) {
                this.commandCooldowns = new ArrayList<>();
            }
            return this.commandCooldowns;
        }

        public void commandCooldowns(final List<CommandCooldown> value) {
            this.commandCooldowns = value;
        }
    }
}
