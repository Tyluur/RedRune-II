package plugin.npc;

import com.rs.game.content.PlayerLook;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.NPCPlugin;
import com.rs.utility.game.ClickOption;

import static com.rs.utility.game.ClickOption.FIRST;
import static com.rs.utility.game.ClickOption.SECOND;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class MakeoverMageNPCPlugin extends NPCPlugin {
	
	@Override
	public void handle(Player player, NPC npc, ClickOption option) {
		if (option == FIRST) {
			player.getDialogueManager().startDialogue("MakeOverMage", npc.getId(), 0);
		} else if (option == SECOND) {
			PlayerLook.openMageMakeOver(player);
		}
	}
	
	@Override
	public void register() {
		register(2676, FIRST, SECOND);
		register(599, FIRST, SECOND);
	}
}
