package plugin.npc;

import org.redrune.game.content.entity.actor.player.PlayerLook;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.NPCPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class MakeoverMageNPCPlugin implements NPCPlugin {
	
	@Override
	public boolean handle(Player player, NPC npc, String option) {
		switch (option) {
			case "Talk-to":
				player.getDialogueManager().startDialogue("MakeOverMage", npc.getId(), 0);
				return true;
			case "Makeover":
				PlayerLook.openMageMakeOver(player);
				return true;
		}
		return false;
	}
	
	@Override
	public void register() {
		int[] ids = { 2676, 599 };
		for (int id : ids) {
			registerNPC(id, "Talk-to");
			registerNPC(id, "Makeover");
		}
	}
}
