package org.redrune.utility.xtea;

import org.redrune.utility.io.BufferUtils;

/**
 * XTEACryption.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class XTEACryption {

	private static final int DELTA = -1640531527;
	private static final int SUM = -957401312;
	private static final int NUM_ROUNDS = 32;

	private final int[] key;

	private XTEACryption(int[] key) {
		this.key = key;
	}

	public static XTEACryption set(int[] key) {
		return new XTEACryption(key);
	}

	public byte[] decrypt(byte[] data, int offset, int length) {
		int numBlocks = length / 8;
		int[] block = new int[2];
		for (int i = 0; i < numBlocks; i++) {
			block[0] = BufferUtils.readInt((i * 8) + offset, data);
			block[1] = BufferUtils.readInt((i * 8) + offset + 4, data);
			decipher(block);
			BufferUtils.writeInt(block[0], (i * 8) + offset, data);
			BufferUtils.writeInt(block[1], (i * 8) + offset + 4, data);
		}
		return data;
	}

	private void decipher(int[] block) {
		long sum = SUM;
		for (int i = 0; i < NUM_ROUNDS; i++) {
			block[1] -= (key[(int) ((sum & 0x1933) >>> 11)] + sum ^ block[0] + (block[0] << 4 ^ block[0] >>> 5));
			sum -= DELTA;
			block[0] -= ((block[1] << 4 ^ block[1] >>> 5) + block[1] ^ key[(int) (sum & 0x3)] + sum);
		}
	}

}
