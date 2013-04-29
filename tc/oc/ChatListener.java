package tc.oc;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IChatListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.StringUtils;

/**
 * @author Flv92
 */
public class ChatListener implements IChatListener {

    public ChatListener() {
    }

    @Override
    public Packet3Chat serverChat(NetHandler handler, Packet3Chat message) {
        return message;
    }

    @Override
    public Packet3Chat clientChat(NetHandler handler, Packet3Chat packet) {
        try
        {
            Minecraft mc = FMLClientHandler.instance().getClient();
            EntityPlayer player = mc.thePlayer;
            AresModClass.username = mc.thePlayer.username;
            String message = StringUtils.stripControlCodes(packet.message);
            // stop global msg to go through
            if (!message.startsWith("<"))
            {
                new AresChatHandler(message, AresModClass.username, player);
            }
        } catch (Exception e)
        {
        }
        packet.message = AresChatHandler.handleTip(packet);
        return packet;
    }
}
