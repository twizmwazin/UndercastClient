package undercast.client;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet3Chat;

public class UndercastChatHandler {

    public UndercastChatHandler(String message, String username, EntityPlayer player) {
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
        else if (message.startsWith(username)) {
            UndercastData.addDeaths(1);
            UndercastData.resetKillstreak();
        } //if you die from someone
        else if (message.startsWith(username) && (message.contains(" by ") || message.contains(" took "))) {
            UndercastData.addKilled(1);
            UndercastData.resetKillstreak();
        } //if you kill a person
        else if (message.contains("by " + username) || message.contains("took " + username) || message.contains("fury of " + username)) {
            UndercastData.addKills(1);
            UndercastData.addKillstreak(1);
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
        } else if (!message.startsWith("<") && message.toLowerCase().contains("the match has started")) {
            UndercastData.isGameOver = false;
        } //when a map is done. Display all the stats
        else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second")) {
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
            UndercastData.setServer(message.replace("You are currently on ", ""));
            UndercastCustomMethods.handleServerSwap();
        } else if (message.equals(" ")) {
            if (!UndercastData.welcomeMessageExpected) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/server");
            } else {
                UndercastData.welcomeMessageExpected = false;
            }
            if (UndercastConfig.matchOnServerJoin) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/match");
            }
        }
    }

    public static String handleTip(Packet3Chat packet) {
        if (packet.message.contains("Tip") && UndercastConfig.filterTips) {
            return null;
        }
        return packet.message;
    }
}