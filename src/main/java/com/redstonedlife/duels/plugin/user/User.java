package com.redstonedlife.duels.plugin.user;

import com.redstonedlife.duels.api.IMessageRecipient;
import com.redstonedlife.duels.api.IUser;
import com.redstonedlife.duels.plugin.utils.EnumUtil;
import org.bukkit.Statistic;

public class User extends UserData implements Comparable<User>, IMessageRecipient, IUser {
    private static final Statistic PLAY_ONE_TICK = EnumUtil.getStatistic("PLAY_ONE_MINUTE", "PLAY_ONE_TICK");
}
