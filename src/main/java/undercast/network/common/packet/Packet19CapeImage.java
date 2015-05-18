package undercast.network.common.packet;

import undercast.network.client.NetClientManager;
import undercast.network.common.Buffer;
import undercast.network.common.ImageReader;

public class Packet19CapeImage extends ServerPacket {
    public ImageReader.Image image;

    public Packet19CapeImage() {
    }

    public Packet19CapeImage(ImageReader.Image i) {
        image = i;
    }

    @Override
    public void handlePacket(NetClientManager networkManager) {
        networkManager.handleCapeImage(this);
    }

    @Override
    public void readPacketData(Buffer bufferIn) {
        image = new ImageReader.Image(bufferIn.getByteArray(), (byte) bufferIn.get());
    }

    @Override
    public void writePacketData(Buffer bufferOut) {
        bufferOut.putByteArray(image.image);
        bufferOut.put(image.id);
    }

    @Override
    public int getPacketSize() {
        return getSize(image.id) + getSize(image.image);
    }

    @Override
    public byte getId() {
        return 19;
    }
}
