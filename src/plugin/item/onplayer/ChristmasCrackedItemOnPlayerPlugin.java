package plugin.item.onplayer;

import org.redrune.game.content.entity.actor.player.dialogue.impl.ChristmasCrackerD;
import org.redrune.game.content.plugin.type.ItemOnPlayerPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-09
 */
public class ChristmasCrackedItemOnPlayerPlugin implements ItemOnPlayerPlugin {
	
	@Override
	public boolean handle(Player player, Item item, Player partner) {
		if (player.getInventory().getFreeSlots() < 3 || partner.getInventory().getFreeSlots() < 3) {
			String message = (player.getInventory().getFreeSlots() < 3 ? "You do" : "The other player does") + " not have enough inventory space to open this cracker.";
			player.getPackets().sendGameMessage(message);
			return true;
		}
		player.getDialogueManager().startDialogue(ChristmasCrackerD.class, partner, item.getId());
		return true;
	}
	
	@Override
	public void register() {
		registerItemOnPlayerPlugin(962);
	}
}
