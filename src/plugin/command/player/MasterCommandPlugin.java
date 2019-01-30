package plugin.command.player;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.utility.constants.SkillConstants;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Sets your stats to max")
public class MasterCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		for (int skill = 0; skill < 25; skill++) {
			player.getSkills().set(skill, 99);
			player.getSkills().setXp(skill, SkillConstants.getXPForLevel(99));
		}
		player.getSkills().restoreSkills();
	}
	
	@Override
	public String[] identifiers() {
		return arguments("master");
	}
}
