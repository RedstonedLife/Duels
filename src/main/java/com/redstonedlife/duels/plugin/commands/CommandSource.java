package com.redstonedlife.duels.plugin.commands;

import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.plugin.IDuels;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandSource {
    protected CommandSender sender;

    public CommandSource(final CommandSender base) {
        this.sender = base;
    }

    public final CommandSender getSender() {
        return sender;
    }

    public final Player getPlayer() {
        if(sender instanceof Player) {
            return (Player) sender;
        }
        return null;
    }

    public final IUser getUser(final IDuels duels) {
        if (sender instanceof Player) {
            return duels.getUser((Player) sender);
        }
        return null;
    }

    public final boolean isPlayer() {
        return sender instanceof Player;
    }

    public final CommandSender setSender(final CommandSender base) {
        return this.sender = base;
    }

    public void sendMessage(final String message) {
        if (!message.isEmpty()) {
            sender.sendMessage(message);
        }
    }

    public boolean isAuthorized(final String permission, final IDuels duels) {
        return !(sender instanceof Player) || Objects.requireNonNull(getUser(duels)).isAuthorized(permission);
    }

    public String getSelfSelector() {
        return sender instanceof Player ? Objects.requireNonNull(getPlayer()).getName() : "*";
    }

    public String getDisplayName() {
        return sender instanceof Player ? Objects.requireNonNull(getPlayer()).getDisplayName() : getSender().getName();
    }
}
