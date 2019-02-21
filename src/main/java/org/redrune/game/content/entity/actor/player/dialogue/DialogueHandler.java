package org.redrune.game.content.entity.actor.player.dialogue;

import org.redrune.utility.functions.Misc;

import java.util.HashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @author Matrix Team
 * @since 2/9/19
 */
public final class DialogueHandler {
	
	/**
	 * The map of cached dialogues
	 */
	private static final HashMap<Object, Dialogue> DIALOGUES = new HashMap<>();
	
	/**
	 * Reloads all dialogues
	 */
	public static void reload() {
		DIALOGUES.clear();
		initialize();
	}
	
	/**
	 * Initializes all game dialogues
	 */
	public static void initialize() {
		Misc.getClasses(DialogueHandler.class.getPackage().getName() + ".impl").stream().filter(Dialogue.class::isInstance).forEach(clazz -> {
			try {
				DIALOGUES.put(clazz.getClass().getSimpleName(), (Dialogue) clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		System.out.println("Loaded " + DIALOGUES.size() + " game dialogues");
	}
	
	/**
	 * Gets a dialogue by the key
	 *
	 * @param key
	 * 		The dialogue
	 */
	public static Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue) {
			return (Dialogue) key;
		}
		Dialogue dialogue = DIALOGUES.get(key);
		if (dialogue == null) {
			System.err.println("Unable to find a dialogue for key'" + key + "'");
			return null;
		}
		try {
			return (Dialogue) Class.forName(dialogue.getClass().getName()).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
