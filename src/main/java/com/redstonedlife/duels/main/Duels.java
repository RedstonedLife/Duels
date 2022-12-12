package com.redstonedlife.duels.main;

import org.bukkit.plugin.java.JavaPlugin;

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
}
