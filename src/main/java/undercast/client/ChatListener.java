package undercast.client;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import undercast.client.internetTools.ServersCommandParser;
import undercast.client.server.UndercastServerGUI;

/**
 * @author Flv92
 */
public class ChatListener {

    public ChatListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerChat(ClientChatReceivedEvent event) {
        UndercastModClass UCInstance = UndercastModClass.getInstance();
        String messageWithOutJson = event.message.getFormattedText();
        String message = StringUtils.stripControlCodes(messageWithOutJson);
        try {
            Minecraft mc = FMLClientHandler.instance().getClient();
            EntityPlayer player = mc.thePlayer;
            UCInstance.username = mc.thePlayer.getDisplayName().getFormattedText();
            // stop global msg and team chat and whispered messages to go
            // through
            if (!message.startsWith("<") && !message.startsWith("[Team]") && !message.startsWith("(From ") && !message.startsWith("(To ") && UndercastData.isOC) {
                addLineToChatLines(message);
                if (!(UCInstance.chatHandler.handleMessage(message, UndercastModClass.getInstance().username, player, messageWithOutJson))) {
                    event.message = null;
                }
                UCInstance.achievementChatHandler.handleMessage(message, UndercastModClass.getInstance().username, player, messageWithOutJson);
                if (UndercastConfig.parseMatchState) {
                    if (ServersCommandParser.handleChatMessage(message, messageWithOutJson) && (message.contains("Online: ") || message.contains("-------- Overcast Network Servers"))) {
                        event.message = null;
                    }
                }
                if (RaindropManager.manager == null) {
                    new RaindropManager();
                }
                if (RaindropManager.manager.handleChatMessage(message)) {
                    event.message = null;
                }
                if (FMLClientHandler.instance().isGUIOpen(UndercastServerGUI.class) && (message.contains("Online: ") || message.contains("-------- Overcast Network Servers"))) {
                    event.message = null;
                }
            }
            if (message.startsWith("<") && UndercastData.isPlayingOvercastNetwork()) {
                UndercastData.removeNextChatMessage = false;
            }
        } catch (Exception e) {
        }
        event.setCanceled(UndercastChatHandler.handleTipAndRating(message));
    }

    public void addLineToChatLines(String line) {
        System.arraycopy(UndercastModClass.lastChatLines, 0, UndercastModClass.lastChatLines, 1, 99);
        UndercastModClass.lastChatLines[0] = line;
    }
}
