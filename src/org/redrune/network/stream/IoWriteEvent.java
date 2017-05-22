package org.redrune.network.stream;

import org.redrune.network.packet.PacketHeader;
import org.redrune.network.packet.PacketHeader.PacketType;
import org.redrune.utility.io.BufferUtils;
import org.redrune.utility.isaac.IsaacRandomPair;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * IoWriteEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class IoWriteEvent implements IoHeap {

	private static final int[] BIT_MASK_OUT = new int[] { 0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff, 0x3ff,
			0x7ff, 0xfff, 0x1fff, 0x3fff, 0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff,
			0x7fffff, 0xffffff, 0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };

	private int opcode;

	private int bitPosition = 0;

	private final ByteBuf buffer = Unpooled.buffer();

	public IoWriteEvent() {
		this(-1);
	}

	private IoWriteEvent(int opcode) {
		this.opcode = opcode;
	}

	public void clear() {
		bitPosition = 0;
		buffer.clear();
	}

	public IoWriteEvent write(int val) {
		buffer.writeByte(val);
		return this;
	}

	public IoWriteEvent writeBytes(byte[] b) {
		buffer.writeBytes(b);
		return this;
	}

	public void writeBytes(ByteBuf other) {
		buffer.writeBytes(other);
	}

	public IoWriteEvent writeShort(int s) {
		buffer.writeShort((short) s);
		return this;
	}

	public IoWriteEvent writeInt(int i) {
		buffer.writeInt(i);
		return this;
	}

	public IoWriteEvent writeLong(long l) {
		buffer.writeLong(l);
		return this;
	}

	public IoWriteEvent writeLarge(long l) {
		buffer.writeByte((int) (l >> 32));
		buffer.writeInt((int) (l & 0xffffffff));
		return this;
	}

	public IoWriteEvent writeRS2String(String string) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte(0);
		return this;
	}

	public IoWriteEvent writeGJString(String string) {
		BufferUtils.writeGJString(string, buffer);
		return this;
	}

	public IoWriteEvent writeGJString2(String string) {
		BufferUtils.writeGJString2(string, buffer);
		return this;
	}

	/**
	 * AKA putShortA
	 * @param val
	 * @return
	 */
	public IoWriteEvent writeShort128(int val) {
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) (val + 128));
		return this;
	}

	public IoWriteEvent writeA(int val) {
		buffer.writeByte((byte) (val + 128));
		return this;
	}

	public IoWriteEvent writeShortLE128(int val) {
		buffer.writeByte((byte) (val + 128));
		buffer.writeByte((byte) (val >> 8));
		return this;
	}

	public IoWriteEvent initBitAccess() {
		bitPosition = buffer.writerIndex() * 8;
		return this;
	}

	public IoWriteEvent finishBitAccess() {
		buffer.writerIndex((bitPosition + 7) / 8);
		return this;
	}

	public IoWriteEvent writeBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		int pos = (bitPosition + 7) / 8;
		buffer.ensureWritable(pos + 1); // pos + 1
		buffer.writerIndex(pos);
		byte b;
		for (; numBits > bitOffset; bitOffset = 8) {
			b = buffer.getByte(bytePos);
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos++, (byte) (b | (value >> (numBits - bitOffset)) & BIT_MASK_OUT[bitOffset]));
			numBits -= bitOffset;
		}
		b = buffer.getByte(bytePos);
		if (numBits == bitOffset) {
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos, (byte) (b | value & BIT_MASK_OUT[bitOffset]));
		} else {
			buffer.setByte(bytePos, (byte) (b & ~(BIT_MASK_OUT[numBits] << (bitOffset - numBits))));
			buffer.setByte(bytePos, (byte) (b | (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits)));
		}
		return this;
	}

	public IoWriteEvent writeC(int val) {
		buffer.writeByte((byte) -val);
		return this;
	}

	public IoWriteEvent writeLEShort(int val) {
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		return this;
	}

	public IoWriteEvent writeIntA(int val) {
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) (val >> 16));
		return this;
	}

	public IoWriteEvent writeIntB(int val) {
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		return this;
	}

	public IoWriteEvent writeLEInt(int val) {
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		return this;
	}

	public IoWriteEvent writeBytes(byte[] data, int offset, int length) {
		buffer.writeBytes(data, offset, length);
		return this;
	}

	public IoWriteEvent writeA(byte val) {
		buffer.writeByte((byte) (val + 128));
		return this;
	}

	public IoWriteEvent writeS(int val) {
		buffer.writeByte((byte) (128 - val));
		return this;
	}

	public IoWriteEvent writeBytesA(byte[] data, int offset, int len) {
		for (int k = offset; k < len; k++) {
			buffer.writeByte((byte) (data[k] + 128));
		}
		return this;
	}

	public IoWriteEvent writeReverse(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			write(is[i]);
		}
		return this;
	}

	public IoWriteEvent writeReverseA(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			writeA(is[i]);
		}
		return this;
	}

	public IoWriteEvent writeMedium(int val) {
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) val);
		return this;
	}

	public IoWriteEvent setOpcode(int id) {
		opcode = id;
		return this;
	}

	public IoWriteEvent writeSmart(int val) {
		if (val >= 128) {
			writeShort(val + 32768);
		} else {
			write(val);
		}
		return this;
	}

	public IoWriteEvent writeIntSmart(int val) {
		if (val >= 32768) {
			writeInt(val + 32768);
		} else {
			writeShort(val);
		}
		return this;
	}

	public IoWriteEvent writeBigSmart(int val) {
		if (val > Short.MAX_VALUE) {
			writeInt(val - Integer.MAX_VALUE - 1);
		} else {
			writeShort((short) val >= 0 ? val : 32767);
		}
		return this;
	}

	public IoWriteEvent get() {
		return this;
	}

	@Override
	public ByteBuf getBuffer() {
		return buffer;
	}

	public int getPacketId() {
		return opcode;
	}

	@Override
	public boolean isRaw() {
		return opcode == -1;
	}

	public static ByteBuf serialize(IsaacRandomPair isaacPair, Class<?> clazz, IoWriteEvent message) {
		if (!message.isRaw()) {
			int packetLength = message.getBuffer().readableBytes() + 3;
			ByteBuf response = Unpooled.buffer(packetLength);
			if (message.getPacketId() > 127) {
				response.writeByte((byte) 128 + isaacPair.getOutput().getNextValue());
			}
			response.writeByte((byte) message.getPacketId() + isaacPair.getOutput().getNextValue());
			if (getPacket(clazz).equals(PacketType.VAR_BYTE)) {
				response.writeByte((byte) message.getBuffer().readableBytes());
			} else if (getPacket(clazz).equals(PacketType.VAR_SHORT)) {
				if (packetLength > 65535) // Stack overflow.
					throw new IllegalStateException(
							"Could not send a packet with " + packetLength + " bytes within 16 bits.");
				response.writeByte((byte) (message.getBuffer().readableBytes() >> 8));
				response.writeByte((byte) message.getBuffer().readableBytes());
			}
			response.writeBytes(message.getBuffer());
			return response;
		}
		return message.getBuffer();
	}

	private static PacketType getPacket(Class<?> clazz) {
		PacketHeader header = clazz.getAnnotation(PacketHeader.class);
		if (header == null) {
			return PacketType.STANDARD;
		}
		return header.packet();
	}

	public static IoWriteEvent create(int opcode) {
		return new IoWriteEvent(opcode);
	}

	public static IoWriteEvent create() {
		return new IoWriteEvent();
	}

	public void transfer(IoWriteEvent response) {
		buffer.writeBytes(response.getBuffer());
	}

}