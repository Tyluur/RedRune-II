package plugin.object;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.content.plugin.type.ObjectPlugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
public class NexEntranceObjectPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		switch (option) {
			case "Climb-over":
				player.getDialogueManager().startDialogue("NexEntrance");
				return true;
		}
		return false;
	}
	
	@Override
	public void register() {
		registerObject(57225, "Climb-over");
	}
}
