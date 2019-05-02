package plugin.npc;

import org.redrune.game.content.entity.actor.player.PlayerLook;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.NPCPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class ThessaliaNPCPlugin implements NPCPlugin {
	
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
		registerNPC(548, "Change-clothes");
	}
}
