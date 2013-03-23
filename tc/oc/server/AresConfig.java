package tc.oc.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import net.minecraft.src.ModLoader;

public class AresConfig {
    private static Properties defaults = new Properties();
    private String configPath;
    private Properties config;

    static {
        defaults.setProperty("serverDomain", "oc.tc");
        defaults.setProperty("showFPS", "true");
        defaults.setProperty("showKills", "true");
        defaults.setProperty("showDeaths", "true");
        defaults.setProperty("showKilled", "true");
        defaults.setProperty("showServer", "true");
        defaults.setProperty("showTeam", "true");
        defaults.setProperty("showKD", "true");
        defaults.setProperty("showKK", "true");
        defaults.setProperty("showFriends", "true");
        defaults.setProperty("showMap", "true");
        defaults.setProperty("showStreak", "true");
        defaults.setProperty("showGuiChat", "true");
        defaults.setProperty("showGuiMulti", "true");
        defaults.setProperty("keyGui", "F6");
        defaults.setProperty("keyGui2", "L");
        defaults.setProperty("X", "2");
        defaults.setProperty("Y", "2");
        defaults.setProperty("toggleTitleScreenButton", "true");
        defaults.setProperty("filterTips", "true");
    }

    public AresConfig() {
        System.out.println("[ProjectAres]: Attempting to load/create the configuration.");
        loadConfig();
    }

    private void loadConfig() {
        config = new Properties(defaults);

        try {
            configPath = ModLoader.getMinecraftInstance().getMinecraftDir().getCanonicalPath() + File.separatorChar + "mods" + File.separatorChar + "UnofficialProjectAres" + File.separatorChar;

            File cfg = new File(configPath + "mod_Ares.cfg");

            if (cfg.exists()) {
                System.out.println("[ProjectAres]: Config file found, loading...");
                config.load(new FileInputStream(configPath + "mod_Ares.cfg"));
            } else {
                System.out.println("[ProjectAres]: No config file found, creating...");
                createConfig(cfg);
            }
        } catch (Exception e) {
            displayErrorMessage(e.toString());
        }
    }

    private void createConfig(File cfg) {
        File folder = new File(configPath);
        if (!folder.exists()) {
            System.out.println("[ProjectAres]: No folder found, creating...");
            folder.mkdir();
        }
        try {
            cfg.createNewFile();

            config.setProperty("serverDomain", "oc.tc");
            config.setProperty("showFPS", "true");
            config.setProperty("showKills", "true");
            config.setProperty("showDeaths", "true");
            config.setProperty("showKilled", "true");
            config.setProperty("showServer", "true");
            config.setProperty("showTeam", "true");
            config.setProperty("showKD", "true");
            config.setProperty("showKK", "true");
            config.setProperty("showFriends", "true");
            config.setProperty("showMap", "true");
            config.setProperty("showStreak", "true");
            config.setProperty("showGuiChat", "true");
            config.setProperty("showGuiMulti", "true");
            config.setProperty("keyGui", "F6");
            config.setProperty("keyGui2", "L");
            config.setProperty("X", "2");
            config.setProperty("Y", "2");
            config.setProperty("toggleTitleScreenButton", "true");
            config.setProperty("filterTips", "true");

            config.store(new FileOutputStream(configPath + "mod_Ares.cfg"), null);
        } catch (Exception e) {
            displayErrorMessage(e.toString());
        }
    }

    public void setProperty(String prop, String value) {
        config.setProperty(prop, value);
        saveConfig();
    }

    public void setProperty(String prop, float value) {
        String s = String.valueOf(value);
        config.setProperty(prop, s);
        saveConfig();
    }

    public void setProperty(String prop, int value) {
        String s = String.valueOf(value);
        config.setProperty(prop, s);
        saveConfig();
    }

    public void setProperty(String prop, boolean value) {
        String s = String.valueOf(value);
        config.setProperty(prop, s);
        saveConfig();
    }

    public String getStringProperty(String prop) {
        return config.getProperty(prop);
    }

    public float getFloatProperty(String prop) {
        String s = config.getProperty(prop);
        return Float.parseFloat(s);
    }

    public int getIntProperty(String prop) {
        String s = config.getProperty(prop);
        return Integer.parseInt(s);
    }

    public boolean getBoolProperty(String prop) {
        String s = config.getProperty(prop);
        return Boolean.parseBoolean(s);
    }

    public static String getDefaultPropertyValue(String prop) {
        return defaults.getProperty(prop);
    }

    public static float getDefaultFloatProperty(String prop) {
        String s = defaults.getProperty(prop);
        return Float.parseFloat(s);
    }

    public static int getDefaultIntProperty(String prop) {
        String s = defaults.getProperty(prop);
        return Integer.parseInt(s);
    }

    public static boolean getDefaultBoolProperty(String prop) {
        String s = defaults.getProperty(prop);
        return Boolean.parseBoolean(s);
    }

    public void saveConfig() {
        try {
            config.store(new FileOutputStream(configPath + "mod_Ares.cfg"), null);
            config.load(new FileInputStream(configPath + "mod_Ares.cfg"));
        } catch (Exception e) {
            displayErrorMessage(e.toString());
        }
    }

    private void displayErrorMessage(String error) {
        System.out.println("[ProjectAres]: ERROR: " + error);
    }
}
