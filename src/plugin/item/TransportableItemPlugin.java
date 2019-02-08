package plugin.item;

import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class TransportableItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		int itemId = item.getId();
		if (itemId >= 1706 && itemId <= 1712 || itemId >= 10354 && itemId <= 10360) {
			player.getDialogueManager().startDialogue("Transportation", "Edgeville", new WorldTile(3087, 3496, 0), "Karamja", new WorldTile(2918, 3176, 0), "Draynor Village", new WorldTile(3105, 3251, 0), "Al Kharid", new WorldTile(3293, 3163, 0), itemId);
		} else if (itemId == 1704 || itemId == 10362) {
			player.getPackets().sendGameMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
		} else if (itemId >= 3853 && itemId <= 3867) {
			player.getDialogueManager().startDialogue("Transportation", "Burthrope Games Room", new WorldTile(2880, 3559, 0), "Barbarian Outpost", new WorldTile(2519, 3571, 0), "Gamers' Grotto", new WorldTile(2970, 9679, 0), "Corporeal Beast", new WorldTile(2886, 4377, 0), itemId);
		}
		return true;
	}
	
	@Override
	public void register() {
		// reg glory
		registerItem(1704, "Rub");
		registerItem(1706, "Rub");
		registerItem(1708, "Rub");
		registerItem(1710, "Rub");
		registerItem(1712, "Rub");
		// glory (trimmed)
		registerItem(10354, "Rub");
		registerItem(10356, "Rub");
		registerItem(10358, "Rub");
		registerItem(10360, "Rub");
		registerItem(10362, "Rub");
		// games necklace
		registerItem(3853, "Rub");
		registerItem(3855, "Rub");
		registerItem(3857, "Rub");
		registerItem(3859, "Rub");
		registerItem(3861, "Rub");
		registerItem(3863, "Rub");
		registerItem(3865, "Rub");
		registerItem(3867, "Rub");
		
	}
}
