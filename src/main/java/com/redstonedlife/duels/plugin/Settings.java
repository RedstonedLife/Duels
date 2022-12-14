package com.redstonedlife.duels.plugin;

import com.redstonedlife.duels.plugin.interfaces.ISettings;

public class Settings implements ISettings {
    private boolean debug = true;

    @Override
    public boolean isDebug() {
        return debug;
    }
}
