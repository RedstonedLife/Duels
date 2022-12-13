package com.redstonedlife.duels.plugin.interfaces.config.file;

import com.redstonedlife.duels.plugin.exceptions.confg.file.FileNotFound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;

public interface IFile {

    /**
     * Initializes the file instance of the file.
     */
    void initialize();

    /**
     * Copies file contents from an input stream to a desired file, If none is specified, will copy File contents to
     * the stored file in instance.
     * @param inStream inputStream from desired copy file
     * @param inFile File of desired file to copy to
     */
    void copy(InputStream inStream, File inFile);

    /**
     * Verifies the existence of the file outside the jar
     * @return True if file exists, false otherwise
     * @throws FileNotFound If file is not accessible, path is restricted or a critical error happened
     */
    boolean exists() throws FileNotFound;

    /**
     * Creates the file alongside the dirs needed to host it
     * @throws Exception If permissions are not set right and the file creation is not possible an exception is thrown
     */
    void create() throws Exception;

    /**
     * Loads file contents to cache upon loading of server
     * @throws IOException,InvalidPathException,FileNotFound If file is inaccessible or doesn't exist.
     */
    void load() throws IOException, InvalidPathException, FileNotFound, InvalidConfigurationException;

    /**
     * Saves the contents of the cache to the file
     * @throws IOException If a permission error happens
     * @throws InvalidPathException If path is invalid
     * @throws FileNotFound If file was not found, In case it was deleted.
     */
    void save() throws IOException,InvalidPathException,FileNotFound;

    /**
     * Returns file object containing the path, object.
     * @return File
     */
    File getFile();

    /**
     * Returns configuration through which we can access values stored
     * @return {@link FileConfiguration}
     */
    FileConfiguration getConfiguration();

    /**
     * Allows to set a different file object unto which we will write the data.
     * @param newFile File object
     * @return new File object provided
     * @throws InvalidPathException If path is invalid
     * @throws FileNotFound If File provided with a valid path does not exist
     */
    File setFile(File newFile) throws InvalidPathException,FileNotFound;

    /**
     * Allows to set a different file configuration from a different configuration file
     * @param newFileConfiguration
     * @return
     */
    FileConfiguration setConfiguration(FileConfiguration newFileConfiguration);

    /**
     * Returns key of value specified
     * @param key String
     * @return value if exists
     * @throws Exception if value wasn't found.
     */
    Object get(String key) throws Exception;

    /**
     * Sets a value of a key by the value provided
     * @param key String
     * @param value Value Object of valid datatype that .YAML supports (If not will be reverted to String)
     * @return {@link Boolean} If value was successfully set True, if otherwise False
     */
    <T> boolean set(String key, T value) throws Exception;
}
