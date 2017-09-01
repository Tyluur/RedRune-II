package plugin.npc;

import com.rs.game.content.PlayerLook;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.NPCPlugin;
import com.rs.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class ThessaliaNPCPlugin extends NPCPlugin {
	
	@Override
	public void handle(Player player, NPC npc, ClickOption option) {
		PlayerLook.openThessaliasMakeOver(player);
	}
	
	@Override
	public void register() {
		register(548, ClickOption.FIRST);
	}
}
