package org.redrune.rs2.node.entity.player.event.context;

import lombok.Getter;
import org.redrune.rs2.node.entity.player.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class CommandEventContext implements EventContext {
	
	/**
	 * The arguments of the command
	 */
	@Getter
	private final String[] arguments;
	
	public CommandEventContext(String[] arguments) {
		this.arguments = arguments;
	}
}
