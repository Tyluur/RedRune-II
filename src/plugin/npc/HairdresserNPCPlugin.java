package plugin.npc;

import org.redrune.game.content.PlayerLook;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.NPCPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class HairdresserNPCPlugin extends NPCPlugin {
	
	@Override
	public boolean handle(Player player, NPC npc, String option) {
		switch (option) {
			case "Talk-to":
				player.getDialogueManager().startDialogue("Hairdresser", npc.getId());
				return true;
			case "Hair-cut":
				PlayerLook.openHairdresserSalon(player);
				return true;
		}
		return false;
	}
	
	@Override
	public void register() {
		register(598, "Talk-to");
		register(598, "Hair-cut");
	}
}
