package org.redrune.game.node.entity.player.event;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.EventPolicy.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public abstract class Event<T extends EventContext> {
	
	/**
	 * If the event can be executed, defaults to true.
	 *
	 * @param player
	 * 		The player executing the event
	 */
	public boolean canStart(Player player) {
		return true;
	}
	
	/**
	 * Handles the running of the event
	 *
	 * @param player
	 * 		The player
	 */
	public abstract void run(Player player);
	
	/**
	 * The context instance
	 */
	@Getter
	private final T context;
	
	/**
	 * The policy for walking
	 */
	@Getter
	@Setter
	private WalkablePolicy walkablePolicy = WalkablePolicy.NONE;
	
	/**
	 * The policy for interfaces
	 */
	@Getter
	@Setter
	private InterfacePolicy interfacePolicy = InterfacePolicy.NONE;
	
	/**
	 * The policy for stacking events
	 */
	@Getter
	@Setter
	private StackPolicy stackPolicy = StackPolicy.STACK;
	
	/**
	 * The policy for animations
	 */
	@Getter
	@Setter
	private AnimationPolicy animationPolicy = AnimationPolicy.NONE;
	
	/**
	 * The policy for actions
	 */
	@Getter
	@Setter
	private ActionPolicy actionPolicy = ActionPolicy.NONE;
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public Event(T context) {
		this.context = context;
	}
}
