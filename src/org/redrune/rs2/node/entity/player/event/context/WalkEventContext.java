package org.redrune.rs2.node.entity.player.event.context;

import lombok.Getter;
import org.redrune.rs2.node.entity.player.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class WalkEventContext implements EventContext {
	
	/**
	 * The destination x coordinate
	 */
	@Getter
	private final int x;
	
	/**
	 * The destination y coordinate
	 */
	@Getter
	private final int y;
	
	/**
	 * If ctrl was pressed when we started running
	 */
	@Getter
	private final boolean running;
	
	public WalkEventContext(int x, int y, boolean running) {
		this.x = x;
		this.y = y;
		this.running = running;
	}
}
