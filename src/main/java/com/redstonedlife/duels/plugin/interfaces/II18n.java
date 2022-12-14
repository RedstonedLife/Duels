package com.redstonedlife.duels.plugin.interfaces;

import org.bukkit.entity.Player;

import java.util.Locale;

/**
 * Provdes access to the current locale in use
 *
 * @deprecated External plugins should prefer to use either the player's client language ({@link Player#getLocale()} or
 *             ({@link com.redstonedlife.duels.api.II18n} in case of future additions.
 *             Will be deprecated in future version in favour of a better system.
 */
@Deprecated(forRemoval = true)
public interface II18n {
    /**
     * Gets the current locale setting
     *
     * @return the current locale, if not set it will return the default locale
     */
    Locale getCurrentLocale();
}
