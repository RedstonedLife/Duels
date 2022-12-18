package com.redstonedlife.duels.api;

import java.util.Locale;

import static com.redstonedlife.duels.plugin.I18n.tl;

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException(Locale lang, final String name) {
        super(tl((lang == null) ? Locale.ENGLISH : lang, "userDoesNotExist", name));
    }
}
