package net.minecraft.src;
//You may not release this source under any condition, it must be linked to this page
//You may recompile and publish as long as skipperguy12 and Guru_Fraser are given credit
//You may not claim this to be your own
//You may not remove these comments

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
	private static Minecraft mc = Minecraft.getMinecraft();

	/**
	 * Gets the current map (multi thread) Sets it to the AresVariable map variable
	 */
	protected static void getMap() {
		Thread thread = new Thread() {
			public void run() {

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
					var2.connect(
							new InetSocketAddress(server, Integer
									.parseInt(port)), 3000);
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

				AresVariablesHandler.setMap(map);
			}
		};
		thread.start();
	}

	// simple rounding method
	private static double round(double d) {
		d = d * 100;
		d = Math.round(d);
		d = d / 100;
		return d;
	}

	/**
	 * Calculates the KD Ratio
	 * 
	 * @return KD double rounded
	 */
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

	/**
	 * Calculates the KK Ratio
	 * 
	 * @return KK double rounded
	 */
	protected static double getKK() {
		double k = AresVariablesHandler.getKills();
		double kk = AresVariablesHandler.getKilled();
		if (k == kk && k == 0) {
			return 0D;
		} else if (k > 0 && kk == 0) {
			return k;
		} else if (k == kk && kk > 0) {
			return k;
		} else {
			return round(k / kk);
		}
	}

	/**
	 * Gets the Ares server you are playing on
	 * 
	 * @param currentServer  The ip of the server that you are on
	 * @return The string of the server you are on
	 */
	protected static String getServer(String currentServer) {
		if (currentServer.contains("alpha"))
			return "Alpha";
		else if (currentServer.contains("beta"))
			return "Beta";
		else if (currentServer.contains("gamma"))
			return "Gamma";
		else if (currentServer.contains("delta"))
			return "Delta";
		else if (currentServer.contains("epsilon"))
			return "Epsilon";
		else if (currentServer.contains("theta"))
			return "Theta";
		else if (currentServer.contains("eta"))
			return "Eta";
		else if (currentServer.contains("iota"))
			return "Iota";
		else if (currentServer.contains("kappa"))
			return "Kappa";
		else if (currentServer.contains("lambda"))
			return "Lambda";
		else if (currentServer.contains("nostalgia"))
			return "Nostalgia";
		else
			return "???";
	}

}
