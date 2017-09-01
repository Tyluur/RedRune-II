package plugin.inter;

import com.rs.cores.CoresManager;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.InterfacePlugin;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

import java.util.TimerTask;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public class CombatTabInterfacePlugin extends InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 4) {
			if (player.hasInstantSpecial(player.getEquipment().getWeaponId())) {
				return true;
			}
			CoresManager.fastExecutor.schedule(new TimerTask() {
				@Override
				public void run() {
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getCombatDefinitions().switchUsingSpecialAttack();
						}
					}, 0);
					
				}
			}, 200);
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
