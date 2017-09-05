package plugin.command.administrator;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.utility.Misc;
import com.rs.utility.constants.ColorConstants;
import plugin.command.CommandManifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Finds an item by the name we want", types = { String.class })
public class FindItemByNameCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		// the identifier to search by
		final String identifier = args[1].replaceAll("_", " ");
		// the option flag
		final String optionFlag = stringParamOrDefault(args, 2, null);
		
		// the list of items that were found
		List<String> found = new ArrayList<>();
		for (int itemId = 0; itemId < Misc.getItemDefinitionsSize(); itemId++) {
			// the definition instance
			ItemDefinitions definition = ItemDefinitions.getItemDefinitions(itemId);
			if (definition == null) {
				System.out.println("Item #" + itemId + " had no definitions.");
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
			found.add("[<col=FF0000>" + itemId+ "</col>] <col=" + ColorConstants.LIGHT_BLUE + ">" + definition.getName() + "</col> inventory=" + Arrays.toString(definition.getInventoryOptions()) + " ground=" + Arrays.toString(definition.getGroundOptions()));
		}
		// shows the entries found
		found.forEach(entry -> player.getPackets().sendConsoleMessage(entry));
		// sends a response with the amount of entries found
		sendResponse(player, "Found " + found.size() + " items by name '" + identifier + "'.", console);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("itemn");
	}
}
