package com.redstonedlife.duels.plugin;

import com.earth2me.essentials.User;
import com.redstonedlife.duels.plugin.interfaces.ISettings;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

public interface IDuels extends Plugin {

    boolean isDebug();
    void onReload();
    void initiateReload();

    User getOfflineUser(String name);

    int getConfigVersion();
    Collection<Player> getOnlinePlayers();
    ISettings getSettings();
    InputStream getResource(String filename);
    @Deprecated User getUser(Object base);
    User getUser(UUID base);
    User getUser(String base);
    User getUser(Player base);
    BukkitScheduler getScheduler();

    BukkitTask runTaskAsynchronously(Runnable run);

    BukkitTask runTaskLaterAsynchronously(Runnable run, long delay);

    BukkitTask runTaskTimerAsynchronously(Runnable run, long delay, long period);

    int scheduleSyncDelayedTask(Runnable run);

    int scheduleSyncDelayedTask(Runnable run, long delay);

    int scheduleSyncRepeatingTask(Runnable run, long delay, long period);
}
