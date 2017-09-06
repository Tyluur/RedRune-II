package plugin.command.administrator;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.utility.constants.SkillConstants;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
@CommandManifest(description = "Heals you to maximum health")
public class HealCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		player.getCombatDefinitions().setSpecialAttack(100);
		player.getPoisonManager().reset();
		player.getPrayer().setPrayerpoints((int) ((player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 10) * 1.15));
		player.getPrayer().refreshPrayerPoints();
		player.heal(player.getMaxHitpoints(), (int) ((player.getSkills().getLevelForXp(SkillConstants.HITPOINTS) * 10) * 0.05));
		player.getSkills().restoreSkills();
		player.setRunEnergy(100);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("heal");
	}
}
