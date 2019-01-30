package plugin.npc;

import org.redrune.game.content.PlayerLook;
import org.redrune.game.content.plugin.type.NPCPlugin;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class HairdresserNPCPlugin implements NPCPlugin {
	
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
		registerNPC(598, "Talk-to");
		registerNPC(598, "Hair-cut");
	}
	
}
