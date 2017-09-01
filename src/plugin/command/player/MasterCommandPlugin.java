package plugin.command.player;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.plugin.type.CommandPlugin;
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
			player.getSkills().addXp(skill, PlayerSkills.MAXIMUM_EXP);
		}
	}
	
	@Override
	public String[] identifiers() {
		return arguments("master");
	}
}
