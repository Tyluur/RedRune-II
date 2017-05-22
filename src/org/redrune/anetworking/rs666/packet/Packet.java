package org.redrune.network.rs666.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.redrune.utility.io.BufferUtils;

import lombok.Getter;

/**
 * @author 'Mystic Flow
 */
public class Packet {
	
	private final int opcode;
	
	private final PacketType type;
	
	private final ChannelBuffer buffer;
	
	private final int length;
	
	public Packet(int opcode, PacketType type, ChannelBuffer buffer) {
		this.opcode = opcode;
		this.type = type;
		this.buffer = buffer;
		this.length = buffer.readableBytes();
	}
	
	public boolean isRaw() {
		return opcode == -1;
	}
	
	public int getOpcode() {
		return opcode;
	}
	
	public PacketType getType() {
		return type;
	}
	
	public void read(byte[] b) {
		buffer.readBytes(b);
	}
	
	/**
	 * Reads an unsigned short.
	 *
	 * @return An unsigned short.
	 */
	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
	}
	
	/**
	 * Reads an integer.
	 *
	 * @return An integer.
	 */
	public int readInt() {
		return buffer.readInt();
	}
	
	/**
	 * Reads a long.
	 *
	 * @return A long.
	 */
	public long readLong() {
		return buffer.readLong();
	}
	
	/**
	 * Reads a type C byte.
	 *
	 * @return A type C byte.
	 */
	public byte readByteC() {
		return (byte) (-readByte());
	}
	
	public byte readByte() {
		return buffer.readByte();
	}
	
	/**
	 * reads a type S byte.
	 *
	 * @return A type S byte.
	 */
	public byte readByteS() {
		return (byte) (128 - readByte());
	}
	
	/**
	 * Reads a little-endian type A short.
	 *
	 * @return A little-endian type A short.
	 */
	public int readLEShortA() {
		int i = (buffer.readByte() - 128 & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
		return i;
	}
	
	/**
	 * Reads a little-endian short.
	 *
	 * @return A little-endian short.
	 */
	public int readLEShort() {
		int i = (buffer.readByte() & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
		return i;
	}
	
	/**
	 * Reads a V1 integer.
	 *
	 * @return A V1 integer.
	 */
	public int readInt1() {
		byte b1 = buffer.readByte();
		byte b2 = buffer.readByte();
		byte b3 = buffer.readByte();
		byte b4 = buffer.readByte();
		return ((b3 << 24) & 0xFF) | ((b4 << 16) & 0xFF) | ((b1 << 8) & 0xFF) | (b2 & 0xFF);
	}
	
	/**
	 * Reads a V2 integer.
	 *
	 * @return A V2 integer.
	 */
	public int readInt2() {
		int b1 = buffer.readByte() & 0xFF;
		int b2 = buffer.readByte() & 0xFF;
		int b3 = buffer.readByte() & 0xFF;
		int b4 = buffer.readByte() & 0xFF;
		return ((b2 << 24) & 0xFF) | ((b1 << 16) & 0xFF) | ((b4 << 8) & 0xFF) | (b3 & 0xFF);
	}
	
	/**
	 * reads a 3-byte integer.
	 *
	 * @return The 3-byte integer.
	 */
	public int readTriByte() {
		return ((buffer.readByte() << 16) & 0xFF) | ((buffer.readByte() << 8) & 0xFF) | (buffer.readByte() & 0xFF);
	}
	
	/**
	 * Reads a type A short.
	 *
	 * @return A type A short.
	 */
	public int readShortA() {
		int i = ((buffer.readByte() & 0xFF) << 8) | (buffer.readByte() - 128 & 0xFF);
		return i;
	}
	
	/**
	 * Reads a series of bytes in reverse.
	 *
	 * @param is
	 * 		The tarread byte array.
	 * @param offset
	 * 		The offset.
	 * @param length
	 * 		The length.
	 */
	public void readReverse(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			is[i] = buffer.readByte();
		}
	}
	
	/**
	 * Reads a series of type A bytes in reverse.
	 *
	 * @param is
	 * 		The tarread byte array.
	 * @param offset
	 * 		The offset.
	 * @param length
	 * 		The length.
	 */
	public void readReverseA(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			is[i] = readByteA();
		}
	}
	
	/**
	 * Reads a type A byte.
	 *
	 * @return A type A byte.
	 */
	public byte readByteA() {
		return (byte) (readByte() - 128);
	}
	
	/**
	 * Reads a series of bytes.
	 *
	 * @param is
	 * 		The tarread byte array.
	 * @param offset
	 * 		The offset.
	 * @param length
	 * 		The length.
	 */
	public void read(byte[] is, int offset, int length) {
		for (int i = 0; i < length; i++) {
			is[offset + i] = buffer.readByte();
		}
	}
	
	/**
	 * reads a smart.
	 *
	 * @return The smart.
	 */
	public int readSmart() {
		int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128) {
			return (readByte() & 0xFF);
		} else {
			return (readShort() & 0xFFFF) - 32768;
		}
	}
	
	/**
	 * Reads a short.
	 *
	 * @return A short.
	 */
	public int readShort() {
		return buffer.readShort();
	}
	
	public int remaining() {
		return buffer.readableBytes();
	}
	
	public ChannelBuffer getBuffer() {
		return buffer;
	}
	
	public int getLength() {
		return length;
	}
	
	public void readBytes(byte[] textBuffer, int length) {
		buffer.readBytes(textBuffer, 0, length);
	}
	
	public String readJagString() {
		readByte();
		return readRS2String();
	}
	
	/**
	 * Reads a RuneScape string.
	 *
	 * @return The string.
	 */
	public String readRS2String() {
		return BufferUtils.readRS2String(buffer);
	}
	
	public int readLEInt() {
		return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + (readUnsignedByte() << 24);
	}
	
	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	/**
	 * @author 'Mystic Flow
	 */
	public enum PacketType {
		STANDARD(0),
		VAR_BYTE(1),
		VAR_SHORT(2);
		
		@Getter
		private final int size;
		
		PacketType(int size) {
			this.size = size;
		}
		
	}
	
}
