package undercast.client.internetTools;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.src.ModLoader;
import net.minecraft.util.StringUtils;
import undercast.client.UndercastData;
import undercast.client.UndercastData.MatchState;

public class ServersCommandParser {

    private static boolean isListening = false;
    //is set to true if the mod casts /servers in order to delete the messages
    public static boolean castedByMod = false;

    public static void handleChatMessage(String message, String unstripedMessage) {
        if (isListening) {
            // check if the message belongs to the command
            if (!message.contains("Online: ")) {
                isListening = false;
                castedByMod = false;
                return;
            }
            //our only interest is the MatchState
            if (message.contains("Current Map: ")) {
                String name;
                String map;
                char matchStatusColor;
                MatchState state;

                name = message.substring(0, message.indexOf(": "));
                map = message.substring(message.indexOf("Current Map: ") + 13);
                matchStatusColor = unstripedMessage.charAt(unstripedMessage.indexOf("Current Map: ") + 14);

                //c == red
                //e == yellow
                //a = green
                switch (matchStatusColor) {
                    case 'a':
                        state = MatchState.Starting;
                        break;
                    case 'c':
                        state = MatchState.Finished;
                        break;
                    case 'e':
                        state = MatchState.Started;
                        break;
                    default:
                        state = MatchState.Unknown;
                }

                //insert the data
                for (int c = 0; c < UndercastData.serverInformation.length; c++) {
                    if (!(UndercastData.serverInformation[c].name == null)) {
                        if (UndercastData.serverInformation[c].name.equals(name)) {
                            UndercastData.serverInformation[c].currentMap = map;
                            UndercastData.serverInformation[c].matchState = state;
                        }
                    }
                }
            }
            // remove the message
            if (castedByMod) {
                System.out.println("filtering");
                try {
                    List chatLines;
                    // get the lines
                    chatLines = (List) ModLoader.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), 3);
                    //remove the message (20 most recent chat messages are enough)
                    for (int c = 0; c < 20; c++) {
                        ChatLine line = (ChatLine) chatLines.get(c);
                        if (StringUtils.stripControlCodes(line.getChatLineString()).contains("Online: ")) {
                            chatLines.remove(c);
                            break;
                        }
                    }
                    // set them back
                    ModLoader.setPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), 3, chatLines);
                } catch (Exception e) {
                    System.out.println("[UndercastMod]: Getting a private value (chatLines) failed");
                    System.out.println("[UndercastMod]: ERROR: " + e.toString());
                }
            }
        } else {
            if (message.equals("------------- Overcast Network Servers -------------")) {
                if (castedByMod) {
                    try {
                        List chatLines;
                        // get the lines
                        chatLines = (List) ModLoader.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), 3);
                        for (int c = 0; c < 20; c++) {
                            ChatLine line = (ChatLine) chatLines.get(c);
                            if (StringUtils.stripControlCodes(line.getChatLineString()).equals("------------- Overcast Network Servers -------------")) {
                                chatLines.remove(c);
                                break;
                            }
                        }
                        ModLoader.setPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), 3, chatLines);
                    } catch (Exception e) {
                        System.out.println("[UndercastMod]: Getting a private value (chatLines) failed");
                        System.out.println("[UndercastMod]: ERROR: " + e.toString());
                    }
                }
                System.out.println("casted by mod: " + castedByMod);
                isListening = true;
            }
        }
    }
}
