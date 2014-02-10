package undercast.network.common.packet;


import undercast.network.common.Buffer;

public class Packet18AskForCape extends ClientPacket {
    public byte id;

    public Packet18AskForCape(){}
    public Packet18AskForCape(byte id){
        this.id = id;
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        this.id = (byte)bufferIn.get();
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.put(this.id);
    }

    @Override
    public int getPacketSize() {
        return getSize(this.id);
    }

    @Override
    public byte getId() {
        return 18;
    }
}
