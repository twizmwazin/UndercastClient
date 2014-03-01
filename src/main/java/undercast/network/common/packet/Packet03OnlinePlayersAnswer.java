package undercast.network.common.packet;

import java.util.ArrayList;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;

public class Packet03OnlinePlayersAnswer extends ServerPacket {

    public ArrayList<String> onlinePlayers;
    public Packet03OnlinePlayersAnswer(){}

    public Packet03OnlinePlayersAnswer(ArrayList<String> players){
        onlinePlayers = players;
    }
    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleOnlinePlayersAnswer(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        String onlinePlayersString = bufferIn.getString();
        // Already allocate some space for the ArrayList
        // As a username is +- 9/10 characters, it should be a good approximation :p
        onlinePlayers = new ArrayList<String>(onlinePlayersString.length() / 10);
        String currentUsername = "";
        for (int i = 0; i < onlinePlayersString.length(); i++){
            if(onlinePlayersString.charAt(i) != ':'){
                currentUsername += onlinePlayersString.charAt(i); 
            } else {
                onlinePlayers.add(currentUsername);
                currentUsername = "";
            }
        }
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        String onlinePlayersString = "";
        for(String str : onlinePlayers){
            onlinePlayersString += str + ":";
        }
        bufferOut.putString(onlinePlayersString);
    }

    @Override
    public int getPacketSize() {
        String onlinePlayersString = "";
        for(String str : onlinePlayers){
            onlinePlayersString += ":" + str + ":";
        }
        onlinePlayersString.substring(1); //Remove the first ':'
        return onlinePlayersString.length();
    }

    @Override
    public byte getId() {
        return 3;
    }

}
