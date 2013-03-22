package tc.oc;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.mod_Ares;

public class AresChatHandler 
{
	public AresChatHandler(String message, String username, EntityPlayer player)
	{
		//Friend tracking Joining.
		if (message.contains(" joined the game")) 
		{
			String name;
			message = message.replace(" joined the game", "");
			if(message.contains("["))
				name = message.split(" ")[1];
			else
				name = message;
			
			AresData.addFriend(name);
		}
		//friend traking. Leaving
		else if (message.contains("left the game"))
		{
			String name;
			message = message.replace(" left the game", "");
			if(message.contains("["))
			{
				name = message.split(" ")[1];
			}
			else
			{
				name = message;
			}
			if (AresData.isFriend(name))
			{
				AresData.removeFriend(name);
			}
		}
		//update what map you are playing on
		else if(message.contains("Now playing"))
		{
			message = message.replace("Now playing ", "");
			AresData.map=(message.split(" by ")[0]);
		}
		//if you die from someone
		else if(message.startsWith(username) && (message.contains(" by ") || message.contains(" took ")))
		{
			AresData.addKilled(1);
			AresData.killstreak=0;
		}
		//if you kill a person
		else if(message.contains("by " + username) || message.contains("took " + username))
		{
			AresData.addKills(1);
			AresData.addKillstreak(1);
		}
		//when you join a match
		else if (message.contains("You joined the"))
		{
			AresData.kills=0;
			AresData.killed=0;
			AresData.deaths=0;
			AresData.killstreak=0;
			AresData.largestKillstreak=0;
			AresData.team=message.replace("You joined the ", "");
		}
		//when a map is done. Display all the stats
		else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second"))
		{
			player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
			player.addChatMessage("Final Stats:");
			player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
			player.addChatMessage("Kills: " + AresData.kills);
			player.addChatMessage("Deaths: " + AresData.deaths);
			player.addChatMessage("K/D: " + AresCustomMethods.getKD());
			player.addChatMessage("Kill Streak: " + AresData.largestKillstreak);
			AresData.kills=0;
			AresData.killed=0;
			AresData.deaths=0;
			AresData.killstreak=0;
			AresData.largestKillstreak=0;
			AresData.team="Observers";
		}
		//filters [Tip] messages
		else if(message.startsWith("[Tip]") && mod_Ares.filterTips){
			Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(0);
		}
	}
}
