package undercast.client.internetTools;

import undercast.client.UndercastData;
import undercast.client.UndercastData.MatchState;

public class ServersCommandParser {

    private static boolean isListening = false;

    public static void handleChatMessage(String message, String unstripedMessage) {
        if (isListening) {
            // check if the message belongs to the command
            if (!message.contains("Online: ")) {
                isListening = false;
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
        } else {
            if (message.equals("------------- Overcast Network Servers -------------")) {
                isListening = true;
            }
        }
    }
}
