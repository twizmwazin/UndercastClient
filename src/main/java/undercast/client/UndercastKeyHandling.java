package undercast.client;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import undercast.client.server.UndercastServerGUI;
import undercast.client.settings.SettingsGUI;

/**
 * @author Flv92
 */
public class UndercastKeyHandling {

    public final static String KEY_CATEGORY = "Undercast Client";

    public static KeyBinding keyGuiHide = new KeyBinding("Hide Gui", Keyboard.KEY_F6, KEY_CATEGORY);
    public static KeyBinding keyGuiServer = new KeyBinding("Server List", Keyboard.KEY_L, KEY_CATEGORY);
    public static KeyBinding keyGuiFullBright = new KeyBinding("Full Bright", Keyboard.KEY_G, KEY_CATEGORY);
    public static KeyBinding keySettingsGui = new KeyBinding("Settings", Keyboard.KEY_P, KEY_CATEGORY);
    public static KeyBinding keyGlobalChat = new KeyBinding("Global Chat", Keyboard.KEY_Y, KEY_CATEGORY);

    public UndercastKeyHandling() {
        // the first value is an array of KeyBindings, the second is whether or
        // not the call
        // keyDown should repeat as long as the key is down
        FMLCommonHandler.instance().bus().register(this);

        // register keybindings
        ClientRegistry.registerKeyBinding(keyGuiHide);
        ClientRegistry.registerKeyBinding(keyGuiServer);
        ClientRegistry.registerKeyBinding(keyGuiFullBright);
        ClientRegistry.registerKeyBinding(keySettingsGui);
        ClientRegistry.registerKeyBinding(keyGlobalChat);
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event) {
        if (keyGuiHide.isPressed()) {
            UndercastData.guiShowing = !UndercastData.guiShowing;
        }
        if (keyGuiServer.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new UndercastServerGUI(true));
        }
        if (keyGuiFullBright.isPressed() && Minecraft.getMinecraft().inGameHasFocus && UndercastConfig.fullBright && UndercastData.isPlayingOvercastNetwork() && (UndercastData.team.equals("Observers") || UndercastData.isGameOver)) {
            UndercastModClass.brightActive = !UndercastModClass.brightActive;
            if (UndercastModClass.brightActive) {
                Minecraft.getMinecraft().gameSettings.gammaSetting = UndercastModClass.getInstance().brightLevel;
            } else {
                Minecraft.getMinecraft().gameSettings.gammaSetting = UndercastModClass.getInstance().defaultLevel;
            }
        }
        if (keySettingsGui.isPressed()) {
            FMLClientHandler.instance().getClient().displayGuiScreen(new SettingsGUI(null));
        }
        if (keyGlobalChat.isPressed() && Minecraft.getMinecraft().inGameHasFocus && UndercastData.isPlayingOvercastNetwork()) {
            FMLClientHandler.instance().getClient().displayGuiScreen(new GuiChat("/g "));
        }
    }
}
