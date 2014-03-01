package undercast.network.common.packet;

import undercast.network.common.Buffer;

public class Packet06SendServer extends ClientPacket {

    public String server;
    public Location loc;
    public Packet06SendServer(){}
    public enum Location {
        US, EU, UNKNOWN
    }
    public Packet06SendServer(String server, String loc){
        this.server = server;
        try{
            this.loc = Enum.valueOf(Location.class, loc);
        } catch(Exception e){
            this.loc = Location.UNKNOWN;
        }
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        this.server = bufferIn.getString();
        String loc = bufferIn.getString();
        for (Location l : Location.values()){
            if(l.name().equals(loc)){
                this.loc = l;
                return;
            }
        }
        this.loc = Location.UNKNOWN;
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putString(server);
        bufferOut.putString(loc.name());
    }

    @Override
    public int getPacketSize() {
        return this.getSize(server) + this.getSize(loc.name());
    }

    @Override
    public byte getId() {
        return 6;
    }
}
