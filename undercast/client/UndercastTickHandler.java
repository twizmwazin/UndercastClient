package undercast.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;

/**
 * @author Flv92
 */
public class UndercastTickHandler implements ITickHandler {

    GuiScreen current;
    Minecraft mc;

    public UndercastTickHandler() {
    }

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        if (type.equals(EnumSet.of(TickType.CLIENT))) {
            mc = FMLClientHandler.instance().getClient();
            current = mc.currentScreen;
            if (current instanceof GuiMainMenu && !(current instanceof UndercastGuiMainMenu)) {
                mc.displayGuiScreen(new UndercastGuiMainMenu());
            } else if (current instanceof GuiOptions) {
                List customButtonList;
                try {
                    customButtonList = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, (GuiOptions) current, 3);
                    if (UndercastModClass.getInstance().buttonListSizeOfGuiOptions == null) {
                        UndercastModClass.getInstance().buttonListSizeOfGuiOptions = customButtonList.size();
                    }
                    if (customButtonList.size() == UndercastModClass.getInstance().buttonListSizeOfGuiOptions) {
                        customButtonList.add(new UndercastGuiConfigButton(301, current.width / 2 + 5, current.height / 6 + 60, 150, 20, "Undercast config", current));
                    }
                    ObfuscationReflectionHelper.setPrivateValue(GuiScreen.class, (GuiOptions) current, customButtonList, 3);
                } catch (UnableToAccessFieldException e) {
                }
            }
            boolean hasWorld = mc.theWorld != null;
            if (hasWorld) {
                UndercastModClass.getInstance().onGameTick(mc);
            }
        } else {
            if (type.equals(EnumSet.of(TickType.RENDER))) {
                UndercastModClass.getInstance().onGameTick(mc);
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
