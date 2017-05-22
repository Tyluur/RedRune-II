package org.redrune.network.protocol.filetransfer.msg;

/**
 * FTEncryptRequestEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class FTEncryptRequestEvent {

	private final int key;

	public FTEncryptRequestEvent(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

}
