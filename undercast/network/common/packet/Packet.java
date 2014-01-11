package undercast.network.common.packet;

import java.nio.ByteBuffer;

import undercast.network.common.Buffer;
import jexxus.common.Connection;

public abstract class Packet {
    
    /*
     * Please keep ALWAYS in mind that odd numbers are client packet sent to server
     * and even numbers are server answers!
     * 
     */
    public abstract void readPacketData(Buffer bufferIn);
    public abstract void writePacketData(Buffer bufferOut);
    /*
     * Please keep in mind that string are 1 byte larger than str.length() in this case
     * since the buffer use an empty byte at the end of the string to delimit it
     */
    public abstract int getPacketSize();
    public abstract byte getId();
    public static ServerPacket getPacketFromId(byte id){
        switch(id) {
            case 1:
                return new Packet01IsValidAuthentication();
            case 3:
                return new Packet03OnlinePlayersAnswer();
            case 7:
                return new Packet07KickPacket();
            case 11:
                return new Packet11SendServers();
        }
        return null;
    }
    
    public int getSize(String s){
        return s.length() + 1;
    }
    
    public int getSize(int i){
        return 4;
    }
    
    public int getSize(long i){
        return 8;
    }
    
    public int getSize(short s){
        return 2;
    }
    
    public int getSize(double d){
        return 8;
    }
}
