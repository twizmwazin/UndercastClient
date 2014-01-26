package undercast.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class FinalStats {
    private int kills;
    private int totalKills;
    private int deaths;
    private int totalDeaths;
    private double kd;
    private String totalKD;
    private double killstreak;
    private int coreDifference;
    private int woolDifference;
    private int monumentDifference;
    private int totalCores;
    private int totalWools;
    private int totalMonuments;

    public FinalStats() {
        this.kills = (int) UndercastData.getKills();
        this.totalKills = (int) (UndercastData.kills + UndercastData.stats.kills);
        this.deaths = (int) UndercastData.getDeaths();
        this.totalDeaths = (int) (UndercastData.deaths + UndercastData.stats.deaths);
        this.kd = UndercastCustomMethods.getKD();
        this.totalKD = "";
        if (UndercastConfig.realtimeStats) {
            this.totalKD = "\u00A7f Total: \u00A73" + (((UndercastData.kills + UndercastData.stats.kills) / (UndercastData.deaths + UndercastData.stats.deaths)));
            try {
                this.totalKD = totalKD.substring(0, totalKD.lastIndexOf('.') + 4);
            } catch (Exception e) {
            }
        }
        this.killstreak = (int) UndercastData.getLargestKillstreak();
        UndercastData.isObjectiveReload = true;
        UndercastData.reloadStats();
    }

    public void addObjectives(int coreDiff, int totalCores, int woolDiff, int totalWools, int monumentDiff, int totalMonument) {
        this.coreDifference = coreDiff;
        this.totalCores = totalCores;
        this.woolDifference = woolDiff;
        this.totalWools = totalWools;
        this.monumentDifference = monumentDiff;
        this.totalMonuments = totalMonument;
    }

    public void showStats() {
        sendMessage("\u00A76\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
        sendMessage("\u00A79Final Stats:");
        sendMessage("\u00A76\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
        sendMessage("Kills: \u00A7a" + this.kills + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A7a" + this.totalKills) : ""));
        sendMessage("Deaths: \u00A74" + this.deaths + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A74" + this.totalDeaths) : ""));
        sendMessage("K/D: \u00A73" + this.kd + totalKD);
        sendMessage("Raindrops: \u00A7b" + RaindropManager.RaindropsThisMatch + "\u00A7f Total: \u00A7b" + (RaindropManager.TotalRaindrops + RaindropManager.RaindropsThisMatch));
        RaindropManager.manager.onMatchEnd();
        sendMessage("Kill Streak: \u00A7e" + this.killstreak);
        if (this.woolDifference > 0) {
            sendMessage("Wools: \u00A75+" + this.woolDifference + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A75" + this.totalWools) : ""));
        }
        if (this.coreDifference > 0) {
            sendMessage("Cores: \u00A75+" + this.coreDifference + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A75" + this.totalCores) : ""));
        }
        if (this.monumentDifference > 0) {
            sendMessage("Monuments: \u00A75+" + this.monumentDifference + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A75" + this.totalMonuments) : ""));
        }
        sendMessage("\u00A76\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
    }
    
    private void sendMessage(String text) {
    	IChatComponent thingy = new ChatComponentText(text);
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        player.func_146105_b(thingy);
    }
}
