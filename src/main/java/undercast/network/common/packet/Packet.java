package undercast.network.common.packet;

import undercast.network.common.Buffer;

public abstract class Packet {

    public static ServerPacket getPacketFromId(byte id) {
        switch (id) {
            case 1:
                return new Packet01IsValidAuthentication();
            case 3:
                return new Packet03OnlinePlayersAnswer();
            case 7:
                return new Packet07KickPacket();
            case 11:
                return new Packet11SendServers();
            case 13:
                return new Packet13SendVIPs();
            case 15:
                return new Packet15ShowNotif();
            case 17:
                return new Packet17IsPlayerConnected();
            case 19:
                return new Packet19CapeImage();
        }
        return null;
    }

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

    public int getSize(String s) {
        return s.length() + 1;
    }

    public int getSize(int i) {
        return 4;
    }

    public int getSize(long i) {
        return 8;
    }

    public int getSize(short s) {
        return 2;
    }

    public int getSize(double d) {
        return 8;
    }

    public int getSize(byte b) {
        return 1;
    }

    public int getSize(byte[] array) {
        return 4 + array.length; //first 4 bits are for the length of the array
    }
}
