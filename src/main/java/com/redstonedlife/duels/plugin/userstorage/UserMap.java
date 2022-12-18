package com.redstonedlife.duels.plugin.userstorage;

import com.earth2me.essentials.User;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.redstonedlife.duels.plugin.user.OfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

@Deprecated
public class UserMap {
    private final transient ModernUserMap userMap;
    private final transient UUIDMap uuidMap;

    public UserMap(final ModernUserMap userMap) {
        this.userMap = userMap;
        this.uuidMap = new UUIDMap();
    }

    public User getUser(final String name) {
        return userMap.getUser(name);
    }

    public User getUser(final UUID uuid) {
        return userMap.getUser(uuid);
    }

    public void trackUUID(final UUID uuid, final String name, final boolean replace) {
        // no-op
    }

    public User load(final String stringUUID) throws Exception {
        return userMap.load(UUID.fromString(stringUUID));
    }

    public User load(final org.bukkit.OfflinePlayer player) throws UserDoesNotExistException {
        final Player userPlayer;
        if (player instanceof Player) {
            userPlayer = (Player) player;
        } else {
            final OfflinePlayer Player = new OfflinePlayer(player.getUniqueId(), Bukkit.getServer());
            Player.setName(player.getName());
            userPlayer = Player;
        }

        final User user = userMap.getUser(userPlayer);
        if (user == null) {
            throw new UserDoesNotExistException("User not found");
        }
        return user;
    }

    public Set<UUID> getAllUniqueUsers() {
        return userMap.getAllUserUUIDs();
    }

    public int getUniqueUsers() {
        return userMap.getUserCount();
    }

    protected ConcurrentSkipListMap<String, UUID> getNames() {
        return new ConcurrentSkipListMap<>(userMap.getNameCache());
    }

    public UUIDMap getUUIDMap() {
        return uuidMap;
    }
}
