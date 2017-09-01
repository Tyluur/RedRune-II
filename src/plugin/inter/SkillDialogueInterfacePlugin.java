package plugin.inter;

import com.rs.game.content.SkillsDialogue;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class SkillDialogueInterfacePlugin extends InterfacePlugin{
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		SkillsDialogue.handleSetQuantityButtons(player, componentId);
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(916);
	}
}
