package plugin.item;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning;
import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class SummoningPouchItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		Pouches pouch = Pouches.forId(item.getId());
		if (pouch == null) {
			return false;
		}
		Summoning.spawnFamiliar(player, pouch);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Pouches.values()).forEach(pouch -> registerItem(pouch.getPouchId(), "Summon"));
	}
}
