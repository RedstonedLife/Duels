package com.redstonedlife.duels.plugin.interfaces;

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
}
