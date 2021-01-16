package org.redrune.game.entity.actor.npc.data.extension;

import org.redrune.game.entity.actor.npc.data.extension.impl.TalkingNPCExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
public class NPCExtensionHolder {
	
	/**
	 * The map of extensions
	 */
	private static final Map<Integer, NPCExtension> EXTENSIONS = new HashMap<>();
	
	/**
	 * Registers all extensions
	 */
	public static void initialize() {
		store(13281, new TalkingNPCExtension("Exchange your points into rewards here!", TimeUnit.SECONDS.toMillis(3)));
		store(2290, new TalkingNPCExtension("Manage your account here!", TimeUnit.SECONDS.toMillis(3)));
		System.out.println("Initialized " + EXTENSIONS.size() + " npc extensions");
	}
	
	/**
	 * Stores an extension
	 */
	private static void store(int npcId, NPCExtension extension) {
		EXTENSIONS.put(npcId, extension);
	}
	
	/**
	 * Gets an extension by the id of the npc
	 */
	public static Optional<NPCExtension> getExtension(int npcId) {
		return Optional.ofNullable(EXTENSIONS.get(npcId));
	}
	
}
