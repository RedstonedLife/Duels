package com.redstonedlife.duels.plugin.userstorage;

import com.redstonedlife.duels.plugin.IDuels;
import com.redstonedlife.duels.plugin.config.DuelsConfiguration;
import com.redstonedlife.duels.plugin.config.DuelsUserConfiguration;
import com.redstonedlife.duels.plugin.config.holders.UserConfigHolder;
import com.redstonedlife.duels.plugin.interfaces.config.file.IConf;
import com.redstonedlife.duels.plugin.user.PlayerExtension;
import org.bukkit.entity.Player;

import java.io.File;

public abstract class UserData extends PlayerExtension implements IConf {
    protected final transient IDuels duels;
    private final DuelsConfiguration config;
    private UserConfigHolder holder;

    protected UserData(final Player base, final IDuels duels) {
        super(base);
        this.duels = duels;
        final File folder = new File(duels.getDataFolder(), "userdata");
        if(!folder.exists() && !folder.mkdirs())
            throw new RuntimeException("Unable to create userdata folder!");

        config = new DuelsUserConfiguration(base.getName(), base.getUniqueId(), new File(folder, base.getUniqueId() + ".yml"));
        config.setSaveHook(() -> {
            config.setRootHolder(UserConfigHolder.class, holder);
        });
        reloadConfig();


    }

    public final void reset() {
        config.blockingSave();
        if (!config.getFile().delete()) {
            duels.getLogger().warning("Unable to delete data file for " + config.getFile().getName());
        }
    }

}
