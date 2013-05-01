package tc.oc;

import net.minecraft.src.ModLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


public class AresConfig {
    private static Properties defaults = new Properties();
    private String configPath;
    private Properties config;
    
    // update this value to change the config version.
    private static int version = 3;

    // main variables
    public static boolean showFPS;
    public static boolean showKills;
    public static boolean showDeaths;
    public static boolean showKilled;
    public static boolean showServer;
    public static boolean showTeam;
    public static boolean showKD;
    public static boolean showKK;
    public static boolean showFriends;
    public static boolean showMap;
    public static boolean showNextMap;
    public static boolean showStreak;
    public static boolean showGuiChat;
    public static boolean showGuiMulti;
    public static int x;
    public static int y;
    public static boolean toggleTitleScreenButton;
    public static boolean filterTips;
    public static boolean fullBright;
    public static boolean matchOnServerJoin;
    public static boolean enableButtonTooltips;
    public static int configVersion;

    /**
     * Default values created when class is first referenced
     */
    static {
        defaults.setProperty("showFPS", "true");
        defaults.setProperty("showKills", "true");
        defaults.setProperty("showDeaths", "true");
        defaults.setProperty("showKilled", "true");
        defaults.setProperty("showServer", "true");
        defaults.setProperty("showTeam", "true");
        defaults.setProperty("showKD", "true");
        defaults.setProperty("showKK", "true");
        defaults.setProperty("showFriends", "false");
        defaults.setProperty("showMap", "true");
        defaults.setProperty("showNextMap", "true");
        defaults.setProperty("showStreak", "true");
        defaults.setProperty("showGuiChat", "true");
        defaults.setProperty("showGuiMulti", "true");
        defaults.setProperty("X", "2");
        defaults.setProperty("Y", "2");
        defaults.setProperty("toggleTitleScreenButton", "true");
        defaults.setProperty("filterTips", "true");
        defaults.setProperty("fullBright", "true");
        defaults.setProperty("matchOnServerJoin", "false");
        defaults.setProperty("enableButtonTooltips", "true");
        // if the value is missing, it should force an update. Don't change it.
        defaults.setProperty("configVersion", "0");
    }

    public AresConfig() {
        System.out.println("[ProjectAres]: Attempting to load/create the configuration.");
        loadConfig();
        loadConfigData();
    }

    /**
     * Attempts to find a config
     * If there is one load it
     * If there is not one create one
     */
    private void loadConfig() {
        config = new Properties(defaults);

        try {
            configPath = ModLoader.getMinecraftInstance().getMinecraftDir().getCanonicalPath() + File.separatorChar + "config" + File.separatorChar + "UnofficialProjectAres" + File.separatorChar;

            File cfg = new File(configPath + "mod_Ares.cfg");

            if(cfg.exists()) {
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

    /**
     * Creates a config properties of default values
     * Then saves the config to the config location
     *
     * @param cfg config file
     */
    private void createConfig(File cfg) {
        File folder = new File(configPath);
        if(!folder.exists()) {
            System.out.println("[ProjectAres]: No folder found, creating...");
            folder.mkdir();
        }
        try {
            cfg.createNewFile();

            config.setProperty("showFPS", "true");
            config.setProperty("showKills", "true");
            config.setProperty("showDeaths", "true");
            config.setProperty("showKilled", "true");
            config.setProperty("showServer", "true");
            config.setProperty("showTeam", "true");
            config.setProperty("showKD", "true");
            config.setProperty("showKK", "true");
            config.setProperty("showFriends", "false");
            config.setProperty("showMap", "true");
            config.setProperty("showNextMap", "true");
            config.setProperty("showStreak", "true");
            config.setProperty("showGuiChat", "true");
            config.setProperty("showGuiMulti", "true");
            config.setProperty("X", "2");
            config.setProperty("Y", "2");
            config.setProperty("toggleTitleScreenButton", "true");
            config.setProperty("filterTips", "true");
            config.setProperty("fullBright", "true");
            config.setProperty("matchOnServerJoin", "false");
            config.setProperty("enableButtonTooltips", "true");
            config.setProperty("configVersion", ""+version);

            config.store(new FileOutputStream(configPath +"mod_Ares.cfg"),"This is the Unoffical Project Ares Mod Config" + "\nCustomize it to your taste" + "\nkeyGui = Ingame Stats" +"\nkeyGui2 = Ingame Server Menu" + "\nkeyGui3 = Full Bright\n");
        } catch (Exception e) {
            displayErrorMessage(e.toString());
        }
    }

    /**
     * Loads the property data into the local data
     */
    public void loadConfigData() {
        System.out.println("[ProjectAres]: Loading Config to Local Data");
        showFPS = this.getBoolProperty("showFPS");
        showKills = this.getBoolProperty("showKills");
        showDeaths = this.getBoolProperty("showDeaths");
        showKilled = this.getBoolProperty("showKilled");
        showServer = this.getBoolProperty("showServer");
        showTeam = this.getBoolProperty("showTeam");
        showKD = this.getBoolProperty("showKD");
        showKK = this.getBoolProperty("showKK");
        showFriends = this.getBoolProperty("showFriends");
        showNextMap = this.getBoolProperty("showNextMap");
        showMap = this.getBoolProperty("showMap");
        showStreak = this.getBoolProperty("showStreak");
        showGuiChat = this.getBoolProperty("showGuiChat");
        showGuiMulti = this.getBoolProperty("showGuiMulti");
        x = this.getIntProperty("X");
        y = this.getIntProperty("Y");
        toggleTitleScreenButton = this.getBoolProperty("toggleTitleScreenButton");
        filterTips = this.getBoolProperty("filterTips");
        fullBright = this.getBoolProperty("fullBright");
        matchOnServerJoin = this.getBoolProperty("matchOnServerJoin");
        enableButtonTooltips = this.getBoolProperty("enableButtonTooltips");
        configVersion = this.getIntProperty("configVersion");
        
        checkForConfigUpdate();
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
    
    /***
     * Checks if the config version has changed and adds the options which are new.
     */
    private void checkForConfigUpdate(){
        if(version != configVersion){
            System.out.println("[ProjectAres]: Updating the config...");
            switch(configVersion){
            case 0:
                // add you additional options.
                if(fullBright == true){ // do not overwrite the setting, if it isn't the default value
                    config.setProperty("fullBright", "true");
                }
                if(matchOnServerJoin == false){ // do not overwrite the setting, if it isn't the default value
                    config.setProperty("matchOnServerJoin", "false");
                }
            case 1:
                if(showNextMap == true){
                    config.setProperty("showNextMap", "true");
                }
            case 2:
                if(enableButtonTooltips == true) {
                    config.setProperty("enableButtonTooltips", "true");
                }
            case 3:
                // for the next version.
            }
            config.setProperty("configVersion", ""+version);
            saveConfig();	
        }
    }
}
