package plugin.inter;

import org.redrune.engine.SystemManager;
import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.type.InterfacePlugin;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class CombatTabInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 4) {
			player.putAttribute("special_attack_toggled", true);
			SystemManager.SLOW_EXECUTOR.schedule(() -> {
				try {
					if (player.isDead()) {
						return;
					}
					CombatAlgorithm.checkSpecialToggle(player, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, 400, TimeUnit.MILLISECONDS);
		} else if (componentId >= 11 && componentId <= 14) {
			player.getCombatDefinitions().setAttackStyle(componentId - 11);
		} else if (componentId == 15) {
			player.getCombatDefinitions().switchAutoRelatie();
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(884);
	}
}
