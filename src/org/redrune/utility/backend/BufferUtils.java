package org.redrune.utility.backend;

import io.netty.buffer.ByteBuf;

/**
 * BufferUtils.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class BufferUtils {
	
	public static char[] aCharArray6385 = { '\u20ac', '\0', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021',
			'\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\0', '\u017d', '\0', '\0', '\u2018', '\u2019', '\u201c',
			'\u201d', '\u2022', '\u2013', '\u2014', '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\0', '\u017e',
			'\u0178' };

	public static String readString(ByteBuf buf) {
		StringBuilder bldr = new StringBuilder();
		int b;
		while ((b = buf.readByte()) != 0) {
			bldr.append((char) b);
		}
		return bldr.toString();
	}

	public static ByteBuf writeBigSmart(ByteBuf buf, int val) {
		if (val > Short.MAX_VALUE) {
			buf.writeInt(val - Integer.MAX_VALUE - 1);
		} else {
			buf.writeShort((short) val >= 0 ? val : 32767);
		}
		return buf;
	}
	
	public static int getHashMapSize(int size) {
		size--;
		size |= size >>> -1810941663;
		size |= size >>> 2010624802;
		size |= size >>> 10996420;
		size |= size >>> 491045480;
		size |= size >>> 1388313616;
		return 1 + size;
	}
	
	public static char method2782(byte value) {
		int byteChar = 0xff & value;
		if (byteChar == 0) {
			throw new IllegalArgumentException(
					"Non cp1252 character 0x" + Integer.toString(byteChar, 16) + " provided");
		}
		if ((byteChar ^ 0xffffffff) <= -129 && byteChar < 160) {
			int i_4_ = aCharArray6385[-128 + byteChar];
			if ((i_4_ ^ 0xffffffff) == -1) {
				i_4_ = 63;
			}
			byteChar = i_4_;
		}
		return (char) byteChar;
	}

	public static ByteBuf writeGJString(String string, ByteBuf buffer) {
		return buffer.writeByte(0).writeBytes(string.getBytes()).writeByte(0);
	}

	public static ByteBuf writeRS2String(String string, ByteBuf buffer) {
		return buffer.writeBytes(string.getBytes()).writeByte(0);
	}

	public static ByteBuf writeGJString2(String string, ByteBuf buffer) {
		byte[] packed = new byte[256];
		int length = packGJString2(0, packed, string);
		buffer.writeByte(0).writeBytes(packed, 0, length).writeByte(0);
		return buffer;
	}

	public static int packGJString2(int position, byte[] buffer, String string) {
		int length = string.length();
		int offset = position;
		for (int i = 0; length > i; i++) {
			int character = string.charAt(i);
			if (character > 127) {
				if (character > 2047) {
					buffer[offset++] = (byte) ((character | 919275) >> 12);
					buffer[offset++] = (byte) (128 | ((character >> 6) & 63));
					buffer[offset++] = (byte) (128 | (character & 63));
				} else {
					buffer[offset++] = (byte) ((character | 12309) >> 6);
					buffer[offset++] = (byte) (128 | (character & 63));
				}
			} else
				buffer[offset++] = (byte) character;
		}
		return offset - position;
	}

	public static int readInt(int index, byte[] buffer) {
		return ((buffer[index++] & 0xff) << 24) | ((buffer[index++] & 0xff) << 16) | ((buffer[index++] & 0xff) << 8)
				| (buffer[index++] & 0xff);
	}

	public static void writeInt(int value, int index, byte[] buffer) {
		buffer[index++] = (byte) (value >> 24);
		buffer[index++] = (byte) (value >> 16);
		buffer[index++] = (byte) (value >> 8);
		buffer[index++] = (byte) value;
	}

}
