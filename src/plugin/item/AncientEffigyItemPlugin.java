package plugin.item;

import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class AncientEffigyItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		player.getDialogueManager().startDialogue("AncientEffigiesD", item.getId());
		return true;
	}
	
	@Override
	public void register() {
		for (int i = 18788; i <= 18781; i++) {
			registerItem(i, "Investigate");
		}
	}
}
