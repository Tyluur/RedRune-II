package plugin.command.player;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.utility.constants.SkillConstants;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Sets your levels", types = { Integer.class, Integer.class })
public class SetLevelCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int skill = intParam(args, 1);
		int level = intParam(args, 2);
		if (level < 0 || level > 99) {
			player.getPackets().sendMessage("Please choose a valid level.");
			return;
		}
		player.getSkills().set(skill, level);
		player.getSkills().setXp(skill, SkillConstants.getXPForLevel(level));
		player.getAppearance().generateAppearanceData();
	}
	
	@Override
	public String[] identifiers() {
		return arguments("setlevel");
	}
}
