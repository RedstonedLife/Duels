package com.redstonedlife.duels.provider.providers;

import com.redstonedlife.duels.provider.LoggerProvider;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseLoggerProvider extends LoggerProvider {
    private final Logger logger;

    public BaseLoggerProvider(final Plugin plugin, final Logger logger) {
        super(plugin);
        this.logger = logger;
    }

    @Override protected void doTheLog(Level level, String message, Throwable throwable) {logger.log(level, message, throwable);}
    @Override protected void doTheLog(Level level, String message) {logger.log(level, message);}
}
