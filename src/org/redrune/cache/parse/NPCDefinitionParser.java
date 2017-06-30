package org.redrune.cache.parse;

import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.utility.tool.Misc;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NPCDefinitionParser {
	
	/**
	 * The cached map of npc definitions, the key being the npc id
	 */
	private static final ConcurrentHashMap<Integer, NPCDefinition> npcDefinitions = new ConcurrentHashMap<>();
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(NPCDefinitionParser.class);
	
	/**
	 * Gets an npc definition by the id of the npc
	 *
	 * @param npcId
	 * 		The id of the npc
	 */
	public static NPCDefinition forId(int npcId) {
		NPCDefinition def = npcDefinitions.get(npcId);
		if (def != null) {
			return def;
		}
		try {
			def = NPCDefinition.readDefinitions(npcId);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to parse npc definitions (" + npcId + ")" + e);
			return null;
		}
		npcDefinitions.put(npcId, def);
		return def;
	}
	
	/**
	 * Clears all npc definitions
	 */
	public static void clearNPCDefinitions() {
		npcDefinitions.clear();
	}
	
}