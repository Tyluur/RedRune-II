package plugin.inter;

import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-29
 */
public class GenieSkillSelectionInterfacePlugin implements InterfacePlugin {
	
	/**
	 * The interface id for the skill selection interface
	 */
	private static final int SKILL_SELECTION_INTERFACE_ID = 1139;
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(SKILL_SELECTION_INTERFACE_ID);
	}
	
	public static void displayInterface(Player player) {
		player.getVarManager().sendVar(261, 0);
		player.getInterfaceManager().sendInterface(SKILL_SELECTION_INTERFACE_ID);
	}
}
