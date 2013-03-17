package net.minecraft.src;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

import net.minecraft.src.EntityPlayer;

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
			
			AresVariablesHandler.addFriend(name);
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
			if (AresVariablesHandler.isFriend(name))
			{
				AresVariablesHandler.removeFriend(name);
			}
		}
		//update what map you are playing on
		else if(message.contains("Now playing"))
		{
			message = message.replace("Now playing ", "");
			AresVariablesHandler.setMap(message.split(" by ")[0]);
		}
		//if you die from someone
		else if(message.startsWith(username) && message.contains(" by "))
		{
			AresVariablesHandler.addKilled(1);
			AresVariablesHandler.setKillstreak(0);
		}
		//if you kill a person
		else if(message.contains("by " + username))
		{
			AresVariablesHandler.addKills(1);
			AresVariablesHandler.addKillstreak(1);
		}
		//when you join a match
		else if (message.contains("You joined the"))
		{
			AresVariablesHandler.setKills(0);
			AresVariablesHandler.setKilled(0);
			AresVariablesHandler.setDeaths(0);
			AresVariablesHandler.setKillstreak(0);
			AresVariablesHandler.setLargestKillstreak(0);
			AresVariablesHandler.setTeam(message.replace("You joined the ", ""));
		}
		//when a map is done. Display all the stats
		else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second"))
		{
			player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
			player.addChatMessage("Final Stats:");
			player.addChatMessage("\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
			player.addChatMessage("Kills: " + AresVariablesHandler.getKills());
			player.addChatMessage("Deaths: " + AresVariablesHandler.getDeaths());
			player.addChatMessage("K/D: " + AresCustomMethods.getKD());
			player.addChatMessage("Kill Streak: " + AresVariablesHandler.getLargestKillstreak());
			AresVariablesHandler.setKills(0);
			AresVariablesHandler.setKilled(0);
			AresVariablesHandler.setDeaths(0);
			AresVariablesHandler.setKillstreak(0);
			AresVariablesHandler.setLargestKillstreak(0);
			AresVariablesHandler.setTeam("Observers");
		}
	}
}
