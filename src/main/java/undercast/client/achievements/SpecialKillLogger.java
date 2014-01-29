package undercast.client.achievements;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.util.Date;
import net.minecraft.client.Minecraft;

public class SpecialKillLogger {

    private static String LOG_PATH;
    private final static String LOG_FILE_NAME = "SpecialKills.log";

    static {
        try {
            LOG_PATH = Minecraft.getMinecraft().mcDataDir.getCanonicalPath() + File.separatorChar + "logs" + File.separatorChar + "UndercastClient" + File.separatorChar;
        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to get log path.");
        }
    }

    public static void logSpecialKill(int kill, String name, String server, String map) {
        DateFormat df = DateFormat.getDateTimeInstance();
        String currentDate = df.format(new Date());
        File log;
        FileWriter writer = null;

        try {
            log = new File(LOG_PATH + LOG_FILE_NAME);
            if (!log.exists()) {
                File folder = new File(LOG_PATH);
                folder.mkdirs();
                log.createNewFile();
            }

            writer = new FileWriter(log, true);
            writer.write("Special Kill: " + kill + " Player: " + name + " Map: " + map + " Server: " + server + " Date: " + currentDate + "\r\n");

        } catch (Exception e) {
            System.out.println("[UndercastMod]: Failed to open / write to the log file");
            System.out.println("[UndercastMod]: Exception: " + e.toString());
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
