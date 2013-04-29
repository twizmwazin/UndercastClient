package tc.oc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;
import org.lwjgl.input.Keyboard;
import tc.oc.AresData.Teams;
import tc.oc.server.Ares_ServerGUI;

/**
 * @author Flv92
 */
public class AresKeyHandling extends KeyHandler {

    static KeyBinding keyGuiHide = new KeyBinding("gui", Keyboard.KEY_F6);
    static KeyBinding keyGuiServer = new KeyBinding("inGameGui", Keyboard.KEY_L);
    static KeyBinding keyGuiFullBright = new KeyBinding("fullBright", Keyboard.KEY_G);

    public AresKeyHandling() {
        //the first value is an array of KeyBindings, the second is whether or not the call
        //keyDown should repeat as long as the key is down
        super(new KeyBinding[]
                {
                    keyGuiHide, keyGuiServer, keyGuiFullBright
                }, new boolean[]
                {
                    false, false, false
                });
    }

    @Override
    public String getLabel() {
        return "aresKeyBindig";
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        Minecraft mc = FMLClientHandler.instance().getClient();
        if (mc.inGameHasFocus && tickEnd) //Using this boolean because keyDown is called two times, at the start of a tick and at the end of it.
        //We only want to execute keyDown function once not twice (Won't work in fact, showing -> disabling -> showing gui)
        {
            if (kb == keyGuiHide)
            {
                AresData.guiShowing = !AresData.guiShowing;
            } else if (kb == keyGuiServer)
            {
                ModLoader.openGUI(mc.thePlayer, new Ares_ServerGUI(true));
            } //if you are an obs;have the config to true; toggle fullbright and play sound
            else if (AresData.isPlayingAres() && kb == keyGuiFullBright && AresData.team == Teams.Observers && AresConfig.fullBright)
            {
                if (mc.inGameHasFocus)
                {
                    AresModClass.brightActive = !AresModClass.brightActive;
                    if (AresModClass.brightActive)
                    {
                        mc.gameSettings.gammaSetting = AresModClass.brightLevel;
                    } else
                    {
                        mc.gameSettings.gammaSetting = AresModClass.defaultLevel;
                    }
                    mc.sndManager.playSoundFX("random.click", 0.5F, 1.0F);
                }
            }
        }
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
        //I am unsure if any different TickTypes have any different effects.
    }
}
