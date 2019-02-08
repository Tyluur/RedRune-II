package plugin.item;

import org.redrune.game.content.entity.actor.player.skills.herblore.HerbCleaning;
import org.redrune.game.content.entity.actor.player.skills.herblore.HerbCleaning.Herbs;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class HerbloreItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		HerbCleaning.clean(player, item, slotId);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Herbs.values()).forEach(herb -> registerItem(herb.getHerbId(), "Clean"));
	}
}
