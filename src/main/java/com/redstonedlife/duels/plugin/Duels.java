package com.redstonedlife.duels.plugin;

import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.plugin.interfaces.ISettings;
import com.redstonedlife.duels.plugin.interfaces.config.file.IConf;
import com.redstonedlife.duels.plugin.user.OfflinePlayer;
import com.redstonedlife.duels.plugin.user.User;
import com.redstonedlife.duels.plugin.userstorage.ModernUserMap;
import com.redstonedlife.duels.plugin.userstorage.UserMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.redstonedlife.duels.plugin.I18n.tl;

public class Duels extends JavaPlugin implements IDuels {
    private static final Logger BUKKIT_LOGGER = Logger.getLogger("Duels");
    private static Logger LOGGER = null;
    private final int CONFIG_VERSION = 1;
    private boolean isPluginSetup = false;
    private transient ISettings settings;
    private transient List<IConf> confList;
    private transient ExecuteTimer execTimer;
    private transient I18n i18n;
    @Deprecated
    private transient UserMap legacyUserMap;
    private transient ModernUserMap userMap;

    public boolean isDebug() {
        return true;
    }

    @Override
    public ISettings getSettings() {
        return settings;
    }

    @Override
    public void onEnable() {
        try {
            if(BUKKIT_LOGGER != super.getLogger())
                BUKKIT_LOGGER.setParent(super.getLogger());
            LOGGER = DuelsLogger.getLoggerProvider(this);
            DuelsLogger.updatePluginLogger(this);

            execTimer = new ExecuteTimer();
            execTimer.start();
            i18n = new I18n(this);
            i18n.onEnable();
            execTimer.mark("I18n1");

            // Check for Duplicate plugins the PluginManager loaded and check for Mismatching versions.
            final PluginManager pm = getServer().getPluginManager();
            for(final Plugin plugin : pm.getPlugins()) {
                if(plugin.getDescription().getName().startsWith("Duels") && !plugin.getDescription().getVersion().equals(this.getDescription().getVersion())) {
                    getLogger().warning(tl(null, "versionMismatch", plugin.getDescription().getName()));
                }
            }

            // Configuration.
            confList = new ArrayList<>();
            settings = new Settings(this);
            confList.add(settings);
            execTimer.mark("Settings");

            // UserMap
            userMap = new ModernUserMap(this);
            legacyUserMap = new UserMap(userMap);
            execTimer.mark("Init(Usermap)");

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

    @Deprecated
    @Override
    public User getUser(final Object base) {
        if (base instanceof Player) {
            return getUser((Player) base);
        }
        if (base instanceof org.bukkit.OfflinePlayer) {
            return getUser(((org.bukkit.OfflinePlayer) base).getUniqueId());
        }
        if (base instanceof UUID) {
            return getUser((UUID) base);
        }
        if (base instanceof String) {
            return getOfflineUser((String) base);
        }
        return null;
    }

    //This will return null if there is not a match.
    @Override
    public User getUser(final String base) {
        return getOfflineUser(base);
    }

    @Override
    public IUser getUser(final Player base) {
        return null;
    }

    //This will return null if there is not a match.
    @Override
    public User getUser(final UUID base) {
        return userMap.getUser(base);
    }

    @Override
    public User getOfflineUser(final String name) {
        final User user = userMap.getUser(name);
        if (user != null && user.getBase() instanceof OfflinePlayer) {
            //This code should attempt to use the last known name of a user, if Bukkit returns name as null.
            final String lastName = user.getLastAccountName();
            if (lastName != null) {
                ((OfflinePlayer) user.getBase()).setName(lastName);
            } else {
                ((OfflinePlayer) user.getBase()).setName(name);
            }
        }
        return user;
    }


    @Override public int getConfigVersion() {return CONFIG_VERSION;}

    @Override
    public Collection<Player> getOnlinePlayers() {
        return (Collection<Player>) getServer().getOnlinePlayers();
    }
    @Override public InputStream getResource(@NotNull String filename) {return super.getResource(filename);}

    @Override
    public BukkitScheduler getScheduler() {
        return this.getServer().getScheduler();
    }

    @Override
    public BukkitTask runTaskAsynchronously(final Runnable run) {
        return this.getScheduler().runTaskAsynchronously(this, run);
    }

    @Override
    public BukkitTask runTaskLaterAsynchronously(final Runnable run, final long delay) {
        return this.getScheduler().runTaskLaterAsynchronously(this, run, delay);
    }

    @Override
    public BukkitTask runTaskTimerAsynchronously(final Runnable run, final long delay, final long period) {
        return this.getScheduler().runTaskTimerAsynchronously(this, run, delay, period);
    }

    @Override
    public int scheduleSyncDelayedTask(final Runnable run) {
        return this.getScheduler().scheduleSyncDelayedTask(this, run);
    }

    @Override
    public int scheduleSyncDelayedTask(final Runnable run, final long delay) {
        return this.getScheduler().scheduleSyncDelayedTask(this, run, delay);
    }

    @Override
    public int scheduleSyncRepeatingTask(final Runnable run, final long delay, final long period) {
        return this.getScheduler().scheduleSyncRepeatingTask(this, run, delay, period);
    }
}
