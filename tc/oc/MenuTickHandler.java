package tc.oc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Flv92
 */
public class MenuTickHandler implements ITickHandler {

    GuiScreen current;
    Minecraft mc;

    public MenuTickHandler() {
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (type.equals(EnumSet.of(TickType.CLIENT)))
        {
            mc = FMLClientHandler.instance().getClient();

            current = mc.currentScreen;
            if (current instanceof GuiMainMenu && !(current instanceof AresGuiMainMenu))
            {
                mc.displayGuiScreen(new AresGuiMainMenu());
            }
            boolean hasWorld = mc.theWorld != null;

            if (hasWorld)
            {
                AresModClass.instance.onGameTick(mc);
            }
        } else if (type.equals(EnumSet.of(TickType.RENDER)))
        {
            mc = FMLClientHandler.instance().getClient();
            boolean hasWorld = mc.theWorld != null;
            if (hasWorld)
            {
                AresModClass.instance.onGameTick(mc);
            }

        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        EnumSet<TickType> tick = EnumSet.of(TickType.RENDER);
        tick.add(TickType.CLIENT);
        return tick;
    }

    @Override
    public String getLabel() {
        return null;
    }
}
