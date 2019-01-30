package plugin.npc;

import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.slayer.Strykewyrm;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.NPCPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class StrykewyrmNPCPlugin implements NPCPlugin {
	
	@Override
	public void register() {
		registerNPC(9462, "Investigate");
	}
	
	@Override
	public boolean handle(Player player, NPC npc, String option) {
		Strykewyrm.handleStomping(player, npc);
		return true;
	}
}
