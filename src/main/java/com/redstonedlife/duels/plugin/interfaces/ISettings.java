package com.redstonedlife.duels.plugin.interfaces;

import com.redstonedlife.duels.plugin.enums.DuelID;
import com.redstonedlife.duels.plugin.enums.SchematicLoader;
import com.redstonedlife.duels.plugin.interfaces.config.file.IConf;

import java.io.File;

public interface ISettings extends IConf {

    File getConfigFile();
    boolean isDebug();
    void setDebug(boolean debug);
    int config_ver();
    String mysql_ip();
    int    mysql_port();
    String mysql_user();
    String mysql_pass();
    String schematics_location();
    SchematicLoader schematic_loader();
    double minBetSum();
    double maxBetSum();
    double minReturnCap();
    double maxReturnCap();
    double opposingSelectKitSum();
    double opposingSelectChallengeSum();
    void setMinBetSum(double var);
    void setMaxBetSum(double var);
    void setMinReturnCap(double var);
    void setMaxReturnCap(double var);
    void setOpposingSelectKitSum(double var);
    void setOpposingSelectChallengeSum(double var);
    DuelID duelID();
    void setDuelID(String value);
    boolean isSafeUsermap();
    int getMaxUserCacheCount();
    double getTeleportCooldown();
    boolean isAlwaysTeleportSafety();
    boolean isTeleportToCenterLocation();
    boolean isForceDisableTeleportSafety();
    boolean isTeleportSafetyEnabled();
}
