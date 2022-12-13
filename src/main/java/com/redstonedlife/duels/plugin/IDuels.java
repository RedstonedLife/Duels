package com.redstonedlife.duels.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;

public interface IDuels {

    void onReload();
    void initiateReload();

    InputStream getResource(String filename);
}
