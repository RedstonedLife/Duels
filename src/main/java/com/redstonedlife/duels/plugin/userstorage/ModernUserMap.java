package com.redstonedlife.duels.plugin.userstorage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.redstonedlife.duels.plugin.IDuels;
import com.redstonedlife.duels.plugin.user.OfflinePlayer;
import com.redstonedlife.duels.plugin.utils.NumberUtil;
import org.bukkit.entity.Player;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

public class ModernUserMap extends CacheLoader<UUID, User> implements IUserMap {
    private final transient IDuels duels;
    private final transient ModernUUIDCache uuidCache;
    private final transient LoadingCache<UUID, User> userCache;

    private final boolean debugPrintStackWithWarn;
    private final long debugMaxWarnsPerType;
    private final ConcurrentMap<String, AtomicLong> debugNonPlayerWarnCounts;

    public ModernUserMap(final IDuels duels) {
        this.duels = duels;
        this.uuidCache = new ModernUUIDCache(duels);
        this.userCache = CacheBuilder.newBuilder()
                .maximumSize(duels.getSettings().getMaxUserCacheCount())
                .softValues()
                .build(this);

        // -Dnet.essentialsx.usermap.print-stack=true
        final String printStackProperty = System.getProperty("net.duels.usermap.print-stack", "false");
        // -Dnet.essentialsx.usermap.max-warns=20
        final String maxWarnProperty = System.getProperty("net.duels.usermap.max-warns", "100");

        this.debugMaxWarnsPerType = NumberUtil.isLong(maxWarnProperty) ? Long.parseLong(maxWarnProperty) : -1;
        this.debugPrintStackWithWarn = Boolean.parseBoolean(printStackProperty);
        this.debugNonPlayerWarnCounts = new ConcurrentHashMap<>();
    }

    @Override
    public Set<UUID> getAllUserUUIDs() {
        return uuidCache.getCachedUUIDs();
    }

    @Override
    public long getCachedCount() {
        return userCache.size();
    }

    @Override
    public int getUserCount() {
        return uuidCache.getCacheSize();
    }

    @Override
    public User getUser(final UUID uuid) {
        if (uuid == null) {
            return null;
        }

        try {
            return userCache.get(uuid);
        } catch (ExecutionException e) {
            if (duels.isDebug()) {
                duels.getLogger().log(Level.WARNING, "Exception while getting user for " + uuid, e);
            }
            return null;
        }
    }

    @Override
    public User getUser(final Player base) {
        final User user = loadUncachedUser(base);
        userCache.put(user.getUUID(), user);
        return user;
    }

    @Override
    public User getUser(final String name) {
        if (name == null) {
            return null;
        }

        final User user = getUser(uuidCache.getCachedUUID(name));
        if (user != null && user.getBase() instanceof OfflinePlayer) {
            if (user.getLastAccountName() != null) {
                ((OfflinePlayer) user.getBase()).setName(user.getLastAccountName());
            } else {
                ((OfflinePlayer) user.getBase()).setName(name);
            }
        }
        return user;
    }

    public void addCachedNpcName(final UUID uuid, final String name) {
        if (uuid == null || name == null) {
            return;
        }

        uuidCache.updateCache(uuid, name);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public User load(final UUID uuid) throws Exception {
        final User user = loadUncachedUser(uuid);
        if (user != null) {
            return user;
        }

        throw new Exception("User not found!");
    }

    @Override
    public User loadUncachedUser(final Player base) {
        if (base == null) {
            return null;
        }

        User user = getUser(base.getUniqueId());
        if (user == null) {
            debugLogUncachedNonPlayer(base);
            user = new User(base, duels);
        } else if (!base.equals(user.getBase())) {
            duels.getLogger().log(Level.INFO, "Duels updated the underlying Player object for " + user.getUUID());
            user.update(base);
        }
        uuidCache.updateCache(user.getUUID(), user.getName());

        return user;
    }

    @Override
    public User loadUncachedUser(final UUID uuid) {
        User user = userCache.getIfPresent(uuid);
        if (user != null) {
            return user;
        }

        Player player = duels.getServer().getPlayer(uuid);
        if (player != null) {
            // This is a real player, cache their UUID.
            user = new User(player, duels);
            uuidCache.updateCache(uuid, player.getName());
            return user;
        }

        final File userFile = getUserFile(uuid);
        if (userFile.exists()) {
            player = new OfflinePlayer(uuid, duels.getServer());
            user = new User(player, duels);
            ((OfflinePlayer) player).setName(user.getLastAccountName());
            uuidCache.updateCache(uuid, null);
            return user;
        }

        return null;
    }

    public void addCachedUser(final User user) {
        userCache.put(user.getUUID(), user);
    }

    @Override
    public Map<String, UUID> getNameCache() {
        return uuidCache.getNameCache();
    }

    public String getSanitizedName(final String name) {
        return uuidCache.getSanitizedName(name);
    }

    public void blockingSave() {
        uuidCache.blockingSave();
    }

    public void invalidate(final UUID uuid) {
        userCache.invalidate(uuid);
        uuidCache.removeCache(uuid);
    }

    private File getUserFile(final UUID uuid) {
        return new File(new File(duels.getDataFolder(), "userdata"), uuid.toString() + ".yml");
    }

    public void shutdown() {
        uuidCache.shutdown();
    }

    private void debugLogUncachedNonPlayer(final Player base) {
        final String typeName = base.getClass().getName();
        final long count = debugNonPlayerWarnCounts.computeIfAbsent(typeName, name -> new AtomicLong(0)).getAndIncrement();
        if (debugMaxWarnsPerType < 0 || count <= debugMaxWarnsPerType) {
            final Throwable throwable = debugPrintStackWithWarn ? new Throwable() : null;
            duels.getLogger().log(Level.INFO, "Created a User for " + base.getName() + " (" + base.getUniqueId() + ") for non Bukkit type: " + typeName, throwable);
            if (count == debugMaxWarnsPerType) {
                duels.getLogger().log(Level.WARNING, "Duels will not log any more warnings for " + typeName + ". Please report this to the EssentialsX team.");
            }
        }
    }
}
