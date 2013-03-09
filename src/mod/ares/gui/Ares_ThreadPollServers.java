package mod.ares.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

public class Ares_ThreadPollServers extends Thread {

	public String MOTD = "???";
	public String gameVersion = "???";
	public String serverIP = "";
	public int port = 25665;
	public String populationInfo = "???";
	public String pingToServer = "???";

	public Ares_ThreadPollServers(String server, int port){
		this.serverIP=server;
		this.port=port;
	}
	public void run() {
			long var1 = System.nanoTime();
			//get the server info
			try {
			pollServer(serverIP,port);
			}catch(Exception e){
				e.printStackTrace();
			}
            long var3 = System.nanoTime();
            long temp = (var3 - var1) / 1000000L;
            pingToServer = String.valueOf(temp);
	}

	public void pollServer(String server, int port) throws IOException {
		Socket var2 = null;
		DataInputStream var3 = null;
		DataOutputStream var4 = null;

		try {
			var2 = new Socket();
			var2.setSoTimeout(3000);
			var2.setTcpNoDelay(true);
			var2.setTrafficClass(18);
			var2.connect(new InetSocketAddress(server, port),
					3000);
			var3 = new DataInputStream(var2.getInputStream());
			var4 = new DataOutputStream(var2.getOutputStream());
			var4.write(254);
			var4.write(1);

			if (var3.read() != 255) {
				throw new IOException("Bad message");
			}

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
			//if server is 1.4.7 get info
			if (var5.startsWith("\u00a7") && var5.length() > 1) {
				var26 = var5.substring(1).split("\u0000");

				if (MathHelper.parseIntWithDefault(var26[0], 0) == 1) {
					MOTD = var26[3];
					gameVersion = var26[2];
					var8 = MathHelper.parseIntWithDefault(var26[4], 0);
					var9 = MathHelper.parseIntWithDefault(var26[5], 0);

					if (var8 >= 0 && var9 >= 0) {
						populationInfo = "§f" + var8
								+ "\u00a78/" + var9;
					} else {
						populationInfo = "\u00a78???";
					}
				} else {
					gameVersion = "???";
					MOTD = "\u00a78???";
					populationInfo = "\u00a78???";
				}
			} 
			//if the server is 1.3 get its info
			else {
				var26 = var5.split("\u00a7");
				var5 = var26[0];
				var8 = -1;
				var9 = -1;

				try {
					var8 = Integer.parseInt(var26[1]);
					var9 = Integer.parseInt(var26[2]);
				} catch (Exception var24) {
					;
				}

				MOTD = "\u00a77" + var5;

				if (var8 >= 0 && var9 > 0) {
					populationInfo = "§f" + var8
							+ "\u00a78/" + var9;
				} else {
					populationInfo = "\u00a78???";
				}

				gameVersion = "1.3";
			}
		} 
		//close all the open ports to the server
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
	}
}
