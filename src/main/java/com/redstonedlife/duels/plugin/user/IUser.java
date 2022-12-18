package com.redstonedlife.duels.plugin.user;

import com.redstonedlife.duels.api.IAsyncTeleport;
import com.redstonedlife.duels.api.MaxMoneyException;
import com.redstonedlife.duels.plugin.commands.CommandSource;
import com.redstonedlife.duels.plugin.commands.IDuelCommand;
import com.redstonedlife.duels.plugin.config.entities.CommandCooldown;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Provides access to the user abstraction and stored data. Maintainers should add methods to <i>this interface</i>
 */
public interface IUser {
    boolean isAuthorized(String node);
    boolean isAuthorized(IDuelCommand cmd);
    boolean isAuthorized(IDuelCommand cmd, String permissionPrefix);
    boolean isPermissionSet(String node);
    void healCooldown() throws Exception;
    void giveMoney(BigDecimal value) throws MaxMoneyException;
    void giveMoney(final BigDecimal value, final CommandSource initiator) throws MaxMoneyException;
    void payUser(final User receiver, final BigDecimal value) throws Exception;
    void takeMoney(BigDecimal value);
    void takeMoney(final BigDecimal value, final CommandSource initiator);
    boolean canAfford(BigDecimal value);
    void setLastLocation();
    void setLogoutLocation();
    IAsyncTeleport getAsyncTeleport();
    BigDecimal getMoney();
    void setMoney(final BigDecimal value) throws MaxMoneyException;
    String getGroup();
    boolean inGroup(final String group);
    void enableInvulnerabilityAfterTeleport();
    void resetInvulnerabilityAfterTeleport();
    boolean hasInvulnerabilityAfterTeleport();
    void sendMessage(String message);
    /*
     *  User Data
     */
    Location getLastLocation();
    Location getLogoutLocation();
    @Deprecated void setConfigProperty(String node, Object object);
    Set<String> getConfigKeys();
    Map<String, Object> getConfigMap();
    Map<String, Object> getConfigMap(String node);
    @Deprecated Map<Pattern, Long> getCommandCooldowns();
    List<CommandCooldown> getCooldownsList();
    Date getCommandCooldownExpiry(String label);
    void addCommandCooldown(Pattern pattern, Date expiresAt, boolean save);
    boolean clearCommandCooldown(Pattern pattern);
    /*
     *  PlayerExtension
     */
    Player getBase();
    CommandSource getSource();
    String getName();
    UUID getUUID();
    String getDisplayName();
    String getFormattedNickname();
    Block getTargetBlock(int maxDistance);
    List<String> getPastUsernames();
    void addPastUsername(String username);
}
