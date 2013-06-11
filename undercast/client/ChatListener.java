package undercast.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IChatListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.StringUtils;
import undercast.client.internetTools.ServersCommandParser;

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
        UndercastModClass UCInstance = UndercastModClass.getInstance();
        try {
            Minecraft mc = FMLClientHandler.instance().getClient();
            EntityPlayer player = mc.thePlayer;
            UCInstance.username = mc.thePlayer.username;
            String message = StringUtils.stripControlCodes(packet.message);
            // stop global msg and team chat and whispered messages to go through
            if (!message.startsWith("<") && !message.startsWith("[Team]") && !message.startsWith("(From ") && !message.startsWith("(To ") && UndercastData.isOC) {
                addLineToChatLines(message);
                if (!(UCInstance.chatHandler.handleMessage(message, UndercastModClass.getInstance().username, player, packet.message))) {
                    packet.message = null;
                }
                if (UndercastConfig.showAchievements) {
                    UCInstance.achievementChatHandler.handleMessage(message, UndercastModClass.getInstance().username, player);
                }
                if (UndercastConfig.parseMatchState) {
                    if (ServersCommandParser.handleChatMessage(message, packet.message)) {
                        packet.message = null;
                    }
                }
                if(UndercastConfig.showFriends){
                    if(!UCInstance.friendHandler.handleMessage(message)){
                        packet.message = null;
                    }
                }
            }
            if (message.startsWith("<") && UndercastData.isPlayingOvercastNetwork()) {
                UndercastData.removeNextChatMessage = false;
            }
        } catch (Exception e) {
        }
        packet.message = UndercastChatHandler.handleTip(packet);
        return packet;
    }

    public void addLineToChatLines(String line) {
        System.arraycopy(UndercastModClass.lastChatLines, 0, UndercastModClass.lastChatLines, 1, 99);
        UndercastModClass.lastChatLines[0] = line;
    }
}
