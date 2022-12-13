package com.redstonedlife.duels.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class Duels extends JavaPlugin implements IDuels {

    private boolean isPluginSetup = false;

    @Override
    public void onEnable() {
        System.out.println("Duels plugin loaded successfully!!!");
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onReload() {
    }

    @Override
    public void initiateReload() {
    }

    public boolean isIsPluginSetup() {return isPluginSetup;}

    public void setIsPluginSetup(boolean isPluginSetup) {this.isPluginSetup = isPluginSetup;}

    @Override public InputStream getResource(@NotNull String filename) {return super.getResource(filename);}
}
