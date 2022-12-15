package com.redstonedlife.duels.plugin.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;

import java.util.Map;

public interface IDuelCommand {
    String getName();
    Map<String, String> getUsageStrings();
    void run(Server server, String commandLabel, Command cmd, String[] args) throws Exception;
    void run(Server server, CommandSource)
}
