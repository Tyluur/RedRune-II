package org.redrune.network.master;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * This is the packet used for communication between the game server and the master server
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/9/2017
 */
public class MasterPacket {
	
	/**
	 * The buffer that we're reading
	 */
	private byte[] inBuffer;
	
	/**
	 * Thet buffer that we're writing
	 */
	private ChannelBuffer outBuffer;
	
	/**
	 * The position of the buffer
	 */
	private int position;
	
	public MasterPacket(int opcode) {
		this.outBuffer = ChannelBuffers.dynamicBuffer();
		this.writeByte(opcode);
	}
	
	public MasterPacket(byte[] inBuffer) {
		this.inBuffer = inBuffer;
	}
	
	public boolean readBoolean() {
		int b = readByte();
		return b == 1;
	}
	
	public int readInt() {
		return readShort() << 16 | readShort();
	}
	
	public int readShort() {
		return readByte() << 8 | readByte();
	}
	
	public int readByte() {
		return inBuffer[position++] & 0xff;
	}
	
	public long readLong() {
		long value = 0;
		value |= (long) readByte() << 56L;
		value |= (long) readByte() << 48L;
		value |= (long) readByte() << 40L;
		value |= (long) readByte() << 32L;
		value |= (long) readByte() << 24L;
		value |= (long) readByte() << 16L;
		value |= (long) readByte() << 8L;
		value |= readByte();
		return value;
	}
	
	public String readString() {
		int c;
		StringBuilder builder = new StringBuilder();
		while ((c = readByte()) != 10) {
			builder.append((char) c);
		}
		return builder.toString();
	}
	
	public MasterPacket writeBoolean(boolean b) {
		outBuffer.writeByte(b ? 1 : 0);
		return this;
	}
	
	public MasterPacket writeByte(int i) {
		outBuffer.writeByte(i);
		return this;
	}
	
	public MasterPacket writeShort(int i) {
		outBuffer.writeShort(i);
		return this;
	}
	
	public MasterPacket writeInt(int i) {
		outBuffer.writeInt(i);
		return this;
	}
	
	public MasterPacket writeLong(long l) {
		outBuffer.writeLong(l);
		return this;
	}
	
	public MasterPacket writeString(String s) {
		for (byte b : s.getBytes()) {
			writeByte(b);
		}
		writeByte(10);
		return this;
	}
	
	public int getLength() {
		return outBuffer.writerIndex();
	}
	
	public ChannelBuffer getData() {
		return outBuffer;
	}
}
