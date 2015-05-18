package undercast.network.common;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Buffer {

    private byte[] buffer;
    private int readPos = 0, writePos = 0, sizePos = 0;

    public Buffer() {
        this(128);
    }

    public Buffer(int size) {
        buffer = new byte[size];
    }
    public Buffer(byte[] data) {
        /*
         * This create a new buffer from an array of byte
         * It also excludes the first element (packet id)
         */
        buffer = new byte[data.length - 1];
        for (int i = 0; i < data.length - 1; i++) {
            buffer[i] = data[i + 1];
        }
    }

    public byte[] array() {
        return Arrays.copyOf(buffer, writePos);
    }

    public boolean getBool() {
        return get() % 2 == 1;
    }

    public int get() {
        return buffer[readPos++] & 0xFF;
    }

    public int getSigned() {
        return buffer[readPos++];
    }

    public int getUShort() {
        return buffer[readPos++] << 8 | (buffer[readPos++] & 0xFF);
    }

    public int getShort() {
        return (buffer[readPos++] & 0xFF) << 8 | (buffer[readPos++] & 0xFF);
    }

    public int getInt() {
        return (buffer[readPos++] & 0xFF) << 24 | (buffer[readPos++] & 0xFF) << 16 | (buffer[readPos++] & 0xFF) << 8 | (buffer[readPos++] & 0xFF);
    }

    public long getLong() {
        return (buffer[readPos++] & 0xFFL) << 56 | (buffer[readPos++] & 0xFFL) << 48 | (buffer[readPos++] & 0xFFL) << 40 | (buffer[readPos++] & 0xFFL) << 32 | (buffer[readPos++] & 0xFFL) << 24 | (buffer[readPos++] & 0xFFL) << 16 | (buffer[readPos++] & 0xFFL) << 8 | buffer[readPos++] & 0xFFL;
    }

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    public int getLEShort() {
        return (buffer[readPos++] & 0xFF) | (buffer[readPos++] & 0xFF) << 8;
    }

    public int getLEInt() {
        return (buffer[readPos++] & 0xFF) | (buffer[readPos++] & 0xFF) << 8 | (buffer[readPos++] & 0xFF) << 16 | (buffer[readPos++] & 0xFF) << 24;
    }

    public String getString() {
        StringBuilder bldr = new StringBuilder();
        char c;
        while ((c = (char) buffer[readPos++]) != 0) {
            bldr.append(c);
        }
        return bldr.toString();
    }

    public byte[] getByteArray() {
        int length = this.getInt();
        byte[] returnArray = new byte[length];
        for (int i = 0; i < length; i++) {
            returnArray[i] = (byte) get();
        }
        return returnArray;
    }

    public int readPosition() {
        return readPos;
    }

    public int remaining() {
        return writePos - readPos;
    }

    public int writePosition() {
        return writePos;
    }

    public Buffer put(ByteBuffer buffer) {
        int len = buffer.position();
        ensure(len);
        System.arraycopy(buffer.array(), 0, this.buffer, writePos, len);
        writePos += len;
        return this;
    }

    public Buffer put(byte[] buf) {
        int len = buf.length;
        ensure(len);
        System.arraycopy(buf, 0, buffer, writePos, len);
        writePos += len;
        return this;
    }

    public Buffer put(byte[] buf, int off, int len) {
        ensure(len - off);
        System.arraycopy(buf, off, buffer, writePos, len - off);
        writePos += len - off;
        return this;
    }

    public Buffer putBool(boolean b) {
        ensure(1);
        buffer[writePos++] = (byte) (b ? 1 : 0);
        return this;
    }

    public Buffer put(int b) {
        ensure(1);
        buffer[writePos++] = (byte) b;
        return this;
    }

    public Buffer putShort(int s) {
        ensure(2);
        buffer[writePos++] = (byte) (s >> 8);
        buffer[writePos++] = (byte) s;
        return this;
    }

    public Buffer putInt(int i) {
        ensure(4);
        buffer[writePos++] = (byte) (i >> 24);
        buffer[writePos++] = (byte) (i >> 16);
        buffer[writePos++] = (byte) (i >> 8);
        buffer[writePos++] = (byte) i;
        return this;
    }

    public Buffer putLong(long l) {
        ensure(8);
        buffer[writePos++] = (byte) (l >> 56);
        buffer[writePos++] = (byte) (l >> 48);
        buffer[writePos++] = (byte) (l >> 40);
        buffer[writePos++] = (byte) (l >> 32);
        buffer[writePos++] = (byte) (l >> 24);
        buffer[writePos++] = (byte) (l >> 16);
        buffer[writePos++] = (byte) (l >> 8);
        buffer[writePos++] = (byte) l;
        return this;
    }

    public Buffer putDouble(double d) {
        return putLong(Double.doubleToRawLongBits(d));
    }

    public Buffer putLEInt(int i) {
        ensure(4);
        buffer[writePos++] = (byte) i;
        buffer[writePos++] = (byte) (i >> 8);
        buffer[writePos++] = (byte) (i >> 16);
        buffer[writePos++] = (byte) (i >> 24);
        return this;
    }

    public Buffer putLEShort(int s) {
        ensure(2);
        buffer[writePos++] = (byte) s;
        buffer[writePos++] = (byte) (s >> 8);
        return this;
    }

    public Buffer putString(String s) {
        // cannot use character 0
        ensure(s.length() + 1);
        for (int i = 0; i < s.length(); i++) {
            buffer[writePos++] = (byte) s.charAt(i);
        }
        buffer[writePos++] = 0;
        return this;
    }

    public Buffer putByteArray(byte[] array) {
        putInt(array.length);
        for (int i = 0; i < array.length; i++) {
            buffer[writePos++] = array[i];
        }
        return this;
    }

    public Buffer end() {
        buffer[sizePos] = (byte) (writePos - sizePos - 1);
        return this;
    }

    public Buffer start(int opcode) {
        ensure(2);
        buffer[writePos++] = (byte) opcode;
        sizePos = writePos++;
        return this;
    }

    public void clear() {
        readPos = writePos = sizePos = 0;
        Arrays.fill(buffer, (byte) 0);
    }

    private void ensure(int size) {
        int required = writePos + size;
        if (buffer.length < required) {
            byte[] temp = new byte[required];
            System.arraycopy(buffer, 0, temp, 0, buffer.length);
            buffer = temp;
        }
    }

    public void rewind() {
        readPos = 0;
    }

    public void skip(int n) {
        readPos += n;
    }
}