package undercast.client;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

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
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.stats.Achievement;

public class UndercastChatHandler {

    public UndercastChatHandler() {
    }

    public boolean handleMessage(String message, String username, EntityPlayer player){
        return handleMessage(message,username,player,message);
    }
    /**
     * handle a chat message received by the playe
     *
     * @param message The message received
     * @param username The player's username receiving the message
     * @param player an EntityPlayer instance linking to the client-player
     * @return true if the message should be displayed to the user
     */
    public boolean handleMessage(String message, String username, EntityPlayer player, String normalMessage) {
        boolean returnStatement = true;
        //Friend tracking Joining.
        if (message.contains(" joined the game")) {
            String name;
            message = message.replace(" joined the game", "");
            if (message.contains("[")) {
                name = message.split(" ")[1];
            } else {
                name = message;
            }

            UndercastData.addFriend(name);
        } //friend traking. Leaving
        else if (message.contains("left the game")) {
            String name;
            message = message.replace(" left the game", "");
            if (message.contains("[")) {
                name = message.split(" ")[1];
            } else {
                name = message;
            }
            if (UndercastData.isFriend(name)) {
                UndercastData.removeFriend(name);
            }
        } //update what map you are playing on
        else if (message.contains("Now playing")) {
            message = message.replace("Now playing ", "");
            UndercastData.setMap((message.split(" by ")[0]));
            if (UndercastData.getKills() == 0 && UndercastData.getDeaths() == 0) { // new match or observer or noob
                UndercastData.reload();
            }
        } //if you die
        else if (message.startsWith(username) && !message.toLowerCase().endsWith(" team")) {
            // if you die form someone
            if ((message.contains(" by ") || message.contains(" took ") || message.contains(" fury of"))) {
                UndercastData.addKilled(1);
            }
            UndercastData.addDeaths(1);
            UndercastData.resetKillstreak();
        } //if you kill a person
        else if ((message.contains("by " + username) || message.contains("took " + username) || message.contains("fury of " + username))
                && !message.toLowerCase().contains(" destroyed by ")) {
            if (UndercastData.isNextKillFirstBlood) {
                if (UndercastConfig.showFirstBloodAchievement) {
                    printFirstBloodAchievement();
                }
                UndercastData.isNextKillFirstBlood = false;
            }
            UndercastData.addKills(1);
            UndercastData.addKillstreak(1);
        } //When someone die
        else if ((message.contains("by ") || message.contains("took ") || message.contains("fury of ")) && !message.toLowerCase().endsWith(" team")) {
            UndercastData.isNextKillFirstBlood = false;
        } //when you join a match
        else if (message.contains("You joined the")) {
            try {
                UndercastData.setTeam(UndercastData.Teams.valueOf(message.replace("You joined the ", "").replace(" Team", "").replace(" team", "")));
            } catch (Exception e) {
                // if the team set fails because of an alias, set the team to Unknown
                UndercastData.setTeam(UndercastData.Teams.Unknown);
            }
        } else if (!message.startsWith("<") && message.toLowerCase().contains("game over")) {
            UndercastData.isGameOver = true;
            UndercastData.isNextKillFirstBlood = false;
            if (isLastKillYourKill()) {
                printLastKillAchievement();
            }

        } else if (!message.startsWith("<") && message.toLowerCase().contains("the match has started")) {
            UndercastData.isGameOver = false;
            UndercastData.isNextKillFirstBlood = true;
        } //when a map is done. Display all the stats
        else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second")) {
            player.addChatMessage(normalMessage);
            player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
            player.addChatMessage("Final Stats:");
            player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
            player.addChatMessage("Kills: " + UndercastData.getKills());
            player.addChatMessage("Deaths: " + UndercastData.getDeaths());
            player.addChatMessage("K/D: " + UndercastCustomMethods.getKD());
            player.addChatMessage("Kill Streak: " + UndercastData.getLargestKillstreak());
            UndercastData.resetKills();
            UndercastData.resetKilled();
            UndercastData.resetDeaths();
            UndercastData.resetKillstreak();
            UndercastData.resetLargestKillstreak();
            UndercastData.setTeam(UndercastData.Teams.Observers);
            return false;
        } //sends /match when you join a server.
        else if (message.contains("Welcome to the Overcast Network")) {
            if (UndercastData.redirect && UndercastData.server.equalsIgnoreCase("lobby")) {
                UndercastData.redirect = false;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server " + UndercastData.directionServer);
            }
        } //server detection
        else if (message.contains("Teleporting you to ")) {
            UndercastData.setServer(message.replace("Teleporting you to ", ""));
            if (!message.toLowerCase().contains("lobby")) {
                UndercastData.welcomeMessageExpected = true;
            }
            UndercastCustomMethods.handleServerSwap();
        } else if (message.contains("You are currently on ")) {
            if (UndercastData.serverDetectionCommandExecuted) {
                UndercastData.serverDetectionCommandExecuted = false;
                UndercastData.setServer(message.replace("You are currently on ", ""));
                UndercastCustomMethods.handleServerSwap();
            }
        } else if (message.equals(" ")) {
            if (!UndercastData.welcomeMessageExpected) {
                UndercastData.serverDetectionCommandExecuted = true;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server");
            } else {
                UndercastData.welcomeMessageExpected = false;
            }
            if (UndercastConfig.matchOnServerJoin) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/match");
            }
        }
        return returnStatement;
    }

    public static String handleTip(Packet3Chat packet) {
        try {
            if (packet.message.contains("Tip") && UndercastConfig.filterTips) {
                return null;
            }
            return packet.message;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static void printFirstBloodAchievement() {
        final long waitingTime;
        if (UndercastConfig.showAchievements && UndercastConfig.showKillAchievements) {
            waitingTime = 4000L;
        } else {
            waitingTime = 0L;
        }
        UndercastKillsHandler.killerBuffer = UndercastKillsHandler.steveHeadBuffer;
        //Thread charged to load the achievment gui
        Runnable r1 = new Runnable() {
            public void run() {
                URLConnection spoof = null;
                try {
                    System.out.println("Beginning");
                    spoof = new URL("https://minotar.net/helm/" + FMLClientHandler.instance().getClient().thePlayer.username + "/16.png").openConnection();
                    spoof.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                    UndercastKillsHandler.killerBuffer = ((BufferedImage) ImageIO.read(spoof.getInputStream()));
                    System.out.println("finished");
                } catch (Exception ex) {
                    Logger.getLogger(UndercastKillsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Runnable r2 = new Runnable() {
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
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }

    public static boolean isLastKillYourKill() {
        String username = FMLClientHandler.instance().getClient().thePlayer.username;
        for (int i = 0; i < UndercastModClass.lastChatLines.length; i++) {
            String message = UndercastModClass.lastChatLines[i];
            if (message != null) {
                if (message.contains("by ") || message.contains("took ") || message.contains("fury of ")) {
                    if ((message.contains("by " + username) || message.contains("took " + username) || message.contains("fury of " + username))) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;

    }

    public static void printLastKillAchievement() {
        UndercastKillsHandler.killerBuffer = UndercastKillsHandler.steveHeadBuffer;
        //Thread charged to load the achievment gui
        Runnable r1 = new Runnable() {
            public void run() {
                URLConnection spoof = null;
                try {
                    System.out.println("Beginning");
                    spoof = new URL("https://minotar.net/helm/" + FMLClientHandler.instance().getClient().thePlayer.username + "/16.png").openConnection();
                    spoof.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                    UndercastKillsHandler.killerBuffer = ((BufferedImage) ImageIO.read(spoof.getInputStream()));
                    System.out.println("finished");
                } catch (Exception ex) {
                    Logger.getLogger(UndercastKillsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Runnable r2 = new Runnable() {
            public void run() {
                Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
                Minecraft client = FMLClientHandler.instance().getClient();
                ((UndercastGuiAchievement) client.guiAchievement)
                        .addFakeAchievementToMyList(custom, true, client.thePlayer.username, client.thePlayer.username, "got the last Kill!");

            }
        };
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
    }
}
