package net.minecraft.src;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ChatAllowedCharacters;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Packet;
import net.minecraft.src.StringUtils;

public class AresCustomMethods {
	protected static AresCustomMethods methods;
	private static Minecraft mc = Minecraft.getMinecraft();

	protected static String getMap() {
		String map;
		mc = Minecraft.getMinecraft();
		Socket var2 = null;
		DataInputStream var3 = null;
		DataOutputStream var4 = null;

		try {
			var2 = new Socket();
			var2.setSoTimeout(3000);
			var2.setTcpNoDelay(true);
			var2.setTrafficClass(18);

			String server = mc.getServerData().serverIP.substring(0,
					mc.getServerData().serverIP.indexOf(":"));
			String port = mc.getServerData().serverIP.substring(mc
					.getServerData().serverIP.indexOf(":") + 1);
			var2.connect(new InetSocketAddress(server, Integer.parseInt(port)),
					3000);
			var3 = new DataInputStream(var2.getInputStream());
			var4 = new DataOutputStream(var2.getOutputStream());
			var4.write(254);
			var4.write(1);
			var3.read();

			String var5 = Packet.readString(var3, 256);
			char[] var6 = var5.toCharArray();

			for (int var7 = 0; var7 < var6.length; ++var7) {
				if (var6[var7] != 167
						&& var6[var7] != 0
						&& ChatAllowedCharacters.allowedCharacters
								.indexOf(var6[var7]) < 0) {
					var6[var7] = 63;
				}
			}

			var5 = new String(var6);
			int var8;
			int var9;
			String[] var26;
			// if server is 1.4.7 get info
			var26 = var5.substring(1).split("\u0000");

			if (MathHelper.parseIntWithDefault(var26[0], 0) == 1) {
				map = var26[3];
			} else {
				map = "???";
			}
		} catch (Exception e) {
			map = "???";
			e.printStackTrace();
		}
		// close all the open ports to the server
		finally {
			try {
				if (var3 != null) {
					var3.close();
				}
			} catch (Throwable var23) {
				;
			}

			try {
				if (var4 != null) {
					var4.close();
				}
			} catch (Throwable var22) {
				;
			}

			try {
				if (var2 != null) {
					var2.close();
				}
			} catch (Throwable var21) {
				
			}
		}

		// clean the map info
		map = StringUtils.stripControlCodes(map);
		if (!map.equals("???"))
			map = map.substring(2, map.length() - 2);
		// map = map.replace(">>","");
		// map = map.replace("<<", "");

		return map;
	}

	private static double round(double d) {
		d = d * 100;
		d = Math.round(d);
		d = d / 100;
		return d;
	}

	protected static double getKD() {
		double k = AresVariablesHandler.getKills();
		double d = AresVariablesHandler.getDeaths();
		if (k == d && k == 0) {
			return 0D;
		} else if (k > 0 && d == 0) {
			return k;
		} else if (k == d && k > 0) {
			return k;
		} else {
			return round(k / d);
		}
	}

	protected static double getKK() {
		double k = AresVariablesHandler.getKills();
		double kk = AresVariablesHandler.getKilled();
		if (k == kk && k == 0) {
			return 0D;
		} else if (k > 0 && k == 0) {
			return k;
		} else if (k == kk && kk > 0) {
			return k;
		} else {
			return round(k / kk);
		}
	}

	protected static String getServer(String s) {
		if (s.contains("alpha"))
			return "Alpha";
		else if (s.contains("beta"))
			return "Beta";
		else if (s.contains("gamma"))
			return "Gamma";
		else if (s.contains("delta"))
			return "Delta";
		else if (s.contains("epsilon"))
			return "Epsilon";
		else if (s.contains("theta"))
			return "Theta";
		else if (s.contains("eta"))
			return "Eta";
		else if (s.contains("iota"))
			return "Iota";
		else if (s.contains("kappa"))
			return "Kappa";
		else if (s.contains("lambda"))
			return "Lambda";
		else if (s.contains("nostalgia"))
			return "Nostalgia";
		else
			return "Not project ares";
	}

}
