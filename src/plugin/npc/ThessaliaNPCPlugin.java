package plugin.npc;

import org.redrune.game.content.PlayerLook;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.NPCPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class ThessaliaNPCPlugin extends NPCPlugin {
	
	@Override
	public boolean handle(Player player, NPC npc, String option) {
		switch (option) {
			case "Change-clothes":
				PlayerLook.openThessaliasMakeOver(player);
				return true;
		}
		return false;
	}
	
	@Override
	public void register() {
		register(548, "Change-clothes");
	}
}
