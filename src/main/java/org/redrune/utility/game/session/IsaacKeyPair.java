package org.redrune.utility.game.session;

public class IsaacKeyPair {
	
	private final ISAACCipher inKey;
    private final ISAACCipher outKey;
	
	public IsaacKeyPair(int[] seed) {
		inKey = new ISAACCipher(seed);
		for (int i = 0; i < seed.length; i++) {
			seed[i] += 50;
		}
		outKey = new ISAACCipher(seed);
	}
	
	public ISAACCipher inKey() {
		return inKey;
	}
	
	public ISAACCipher outKey() {
		return outKey;
	}
	
}
