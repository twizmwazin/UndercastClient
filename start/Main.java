package start;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;

public class Main {

	/*
	 * To be authenticated when launching the game from eclipse,
	 * create a new Launch Configuration with this class as the Main class.
	 * 
	 * In the arguments tab add "--username YOUR_USERNAME --password YOUR_PASSWORD"
	 * for the program arguments.
	 * And add "-Dfml.ignoreInvalidMinecraftCertificates=true" as a VM argument.
	 * 
	 * Then launch the game !
	 */
	public static int NULL_VALUE = -1;
	public static int USERNAME_VALUE = 0;
	public static int PASSWORD_VALUE = 1;

	public static void main(String[] args) {
		int nextArg = -1;
		String PASSWORD = new String(), USERNAME = new String();
		for(int i = 0; i < args.length; i++){
			if(nextArg == NULL_VALUE){
				if(args[i].equals("--username")){
					nextArg = USERNAME_VALUE;
				} else if(args[i].equals("--password")){
					nextArg = PASSWORD_VALUE;
				}
			} else if(nextArg == USERNAME_VALUE){
				USERNAME = args[i];
				nextArg = NULL_VALUE;
			} else if (nextArg == PASSWORD_VALUE){
				PASSWORD = args[i];
				nextArg = NULL_VALUE;
			}
		}
		String json = String.format("{\"password\": \"%s\", \"agent\": {\"name\": \"Minecraft\", \"version\": 1}, \"username\": \"%s\"}",
				PASSWORD,USERNAME);

		HttpURLConnection connection = null;
		URL url;
		try {
			url = new URL("https://authserver.mojang.com/authenticate");
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream e = new DataOutputStream(connection.getOutputStream());
			e.writeBytes(json);
			e.flush();
			e.close();
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuffer response = new StringBuffer();
			String line;
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			String r = response.toString();
			String uuid = r.substring(r.indexOf("\"id\":\"") + 6, r.indexOf("\",\"name\""));
			String accessToken = r.substring(16, r.indexOf("\",\"clientToken\""));

			String newArg = "--version 1.6 --tweakClass cpw.mods.fml.common.launcher.FMLTweaker --accessToken " +  accessToken + " --username " + USERNAME + " --uuid " + uuid;
			System.out.println(newArg);
			net.minecraft.launchwrapper.Launch.main(newArg.split(" "));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSession() {
		String result = null;
		try {
			String parameters = (new StringBuilder("user=")).append(URLEncoder.encode("tuturo92", "UTF-8")).append("&password=").append(URLEncoder.encode("florian92", "UTF-8")).append("&version=").append(12).toString();
			result = createNewSession("https://login.minecraft.net/", parameters);
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(result == null) {
			return "";
		}

		if(!result.contains(":")) {
			return "";
		}
		String values[] = result.split(":");
		System.out.println(result);
		return "";
	}

	public static String createNewSession(String targetURL, String urlParameters) {
		HttpURLConnection connection = null;
		boolean trying = false;
		String rawResponse;
		label84: {
			try {
				trying = true;
				URL url = new URL(targetURL);
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				DataOutputStream e = new DataOutputStream(connection.getOutputStream());
				e.writeBytes(urlParameters);
				e.flush();
				e.close();
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				StringBuffer response = new StringBuffer();
				String line;
				while((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				rd.close();
				rawResponse = response.toString();
				trying = false;
				break label84;
			} catch (Exception var14) {
				var14.printStackTrace();
				trying = false;
			} finally {
				if(trying) {
					if(connection != null) {
						connection.disconnect();
					}
				}
			}
			if(connection != null) {
				connection.disconnect();
			}
			return null;
		}
		if(connection != null) {
			connection.disconnect();
		}
		return rawResponse;
	}
}
