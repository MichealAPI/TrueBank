/*
 * GNU GENERAL PUBLIC LICENSE
 * Version 3, 29 June 2007
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.mikeslab.truebank.util.language;

import it.mikeslab.truebank.util.color.ChatColor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Provides utility methods for managing languages files for Bukkit/Spigot plugins.
 */
public class Language {

    private static final String
            subFolder = "languages",
            defaultLanguage = "en_US";

    private static String language = defaultLanguage;

    @Getter
    private static FileConfiguration languageFile;
    private static File dataFolder;
    private static JavaPlugin plugin;
    private static Map<LangKey, String> cache;

    /**
     * Initializes the languages file management system.
     *
     * @param plugin   The Bukkit/Spigot plugin instance.
     * @param language The languages to load.
     */
    public static void initialize(JavaPlugin plugin, String language) {

        Language.language = language;
        Language.dataFolder = plugin.getDataFolder();
        Language.plugin = plugin;

        // Initialize the cache with a maximum size of 1000 entries.
        cache = new HashMap<>();

        // Generate languages files for recognized languages if they don't exist, and load the specified languages.
        generate();
        loadLanguage(language);
    }

    public static void reload() {
        loadLanguage(language);
    }

    /**
     * Returns the languages string for the given key, or the default value if the key is not found in the languages file.
     *
     * @param langKey The languages key to retrieve the value for.
     * @return The languages string for the given key.
     */
    public static String getString(LangKey langKey, boolean withPrefix) {
        try {
            // Try to get the languages string from the cache. If it's not there, load it from the languages file and add it to the cache.
            String value = cache.getOrDefault(langKey, loadString(langKey));

            if(withPrefix) {
                String prefix = cache.getOrDefault(LangKey.PREFIX, loadString(LangKey.PREFIX));
                return ChatColor.color(prefix + value);
            }

            return ChatColor.color(value);
        } catch (Exception e) {
            // If an exception occurs while loading the languages string, log a warning and return the default value.
            plugin.getLogger().log(Level.WARNING, "Error while getting languages string for key " + langKey, e);
            return langKey.getDefaultValue();
        }
    }

    public static String getString(LangKey langKey, boolean withPrefix, Map<String, String> replacements) {
        String value = getString(langKey, withPrefix);

        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            value = value.replace(replacement.getKey(), replacement.getValue());
        }
        return value;
    }

    /**
     * Loads the languages string for the given key from the languages file.
     *
     * @param langKey The languages key to load the value for.
     * @return The languages string for the given key.
     */
    private static String loadString(LangKey langKey) {
        String parsedKey = langKey.name().toLowerCase().replace("_", "-");

        // If the languages file doesn't contain the specified key, log a warning.
        if (!languageFile.contains(parsedKey)) {
            Bukkit.getLogger().warning("Missing languages key: " + parsedKey);
        }
        String result = languageFile.getString(parsedKey, langKey.getDefaultValue());

        // Adding to cache
        cache.put(langKey, result);

        // Return the languages string for the specified key, or the default value if it's not found.
        return languageFile.getString(parsedKey, result);
    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }


    /**
     * Closes the access to the languages file.
     */
    public static void close() {
        languageFile = null;
    }

    /**
     * Returns true if a languages file with the given name exists in the plugin's data folder.
     *
     * @param language The languages to check for.
     * @return True if a languages file with the given name exists in the plugin's data folder. @
     */
    public static boolean isLanguageFile(String language) {
        return new File(dataFolder, subFolder + File.separator + language + ".yml").exists();
    }

    /**
     * Generates all languages files for recognized languages if they don't exist.
     */
    private static void generate() {
        // Generate the subfolder for languages files if it doesn't exist.
        generateSubFolder();

        // Generate languages files for each recognized languages if they don't exist.
        for (RecognizedLanguages language : RecognizedLanguages.values()) {
            generateLanguageFile(language);
        }
    }

    /**
     * Creates the languages subfolder in the plugin's data folder if it doesn't exist.
     */
    private static void generateSubFolder() {
        File file = new File(dataFolder, subFolder);

        // If the subfolder doesn't exist, create it.
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * Generates the languages file for the given recognized languages if it doesn't exist.
     *
     * @param language The recognized languages to generate the file for.
     */
    private static void generateLanguageFile(RecognizedLanguages language) {
        String path = subFolder + File.separator + language.name() + ".yml";
        File file = new File(dataFolder + File.separator + path);

        generateFile(file, path);
    }

    public static void generateFile(File file, String path) {

        // If the languages file already exists and has a non-zero size, skip it.
        if (file.exists() && file.length() > 0) {
            return;
        }

        try {
            // Generate the languages file from the plugin resources.
            plugin.saveResource(path, false);
        } catch (IllegalArgumentException e) {
            // If an exception occurs while generating the languages file, log a warning.
            plugin.getLogger().log(Level.WARNING, "Error while generating file " + file.getName(), e);
        }
    }

    /**
     * Loads the languages file with the given name, or the default languages file if the given name is not recognized.
     *
     * @param language The languages to load.
     */
    private static void loadLanguage(String language) {
        // If the specified languages is not recognized, use the default languages.
        if (!RecognizedLanguages.isRecognizedLanguage(language)) {
            language = defaultLanguage;
        }

        // Load the languages file from the plugin's data folder.
        File file = new File(dataFolder, subFolder + File.separator + language + ".yml");
        try {
            Language.languageFile = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            // If an exception occurs while loading the languages file, log an error and use an empty configuration.
            plugin.getLogger().log(Level.SEVERE, "Failed to load languages file: " + language + ".yml", e);
            Language.languageFile = new YamlConfiguration();
        }
    }
}