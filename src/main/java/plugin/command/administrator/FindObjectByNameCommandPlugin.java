package plugin.command.administrator;

import org.apache.commons.cli.*;
import org.redrune.cache.loaders.ObjectDefinitions;
import org.redrune.game.GameFlags;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.DebugFunctions;
import org.redrune.utility.functions.Misc;
import plugin.command.CommandManifest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-01
 */
@CommandManifest(description = "Finds an object by the name, and other possible parameters", types = { String.class })
public class FindObjectByNameCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String identifier = stringParam(args, 1).replaceAll("_", " ");
		String[] argParameters = Arrays.copyOfRange(args, 2, args.length);
		Options options = new Options();
		Option menuOption = new Option("option", "input", true, "context menu option");
		options.addOption(menuOption);
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, argParameters);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		String optionValue = cmd.getOptionValue("option");
		
		
		// the list of items that were found
		List<String> found = new ArrayList<>();
		for (int id = 0; id < Misc.getObjectDefinitionsSize(); id++) {
			ObjectDefinitions def = ObjectDefinitions.getObjectDefinitions(id);
			if (def == null) {
				continue;
			}
			String objectName = def.getName().toLowerCase();
			if (!objectName.contains(identifier)) {
				continue;
			}
			String e = "Object{id=" + def.getId() + ", name=" + def.getName() + ", options=" + Arrays.toString(def.getOptions()) + ", sizes=[x=" + def.getSizeX() + ",y=" + def.getSizeY() + "]}";
			if (optionValue != null) {
				if (def.containsOption(optionValue)) {
					found.add(e);
				}
			} else {
				found.add(e);
			}
		}
		// shows the entries found
		found.forEach(entry -> {
			if (GameFlags.debugMode) {
				DebugFunctions.writeLogText(entry);
			}
			player.getPackets().sendConsoleMessage(entry);
		});
		if (GameFlags.debugMode) {
			DebugFunctions.writeLogText("--- END OF LOG ENTRY --- ");
		}
		// sends a response with the amount of entries found
		sendResponse(player, "Found " + found.size() + " objects by name '" + identifier + "'.", console);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("objn");
	}
}
