package org.redrune.game.node.entity.player.link;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventContext;
import org.redrune.game.node.entity.player.event.EventPolicy.*;
import org.redrune.game.node.entity.player.event.impl.*;
import org.redrune.game.node.entity.player.event.impl.item.FloorItemPickupEvent;
import org.redrune.game.node.entity.player.event.impl.item.ItemEvent;
import org.redrune.game.node.entity.player.event.impl.item.ItemOnItemEvent;
import org.redrune.game.node.entity.player.event.impl.item.ItemRemovalEvent;
import org.redrune.utility.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public final class EventManager {
	
	/**
	 * The map of all events
	 */
	private static final Map<String, Event> EVENT_MAP = new ConcurrentHashMap<>();
	
	/**
	 * The instance of the logger
	 */
	private static final Logger logger = Misc.constructLogger(EventManager.class);
	
	/**
	 * The actions that are about to be processed
	 */
	private final List<Event> eventsToProcess = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * Executes an event for a player
	 *
	 * @param player
	 * 		The player
	 * @param clazz
	 * 		The class of the event
	 * @param context
	 * 		The context
	 */
	@SuppressWarnings("unchecked")
	public static void executeEvent(Player player, Class<? extends Event> clazz, EventContext context) {
		Event event = EVENT_MAP.get(clazz.getSimpleName());
		if (event == null) {
			logger.severe("Unable to identify event for class {" + clazz + "}");
			return;
		}
		if (!sendPreExecuteFlags(player, event, context)) {
			return;
		}
		event.run(player, context);
		if (event.getStackPolicy() == StackPolicy.NONE) {
			player.getManager().getEvents().eventsToProcess.clear();
		}
	}
	
	/**
	 * Handles the policies before the event is executed
	 *
	 * @param player
	 * 		The player
	 * @param event
	 * 		The event
	 * @return True if we should start the event
	 */
	private static boolean sendPreExecuteFlags(Player player, Event event, EventContext context) {
		final boolean stopWalk = event.getWalkablePolicy() == WalkablePolicy.RESET;
		final boolean stopInterfaces = event.getInterfacePolicy() == InterfacePolicy.CLOSE;
		final boolean stopActions = event.getActionPolicy() == ActionPolicy.RESET;
		final boolean stopAnimation = event.getAnimationPolicy() == AnimationPolicy.RESET;
		if (!event.canStart(player, context)) {
			return false;
		}
		player.stop(stopActions, stopWalk, stopInterfaces, stopAnimation);
		return true;
	}
	
	/**
	 * Registers all game events
	 */
	public static void registerEvents() {
		try {
			registerEvent(CommandEvent.class);
			registerEvent(NodeReachEvent.class);
			registerEvent(NPCEvent.class);
			registerEvent(ObjectEvent.class);
			registerEvent(WalkEvent.class);
			registerEvent(FloorItemPickupEvent.class);
			registerEvent(ItemEvent.class);
			registerEvent(ItemOnItemEvent.class);
			registerEvent(ItemRemovalEvent.class);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		logger.info("Registered " + EVENT_MAP.size() + " events.");
	}
	
	/**
	 * Registers an event
	 */
	private static void registerEvent(Class<? extends Event> clazz) throws IllegalAccessException, InstantiationException {
		final Event event = clazz.newInstance();
		EVENT_MAP.put(clazz.getSimpleName(), event);
	}
	
}