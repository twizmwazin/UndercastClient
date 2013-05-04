package tc.oc;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * @author Flv92
 */
public class AresConfig {

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

    public AresConfig(Configuration configuration) {
        config = configuration;
        config.load();
        System.out.println("[ProjectAres]: Attempting to load/create the configuration.");
        showFPS = config.get("ProjectAres", "showFPS", true).getBoolean(true);
        showKills = config.get("ProjectAres", "showKills", true).getBoolean(true);
        showDeaths = config.get("ProjectAres", "showDeaths", true).getBoolean(true);
        showKilled = config.get("ProjectAres", "showKilled", true).getBoolean(true);
        showServer = config.get("ProjectAres", "showServer", true).getBoolean(true);
        showTeam = config.get("ProjectAres", "showTeam", true).getBoolean(true);
        showKD = config.get("ProjectAres", "showKD", true).getBoolean(true);
        showKK = config.get("ProjectAres", "showKK", true).getBoolean(true);
        showFriends = config.get("ProjectAres", "showFriends", true).getBoolean(true);
        showMap = config.get("ProjectAres", "showMap", true).getBoolean(true);
        showNextMap = config.get("ProjectAres", "showNextMap", true).getBoolean(true);
        showStreak = config.get("ProjectAres", "showStreak", true).getBoolean(true);
        showGuiChat = config.get("ProjectAres", "showGuiChat", true).getBoolean(true);
        showGuiMulti = config.get("ProjectAres", "showGuiMulti", true).getBoolean(true);
        enableButtonTooltips = config.get("ProjectAres", "enableButtonTooltips", true).getBoolean(true);
        x = config.get("ProjectAres", "X", 2).getInt();
        y = config.get("ProjectAres", "Y", 2).getInt();
        toggleTitleScreenButton = config.get("ProjectAres", "toggleTitleScreenButton", true).getBoolean(true);
        filterTips = config.get("ProjectAres", "filterTips", true).getBoolean(true);
        fullBright = config.get("ProjectAres", "fullBright", true).getBoolean(true);
        matchOnServerJoin = config.get("ProjectAres", "matchOnServerJoin", false).getBoolean(false);
        showPlayingTime = config.get("ProjectAres", "showPlayingTime", true).getBoolean(true);
        config.save();
        System.out.println("[ProjectAres]: Config loaded!");

    }
}
