package undercast.client;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.FMLRelaunchLog;
import jexxus.client.ClientConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;
import undercast.client.UndercastData.ServerType;
import undercast.client.achievements.UndercastGuiAchievement;
import undercast.client.achievements.UndercastKillsHandler;
import undercast.client.update.UndercastUpdaterThread;
import undercast.network.client.UndercastClientConnectionListener;
import undercast.network.common.packet.VIPUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Flv92
 */
@Mod(modid = UndercastModClass.MOD_NAME, name = UndercastModClass.MOD_NAME, version = UndercastModClass.MOD_VERSION)
public class UndercastModClass {

    public final static String MOD_VERSION = "1.8.0";
    public final static String MOD_NAME = "UndercastMod";
    public static Configuration CONFIG;
    public static boolean brightActive;
    public static String[] lastChatLines = new String[100];
    public static int capeCounter = 0;
    @Mod.Instance(UndercastModClass.MOD_NAME)
    private static UndercastModClass instance;
    public Integer buttonListSizeOfGuiOptions;
    public float brightLevel = (float) 20.0D;
    public float defaultLevel;
    public UndercastChatHandler chatHandler;
    public UndercastKillsHandler achievementChatHandler;
    public UndercastGuiAchievement guiAchievement;
    public ClientConnection connection;
    public List<VIPUser> vips;
    protected String username = "Not_Found";
    protected Minecraft mc = Minecraft.getMinecraft();
    private boolean mainMenuActive;

    public static void startCapeTimer() {
        Timer timer = new Timer();
        timer.schedule(new CapeTimeTask(), 0, 40);
    }

    /**
     * get an instance of UndercastModClass
     *
     * @return the instance
     */
    public static UndercastModClass getInstance() {
        return instance;
    }

    /**
     * preInitialisation method automatically called by Forge with
     *
     * @param event
     * @Mod.PreInit use Must be used to load config and start downloading thread if necessary.
     */
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ((org.apache.logging.log4j.core.Logger) FMLRelaunchLog.log.getLogger()).setLevel(Level.OFF);
        // With the renaming, the config file name changed.
        // Just renaming the old one as the new one if necessary.
        File newConfig = event.getSuggestedConfigurationFile();
        File oldConfig = new File(newConfig.getParentFile().getAbsolutePath() + "/overcastNetwork-unofficialMod.cfg");
        if (oldConfig.exists() && !newConfig.exists()) {
            oldConfig.renameTo(newConfig);
        }
        vips = new ArrayList<VIPUser>();
        chatHandler = new UndercastChatHandler();
        achievementChatHandler = new UndercastKillsHandler();
        defaultLevel = FMLClientHandler.instance().getClient().gameSettings.gammaSetting;
        CONFIG = new Configuration(newConfig);
        new UndercastConfig(CONFIG, event.getSuggestedConfigurationFile());
        new UndercastRenderHandler();
        new UndercastKeyHandling();
        new UndercastData();
        new UndercastUpdaterThread();
        guiAchievement = new UndercastGuiAchievement(mc);
        UndercastCustomMethods.init();
        //15652: v1.7.3 port
        //25565: v1.7.4 port (ip will change)
        connection = new ClientConnection(new UndercastClientConnectionListener(), "69.175.123.173", 25565, true);
        startCapeTimer();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        new UndercastTickHandler();
        new ChatListener();
        new UndercastConnectionHandler();
        LanguageRegistry.instance().addStringLocalization("undercast.gui", "Toggle Overcast Network mod gui");
        LanguageRegistry.instance().addStringLocalization("undercast.inGameGui", "Change server");
        LanguageRegistry.instance().addStringLocalization("undercast.fullBright", "Toggle fullbright");
        LanguageRegistry.instance().addStringLocalization("undercast.settings", "Show Undercast mod settings");
        UndercastData.filterIndex = UndercastConfig.lastUsedFilter;
    }

    /**
     * onGameTick custom method called from the tickHandler UndercastTickHandler. Only called from two kind of ticks, TickType.CLIENT and TickType.RENDER Client ticks are for remplace a gui at the exact moment where the gui appears so this is invisible Render ticks are gui ticks in order to correctly render text inside a gui
     */
    public void onGameTick(Minecraft mc, Boolean rendering) {
        if (mc == null) return;

        if (!rendering) {
            // if the game over screen is active then you have died
            // if it is the first time it is active count a death
            // if it is not don't do anything
            if (mc.currentScreen instanceof GuiGameOver) {
                mc.currentScreen = null;
                mc.displayGuiScreen(new UndercastGuiGameOver());
                // if the button is enabled and the user wants to disable it
            }

            if (mc.currentScreen instanceof UndercastGuiGameOver && UndercastConfig.toggleTitleScreenButton) {
                ((UndercastGuiGameOver) mc.currentScreen).setTitleScreenButtonState(false);
            }
            BossBarListener.update();
        } else {
            // do all rendering tasks
            guiAchievement.updateScreen();

            // get debug info for the fps
            String fps = mc.debug.split(",")[0];
            int height = UndercastConfig.x;
            int width = UndercastConfig.y;
            boolean isInGameGuiEmpty = !this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.keyBindPlayerList.isPressed();
            FontRenderer fontRenderer = UndercastCustomMethods.getFontRenderer();
            // if the gui is enabled display
            // if chat is open and config says yes then show gui
            if (isInGameGuiEmpty && UndercastData.guiShowing && (mc.inGameHasFocus || UndercastConfig.showGuiChat && mc.currentScreen instanceof GuiChat)) {
                // show fps
                if (UndercastConfig.showFPS) {
                    mc.fontRendererObj.drawStringWithShadow(fps, width, height, 0xffff);
                    height += 8;
                }
            }
            // if on OvercastNetwork server then display this info.
            // if chat is open and config says yes then show gui
            if (isInGameGuiEmpty && UndercastData.isPlayingOvercastNetwork() && UndercastData.guiShowing && (mc.inGameHasFocus || UndercastConfig.showGuiChat && mc.currentScreen instanceof GuiChat)) {
                // Server display
                if (UndercastConfig.showServer) {
                    fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "S: " : "Server: ") + "\u00A76" + UndercastData.getServer(), width, height, 16777215);
                    height += 8;
                }

                // Team display (based on color)
                if (UndercastConfig.showTeam && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "T: " : "Team: ") + "\u00A7" + UndercastData.teamColor + UndercastData.getTeam(), width, height, 16777215);
                    height += 8;
                }
                // Class display (Ghost Squadron only)
                if (UndercastConfig.showGSClass && UndercastData.currentServerType == ServerType.ghostsquadron) {
                    fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "Cl: " : "Class: ") + UndercastData.currentGSClass, width, height, 2446535);
                    height += 8;
                }
                // Playing Time display:
                if (UndercastConfig.showPlayingTime) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getPlayingTimeString(), width, height, 16777215);
                    height += 8;
                }
                // Match Time display:
                if (UndercastConfig.showMatchTime && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getMatchTimeString(), width, height, 16777215);
                    height += 8;
                }
                // Map fetcher:
                if (UndercastConfig.showMap && !UndercastData.isLobby) {
                    if (UndercastData.getMap() != null) {
                        fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "M: " : "Current Map: ") + "\u00A7d" + UndercastData.getMap(), width, height, 16777215);
                        height += 8;
                    } else {
                        UndercastData.setMap("Fetching...");
                        fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "M: " : "Current Map: ") + "\u00A78" + UndercastData.getMap(), width, height, 16777215);
                        height += 8;
                    }
                }
                // Show next map
                if (UndercastConfig.showNextMap && !UndercastData.isLobby) {
                    if (UndercastData.getNextMap() != null) {
                        fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "N: " : "Next Map: ") + "\u00A7d" + UndercastData.getNextMap(), width, height, 16777215);
                        height += 8;
                    } else {
                        fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "N: " : "Next Map: ") + "\u00A78Loading...", width, height, 16777215);
                        height += 8;
                    }
                }
                // Show KD Ratio
                if (UndercastConfig.showKD && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getKDDisplayString(), width, height, 16777215);
                    height += 8;
                }
                // show KK Ratio
                if (UndercastConfig.showKK && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getKKDisplayString(), width, height, 16777215);
                    height += 8;
                }
                // show amount of kills
                if (UndercastConfig.showKills && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getKillDisplayString(), width, height, 16777215);
                    height += 8;
                }
                // show amount of deaths
                if (UndercastConfig.showDeaths && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getDeathDisplayString(), width, height, 16777215);
                    height += 8;
                }
                // show raindrop count
                if (UndercastConfig.showRaindropCounter) {
                    fontRenderer.drawStringWithShadow(UndercastCustomMethods.getRaindropDisplayString(), width, height, 16777215);
                    height += 8;
                }
                // Kill Streak display
                if (UndercastConfig.showStreak && !UndercastData.isLobby) {
                    fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "KS: " : "Current Killstreak: ") + "\u00A75" + (int) UndercastData.getKillstreak() + "\u00A7f/\u00A75" + (int) UndercastData.getLargestKillstreak(), width, height, 16777215);
                    height += 8;
                }
                // Score display
                if (UndercastConfig.showScore && !UndercastData.isLobby && UndercastData.score != 0) {
                    fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "Sc: " : "Score: ") + "\u00A79" + UndercastData.score, width, height, 16777215);
                    height += 8;
                }
            }

            // if you not on obs turn it off
            if ((!UndercastData.team.equals("Observers") && !UndercastData.isGameOver) || !UndercastData.isPlayingOvercastNetwork()) {
                brightActive = false;
                // if full bright is on turn it off
                if (mc.gameSettings.gammaSetting >= brightLevel) {
                    mc.gameSettings.gammaSetting = defaultLevel;
                    // if the defaultLevel gets mixed up, reset to bright, not to moody
                    // bright is for sure more common than moody
                    if (defaultLevel >= brightLevel) {
                        mc.gameSettings.gammaSetting = (float) 1.0D;
                        defaultLevel = (float) 1.0D;
                    }
                }
            }

            // gui display for obs if you have brightness
            if (isInGameGuiEmpty && UndercastData.isPlayingOvercastNetwork() && UndercastData.guiShowing && (mc.inGameHasFocus || UndercastConfig.showGuiChat && mc.currentScreen instanceof GuiChat)) {
                if (brightActive && UndercastConfig.fullBright && (UndercastData.team.equals("Observers") || UndercastData.isGameOver)) {
                    fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "FB: " : "Full Bright: ") + "\u00A72ON", width, height, 16777215);
                    height += 8;
                } else {
                    if (!brightActive && UndercastConfig.fullBright && (UndercastData.team.equals("Observers") || UndercastData.isGameOver)) {
                        fontRenderer.drawStringWithShadow((UndercastConfig.lessObstructive ? "FB: " : "Full Bright: ") + "\u00A7cOFF", width, height, 16777215);
                        height += 8;
                    }
                }
            }
        }
    }

    static class CapeTimeTask extends TimerTask {

        @Override
        public void run() {
            UndercastModClass.capeCounter = (UndercastModClass.capeCounter + 1) % 200;
            //between 0 and 199
        }

    }
}
