package plugin.npc;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.NPCPlugin;
import com.rs.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class SlayerMasterNPCPlugin extends NPCPlugin {
	
	@Override
	public void handle(Player player, NPC npc, ClickOption option) {
		player.getDialogueManager().startDialogue("Turael", npc.getId());
	}
	
	@Override
	public void register() {
		register(8461, ClickOption.FIRST);
	}
}
