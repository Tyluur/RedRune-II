package org.redrune.cache.parse;

import org.redrune.cache.parse.definition.CS2ScriptDefinition;
import org.redrune.utility.Misc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author `Discardedx2
 */
public final class CS2ScriptParser {
	
	/**
	 * The definitions mapping.
	 */
	private static final Map<Integer, CS2ScriptDefinition> DEFINITIONS = new HashMap<>();
	
	/**
	 * The instance of the logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(CS2ScriptParser.class);
	
	/**
	 * The client script setting definitions.
	 *
	 * @param id
	 * 		The client script id.
	 * @return The definitions.
	 */
	public static CS2ScriptDefinition forId(int id) {
		CS2ScriptDefinition def = DEFINITIONS.get(id);
		if (def != null) {
			return def;
		}
		try {
			def = def.load(id);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to parse CS2 definitions for id " + id, e);
			return null;
		}
		DEFINITIONS.put(id, def);
		return def;
	}
	
}