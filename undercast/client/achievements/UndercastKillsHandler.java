package undercast.client.achievements;

import cpw.mods.fml.client.FMLClientHandler;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import undercast.client.UndercastConfig;
import undercast.client.UndercastCustomMethods;
import undercast.client.UndercastData;

/**
 * @author Flv92
 */
public class UndercastKillsHandler {

    private String killer;
    private boolean killOrKilled;

    public UndercastKillsHandler() {
    }

    public void handleMessage(String message, String username, EntityPlayer player, String unstripedMessage) {
        //When you die from someone
        if (UndercastConfig.showDeathAchievements && message.startsWith(username) && !message.toLowerCase().contains(" the game") && !message.toLowerCase().endsWith(" team") && (message.contains(" by ") || message.contains(" took ") || message.contains("fury of"))) {
            if (!message.contains("fury of") && !message.contains("took ")) {
                killer = message.substring(message.indexOf("by") + 3, message.lastIndexOf("'s") == -1 ? message.length() : message.lastIndexOf("'s"));
            } else if (message.contains("fury of")) {
                killer = message.substring(message.indexOf("fury of ") + 8).split("'s")[0];
            } else {
                killer = message.substring(message.indexOf("took ") + 5).split("'s")[0];
            }
            killOrKilled = false;
            UndercastData.isLastKillFromPlayer = false;
            UndercastData.isNextKillFirstBlood = false;
            if (UndercastCustomMethods.isTeamkill(unstripedMessage, killer, username)) {
                this.printTeamKillAchievement();
            } else {
                this.printAchievement();
            }
        } //if you kill a person
        else if (UndercastConfig.showKillAchievements && !message.toLowerCase().contains(" the game") && (message.contains("by " + username) || message.contains("took " + username) || message.contains("fury of " + username)) && !message.toLowerCase().contains(" destroyed by ")) {
            killer = message.substring(0, message.indexOf(" "));
            killOrKilled = true;
            if (UndercastCustomMethods.isTeamkill(unstripedMessage, killer, username)) {
                this.printTeamKillAchievement();
            } else {
                // check if there is a special kill coming
                int kills = (int) UndercastData.getKills() + UndercastData.stats.kills;
                if (UndercastConfig.displaySpecialKillMessages) {
                    if (isSpecialKill(kills + 10)) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage("[UndercastMod] Your are \u00A7c10\u00A7f kills away from a \u00A7ospecial kill\u00A7r (" + (kills + 10) + ")");
                    } else if (isSpecialKill(kills + 5)) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage("[UndercastMod] Your are \u00A7c5\u00A7f kills away from a \u00A7ospecial kill\u00A7r (" + (kills + 5) + ")");
                    } else if (isSpecialKill(kills + 2)) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage("[UndercastMod] Your are \u00A7c2\u00A7f kills away from a \u00A7ospecial kill\u00A7r (" + (kills + 2) + ")");
                    } else if (isSpecialKill(kills + 1)) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage("[UndercastMod] Your are \u00A7c1\u00A7f kill away from a \u00A7ospecial kill\u00A7r (" + (kills + 1) + ")");
                    }
                }
                if (isSpecialKill(kills)) {
                    if (UndercastConfig.displaySpecialKillMessages) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage("[UndercastMod] \u00A7lSPECIAL KILL(" + kills + "): \u00A7c" + killer);
                    }
                    SpecialKillLogger.logSpecialKill(kills, killer, UndercastData.server, UndercastData.map);
                }
                this.printAchievement();
            }
            UndercastData.isLastKillFromPlayer = true;
            if (UndercastData.isNextKillFirstBlood) {
                if (UndercastConfig.showFirstBloodAchievement) {
                    printFirstBloodAchievement();
                }
                UndercastData.isNextKillFirstBlood = false;
            }
        } //when you die, but nobody killed you.
        else if (UndercastConfig.showDeathAchievements && message.startsWith(username) && !message.toLowerCase().contains(" the game") && !message.toLowerCase().endsWith(" team")) {
            killer = username;
            killOrKilled = false;
            this.printAchievement();
        } else if (message.toLowerCase().contains("game over")) {
            if (UndercastData.isLastKillFromPlayer && UndercastConfig.showLastKillAchievement) {
                printLastKillAchievement();
            }
        } //When someone die
        else if ((message.contains("by ") || message.contains("took ") || message.contains("fury of ")) && !message.toLowerCase().endsWith(" team")) {
            UndercastData.isLastKillFromPlayer = false;
            UndercastData.isNextKillFirstBlood = false;
        }
    }

    private void printAchievement() {

        Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
        ((UndercastGuiAchievement) FMLClientHandler.instance().getClient().guiAchievement)
                .addFakeAchievementToMyList(custom, killOrKilled, killer);
    }

    private void printTeamKillAchievement() {
        Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
        ((UndercastGuiAchievement) FMLClientHandler.instance().getClient().guiAchievement)
                .addFakeAchievementToMyList(custom, !killOrKilled, killer, killer, "Teamkill!");
    }

    public void printFirstBloodAchievement() {
        final long waitingTime;
        if (UndercastConfig.showAchievements && UndercastConfig.showKillAchievements) {
            waitingTime = 4000L; //This is to get enough time between two achievements
        } else {
            waitingTime = 0L;
        }
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(waitingTime);
                    Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
                    Minecraft client = FMLClientHandler.instance().getClient();
                    ((UndercastGuiAchievement) client.guiAchievement)
                            .addFakeAchievementToMyList(custom, true, client.thePlayer.username, client.thePlayer.username, "got the first Blood!");
                } catch (InterruptedException ex) {
                    Logger.getLogger(UndercastKillsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();
    }

    public void printLastKillAchievement() {

        Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
        Minecraft client = FMLClientHandler.instance().getClient();
        ((UndercastGuiAchievement) client.guiAchievement)
                .addFakeAchievementToMyList(custom, true, client.thePlayer.username, client.thePlayer.username, "got the last Kill!");

    }

    public static boolean isSpecialKill(int kill) {
        if (kill > 99) {
            if (kill < 1000) {
                // detect special kills like 100, 200, 300, 500, 900
                int i = kill / 100;
                if (i * 100 == kill) {
                    return true;
                }
            } else {
                // detect special kills like 1000m 5000, 7500, 10500
                int i1 = kill / 1000;
                int i2 = (kill + 500) / 1000;
                if ((i1 * 1000 == kill) || (i2 * 1000 == kill + 500)) {
                    return true;
                }
            }
            // detect special kills like 3333, 5555, 16666
            String s = String.valueOf(kill);
            char c1, c2, c3, c4;
            c1 = s.charAt(s.length() - 1);
            c2 = s.charAt(s.length() - 2);
            c3 = s.charAt(s.length() - 3);
            if (s.length() > 3) {
                c4 = s.charAt(s.length() - 4);
            } else {
                c4 = c3;
            }

            if (c1 == c2 && c1 == c3 && c1 == c4) {
                return true;
            }
        }
        return false;
    }
}
