package tc.oc;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

/**
 * @author Flv92
 */
public class PlayerClickOnSignEvent {

    @ForgeSubscribe
    public void onPlayerClickEvent(PlayerInteractEvent event) {
        int blockId = event.entityPlayer.worldObj.getBlockId(event.x, event.y, event.z);
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && (blockId == 63 || blockId == 68) && AresData.isPlayingAres()) {
            Runnable r1 = new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000L);
                        FMLClientHandler.instance().getClient().thePlayer.sendChatMessage("/server");
                    } catch (InterruptedException iex) {
                    }
                }
            };
            Thread thread = new Thread(r1);
            thread.start();
        }
    }
}
