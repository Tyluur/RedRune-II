package plugin.item;

import org.redrune.game.content.entity.actor.player.skills.hunter.Hunter;
import org.redrune.game.content.entity.actor.player.skills.hunter.Hunter.HunterEquipment;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public class HunterEquipmentItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		int itemId = item.getId();
		switch(itemId) {
			case 10006:
				player.getActionManager().setAction(new Hunter(HunterEquipment.BRID_SNARE));
				break;
			case 10008:
				player.getActionManager().setAction(new Hunter(HunterEquipment.BOX));
				break;
		}
		return true;
	}
	
	@Override
	public void register() {
		registerItem(HunterEquipment.BOX.getId(), "Lay");
		registerItem(HunterEquipment.BRID_SNARE.getId(), "Lay");
	}
}
