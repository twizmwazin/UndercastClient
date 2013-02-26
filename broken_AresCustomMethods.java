package net.minecraft.src;

import net.minecraft.client.Minecraft;

public static class AresCustomMethods
{
	public void getMap(Minecraft mc)
	{
		if(mc.getServerData().serverMOTD != null)
		{
			mod_Ares.map = mc.getServerData().serverMOTD;
			mod_Ares.map = mod_Ares.map.replace(">>", "");
			mod_Ares.map = mod_Ares.map.replace("<<", "");
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
		if(mod_Ares.kills == mod_Ares.deaths)
		{
			return 1.00D;
		}
		else
		{
			return round(mod_Ares.kills / mod_Ares.deaths);
		}
	}

	protected double getKK() 
	{
		if(mod_Ares.kills == mod_Ares.killed)
		{
			return 1.00D;
		}
		else
		{
			return round(mod_Ares.kills / mod_Ares.killed);
		}
	}
	
	protected void endKillStreak()
	{
		mod_Ares.killStreak = 0;
	}

}
