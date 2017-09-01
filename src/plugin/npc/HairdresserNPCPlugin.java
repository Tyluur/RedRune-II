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
public class HairdresserNPCPlugin extends NPCPlugin {
	
	@Override
	public void handle(Player player, NPC npc, ClickOption option) {
		if (option == FIRST) {
			player.getDialogueManager().startDialogue("Hairdresser", npc.getId());
		} else if (option == SECOND) {
			PlayerLook.openHairdresserSalon(player);
		}
	}
	
	@Override
	public void register() {
		register(598, FIRST, SECOND);
	}
}
