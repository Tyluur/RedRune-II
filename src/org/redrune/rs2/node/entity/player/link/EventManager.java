package org.redrune.rs2.node.entity.player.link;

import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.event.Event;
import org.redrune.rs2.node.entity.player.event.EventPolicy.*;
import org.redrune.rs2.node.entity.player.render.flag.impl.Animation;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class EventManager {
	
	/**
	 * The actions that are waiting to be added to the list of processing actions
	 */
	private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * The actions that are about to be processed
	 */
	private final List<Event> actionsToProcess = Collections.synchronizedList(new ArrayList<>());
	
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
			sendPreExecuteFlags(player, event);
			event.run(player);
			if (event.getStackPolicy() == StackPolicy.NONE) {
				actionsToProcess.clear();
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
	 * @param action
	 * 		The action
	 */
	private void sendPreExecuteFlags(Player player, Event action) {
		final boolean stopWalk = action.getWalkablePolicy() == WalkablePolicy.RESET;
		final boolean stopInterfaces = action.getInterfacePolicy() == InterfacePolicy.CLOSE;
		final boolean stopActions = action.getActionPolicy() == ActionPolicy.RESET;
		final boolean stopAnimation = action.getAnimationPolicy() == AnimationPolicy.RESET;
		
		if (stopAnimation) {
			player.getUpdateMasks().register(new Animation(-1));
		}
		if (stopInterfaces) {
			player.getTransmitter().closeInputBox();
			player.getManager().getInterfaces().closeScreenInterface();
		}
		if (stopWalk) {
			player.getWalkingQueue().reset();
			player.getTransmitter().sendMinimapFlagReset();
		}
		if (stopActions) {
			// TODO: stop actions from being done
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
			actionsToProcess.addAll(eventQueue);
			eventQueue.clear();
			
			for (Iterator<Event> iterator = actionsToProcess.iterator(); iterator.hasNext(); ) {
				Event event = iterator.next();
				sendPreExecuteFlags(player, event);
				try {
					event.run(player);
				} catch (Exception e) {
					iterator.remove();
					e.printStackTrace();
					break;
				}
				if (event.getStackPolicy() == StackPolicy.NONE) {
					actionsToProcess.clear();
					break;
				} else {
					iterator.remove();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
