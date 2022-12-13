package com.redstonedlife.duels.plugin.config.file;

import com.redstonedlife.duels.api.IFile;
import com.redstonedlife.duels.plugin.IDuels;
import com.redstonedlife.duels.plugin.exceptions.confg.file.FileNotFound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.file.InvalidPathException;

public class ConfigFile implements IFile {
    private File file;
    private FileConfiguration fileConfig;
    private final String filename;
    private final IDuels instance;

    public ConfigFile(final File file, final String filename, final IDuels instance) {
        this.file = file;
        this.filename = filename;
        this.instance = instance;
    }



    @Override
    public void initialize() {
        this.file = new File(this.file, this.filename);
        this.fileConfig = (FileConfiguration) new YamlConfiguration();
    }

    @Override
    public void copy(InputStream inStream, File inFile) {
        try {
            OutputStream out = new FileOutputStream(inFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inStream.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists() throws FileNotFound {
        return (this.file.exists());
    }

    @Override
    public void create() throws Exception {
        if(!this.file.getParentFile().mkdirs())
            throw new Exception("Was unable to create intermediary directories in order to create "+filename);
        copy(this.instance.getResource(this.filename), this.file);
    }

    @Override
    public void load() throws IOException, InvalidPathException, FileNotFound, InvalidConfigurationException {
        if(!this.exists())
            throw new FileNotFound(filename+" from which configuration was attempted to be loaded was not found");
        this.fileConfig.load(this.file);
    }

    @Override
    public void save() throws IOException, InvalidPathException, FileNotFound {
        if(!this.exists())
            throw new FileNotFound(filename+" unto which information was attempted to be saved was not found");
        this.fileConfig.save(this.file);
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public FileConfiguration getConfiguration() {
        return this.fileConfig;
    }

    @Override
    public File setFile(final File newFile) throws InvalidPathException, FileNotFound {
        return this.file = newFile;
    }

    @Override
    public FileConfiguration setConfiguration(FileConfiguration newFileConfiguration) {
        return this.fileConfig = newFileConfiguration;
    }

    @Override
    public Object get(String key) throws Exception {
        Object _tmp = this.fileConfig.get(key);
        if(_tmp == null)
            throw new Exception(key + " was not found in configuration");
        else
            return _tmp;
    }

    @Override
    public <T> boolean set(String key, T value) throws Exception {
        this.fileConfig.set(key, value);
        return this.get(key) == value;
    }
}
