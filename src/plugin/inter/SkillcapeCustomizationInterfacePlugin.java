package plugin.inter;

import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.InterfacePlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class SkillcapeCustomizationInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 20) {
			SkillCapeCustomizer.handleSkillCapeCustomizer(player, componentId);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(20);
	}
}
