package org.redrune.utility.backend.isaac;

/**
 * IsaacRandomPair.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public final class IsaacRandomPair {

	private IsaacRandom input;

	private IsaacRandom output;

	public IsaacRandomPair(IsaacRandom input, IsaacRandom output) {
		this.input = input;
		this.output = output;
	}

	public IsaacRandom getInput() {
		return input;
	}

	public IsaacRandom getOutput() {
		return output;
	}

}