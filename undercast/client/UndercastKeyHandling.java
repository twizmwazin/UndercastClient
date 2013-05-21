package undercast.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import undercast.client.UndercastData.Teams;
import undercast.client.server.UndercastServerGUI;
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
                mc.displayGuiScreen(new UndercastServerGUI(true));
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
            } else if (kb == keySettingsGui) {
                FMLClientHandler.instance().getClient().displayGuiScreen(new SettingsGUI(null));
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
