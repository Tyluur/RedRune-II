package plugin.item;

import org.redrune.game.content.entity.item.Burying;
import org.redrune.game.content.entity.item.Burying.Bone;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-07
 */
public class BoneBurialItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		Burying.bury(player, slotId);
		return true;
	}
	
	@Override
	public void register() {
		Arrays.stream(Bone.values()).forEach(bone -> registerItem(bone.getId(), "Bury"));
	}
}
