package com.redstonedlife.duels.plugin;

import com.redstonedlife.duels.plugin.config.file.ConfigFile;
import com.redstonedlife.duels.plugin.interfaces.ISettings;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Duels extends JavaPlugin implements IDuels {
    private static final Logger BUKKIT_LOGGER = Logger.getLogger("Duels");
    private static Logger LOGGER = null;
    private final int CONFIG_VERSION = 1;
    private boolean isPluginSetup = false;
    private ConfigFile config;


    @Override
    public void onEnable() {
        try {
            if(BUKKIT_LOGGER != super.getLogger())
                BUKKIT_LOGGER.setParent(super.getLogger());
            LOGGER = DuelsLogger.getLoggerProvider(this);
            DuelsLogger.updatePluginLogger(this);
        } catch(final Error ex) {
            handleCrash(ex);
            throw ex;
        }
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

    private void handleCrash(final Throwable exception) {
        final PluginManager pm = getServer().getPluginManager();
        LOGGER.log(Level.SEVERE, exception.toString());
        exception.printStackTrace();
        pm.registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.LOW)
            public void onPlayerJoin(final PlayerJoinEvent event) {
                event.getPlayer().sendMessage(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                "&cDuels failed to load, read the log file."
                        )
                );
            }
        }, this);
        for (final Player player : getOnlinePlayers()) {
            player.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            "&cDuels failed to load, read the log file."
                    )
            );
        }
        this.setEnabled(false);
    }

    private boolean isDuelsPlugin(Plugin plugin) {
        return plugin.getDescription().getMain().contains("com.redstonedlife.duels")
                || plugin.getDescription().getMain().contains("net.rdl.dlx");
    }

    public boolean isIsPluginSetup() {return isPluginSetup;}

    public void setIsPluginSetup(boolean isPluginSetup) {this.isPluginSetup = isPluginSetup;}

    public static Logger getWrappedLogger() {
        if (LOGGER != null) {
            return LOGGER;
        }

        return BUKKIT_LOGGER;
    }

    @Override public int getConfigVersion() {return CONFIG_VERSION;}

    @Override
    public Collection<Player> getOnlinePlayers() {
        return (Collection<Player>) getServer().getOnlinePlayers();
    }

    @Override
    public ISettings getSettings() {
        return null;
    }

    @Override public InputStream getResource(@NotNull String filename) {return super.getResource(filename);}
}
