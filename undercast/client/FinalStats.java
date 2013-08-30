package undercast.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

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
            this.totalKD = totalKD.substring(0, totalKD.lastIndexOf('.') + 4);
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
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        player.addChatMessage("\u00A76\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
        player.addChatMessage("\u00A79Final Stats:");
        player.addChatMessage("\u00A76\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
        player.addChatMessage("Kills: \u00A7a" + this.kills + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A7a" + this.totalKills) : ""));
        player.addChatMessage("Deaths: \u00A74" + this.deaths + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A74" + this.totalDeaths) : ""));
        player.addChatMessage("K/D: \u00A73" + this.kd + totalKD);
        player.addChatMessage("Kill Streak: \u00A7e" + this.killstreak);
        if (this.woolDifference > 0) {
            player.addChatMessage("Wools: \u00A75+" + this.woolDifference + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A75" + this.totalWools) : ""));
        }
        if (this.coreDifference > 0) {
            player.addChatMessage("Cores: \u00A75+" + this.coreDifference + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A75" + this.totalCores) : ""));
        }
        if (this.monumentDifference > 0) {
            player.addChatMessage("Monuments: \u00A75+" + this.monumentDifference + ((UndercastConfig.realtimeStats) ? ("\u00A7f Total: \u00A75" + this.totalMonuments) : ""));
        }
        player.addChatMessage("\u00A76\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-\u00A7m-");
    }
}
