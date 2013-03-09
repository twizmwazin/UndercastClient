package mod.ares;


import net.minecraft.entity.player.EntityPlayer;

public class AresChatHandler 
{
	public AresChatHandler(String message, String username, EntityPlayer player)
	{
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

		else if(message.equalsIgnoreCase("welcome to project ares"))
		{
			AresVariablesHandler.setMap(AresCustomMethods.methods.getMap());
		}
		else if(message.contains("Now playing"))
		{
			message = message.replace("Now playing ", "");
			AresVariablesHandler.setMap(message.split(" by ")[0]);
		}
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
		else if (message.startsWith(username + " was"))
		{
			AresVariablesHandler.addKilled(1);
			AresVariablesHandler.addDeaths(1);
			AresVariablesHandler.setKillstreak(0);
		}
		else if (message.startsWith(username) && !message.contains("scored"))
		{
			AresVariablesHandler.setKillstreak(0);
			AresVariablesHandler.addDeaths(1);
		}

		else if(message.contains("was shot by " + username) || message.contains("was blown up by " + username) || message.contains("was slain by " + username))
		{
			AresVariablesHandler.addKills(1);
			AresVariablesHandler.addKillstreak(1);
		}
		else if (message.contains("You joined the"))
		{
			AresVariablesHandler.setKills(0);
			AresVariablesHandler.setKilled(0);
			AresVariablesHandler.setDeaths(0);
			AresVariablesHandler.setKillstreak(0);
			AresVariablesHandler.setTeam(message.replace("You joined the ", ""));
		}

		else if (!message.startsWith("<") && message.toLowerCase().contains("cycling to") && message.contains("1 second"))
		{
			player.addChatMessage("-------------- Final Stats --------------");
			player.addChatMessage("-------------- Kills: " + AresVariablesHandler.getKills() + "--------------");
			player.addChatMessage("-------------- Deaths: " + AresVariablesHandler.getDeaths() + "--------------");
			player.addChatMessage("-------------- K/D: " + AresCustomMethods.methods.getKD() + "--------------");
			AresVariablesHandler.setKills(0);
			AresVariablesHandler.setKilled(0);
			AresVariablesHandler.setDeaths(0);
			AresVariablesHandler.setKillstreak(0);
			AresVariablesHandler.setTeam("Observers");
		}
	}
}
