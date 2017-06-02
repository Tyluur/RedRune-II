package org.redrune.game.node.entity.player.event.context;

import lombok.Getter;
import org.redrune.game.node.entity.player.event.EventContext;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class ObjectEventContext implements EventContext {
	
	/**
	 * The game object
	 */
	@Getter
	private final GameObject object;
	
	/**
	 * The option
	 */
	@Getter
	private final InteractionOption option;
	
	public ObjectEventContext(GameObject object, InteractionOption option) {
		this.object = object;
		this.option = option;
	}
}
