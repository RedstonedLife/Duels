package com.redstonedlife.duels.plugin;

import com.redstonedlife.duels.plugin.config.DuelsConfiguration;
import com.redstonedlife.duels.plugin.enums.DuelID;
import com.redstonedlife.duels.plugin.enums.SchematicLoader;
import com.redstonedlife.duels.plugin.interfaces.ISettings;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class Settings implements ISettings {
    private boolean debug = true;
    private final transient DuelsConfiguration config;
    private final transient IDuels duels;
    private final transient AtomicInteger reloadCount = new AtomicInteger(0);
    private int config_version;
    private String mysql_ip;
    private int    mysql_port;
    private String mysql_user;
    private String mysql_pass;
    private String schematics_location;
    private SchematicLoader schematics_loader;
    private double minBetSum;
    private double maxBetSum;
    private double minReturnCap;
    private double maxReturnCap;
    private double opposingSelectKitSum;
    private double opposingSelectChallengeSum;
    private DuelID duelID;
    private boolean isSafeUserMap;
    private int maxUserCacheCount;

    public Settings(final IDuels duels) {
        this.duels = duels;
        config = new DuelsConfiguration(new File(duels.getDataFolder(), "config.yml"), "/config.yml");
        reloadConfig();
    }

    @Override public File getConfigFile() {return config.getFile();}
    @Override public boolean isDebug() {
        return debug;
    }
    @Override public void setDebug(boolean debug) {this.debug = debug;}
    @Override public int config_ver() {return config_version;}
    @Override public String mysql_ip() {return mysql_ip;}
    @Override public int mysql_port() {return mysql_port;}
    @Override public String mysql_user() {return mysql_user;}
    @Override public String mysql_pass() {return mysql_pass;}
    @Override public String schematics_location() {return schematics_location;}
    @Override public SchematicLoader schematic_loader() {return schematics_loader;}
    @Override public double minBetSum() {return minBetSum;}
    @Override public double maxBetSum() {return maxBetSum;}
    @Override public double minReturnCap() {return minReturnCap;}
    @Override public double maxReturnCap() {return maxReturnCap;}
    @Override public double opposingSelectKitSum() {return opposingSelectKitSum;}
    @Override public double opposingSelectChallengeSum() {return opposingSelectChallengeSum;}
    @Override public void setMinBetSum(double var) {this.minBetSum = var;}
    @Override public void setMaxBetSum(double var) {this.maxBetSum = var;}
    @Override public void setMinReturnCap(double var) {this.minReturnCap = var;}
    @Override public void setMaxReturnCap(double var) {this.maxReturnCap = var;}
    @Override public void setOpposingSelectKitSum(double var) {this.opposingSelectKitSum = var;}
    @Override public void setOpposingSelectChallengeSum(double var) {this.opposingSelectChallengeSum = var;}
    @Override public DuelID duelID() {return duelID;}
    @Override
    public void setDuelID(String value) {this.duelID = DuelID.fromValue(value);}
    @Override public boolean isSafeUsermap() {return isSafeUserMap;}
    @Override public int getMaxUserCacheCount() {return config.getInt("max-user-cache-count", (int) (Runtime.getRuntime().maxMemory() / 1024 / 96));}

    @Override
    public void reloadConfig() {
        config.load();

        config_version = _confVer();
        mysql_ip = _msqlIP();
        mysql_port = _msqlPRT();
        mysql_user = _msqlUSR();
        mysql_pass = _msqlPSW();
        schematics_location = _schemLoc();
        schematics_loader = _schemLoader();
        minBetSum = _mnSb();
        maxBetSum = _mxSb();
        minReturnCap = _mnRtCap();
        maxReturnCap = _mxRtCap();
        opposingSelectKitSum = _sltOppKit();
        opposingSelectChallengeSum = _sltOppChallenge();
        isSafeUserMap = _isSafeUserMap();

        reloadCount.incrementAndGet();
    }

    private int _confVer() {return config.getInt("CONFIG_VERSION", 1);}
    private String _msqlIP() {return config.getString("database.ip", "NOTSET");}
    private int    _msqlPRT() {return config.getInt("database.port", 3306);}
    private String _msqlUSR() {return config.getString("database.username", "");}
    private String _msqlPSW() {return config.getString("database.password", "");}
    private String _schemLoc() {return config.getString("schematics.location", "/schematics/");}
    private SchematicLoader _schemLoader() {return SchematicLoader.valueOf(config.getString("schematics.loader", "FAWE"));}
    private double _mnSb() {return config.getDouble("betting.cash.min_sum", 100.0D);}
    private double _mxSb() {return config.getDouble("betting.cash.max_sum", 100000.0D);}
    private double _mnRtCap() {return config.getDouble("betting.cash.min_return_cap", 1.0D);}
    private double _mxRtCap() {return config.getDouble("betting.cash.max_return_cap", 13.0D);}
    private double _sltOppKit() {return config.getDouble("opposing.select_kit_cap", 5000.0D);}
    private double _sltOppChallenge() {return config.getDouble("opposing.select_challenge", 10000.0D);}
    private boolean _isSafeUserMap() {return config.getBoolean("isSafeUserMap", true);}
}
