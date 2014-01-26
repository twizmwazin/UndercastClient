package undercast.client.internetTools;

import net.minecraft.client.Minecraft;
import undercast.client.UndercastCustomMethods;
import undercast.client.UndercastData;
import undercast.client.UndercastData.MatchState;
import undercast.client.UndercastData.ServerLocation;

public class ServersCommandParser {

    private static boolean isListening = false;
    // is set to true if the mod casts /servers in order to delete the messages
    public static boolean castedByMod = false;
    public static boolean nextCastedByMod = true;
    public static int pages = 0;

    public static boolean handleChatMessage(String message, String unstripedMessage) {
        if (isListening) {
            // check if the message belongs to the command
            boolean commandEnded = false;
            if (!message.contains("Online: ") && !message.contains("-------- Overcast Network Servers")) {
                isListening = false;
                castedByMod = nextCastedByMod;
                UndercastCustomMethods.sortAndFilterServers();
                commandEnded = true;
                UndercastData.removeNextChatMessage = false;
            }

            // don't try to handle it if the command ended
            if (!commandEnded) {
                // our only interest is the MatchState
                if (message.contains("Current Map: ")) {
                    String name;
                    String map;
                    char matchStatusColor;
                    MatchState state;

                    name = message.substring(0, message.indexOf(": "));
                    map = message.substring(message.indexOf("Current Map: ") + 13);
                    matchStatusColor = unstripedMessage.charAt(unstripedMessage.indexOf("Current Map\u00A7r\u00A7f: ") + 20);

                    // c == red
                    // e == yellow
                    // a = green
                    // f = white
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
                        case 'f':
                            state = MatchState.Waiting;
                            break;
                        default:
                            state = MatchState.Unknown;
                    }

                    // insert the data
                    for (int c = 0; c < UndercastData.serverInformation.length; c++) {
                        if (!(UndercastData.serverInformation[c].name == null) && ((!UndercastData.isEU && UndercastData.serverInformation[c].location == ServerLocation.US) || (UndercastData.isEU && UndercastData.serverInformation[c].location == ServerLocation.EU))) {
                            if (UndercastData.serverInformation[c].name.equals(name)) {
                                UndercastData.serverInformation[c].currentMap = map;
                                UndercastData.serverInformation[c].matchState = state;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (!isListening && message.contains("-------- Overcast Network Servers (1 of ") && UndercastData.removeNextChatMessage) {
            // get the page count
            try {
                pages = Integer.parseInt(message.substring(message.indexOf("of ") + 3, message.lastIndexOf(")")));
            } catch (Exception e) {
                pages = 10;
            }
            for (int c = 1; c < (UndercastData.isEU ? UndercastData.parsedPagesEU.length : UndercastData.parsedPagesUS.length); c++) {
                int page = UndercastData.isEU ? UndercastData.parsedPagesEU[c] : UndercastData.parsedPagesUS[c];
                if (page < 0) {
                    page = pages + (page + 1); // + 1 is for not removing
                                               // anything from pages (keep in
                                               // mind that I'm adding a
                                               // negative value)
                }
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/servers " + page);
            }

            if (castedByMod) {
                isListening = true;
            }
        }
        return UndercastData.removeNextChatMessage;
    }

    public static void castByMod() {
        if (!isListening) {
            castedByMod = true;
        } else {
            nextCastedByMod = true;
        }
    }
}
