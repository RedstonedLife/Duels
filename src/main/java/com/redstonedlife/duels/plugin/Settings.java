package com.redstonedlife.duels.plugin;

import com.redstonedlife.duels.plugin.config.DuelsConfiguration;
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

        reloadCount.incrementAndGet();
    }

    private int _confVer() {return config.getInt("CONFIG_VERSION", 1);}
    private String _msqlIP() {return config.getString("database.ip", "NOTSET");}
    private int    _msqlPRT() {return config.getInt("database.port", 3306);}
    private String _msqlUSR() {return config.getString("database.username", "");}
    private String _msqlPSW() {return config.getString("database.password", "");}
    private String _schemLoc() {return config.getString("schematics.location", "/schematics/");}
    private SchematicLoader _schemLoader() {return SchematicLoader.valueOf(config.getString("schematics.loader", "FAWE"));}
}
