package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet3Chat;

public class AresChatHandler {
    public AresChatHandler(String message, String username, EntityPlayer player) {
        //Friend tracking Joining.
        if (message.contains(" joined the game")) {
            String name;
            message = message.replace(" joined the game", "");
            if (message.contains("[")) {
                name = message.split(" ")[1];
            } else {
                name = message;
            }

            AresData.addFriend(name);
        }
        //friend traking. Leaving
        else if (message.contains("left the game")) {
            String name;
            message = message.replace(" left the game", "");
            if (message.contains("[")) {
                name = message.split(" ")[1];
            } else {
                name = message;
            }
            if (AresData.isFriend(name)) {
                AresData.removeFriend(name);
            }
        }
        //update what map you are playing on
        else if (message.contains("Now playing")) {
            message = message.replace("Now playing ", "");
            AresData.setMap((message.split(" by ")[0]));
            if(AresData.getKills() == 0 && AresData.getDeaths() == 0) { // new match or observer or noob
                AresData.reload();
            }
        }
        //if you die from someone
        else if (message.startsWith(username) && (message.contains(" by ") || message.contains(" took "))) {
            AresData.addKilled(1);
            AresData.resetKillstreak();
        }
        //if you kill a person
        else if (message.contains("by " + username) || message.contains("took " + username)) {
            AresData.addKills(1);
            AresData.addKillstreak(1);
        }
        //when you join a match
        else if (message.contains("You joined the")) {
            AresData.resetKills();
            AresData.resetKilled();
            AresData.resetDeaths();
            AresData.resetKillstreak();
            ;
            AresData.resetLargestKillstreak();
            
            try {
                AresData.setTeam(AresData.Teams.valueOf(message.replace("You joined the ", "").replace(" Team", "")));
            } catch(Exception e) {
                // if the team set fails because of an alias, set the team to Unknown
                AresData.setTeam(AresData.Teams.Unknown);
            }

        }
        //when a map is done. Display all the stats
        else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second")) {
            player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
            player.addChatMessage("Final Stats:");
            player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
            player.addChatMessage("Kills: " + AresData.getKills());
            player.addChatMessage("Deaths: " + AresData.getDeaths());
            player.addChatMessage("K/D: " + AresCustomMethods.getKD());
            player.addChatMessage("Kill Streak: " + AresData.getLargestKillstreak());
            AresData.resetKills();
            AresData.resetKilled();
            AresData.resetDeaths();
            AresData.resetKillstreak();
            AresData.resetLargestKillstreak();
            AresData.setTeam(AresData.Teams.Observers);
        }
        //sends /match when you join a server.
        else if(message.equals("Welcome to Project Ares") && AresConfig.matchOnServerJoin){
        	Minecraft.getMinecraft().thePlayer.sendChatMessage("/match");
        }
    }
    public static String handleTip(Packet3Chat packet) {
        if (packet.message.contains("Tip") && AresConfig.filterTips)
        {
            return null;
        }
        return packet.message;
    }
}
