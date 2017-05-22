package org.redrune.network.stream;

import org.redrune.utility.io.BufferUtils;

import io.netty.buffer.ByteBuf;

/**
 * IoReadEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class IoReadEvent implements IoHeap {

	private final int packetId;

	private final ByteBuf buffer;

	public IoReadEvent(int packetId, ByteBuf buffer) {
		this.packetId = packetId;
		this.buffer = buffer;
	}

	public int getPacketId() {
		return packetId;
	}

	public byte readByte() {
		return buffer.readByte();
	}

	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}

	public void read(byte[] b) {
		buffer.readBytes(b);
	}

	public short readShort() {
		if (buffer.readableBytes() < 2)
			return 0;
		return buffer.readShort();
	}

	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}

	public int readInt() {
		if (buffer.readableBytes() < 4)
			return 0;
		return buffer.readInt();
	}

	public long readLong() {
		if (buffer.readableBytes() < 8)
			return 0;
		return buffer.readLong();
	}

	public int readByteC() {
		return -readByte();
	}

	public int readByteS() {
		return 128 - readByte();
	}

	public int readLEShortA() {
		return (buffer.readByte() - 128 & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
	}

	public int readLEShort() {
		return (buffer.readByte() & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
	}

	public int readShortA() {
		return ((buffer.readByte() & 0xFF) << 8) | (buffer.readByte() - 128 & 0xFF);
	}

	public int readIntA() {
		int b1 = buffer.readByte() & 0xFF;
		int b2 = buffer.readByte() & 0xFF;
		int b3 = buffer.readByte() & 0xFF;
		int b4 = buffer.readByte() & 0xFF;
		return (b3 << 24 | b4 << 16 | b1 << 8 | b2);
	}

	public int readIntB() {
		int b1 = buffer.readByte() & 0xFF;
		int b2 = buffer.readByte() & 0xFF;
		int b3 = buffer.readByte() & 0xFF;
		int b4 = buffer.readByte() & 0xFF;
		return (b2 << 24 | b1 << 16 | b4 << 8 | b3);
	}

	public int readTriByte() {
		return ((buffer.readByte() << 16) & 0xFF) | ((buffer.readByte() << 8) & 0xFF) | (buffer.readByte() & 0xFF);
	}

	public int readByteA() {
		return readByte() - 128;
	}

	public String readRS2String() {
		return BufferUtils.readString(buffer);
	}

	public void readReverse(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			is[i] = buffer.readByte();
		}
	}

	public void readReverseA(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			is[i] = (byte) readByteA();
		}
	}

	public void read(byte[] is, int offset, int length) {
		for (int i = 0; i < length; i++) {
			is[offset + i] = buffer.readByte();
		}
	}

	public int readSmart() {
		int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128) {
			return (readByte() & 0xFF);
		} else {
			return (readShort() & 0xFFFF) - 32768;
		}
	}

	public int readSmart2() {
		int i = 0;
		int i_33_;
		for (i_33_ = readSmart(); ~i_33_ == -32768;) {
			i_33_ = readSmart();
			i += 32767;
		}

		i += i_33_;
		return i;
	}

	public int readUnsignedSmart() {
		int i = 0xff & buffer.getByte(buffer.readerIndex());
		if (i >= 128) {
			return -32768 + readUnsignedShort();
		} else {
			return readUnsignedByte();
		}
	}

	public void readBytes(byte[] textBuffer, int length) {
		buffer.readBytes(textBuffer, 0, length);
	}

	public String readJagString() {
		readByte();
		return readRS2String();
	}

	public int readLEInt() {
		return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + (readUnsignedByte() << 24);
	}

	public int remaining() {
		return buffer.readableBytes();
	}

	@Override
	public ByteBuf getBuffer() {
		return buffer;
	}

	@Override
	public boolean isRaw() {
		return false;
	}

}
