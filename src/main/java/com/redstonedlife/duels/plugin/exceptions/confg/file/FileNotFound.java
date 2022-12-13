package com.redstonedlife.duels.plugin.exceptions.confg.file;

public class FileNotFound extends Exception {

    public FileNotFound(final Throwable thrwb) {
        super(thrwb);
    }

    public FileNotFound(final String msg) {
        super(msg);
    }
}
