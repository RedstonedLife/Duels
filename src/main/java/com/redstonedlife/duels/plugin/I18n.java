package com.redstonedlife.duels.plugin;

import org.flywaydb.core.internal.util.scanner.Resource;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class I18n implements com.redstonedlife.duels.api.II18n {
    private static final String MESSAGES = "messages";
    private static final Pattern NODOUBLEMARK = Pattern.compile("''");
    private static final ResourceBundle NULL_BUNDLE = new ResourceBundle() {
        protected Object handleGetObject(@NotNull String key) {return null;}
        public Enumeration<String> getKeys() {return null;}
    };
    private static I18n instance;
    private final transient Locale defaultLocale = Locale.getDefault();
    private final transient ResourceBundle defaultBundle;
    private final transient IDuels duels;
    private transient Locale currentLocale = defaultLocale;
    private transient Map<Locale, ResourceBundle> customBundles;
    private transient Map<Locale, ResourceBundle> localeBundles;
    private transient Map<String, MessageFormat> messageFormatCache = new HashMap<>();

    public I18n(final IDuels duels) {
        this.duels = duels;
        defaultBundle = ResourceBundle.getBundle(MESSAGES, Locale.ENGLISH, new UTF8PropertiesControl());
        localeBundles = new HashMap<>();
        localeBundles.put(Locale.ENGLISH, defaultBundle);
        customBundles = new HashMap<>();
    }

    public static String tl(final Locale locale, final String string, final Object... objects) {
        if(instance==null)
            return "";
        if(objects.length==0) return NODOUBLEMARK.matcher(instance.translate((locale == null) ? Locale.ENGLISH : locale, string)).replaceAll("'");
        else return instance.format((locale == null) ? Locale.ENGLISH : locale, string, objects);
    }

    public static String capitalCase(final String input) {
        return input == null || input.length() == 0 ? input : input.toUpperCase(Locale.ENGLISH).charAt(0) + input.toLowerCase(Locale.ENGLISH).substring(1);
    }

    public void onEnable() {instance = this;}
    public void onDisable() {instance = null;}

    @Override public Locale getCurrentLocale() {return currentLocale;}

    private String translate(final Locale locale, final String string) {
        try {
            try {
                return customBundles.get(locale).getString(string);
            } catch(final NullPointerException | MissingResourceException ex) {
                return localeBundles.get(locale).getString(string);
            }
        } catch (final NullPointerException ex) {
          if (duels == null)
              duels.getLogger().log(
                      Level.WARNING,
                      String.format("Could not find Messages for Language Locale %s", locale.toString())
              );
          return defaultBundle.getString(string);
        } catch (final MissingResourceException ex) {
            if (duels == null)
                duels.getLogger().log(
                        Level.WARNING,
                        String.format(
                                "Missing translation key \"%s\" in translation file %s",
                                ex.getKey(), localeBundles.get(locale).getLocale().toString()
                        ), ex
                );
            return defaultBundle.getString(string);
        }
    }

    private String format(final Locale locale, final String string, final Object... objects) {
        String format = translate(locale, string);
        MessageFormat messageFormat = messageFormatCache.get(format);
        if(messageFormat == null) {
            try {
                messageFormat = new MessageFormat(format);
            } catch (final IllegalArgumentException e) {
                duels.getLogger().log(Level.SEVERE, "Invalid Translation key for '" + string +"': " + e.getMessage());
                format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
                messageFormat = new MessageFormat(format);
            }
            messageFormatCache.put(format, messageFormat);
        }
        return messageFormat.format(objects).replace(' ', ' '); // replace nbsp with a space
    }

    public void updateLocale(final String loc) {
        Locale _tm = null;
        if (loc != null && !loc.isEmpty()) {
            final String[] parts = loc.split("[_\\.]");
            if (parts.length == 1) {
                _tm = new Locale(parts[0]);
            }
            if (parts.length == 2) {
                _tm = new Locale(parts[0], parts[1]);
            }
            if (parts.length == 3) {
                _tm = new Locale(parts[0], parts[1], parts[2]);
            }
        }
        ResourceBundle.clearCache();
        messageFormatCache = new HashMap<>();
        duels.getLogger().log(Level.INFO, String.format("Using locale %s", _tm.toString()));

        try {
            if(!localeBundles.containsKey(_tm)) {
                localeBundles.put(_tm, ResourceBundle.getBundle(MESSAGES, _tm, new UTF8PropertiesControl()));
            }
        } catch (final MissingResourceException ex) {
            localeBundles.put(_tm, NULL_BUNDLE);
        }

        try {
            if(!customBundles.containsKey(_tm)) {
                customBundles.put(_tm, ResourceBundle.getBundle(MESSAGES, _tm, new FileResClassLoader(I18n.class.getClassLoader(), duels), new UTF8PropertiesControl()));
            }
        } catch (final MissingResourceException ignored) {
            customBundles.put(_tm, NULL_BUNDLE);
        }
    }

    /**
     * Attempts to load properties files from the plugin directory before falling back to the jar.
     */
    private static class FileResClassLoader extends ClassLoader {
        private final transient File dataFolder;

        FileResClassLoader(final ClassLoader classLoader, final IDuels duels) {
            super(classLoader);
            this.dataFolder = duels.getDataFolder();
        }

        @Override
        public URL getResource(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return file.toURI().toURL();
                } catch (final MalformedURLException ignored) {
                }
            }
            return null;
        }

        @Override
        public InputStream getResourceAsStream(final String string) {
            final File file = new File(dataFolder, string);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (final FileNotFoundException ignored) {
                }
            }
            return null;
        }
    }

    /**
     * Reads .properties files as UTF-8 instead of ISO-8859-1, which is the default on Java 8/below.
     * Java 9 fixes this by defaulting to UTF-8 for .properties files.
     */
    private static class UTF8PropertiesControl extends ResourceBundle.Control {
        public ResourceBundle newBundle(final String baseName, final Locale locale, final String format, final ClassLoader loader, final boolean reload) throws IOException {
            final String resourceName = toResourceName(toBundleName(baseName, locale), "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                final URL url = loader.getResource(resourceName);
                if (url != null) {
                    final URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // use UTF-8 here, this is the important bit
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
