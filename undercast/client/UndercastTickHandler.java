package undercast.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;

/**
 * @author Flv92
 */
public class UndercastTickHandler {

    GuiScreen current;
    Minecraft mc;

    public UndercastTickHandler() {
    	FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
    	mc = FMLClientHandler.instance().getClient();
        current = mc.currentScreen;
        if (current instanceof GuiMainMenu && !(current instanceof UndercastGuiMainMenu)) {
            mc.func_147108_a(new UndercastGuiMainMenu());
        } else if (current instanceof GuiOptions) {
            List customButtonList;
            try {
                customButtonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, (GuiOptions) current, 4);
                if (UndercastModClass.getInstance().buttonListSizeOfGuiOptions == null) {
                    UndercastModClass.getInstance().buttonListSizeOfGuiOptions = customButtonList.size();
                }
                if (customButtonList.size() == UndercastModClass.getInstance().buttonListSizeOfGuiOptions) {
                    customButtonList.add(new UndercastGuiConfigButton(301, current.field_146294_l / 2 + 5, current.field_146295_m / 6 + 60, 150, 20, "Undercast config", current));
                }
                ObfuscationReflectionHelper.setPrivateValue(GuiScreen.class, (GuiOptions) current, customButtonList, 4);
            } catch (UnableToAccessFieldException e) {
            }
        }
        
        boolean hasWorld = mc.theWorld != null;
        if (hasWorld) {
            UndercastModClass.getInstance().onGameTick(mc);
        }
    }
    
    @SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
    	if (event.phase == Phase.END) UndercastModClass.getInstance().onGameTick(mc);	
    }
    
    @SubscribeEvent
    public void onRenderPlayer(WorldTickEvent event) {
    	if (event.phase == Phase.END) UndercastModClass.getInstance().onGameTick(mc);	
    }
    
}
