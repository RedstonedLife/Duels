package com.redstonedlife.duels.plugin.user;

import com.redstonedlife.duels.api.IMessageRecipient;
import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.plugin.utils.EnumUtil;
import org.bukkit.Statistic;

import java.math.BigDecimal;
import java.util.Map;
import java.util.WeakHashMap;

public class User extends UserData implements Comparable<User>, IMessageRecipient, IUser {
    private static final Statistic PLAY_ONE_TICK = EnumUtil.getStatistic("PLAY_ONE_MINUTE", "PLAY_ONE_TICK");

    // User Modules
    private final IMessageRecipient messageRecipient;
    private transient final AsyncTeleport teleport;
    private transient final Teleport legacyTeleport;

    // User command confirmation strings
    private final Map<User, BigDecimal> confirmingPayments = new WeakHashMap<>();
}
