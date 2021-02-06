package plugin.item.onitem;

import org.redrune.game.content.entity.actor.player.skills.firemaking.Firemaking;
import org.redrune.game.content.entity.actor.player.skills.firemaking.Firemaking.Fire;
import org.redrune.game.content.plugin.type.ItemOnItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public class FiremakingItemOnItemPlugin implements ItemOnItemPlugin {
	
	@Override
	public boolean handleItemOnItem(Player player, Item used, Item with) {
		Firemaking.isFiremaking(player, used, with);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Fire.values()).forEach(fire -> registerItemOnItemIds(fire.getLogId(), 590));
	}
}
