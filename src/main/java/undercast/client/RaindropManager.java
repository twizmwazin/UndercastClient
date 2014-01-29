package undercast.client;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Scoreboard;

public class RaindropManager {
    public static int TotalRaindrops;
    public static int RaindropsThisMatch;
    public static RaindropManager manager;

    // this reads the current amount of raindrops from
    // the scoreboard, it'll only be read the when you
    // connect to OCN because it's not easy to read it
    // when you get back to the lobby since previous
    // scoreboard objectives aren't cleared.
    public RaindropManager() {
        manager = this;
        Thread thread = new Thread() {
            @Override
            public void run() {
                for(int i=0;i<60;i++) {
                    Scoreboard sb = Minecraft.getMinecraft().thePlayer.getWorldScoreboard();
                    Collection obj = sb.getObjectiveNames();
                    if(!obj.isEmpty()) {
                        readScoreboard();
                        return;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                }
                super.run();
            }
        };
        thread.start();
    }

    /**
     * Only works if there is only one number as an objective name.
     */
    private void readScoreboard() {
        Scoreboard sb = Minecraft.getMinecraft().thePlayer.getWorldScoreboard();
        Object[] obj = new Object[1];
        obj = sb.getObjectiveNames().toArray(obj);
        int raindrops = -1;
        for(int i=0; i<obj.length + 1; i++) {
            try {
                String rds = String.valueOf(obj[i]).replaceAll(" ", "").replace(",","");
                Integer rd = Integer.parseInt(rds);
                raindrops = rd;
            } catch(Exception e) {
                //ignore this. It'll happen for each non number in the collection
            }
        }
        if(raindrops == -1) {
            System.out.println("[UndercastMod]: Failed to read the raindrop count");
            raindrops = 0;
        }
        TotalRaindrops = raindrops;
    }

    /**
     * @return ture if the message has to be canceled
     */
    public boolean handleChatMessage(String message) {
        if(message.startsWith("+") && message.contains("Raindrops")) {
            try {
                RaindropsThisMatch += Integer.parseInt(message.substring(1,message.indexOf(" R")));
            } catch(Exception e) {
            }
        }
        return false;
    }
    public void onMatchEnd() {
        TotalRaindrops += RaindropsThisMatch;
        RaindropsThisMatch = 0;
    }
}
