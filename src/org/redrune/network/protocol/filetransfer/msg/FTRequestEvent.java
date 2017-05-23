package org.redrune.network.protocol.filetransfer.msg;

import lombok.Getter;

/**
 * FTRequestEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class FTRequestEvent {

	@Getter
	private final int container;

	@Getter
	private final int archive;

	@Getter
	private final boolean priority;

	public FTRequestEvent(int container, int archive, boolean priority) {
		this.container = container;
		this.archive = archive;
		this.priority = priority;
	}

}
