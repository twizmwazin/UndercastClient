package undercast.client.achievements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import undercast.client.UndercastConfig;
import undercast.client.UndercastCustomMethods;
import undercast.client.UndercastData;
import undercast.client.UndercastModClass;

/**
 * @author Flv92
 */
public class UndercastKillsHandler {

    private String killer;
    private boolean killOrKilled;

    public UndercastKillsHandler() {
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

    public void handleMessage(String message, String username, EntityPlayer player, String unstripedMessage) {
        // When you die from someone
        if (UndercastConfig.showAchievements && UndercastConfig.showDeathAchievements && message.startsWith(username) && !message.toLowerCase().contains(" the game") && !message.toLowerCase().endsWith(" team") && (message.contains(" by ") || message.contains(" took ") || message.contains("fury of"))) {
            if (!message.contains("fury of") && !message.contains("took ")) {
                killer = message.substring(message.indexOf("by") + 3, message.lastIndexOf("'s") == -1 ? message.length() : message.lastIndexOf("'s"));
                // cut the distance message
                if (killer.contains(" ")) {
                    killer = killer.substring(0, killer.indexOf(' '));
                }
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
                boolean revengeAchievementShown = false;
                if (UndercastConfig.showRevengeAchievement) {
                    // add your killer to the list so it can be detected if you take revenge
                    UndercastData.killerList.add(killer);
                    for (int c = 0; c < UndercastData.victimList.size(); c++) {
                        // test if the killer took revenge
                        if (UndercastData.victimList.get(c).equals(killer)) {
                            this.printRevengeAchievement();
                            revengeAchievementShown = true;
                            UndercastData.victimList.remove(c);
                            break;
                        }
                    }
                }

                if (!revengeAchievementShown) {
                    this.printAchievement();
                }
            }
        } // if you kill a person
        else if (!message.toLowerCase().contains(" the game") && (message.contains("by " + username) || message.contains("took " + username) || message.contains("fury of " + username)) && !message.toLowerCase().contains(" destroyed by ") && !message.toLowerCase().contains("created by")) {
            killer = message.substring(0, message.indexOf(" "));
            killOrKilled = true;
            if (UndercastCustomMethods.isTeamkill(unstripedMessage, killer, username) && UndercastConfig.showAchievements && UndercastConfig.showKillAchievements) {
                this.printTeamKillAchievement();
            } else {
                // check if there is a special kill coming
                int kills = (int) UndercastData.getKills() + UndercastData.stats.kills;
                if (UndercastConfig.displaySpecialKillMessages) {
                    if (isSpecialKill(kills + 10)) {
                        sendMessage("[UndercastMod] Your are \u00A7c10\u00A7f kills away from a \u00A7ospecial kill\u00A7r (" + (kills + 10) + ")");
                    } else if (isSpecialKill(kills + 5)) {
                        sendMessage("[UndercastMod] Your are \u00A7c5\u00A7f kills away from a \u00A7ospecial kill\u00A7r (" + (kills + 5) + ")");
                    } else if (isSpecialKill(kills + 2)) {
                        sendMessage("[UndercastMod] Your are \u00A7c2\u00A7f kills away from a \u00A7ospecial kill\u00A7r (" + (kills + 2) + ")");
                    } else if (isSpecialKill(kills + 1)) {
                        sendMessage("[UndercastMod] Your are \u00A7c1\u00A7f kill away from a \u00A7ospecial kill\u00A7r (" + (kills + 1) + ")");
                    }
                }
                if (isSpecialKill(kills)) {
                    if (UndercastConfig.displaySpecialKillMessages) {
                        sendMessage("[UndercastMod] \u00A7lSPECIAL KILL(" + kills + "): \u00A7c" + killer);
                    }
                    SpecialKillLogger.logSpecialKill(kills, killer, UndercastData.server, UndercastData.map);
                }
                // check if the kill was the longest bow kill
                if (message.contains("blocks)")) {
                    String length = message.substring(message.indexOf("(") + 1, message.indexOf(" blocks)"));
                    try {
                        int l = Integer.valueOf(length);
                        if (l > UndercastConfig.longestBowKill) {
                            UndercastConfig.longestBowKill = l;
                            UndercastConfig.setIntProperty("longestBowKill", l);
                            sendMessage("[UndercastMod] \u00A7lNEW LONGEST BOW KILL: \u00A7c" + l + " blocks");
                            SpecialKillLogger.logLongestBowKill(length, killer, UndercastData.server, UndercastData.map);
                        }
                    } catch (Exception e) {
                    }
                }

                if (!(UndercastConfig.showAchievements && UndercastConfig.showKillAchievements)) {
                    return;
                }
                boolean revengeAchievementShown = false;
                if (UndercastConfig.showRevengeAchievement) {
                    // add the victim to the revenge list in case it takes revenge
                    UndercastData.victimList.add(killer);
                    for (int c = 0; c < UndercastData.killerList.size(); c++) {
                        // test if the player took revenge
                        if (UndercastData.killerList.get(c).equals(killer)) {
                            this.printRevengeAchievement();
                            revengeAchievementShown = true;
                            UndercastData.killerList.remove(c);
                            break;
                        }
                    }
                }
                if (!revengeAchievementShown) {
                    this.printAchievement();
                }
            }
            UndercastData.isLastKillFromPlayer = true;
            if (UndercastData.isNextKillFirstBlood) {
                if (UndercastConfig.showFirstBloodAchievement) {
                    printFirstBloodAchievement();
                }
                UndercastData.isNextKillFirstBlood = false;
            }
        } // when you die, but nobody killed you.
        else if (UndercastConfig.showAchievements && UndercastConfig.showDeathAchievements && message.startsWith(username) && !message.toLowerCase().contains(" the game") && !message.toLowerCase().endsWith(" team")) {
            killer = username;
            killOrKilled = false;
            this.printAchievement();
        } else if (message.toLowerCase().contains("game over")) {
            if (UndercastData.isLastKillFromPlayer && UndercastConfig.showLastKillAchievement && UndercastConfig.showAchievements) {
                printLastKillAchievement();
            }
        } // When someone die
        else if ((message.contains("by ") || message.contains("took ") || message.contains("fury of ")) && !message.toLowerCase().endsWith(" team")) {
            UndercastData.isLastKillFromPlayer = false;
            UndercastData.isNextKillFirstBlood = false;
        }
    }

    private void printAchievement() {
        UndercastAchievement ac = new UndercastAchievement(killer, killOrKilled);
        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(ac);
    }

    private void printTeamKillAchievement() {
        UndercastAchievement ac = new UndercastAchievement(killer, killOrKilled ? "\u00A7a" + killer : "\u00A74" + killer, killOrKilled ? "\u00A7aTeam Kill" : "\u00A74Team Kill");
        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(ac);
    }

    private void printFirstBloodAchievement() {
        Minecraft client = Minecraft.getMinecraft();
        UndercastAchievement ac = new UndercastAchievement(client.thePlayer.getName(), "\u00A7a" + client.thePlayer.getName(), "\u00A7agot the first Blood!");
        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(ac);
    }

    private void printLastKillAchievement() {
        Minecraft client = Minecraft.getMinecraft();
        UndercastAchievement ac = new UndercastAchievement(client.thePlayer.getName(), "\u00A7a" + client.thePlayer.getName(), "\u00A7agot the last Kill!");
        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(ac);
    }

    private void printRevengeAchievement() {
        UndercastAchievement ac = new UndercastAchievement(killer, (killOrKilled ? "\u00A7a" : "\u00A74") + killer, killOrKilled ? "\u00A7aRevengekill!" : "\u00A74 took Revenge!");
        UndercastModClass.getInstance().guiAchievement.queueTakenAchievement(ac);
    }

    private void sendMessage(String text) {
        IChatComponent thingy = new ChatComponentText(text);
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.addChatMessage(thingy);
    }
}
