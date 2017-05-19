package org.redrune.cache.parse;

import org.redrune.cache.CacheConstants;
import org.redrune.cache.CacheManager;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.cache.stream.RSInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public final class NPCDefinitionParser {
	
	/**
	 * The cached map of npc definitions, the key being the npc id
	 */
	private static final ConcurrentHashMap<Integer, NPCDefinition> npcDefinitions = new ConcurrentHashMap<>();
	
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
		def = new NPCDefinition(npcId);
		def.constructModelIds();
		try {
			byte[] data = CacheManager.getData(CacheConstants.NPCDEF_IDX_ID, npcId >>> 7, npcId & 0x7f);
			if (data == null) {
				System.out.println("Failed loading NPC " + npcId + ".");
			} else {
				def.readValueLoop(new RSInputStream(new ByteArrayInputStream(data)));
			}
			npcDefinitions.put(npcId, def);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return def;
	}
	
	/**
	 * Clears all npc definitions
	 */
	public static void clearNPCDefinitions() {
		npcDefinitions.clear();
	}
	
}