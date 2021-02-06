package plugin.item;

import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.content.plugin.type.ItemPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-08
 */
public class SkillcapeItemPlugin implements ItemPlugin {
	
	@Override
	public boolean handle(Player player, Item item, int slotId, String option) {
		switch(option) {
			case "Customise":
				SkillCapeCustomizer.startCustomizing(player, item.getId());
				break;
			case "Features":
				player.getDialogueManager().startDialogue("CompCape", item.getId());
				break;
		}
		return true;
	}
	
	@Override
	public void register() {
		registerItem(20767, "Customise");
		registerItem(20769, "Customise");
		registerItem(20771, "Customise");
		registerItem(20769, "Features");
		registerItem(20771, "Features");
	}
}
