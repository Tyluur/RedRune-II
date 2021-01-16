package plugin.npc;

import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.NPCPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class SlayerMasterNPCPlugin implements NPCPlugin {
	
	@Override
	public boolean handle(Player player, NPC npc, String option) {
		switch (option) {
			case "Talk-to":
				player.getDialogueManager().startDialogue("Turael", npc.getId());
				return true;
		}
		return false;
	}
	
	@Override
	public void register() {
		registerNPC(8461, "Talk-to");
	}
}
