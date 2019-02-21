package plugin.item;

import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class EnchantedGemItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		player.getDialogueManager().startDialogue("EnchantedGemDialouge");
		return true;
	}
	
	@Override
	public void register() {
		registerItem(4155, "Activate");
//		registerItem(4155, "Kills-left");
	}
}
