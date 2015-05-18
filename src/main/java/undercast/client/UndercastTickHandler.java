package undercast.client;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

/**
 * @author Flv92
 */
public class UndercastTickHandler {
    public UndercastTickHandler() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen current = mc.currentScreen;
        if (current instanceof GuiMainMenu && !(current instanceof UndercastGuiMainMenu)) {
            mc.displayGuiScreen(new UndercastGuiMainMenu());
        } else if (current instanceof GuiOptions) {
            List<GuiButton> customButtonList;
            try {
                customButtonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, current, 7);
                if (UndercastModClass.getInstance().buttonListSizeOfGuiOptions == null) {
                    UndercastModClass.getInstance().buttonListSizeOfGuiOptions = customButtonList.size();
                }
                if (customButtonList.size() == UndercastModClass.getInstance().buttonListSizeOfGuiOptions) {
                    customButtonList.add(new UndercastGuiConfigButton(301, current.width / 2 + 5, current.height / 6 + 10, 150, 20, "Undercast config", current));
                }
                ObfuscationReflectionHelper.setPrivateValue(GuiScreen.class, current, customButtonList, 7);
            } catch (UnableToAccessFieldException e) {
            }
        }

        boolean hasWorld = mc.theWorld != null;
        if (hasWorld) {
            UndercastModClass.getInstance().onGameTick(mc, false);
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        if (event.phase == Phase.END) UndercastModClass.getInstance().onGameTick(Minecraft.getMinecraft(), true);
    }
}
