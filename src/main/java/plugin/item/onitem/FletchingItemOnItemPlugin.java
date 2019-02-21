package plugin.item.onitem;

import org.redrune.game.content.entity.actor.player.skills.fletching.Fletching;
import org.redrune.game.content.entity.actor.player.skills.fletching.Fletching.Fletch;
import org.redrune.game.content.plugin.type.ItemOnItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-08
 */
public class FletchingItemOnItemPlugin implements ItemOnItemPlugin {
	
	@Override
	public boolean handleItemOnItem(Player player, Item used, Item with) {
		Fletch fletch = Fletching.isFletching(used, with);
		if (fletch == null) {
			return true;
		}
		player.getDialogueManager().startDialogue("FletchingD", fletch);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Fletch.values()).forEach(fletch -> registerItemOnItemIds(fletch.getId(), fletch.getSelected()));
	}
}
