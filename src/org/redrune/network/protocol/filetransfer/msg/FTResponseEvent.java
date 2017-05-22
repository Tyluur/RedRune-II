package org.redrune.network.protocol.filetransfer.msg;

/**
 * FTResponseEvent.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class FTResponseEvent {

	private final int container;

	private final int archive;

	private final boolean priority;

	public FTResponseEvent(int container, int archive, boolean priority) {
		this.container = container;
		this.archive = archive;
		this.priority = priority;
	}

	public int getContainer() {
		return container;
	}

	public int getArchive() {
		return archive;
	}

	public boolean isPriority() {
		return priority;
	}

}
