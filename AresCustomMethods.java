package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class AresCustomMethods
{
	protected static AresCustomMethods methods;
	private static Minecraft mc = Minecraft.getMinecraft();
	protected static String getMap()
	{
		String map;
		mc = ModLoader.getMinecraftInstance();
		if(mc != null && mc.getServerData() != null && mc.getServerData().serverMOTD != null)
		{
			map = mc.getServerData().serverMOTD.toString();
			map = StringUtils.stripControlCodes(map);
			map = map.replace(" «", "");
			map = map.replace("» ", "");
			if(map != null)
			{
				return map;
			}
			else
			{
				return "server MOTD was null.";
			}
		}
		else
		{
			return "server MOTD was null.";
		}
	}
	
	private static double round(double d)
	{
		d = d * 100;
		d = Math.round(d);
		d = d / 100;
		return d;
	}
	
	protected static double getKD()
	{
		double k = AresVariablesHandler.getKills();
		double d = AresVariablesHandler.getDeaths();
		if(k == d && k == 0)
		{
			return 0D;
		}
		else if(k > 0 && d == 0)
		{
			return k;
		}
		else if(k == d && k > 0)
		{
			return k;
		}
		else
		{
			return round(k / d);
		}
	}

	protected static double getKK()
	{
		double k = AresVariablesHandler.getKills();
		double kk = AresVariablesHandler.getKilled();
		if(k == kk && k == 0)
		{
			return 0D;
		}
		else if(k > 0 && k == 0)
		{
			return k;
		}
		else if(k ==  kk && kk > 0)
		{
			return k;
		}
		else
		{
			return round(k / kk);
		}
	}
	
	protected static String getServer(String s)
	{
		if(s.contains("alpha"))
			return "Alpha";
		else if(s.contains("beta"))
			return "Beta";
		else if(s.contains("gamma"))
			return "Gamma";
		else if(s.contains("delta"))
			return "Delta";
		else if(s.contains("epsilon"))
			return "Epsilon";
		else if(s.contains("theta") && s.contains("25500"))
			return "Nostalgia";
		else if(s.contains("theta") && !s.contains("25500"))
			return "Theta";
		else if(s.contains("eta"))
			return "Eta";
		else if(s.contains("iota"))
			return "Iota";
		else if(s.contains("kappa"))
			return "Kappa";
		else if(s.contains("lambda"))
			return "Lambda";
		else
		return "Not project ares";
	}

}
