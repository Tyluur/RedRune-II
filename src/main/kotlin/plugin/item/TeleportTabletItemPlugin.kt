package plugin.item;

import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-07
 */
public class TeleportTabletItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		Magic.useTabTeleport(player, item.getId());
		return true;
	}
	
	@Override
	public void register() {
		for (int itemId = 8007; itemId <= 8013; itemId++) {
			registerItem(itemId, "Break");
		}
	}
}
