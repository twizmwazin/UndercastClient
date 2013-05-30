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
        if(isListening) {
            // check if the message belongs to the command
            if (!message.contains("Online: ")) {
                isListening = false;
                if(castedByMod) {
                    //clean the chat messages
                    try {
                        int startMessageIndex = 0;
                        List chatLines;
                        // get the lines
                        chatLines = (List)ModLoader.getPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), 3);
                        // get the start point = most recent start message
                        for(int c = 0; c < chatLines.size(); c++) {
                            ChatLine line = (ChatLine)chatLines.get(c);
                            if(StringUtils.stripControlCodes(line.getChatLineString()).equals("------------- Overcast Network Servers -------------")) {
                                startMessageIndex = c;
                                chatLines.remove(c);
                                break;
                            }
                        }
                        // filter all messages
                        if(startMessageIndex != 0) {
                             int c = 1;
                             while(startMessageIndex - c >= 0) {
                                 ChatLine line = (ChatLine)chatLines.get(startMessageIndex - c);
                                 if(StringUtils.stripControlCodes(line.getChatLineString()).contains("Online: ")) {
                                     chatLines.remove(startMessageIndex - c);
                                 } else {
                                     break;
                                 }
                                 c++;
                             }
                             
                             //remove it for the player
                             ModLoader.setPrivateValue(GuiNewChat.class, Minecraft.getMinecraft().ingameGUI.getChatGUI(), 3, chatLines);
                        }
                    } catch (Exception e) {
                        System.out.println("[UndercastMod]: Getting a private value (chatLines) failed");
                        System.out.println("[UndercastMod]: ERROR: " + e.toString());
                    }
                }
                castedByMod = false;
                return;
            }
            //our only interest is the MatchState
            if(message.contains("Current Map: ")) {
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
                switch(matchStatusColor) {
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
                for(int c = 0; c < UndercastData.serverInformation.length; c++) {
                    if(!(UndercastData.serverInformation[c].name == null)) {
                        if(UndercastData.serverInformation[c].name.equals(name)) {
                            UndercastData.serverInformation[c].currentMap = map;
                            UndercastData.serverInformation[c].matchState = state;
                        }
                    }
                }
            }
        } else {
            if(message.equals("------------- Overcast Network Servers -------------")) {
                isListening = true;
            }
        }
    }

}
