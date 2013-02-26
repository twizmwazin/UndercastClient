package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class AresCustomMethods 
{
  mod_Ares ares = new mod_Ares();
	public void getMap(Minecraft mc)
	{
		if(mc.getServerData().serverMOTD != null)
		{
			ares.map = mc.getServerData().serverMOTD;
			ares.map = ares.map.replace(">>", "");
			ares.map = ares.map.replace("<<", "");
		}
	}
	private static double round(double d)
	{
		d = d * 100;
		d = Math.round(d);
		d = d / 100;
		return d;
	}
	
	protected double getKD()
	{
		if(ares.kills == ares.deaths)
		{
			return ares.kills;
		}
		else
		{
			return round(ares.kills / ares.deaths);
		}
	}

	protected double getKK() 
	{
		if(ares.kills == ares.killed)
		{
			return ares.kills;
		}
		else
		{
			return round(ares.kills / ares.killed);
		}
	}
	
	protected void endKillStreak()
	{
		ares.killStreak = 0;
	}

}
