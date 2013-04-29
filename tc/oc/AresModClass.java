package tc.oc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.common.Configuration;
import tc.oc.AresData.Teams;
import tc.oc.update.Ares_Updater;

/**
 * @author Flv92
 */
@Mod(modid = "aresMod", name = "ProjectAres mod", version = AresModClass.MOD_VERSION)
public class AresModClass {

    /**
     * Static unique variable giving the current version of the mod
     */
    public final static String MOD_VERSION = "1.4.1";
    public static ArrayList<String> masterServerList = new ArrayList<String>();
    public static Configuration CONFIG;
    public static String username = "Not_Found";
    private static boolean deathScreenActive;
    public static boolean brightActive;
    public static float brightLevel = (float) 20.0D;
    public static float defaultLevel;
    @Instance("aresMod")
    public static AresModClass instance;

    /**
     * preInitialisation method automatically called by Forge with
     *
     * @Mod.PreInit use Must be used to load config and start downloading thread
     * if necessary.
     * @param event
     */
    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
        defaultLevel = FMLClientHandler.instance().getClient().gameSettings.gammaSetting;
        masterServerList = getServers();
        CONFIG = new Configuration(event.getSuggestedConfigurationFile());
        new AresConfig(CONFIG);
        KeyBindingRegistry.registerKeyBinding(new AresKeyHandling());
        new AresData();
        new Ares_Updater();

        //End of preInit normally
    }

    @Mod.Init
    public void init(FMLInitializationEvent event) {
        TickRegistry.registerTickHandler(new MenuTickHandler(), Side.CLIENT);
        NetworkRegistry.instance().registerChatListener(new ChatListener());
        NetworkRegistry.instance().registerConnectionHandler(new AresConnectionHandler());
    }

    /**
     * Finds the current list of servers from the website.
     *
     * @return ArrayList of server ips
     */
    public static ArrayList<String> getServers() {
        ArrayList<String> list = new ArrayList<String>();

        try
        {
            URL url = new URL("https://oc.tc/play");
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                if (line.startsWith("<li>") && line.endsWith("</li>"))
                {
                    list.add(line.replace("<li>", "").replace("</li>", ""));
                }
            }
        } catch (Exception ignored)
        {
            System.err.println("[ProjectAres]: ERROR: Could not get online list of servers");
            //if you can't get it from the website load defaults
            list.clear();
            list.add("alpha.oc.tc");
            list.add("beta.oc.tc");
            list.add("gamma.oc.tc");
            list.add("delta.oc.tc");
            list.add("epsilon.oc.tc");
            list.add("zeta.oc.tc");
            list.add("eta.oc.tc");
            list.add("theta.oc.tc");
            list.add("iota.oc.tc");
            list.add("kappa.oc.tc");
            list.add("lambda.oc.tc");
            list.add("nostalgia.oc.tc");
        }

        return list;
    }

    public void onGameTick(Minecraft mc) {
        //if the game over screen is active then you have died
        //if it is the first time it is active count a death
        //if it is not don't do anything
        if (mc.currentScreen instanceof GuiGameOver)
        {
            mc.currentScreen = null;
            mc.displayGuiScreen(new AresGuiGameOver());
            if (!deathScreenActive)
            {
                AresData.resetKillstreak();
                deathScreenActive = true;
            }
            //if the button is enabled and the user wants to disable it

        } else
        {
            deathScreenActive = false;
        }
        if (mc.currentScreen instanceof AresGuiGameOver && AresConfig.toggleTitleScreenButton)
        {
            ((AresGuiGameOver) mc.currentScreen).setTitleScreenButtonState(false);
        }

        //get debug info for the fps
        String fps = mc.debug.split(",")[0];
        int height = AresConfig.x;
        int width = AresConfig.y;
        //if the gui is enabled display
        //if chat is open and config says yes then show gui
        if (AresData.guiShowing && (mc.inGameHasFocus || AresConfig.showGuiChat && mc.currentScreen instanceof GuiChat))
        {
            //show fps
            if (AresConfig.showFPS)
            {
                mc.fontRenderer.drawStringWithShadow(fps, width, height, 0xffff);
                height += 8;
            }
        }
        //if on Ares server then display this info.
        //if chat is open and config says yes then show gui
        if (AresData.isPlayingAres() && AresData.guiShowing && (mc.inGameHasFocus || AresConfig.showGuiChat && mc.currentScreen instanceof GuiChat))
        {
            // Server display
            if (AresConfig.showServer)
            {
                mc.fontRenderer.drawStringWithShadow("Server: \u00A76" + AresData.getServer(), width, height, 16777215);
                height += 8;
            }

            // Team display (based on color)
            if (AresConfig.showTeam)
            {
                mc.fontRenderer.drawStringWithShadow("Team: " + AresData.getTeam(), width, height, getTeamColors());
                height += 8;
            }
            // Friend display:
            if (AresConfig.showFriends)
            {
                mc.fontRenderer.drawStringWithShadow("Friends Online: \u00A73" + AresData.getFriends(), width, height, 16777215);
                height += 8;
            }
            // Map fetcher:
            if (AresConfig.showMap)
            {
                if (AresData.getMap() != null)
                {
                    mc.fontRenderer.drawStringWithShadow("Current Map: \u00A7d" + AresData.getMap(), width, height, 16777215);
                    height += 8;
                } else
                {
                    AresData.setMap("Fetching...");
                    mc.fontRenderer.drawStringWithShadow("Current Map: \u00A78" + AresData.getMap(), width, height, 16777215);
                    height += 8;
                }
            }
            // Show next map
            if (AresConfig.showNextMap) {
                if (AresData.getNextMap() != null) {
                    mc.fontRenderer.drawStringWithShadow("Next Map: \u00A7d" + AresData.getNextMap(), width, height, 16777215);
                    height += 8;
                } else {
                    mc.fontRenderer.drawStringWithShadow("Next Map: \u00A78Loading...", width, height, 16777215);
                    height += 8;
                }
            }
            //Show KD Ratio
            if (AresConfig.showKD)
            {
                mc.fontRenderer.drawStringWithShadow("K/D: \u00A73" + AresCustomMethods.getKD(), width, height, 16777215);
                height += 8;
            }
            //show KK Ratio
            if (AresConfig.showKK)
            {
                mc.fontRenderer.drawStringWithShadow("K/K: \u00A73" + AresCustomMethods.getKK(), width, height, 16777215);
                height += 8;
            }
            //show amount of kills
            if (AresConfig.showKills)
            {
                mc.fontRenderer.drawStringWithShadow("Kills: \u00A7a" + AresData.getKills(), width, height, 16777215);
                height += 8;
            }
            //show amount of deaths
            if (AresConfig.showDeaths)
            {
                mc.fontRenderer.drawStringWithShadow("Deaths: \u00A74" + AresData.getDeaths(), width, height, 16777215);
                height += 8;
            }
            // Kill Streak display
            if (AresConfig.showStreak)
            {
                mc.fontRenderer.drawStringWithShadow("Current Killstreak: \u00A75" + (int) AresData.getKillstreak() + "/" + (int) AresData.getLargestKillstreak(), width, height, 16777215);
                height += 8;
            }
        }

        //if you not on obs turn it off
        if (AresData.team != Teams.Observers)
        {
            brightActive = false;
            //if full bright is on turn it off
            if (mc.gameSettings.gammaSetting >= brightLevel)
            {
                mc.gameSettings.gammaSetting = defaultLevel;
                if (defaultLevel >= brightLevel)
                {
                    mc.gameSettings.gammaSetting = (float) 0.0D;
                    defaultLevel = (float) 0.0D;
                }
            }
        }

        //gui display for obs if you have brightness
        if (AresData.isPlayingAres() && AresData.guiShowing && (mc.inGameHasFocus || AresConfig.showGuiChat && mc.currentScreen instanceof GuiChat))
        {
            if (brightActive && AresConfig.fullBright && AresData.team == Teams.Observers)
            {
                mc.fontRenderer.drawStringWithShadow("Full Bright: \u00A72ON", width, height, 16777215);
                height += 8;
            } else if (!brightActive && AresConfig.fullBright && AresData.team == Teams.Observers)
            {
                mc.fontRenderer.drawStringWithShadow("Full Bright: \u00A7cOFF", width, height, 16777215);
                height += 8;
            }
        }
    }

    /**
     * Returns the team color hex based on the team you are on
     *
     * @return hex value of team color
     */
    public int getTeamColors() {
        if (AresData.getTeam() == AresData.Teams.Red)
        {
            return 0x990000;
        } else if (AresData.getTeam() == AresData.Teams.Blue)
        {
            return 0x0033FF;
        } else if (AresData.getTeam() == AresData.Teams.Purple)
        {
            return 0x9933CC;
        } else if (AresData.getTeam() == AresData.Teams.Cyan)
        {
            return 0x00FFFF;
        } else if (AresData.getTeam() == AresData.Teams.Lime)
        {
            return 0x00FF00;
        } else if (AresData.getTeam() == AresData.Teams.Yellow)
        {
            return 0xFFFF00;
        } else if (AresData.getTeam() == AresData.Teams.Green)
        {
            return 0x006600;
        } else if (AresData.getTeam() == AresData.Teams.Orange)
        {
            return 0xFF9900;
        } else if (AresData.getTeam() == AresData.Teams.Observers)
        {
            return 0x00FFFF;
        } else
        {
            return 0x606060;
        }
    }
}
