package org.redrune.game.content.dialogue;

import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class DialogueRepository {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(DialogueRepository.class);
	
	/**
	 * The map of dialogues with a {@link DialogueSubscription} subscription
	 */
	private static Map<String, Dialogue> SUBSCRIPTION_DIALOGUES = new HashMap<>();
	
	/**
	 * Loads all the subscriptions
	 */
	public static void loadSubscriptions() {
		Misc.getClassesInDirectory(DialogueRepository.class.getPackage().getName() + ".impl.subscribe").stream().filter(clazz -> clazz.getClass().isAnnotationPresent(DialogueSubscription.class)).forEach(clazz -> {
			Dialogue dialogue = (Dialogue) clazz;
			DialogueSubscription subscription = dialogue.getClass().getAnnotation(DialogueSubscription.class);
			addEntry(dialogue, subscription);
		});
		LOGGER.info("Loaded " + SUBSCRIPTION_DIALOGUES.size() + " dialogues with subscriptions.");
	}
	
	/**
	 * Adds the dialogue entry
	 *
	 * @param dialogue
	 * 		The dialogue
	 * @param subscription
	 * 		The subscription
	 */
	private static void addEntry(Dialogue dialogue, DialogueSubscription subscription) {
		for (String name : subscription.names()) {
			if (SUBSCRIPTION_DIALOGUES.containsKey(name)) {
				LOGGER.info("Unable to register dialogue with subscription: " + dialogue.getClass().getSimpleName() + "[" + subscription + "]");
				return;
			}
			SUBSCRIPTION_DIALOGUES.put(name, dialogue);
		}
	}
	
	/**
	 * Handles the interaction with the npc for a dialogue
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc
	 */
	public static boolean handleNPC(Player player, NPC npc) {
		Dialogue dialogue = SUBSCRIPTION_DIALOGUES.get(npc.getDefinitions().getName());
		if (dialogue == null) {
			return false;
		}
		try {
			player.getManager().getDialogues().startDialogue(dialogue.getClass().newInstance(), npc.getId());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
