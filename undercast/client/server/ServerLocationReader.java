package undercast.client.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import undercast.client.UndercastConfig;
import undercast.client.UndercastData;
import undercast.client.UndercastData.ServerLocation;

public class ServerLocationReader {
    private static boolean isReading = false;
    private static boolean hasFinishedReading = false;
    private static LocationReaderDelegate delegate;
    private static ArrayList<String> names = new ArrayList<String>(40);
    private static ArrayList<ServerLocation> loactions = new ArrayList<UndercastData.ServerLocation>(40);
    private static final String FILE_NAME = "location_cache.txt";
    public static String CONFIG_PATH;

    public void read() {
        isReading = true;
        BufferedReader reader = null;
        String line;
        ServerLocation currentLocation = ServerLocation.Both;
        try {
            if (!(new File(CONFIG_PATH + FILE_NAME).exists())) {
                isReading = false;
                downloadTheLatestVersion();
                return;
            }
            reader = new BufferedReader(new FileReader(CONFIG_PATH + FILE_NAME));
            line = reader.readLine();
            if (!line.equalsIgnoreCase("[VERSION]")) {
                throw new Exception("File doesn't start with [VERSION]");
            }
            line = reader.readLine();
            UndercastData.localLocationCacheVersion = Integer.parseInt(line);

            // if there is a new version
            if (compareLocaleAndRemoteVersion()) {
                // close the file
                try {
                    reader.close();
                } catch (Exception e) {
                }
                isReading = false;
                // download it (this will read it afterwards
                downloadTheLatestVersion();
                return;
            }

            System.out.println("[UndercastMod]: Reading the local location cache...");

            line = reader.readLine();
            while (line != null) {
                if (line.equalsIgnoreCase("[BOTH]")) {
                    currentLocation = ServerLocation.Both;
                } else if (line.equalsIgnoreCase("[EU]")) {
                    currentLocation = ServerLocation.EU;
                } else if (line.equalsIgnoreCase("[US]")) {
                    currentLocation = ServerLocation.US;
                } else {
                    names.add(line);
                    loactions.add(currentLocation);
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to read the location cache");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
        isReading = false;
        hasFinishedReading = true;
        delegate.locationReaderFinished();
    }

    public static ServerLocation getLocationForServer(String name) {
        if (!hasFinishedReading) {
            return ServerLocation.Both;
        }
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equals(name)) {
                return loactions.get(i);
            }
        }
        return ServerLocation.Both;
    }

    public static void setDelegate(LocationReaderDelegate d) {
        delegate = d;
    }

    public static boolean compareLocaleAndRemoteVersion() {
        if (UndercastData.localLocationCacheVersion == -1 || UndercastData.remoteLocationCacheVersion == -1) {
            return false;
        }
        if (UndercastData.localLocationCacheVersion < UndercastData.remoteLocationCacheVersion) {
            return true;
        }
        return false;
    }

    public static void downloadTheLatestVersion() {
        // if the mod is currently reading, delay the update
        if (isReading) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                    ServerLocationReader.downloadTheLatestVersion();
                    super.run();
                }
            };
            t.start();
            return;
        }
        BufferedWriter writer = null;
        BufferedReader reader = null;
        URL data;
        String line;
        try {
            data = new URL("https://raw.github.com/UndercastTeam/UndercastClient/master/locations.txt");
            writer = new BufferedWriter(new FileWriter(new File(CONFIG_PATH + FILE_NAME)));
            reader = new BufferedReader(new InputStreamReader(data.openStream()));
            line = reader.readLine();

            // copy the data line by line
            while (line != null) {
                writer.write(line + "\r\n");
                line = reader.readLine();
            }

        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to download the latest version of the cache");
            System.out.println("[UndercastMod]: ERROR: " + e.toString());
        } finally {
            try {
                writer.close();
                reader.close();
            } catch (Exception e) {
            }
        }

        // renew the data
        new ServerLocationReader().read();
    }

    static {
        try {
            CONFIG_PATH = Minecraft.getMinecraft().mcDataDir.getCanonicalPath() + File.separatorChar + "config" + File.separatorChar + "UndercastClient" + File.separatorChar;
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to get mod path.");
        }
    }
}
