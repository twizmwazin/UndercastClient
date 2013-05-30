package undercast.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraftforge.common.Configuration;

/**
 * @author Flv92
 */
public class UndercastConfig {

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
    public static boolean showMatchTime;
    public static boolean showMatchTimeSeconds;
    public static boolean showGSClass;
    public static int x;
    public static int y;
    public static boolean toggleTitleScreenButton;
    public static boolean filterTips;
    public static boolean fullBright;
    public static boolean matchOnServerJoin;
    public static boolean enableButtonTooltips;
    public static boolean showAchievements;
    public static boolean showKillAchievements;
    public static boolean showDeathAchievements;
    public static boolean showFirstBloodAchievement;
    public static boolean showLastKillAchievement;
    public static File configFile;

    public UndercastConfig(Configuration configuration, File configF) {
        config = configuration;
        configFile = configF;
        reloadConfig();
    }

    public static void reloadConfig() {
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
        showAchievements = config.get("UndercastMod", "showAchievements", false).getBoolean(false);
        showKillAchievements = config.get("UndercastMod", "showKillAchievements", true).getBoolean(true);
        showDeathAchievements = config.get("UndercastMod", "showDeathAchievements", true).getBoolean(true);
        showFirstBloodAchievement = config.get("UndercastMod", "showFirstBloodAchievement", true).getBoolean(true);
        showLastKillAchievement = config.get("UndercastMod", "showLastKillAchievement", true).getBoolean(true);
        showMatchTime = config.get("UndercastMod", "showMatchTime", true).getBoolean(true);
        showMatchTimeSeconds = config.get("UndercastMod", "showMatchTimeSeconds", true).getBoolean(true);
        showGSClass = config.get("UndercastMod", "showGSClass", true).getBoolean(true);
        config.save();
        System.out.println("[UndercastMod]: Config loaded!");
    }

    public static void setBooleanProperty(String name, boolean bool) {
        File tempFile = new File(configFile.getParent() + "/temporaryFile.temp.cfg");
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            FileWriter fr = new FileWriter(tempFile);
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("B:" + name + "=")) {
                    fr.write(line + "\n");
                } else {
                    if (bool) {
                        fr.write(line.substring(0, line.lastIndexOf("=") + 1) + "true" + "\n");
                    } else {
                        fr.write(line.substring(0, line.lastIndexOf("=") + 1) + "false" + "\n");
                    }
                }
            }
            fr.close();
            br.close();
            configFile.delete();
            tempFile.renameTo(configFile);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void setIntProperty(String name, int newInt) {
        File tempFile = new File(configFile.getParent() + "/temporaryFile.temp.cfg");
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            FileWriter fr = new FileWriter(tempFile);
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains("I:" + name)) {
                    fr.write(line + "\n");
                } else {
                    fr.write(line.substring(0, line.lastIndexOf("=") + 1) + newInt + "\n");
                }
            }
            fr.close();
            br.close();
            configFile.delete();
            tempFile.renameTo(configFile);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
