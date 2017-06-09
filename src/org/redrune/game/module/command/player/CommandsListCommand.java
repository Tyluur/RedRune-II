package org.redrune.game.module.command.player;

import com.google.common.base.Stopwatch;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRight;
import org.redrune.utility.rs.constant.InterfaceConstants;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Lists the commands available.")
public class CommandsListCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("commands", "cmds");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		List<String> messages = new ArrayList<>();
		
		Stopwatch watch = Stopwatch.createStarted();
		
		Set<CommandModule> commandModulesSet = new LinkedHashSet<>(CommandRepository.getCommands().stream().filter(commandModule -> commandModule.getRightRequired().playerHasRights(player)).collect(Collectors.toList()));
		
		List<CommandModule> commandModules = new ArrayList<>(commandModulesSet);
		
		commandModules.sort(Comparator.comparingInt(o -> o.getRightRequired().ordinal()));
		
		PlayerRight lastRight = null;
		
		for (CommandModule command : commandModules) {
			// skips commands that are only for console
			// or commands with no manifest [only i should know about these]
			if (command.consoleUsageOnly() || command.getManifest() == null) {
				continue;
			}
			CommandManifest manifest = command.getManifest();
			StringBuilder bldr = new StringBuilder();
			String[] identifiers = command.identifiers();
			
			for (int i = 0; i < identifiers.length; i++) {
				String identifier = identifiers[i];
				bldr.append(identifier).append(i == identifiers.length - 1 ? " -> " : ", ");
			}
			
			String message = "::" + bldr.toString() + (manifest == null ? "" : manifest.description().equals("") ? "" : manifest.description());
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
		System.out.println(watch.elapsed(TimeUnit.MILLISECONDS));
	}
}
