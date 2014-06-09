package undercast.client;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import org.lwjgl.input.Keyboard;

import undercast.client.server.UndercastServerGUI;
import undercast.client.settings.SettingsGUI;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

/**
 * @author Flv92
 */
public class UndercastKeyHandling {

    public static int keyGuiHide = Keyboard.KEY_F6;
    public static int keyGuiServer = Keyboard.KEY_L;
    public static int keyGuiFullBright = Keyboard.KEY_G;
    public static int keySettingsGui = Keyboard.KEY_P;
    public static int keyGlobalChat = Keyboard.KEY_Y;
    public BufferedImage killerBuffer;
    public double x, y, z;

    public UndercastKeyHandling() {
        // the first value is an array of KeyBindings, the second is whether or
        // not the call
        // keyDown should repeat as long as the key is down
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (Keyboard.isKeyDown(keyGuiHide)) {
            UndercastData.guiShowing = !UndercastData.guiShowing;
        }
        if (Keyboard.isKeyDown(keyGuiServer)) {
            Minecraft.getMinecraft().displayGuiScreen(new UndercastServerGUI(true));
        }
        if (Keyboard.isKeyDown(keyGuiFullBright) && Minecraft.getMinecraft().inGameHasFocus && UndercastConfig.fullBright && UndercastData.isPlayingOvercastNetwork() && (UndercastData.team.equals("Observers") || UndercastData.isGameOver)) {
            UndercastModClass.brightActive = !UndercastModClass.brightActive;
            if (UndercastModClass.brightActive) {
            	Minecraft.getMinecraft().gameSettings.gammaSetting = UndercastModClass.getInstance().brightLevel;
            } else {
            	Minecraft.getMinecraft().gameSettings.gammaSetting = UndercastModClass.getInstance().defaultLevel;
            }
        }
        if (Keyboard.isKeyDown(keySettingsGui)) {
            FMLClientHandler.instance().getClient().displayGuiScreen(new SettingsGUI(null));
        }
        if (Keyboard.isKeyDown(keyGlobalChat) && Minecraft.getMinecraft().inGameHasFocus && UndercastData.isPlayingOvercastNetwork()) {
				FMLClientHandler.instance().getClient().displayGuiScreen(new GuiChat("/g "));
		}
    }
}
