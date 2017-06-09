package org.redrune.game.node.entity.player.link;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public final class EventManager {
	
	/**
	 * The actions that are waiting to be added to the list of processing actions
	 */
	private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * The actions that are about to be processed
	 */
	private final List<Event> eventsToProcess = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * Adds an event to be processed
	 *
	 * @param event
	 * 		The event
	 */
	public boolean addEvent(Event event) {
		return eventQueue.add(event);
	}
	
	/**
	 * Executes an event without queueing it
	 *
	 * @param event
	 * 		The event
	 */
	public void executeEvent(Player player, Event event) {
		try {
			if (!sendPreExecuteFlags(player, event)) {
				return;
			}
			event.run(player);
			if (event.getStackPolicy() == StackPolicy.NONE) {
				eventsToProcess.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Processes all the queued events
	 *
	 * @param player
	 * 		The player
	 */
	public void process(Player player) {
		try {
			eventsToProcess.addAll(eventQueue);
			eventQueue.clear();
			
			for (Iterator<Event> iterator = eventsToProcess.iterator(); iterator.hasNext(); ) {
				Event event = iterator.next();
				if (!sendPreExecuteFlags(player, event)) {
					iterator.remove();
					continue;
				}
				try {
					event.run(player);
				} catch (Exception e) {
					iterator.remove();
					e.printStackTrace();
					break;
				}
				if (event.getStackPolicy() == StackPolicy.NONE) {
					eventsToProcess.clear();
					break;
				} else {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the policies before the event is executed
	 *
	 * @param player
	 * 		The player
	 * @param event
	 * 		The event
	 * 	@return True if we should start the event
	 */
	private boolean sendPreExecuteFlags(Player player, Event event) {
		final boolean stopWalk = event.getWalkablePolicy() == WalkablePolicy.RESET;
		final boolean stopInterfaces = event.getInterfacePolicy() == InterfacePolicy.CLOSE;
		final boolean stopActions = event.getActionPolicy() == ActionPolicy.RESET;
		final boolean stopAnimation = event.getAnimationPolicy() == AnimationPolicy.RESET;
		if (!event.canStart(player)) {
			return false;
		}
		player.stop(stopActions, stopWalk, stopInterfaces, stopAnimation);
		return true;
	}
	
}