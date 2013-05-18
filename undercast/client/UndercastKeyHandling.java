package undercast.client;

import undercast.client.achievements.UndercastKillsHandler;
import undercast.client.achievements.UndercastGuiAchievement;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import org.lwjgl.input.Keyboard;
import undercast.client.UndercastData.Teams;
import undercast.client.server.Undercast_ServerGUI;
import undercast.client.settings.SettingsGUI;

/**
 * @author Flv92
 */
public class UndercastKeyHandling extends KeyHandler {

    static KeyBinding keyGuiHide = new KeyBinding("undercast.gui", Keyboard.KEY_F6);
    static KeyBinding keyGuiServer = new KeyBinding("undercast.inGameGui", Keyboard.KEY_L);
    static KeyBinding keyGuiFullBright = new KeyBinding("undercast.fullBright", Keyboard.KEY_G);
    static KeyBinding keySettingsGui = new KeyBinding("undercast.settings", Keyboard.KEY_P);
    public BufferedImage killerBuffer;

    public UndercastKeyHandling() {
        //the first value is an array of KeyBindings, the second is whether or not the call
        //keyDown should repeat as long as the key is down
        super(new KeyBinding[]{
            keyGuiHide, keyGuiServer, keyGuiFullBright, keySettingsGui
        }, new boolean[]{
            false, false, false, false
        });
    }

    @Override
    public String getLabel() {
        return "UndercastKeyBindig";
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (mc.inGameHasFocus && tickEnd) //Using this boolean because keyDown is called two times, at the start of a tick and at the end of it.
        //We only want to execute keyDown function once not twice (Won't work in fact, showing -> disabling -> showing gui)
        {
            if (kb == keyGuiHide) {
                UndercastData.guiShowing = !UndercastData.guiShowing;
            } else if (kb == keyGuiServer) {
                mc.displayGuiScreen(new Undercast_ServerGUI(true));
            } else if (UndercastData.isPlayingOvercastNetwork() && kb == keyGuiFullBright && (UndercastData.team == Teams.Observers || UndercastData.isGameOver) && UndercastConfig.fullBright) {
                if (mc.inGameHasFocus) {
                    UndercastModClass.brightActive = !UndercastModClass.brightActive;
                    if (UndercastModClass.brightActive) {
                        mc.gameSettings.gammaSetting = UndercastModClass.getInstance().brightLevel;
                    } else {
                        mc.gameSettings.gammaSetting = UndercastModClass.getInstance().defaultLevel;
                    }
                    mc.sndManager.playSoundFX("random.click", 0.5F, 1.0F);
                }
                Runnable r1 = new Runnable() {
                    public void run() {
                        URLConnection spoof = null;
                        try {
                            spoof = new URL("https://minotar.net/helm/" + FMLClientHandler.instance().getClient().thePlayer.username + "/16.png").openConnection();
                            spoof.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                            killerBuffer = ((BufferedImage) ImageIO.read(spoof.getInputStream()));
                            Thread.sleep(1000L);
                            Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
                            Minecraft client = FMLClientHandler.instance().getClient();
                            ((UndercastGuiAchievement) client.guiAchievement)
                                    .addFakeAchievementToMyList(custom, true, client.thePlayer.username, killerBuffer, client.thePlayer.username, "got the first Blood!");
                            Thread.sleep(500L);
                            spoof = new URL("https://minotar.net/helm/palechip" + "/16.png").openConnection();
                            spoof.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                            killerBuffer = ((BufferedImage) ImageIO.read(spoof.getInputStream()));
                            Thread.sleep(1000L);
                            ((UndercastGuiAchievement) client.guiAchievement)
                                    .addFakeAchievementToMyList(custom, true, client.thePlayer.username, killerBuffer, "palechip", "got the first Blood!");
                            Thread.sleep(500L);
                            spoof = new URL("https://minotar.net/helm/goldbattle" + "/16.png").openConnection();
                            spoof.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
                            killerBuffer = ((BufferedImage) ImageIO.read(spoof.getInputStream()));
                            Thread.sleep(1000L);
                            ((UndercastGuiAchievement) client.guiAchievement)
                                    .addFakeAchievementToMyList(custom, true, "palechip", killerBuffer, "goldbattle", "got the first Blood!");

                        } catch (Exception ex) {
                            Logger.getLogger(UndercastKillsHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                Runnable r2 = new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(1000L);
                            Achievement custom = (new Achievement(27, "custom", 1, 4, Item.ingotIron, (Achievement) null));
                            Minecraft client = FMLClientHandler.instance().getClient();
                            ((UndercastGuiAchievement) client.guiAchievement)
                                    .addFakeAchievementToMyList(custom, true, client.thePlayer.username, killerBuffer, client.thePlayer.username, "got the first Blood!");
                        } catch (InterruptedException ex) {
                            Logger.getLogger(UndercastKillsHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                };
                Thread t1 = new Thread(r1);
                Thread t2 = new Thread(r2);
                t1.start();
                t2.start();

            } else if (kb == keySettingsGui) {
                FMLClientHandler.instance().getClient().displayGuiScreen(new SettingsGUI());
            }
        } //if you are an obs;have the config to true; toggle fullbright and play sound
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }
}
