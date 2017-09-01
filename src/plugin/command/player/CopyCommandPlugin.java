package plugin.command.player;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.type.CommandPlugin;
import com.rs.game.world.World;
import plugin.command.CommandManifest;

import java.util.HashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Copies another player", types = { String.class })
public class CopyCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		String username = getCompleted(args, 1);
		Player p2 = World.getPlayerByDisplayName(username);
		if (p2 == null) {
			player.getPackets().sendGameMessage("Couldn't find player " + username + ".");
			return;
		}
		if (!player.getEquipment().isWearingArmour()) {
			player.getPackets().sendGameMessage("Please remove your armour first.");
			return;
		}
		Item[] items = p2.getEquipment().getItems().getItemsCopy();
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			HashMap<Integer, Integer> skillRequirements = items[i].getDefinitions().getWearingSkillRequirements();
			boolean hasRequirements = true;
			if (skillRequirements != null) {
				for (int skillId : skillRequirements.keySet()) {
					if (skillId > 24 || skillId < 0) {
						continue;
					}
					int level = skillRequirements.get(skillId);
					if (level < 0 || level > 120) {
						continue;
					}
					if (player.getSkills().getLevelForXp(skillId) < level) {
						if (hasRequirements) {
							player.getPackets().sendGameMessage("You are not high enough level to use this item.");
						}
						hasRequirements = false;
						String name = PlayerSkills.SKILL_NAME[skillId].toLowerCase();
						player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
					}
					
				}
			}
			if (!hasRequirements) {
				return;
			}
			player.getEquipment().getItems().set(i, items[i]);
			player.getEquipment().refresh(i);
		}
		player.getAppearance().generateAppearanceData();
	}
	
	@Override
	public String[] identifiers() {
		return arguments("copy");
	}
}
