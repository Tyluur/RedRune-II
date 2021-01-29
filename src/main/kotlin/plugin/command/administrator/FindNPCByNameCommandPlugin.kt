package plugin.command.administrator;

import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.ColorConstants;
import plugin.command.CommandManifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Finds all npcs by a certain name", types = { String.class })
public class FindNPCByNameCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		// the identifier to search by
		final String identifier = args[1].replaceAll("_", " ");
		// the option flag
		final String optionFlag = stringParamOrDefault(args, 2, null);
		// the list of items that were found
		List<String> found = new ArrayList<>();
		for (int npcId = 0; npcId < Misc.getNPCDefinitionsSize(); npcId++) {
			// the definition instance
			NPCDefinitions definition = NPCDefinitions.getNPCDefinitions(npcId);
			if (definition == null) {
				System.out.println("NPC #" + npcId + " had no definitions.");
				continue;
			}
			// the name of the item
			final String name = definition.getName().toLowerCase();
			boolean added = false;
			// the name has the identifier we want
			if (name.contains(identifier)) {
				// we only want to find items by the identifier
				if (optionFlag == null) {
					added = true;
				} else {
					// we found that the definition has the option we want
					if (definition.hasOption(optionFlag)) {
						added = true;
					}
				}
			}
			// the item was not added because it was not found by filter
			if (!added) {
				continue;
			}
			found.add("[<col=FF0000>" + npcId + "</col>] <col=" + ColorConstants.LIGHT_BLUE + ">" + definition.getName() + "</col> size=" + definition.getSize() + ", options=" + Arrays.toString(definition.getOptions()));
		}
		// shows the entries found
		found.forEach(entry -> player.getPackets().sendConsoleMessage(entry));
		// sends a response with the amount of entries found
		sendResponse(player, "Found " + found.size() + " npcs by name '" + identifier + "'.", console);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("npcn", "nn");
	}
}
