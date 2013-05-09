package undercast.client;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * @author Flv92
 */
public class UndercastConfig {

    //update this value to change the config version.
    private static int version = 3;
    public static Configuration config;
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
    public static boolean showPlayingTime;
    public static int x;
    public static int y;
    public static boolean toggleTitleScreenButton;
    public static boolean filterTips;
    public static boolean fullBright;
    public static boolean matchOnServerJoin;
    public static boolean enableButtonTooltips;

    public UndercastConfig(Configuration configuration) {
        config = configuration;
        config.load();
        System.out.println("[UndercastMod]: Attempting to load/create the configuration.");
        showFPS = config.get("UndercastMod", "showFPS", true).getBoolean(true);
        showKills = config.get("UndercastMod", "showKills", true).getBoolean(true);
        showDeaths = config.get("UndercastMod", "showDeaths", true).getBoolean(true);
        showKilled = config.get("UndercastMod", "showKilled", true).getBoolean(true);
        showServer = config.get("UndercastMod", "showServer", true).getBoolean(true);
        showTeam = config.get("UndercastMod", "showTeam", true).getBoolean(true);
        showKD = config.get("UndercastMod", "showKD", true).getBoolean(true);
        showKK = config.get("UndercastMod", "showKK", true).getBoolean(true);
        showFriends = config.get("UndercastMod", "showFriends", true).getBoolean(true);
        showMap = config.get("UndercastMod", "showMap", true).getBoolean(true);
        showNextMap = config.get("UndercastMod", "showNextMap", true).getBoolean(true);
        showStreak = config.get("UndercastMod", "showStreak", true).getBoolean(true);
        showGuiChat = config.get("UndercastMod", "showGuiChat", true).getBoolean(true);
        showGuiMulti = config.get("UndercastMod", "showGuiMulti", true).getBoolean(true);
        enableButtonTooltips = config.get("UndercastMod", "enableButtonTooltips", true).getBoolean(true);
        x = config.get("UndercastMod", "X", 2).getInt();
        y = config.get("UndercastMod", "Y", 2).getInt();
        toggleTitleScreenButton = config.get("UndercastMod", "toggleTitleScreenButton", true).getBoolean(true);
        filterTips = config.get("UndercastMod", "filterTips", true).getBoolean(true);
        fullBright = config.get("UndercastMod", "fullBright", true).getBoolean(true);
        matchOnServerJoin = config.get("UndercastMod", "matchOnServerJoin", false).getBoolean(false);
        showPlayingTime = config.get("UndercastMod", "showPlayingTime", true).getBoolean(true);
        config.save();
        System.out.println("[UndercastMod]: Config loaded!");

    }
}
