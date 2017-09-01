package plugin.command.player;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerRight;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.utility.Misc;
import com.rs.utility.constants.InterfaceConstants;
import plugin.command.CommandManifest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Lists the commands available.")
public class ListCommandsCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		List<String> messages = new ArrayList<>();
		
		Set<CommandPlugin> commandModulesSet = new LinkedHashSet<>(PluginRepository.getCommands().stream().filter(commandModule -> commandModule.getRightRequired().playerHasRights(player)).collect(Collectors.toList()));
		
		List<CommandPlugin> commandModules = new ArrayList<>(commandModulesSet);
		
		commandModules.sort(Comparator.comparingInt(o -> o.getRightRequired().ordinal()));
		
		PlayerRight lastRight = null;
		
		for (CommandPlugin command : commandModules) {
			// skips commands that are only for console
			// or commands with no manifest [only i should know about these]
			if (command.clientCommandOnly() || command.getManifest() == null) {
				continue;
			}
			CommandManifest manifest = command.getManifest();
			StringBuilder bldr = new StringBuilder();
			String[] identifiers = command.identifiers();
			
			// adds the identifiers of the command to the string
			for (int i = 0; i < identifiers.length; i++) {
				String identifier = identifiers[i];
				bldr.append(identifier).append(i == identifiers.length - 1 ? " -> " : ", ");
			}
			
			Class[] types = manifest == null ? null : manifest.types();
			
			if (types != null) {
				for (int i = 0; i < types.length; i++) {
					final String simpleName = Misc.getSimplifiedType(types[i].getSimpleName());
					boolean last = i == types.length - 1;
					if (i == 0) {
						bldr.append("[");
						bldr.append(simpleName).append("").append(last ? "" : ", ");
						if (i == types.length - 1) {
							bldr.append("]");
						}
					} else if (i == (types.length - 1)) {
						bldr.append(simpleName).append("").append(last ? "" : ", ");
						bldr.append("]");
					} else {
						bldr.append(types[i].getClass().getSimpleName());
					}
				}
				bldr.append(" ");
			}
			
			// how the command should be executed, with info from the manifest if it exists
			String message = "::" + bldr.toString() + (manifest == null ? "" : manifest.description().equals("") ? "" : manifest.description());
			
			// adds the message to the list, and adds right required formatting
			if (lastRight == null || lastRight != command.getRightRequired()) {
				if (lastRight != null) {
					messages.add("");
				}
				messages.add("[" + command.getRightRequired().name() + "]");
				
				lastRight = command.getRightRequired();
			}
			messages.add(message);
		}
		
		InterfaceConstants.sendQuestScroll(player, "Commands", messages.toArray(new String[messages.size()]));
	}
	
	@Override
	public String[] identifiers() {
		return arguments("commands", "cmds");
	}
}
