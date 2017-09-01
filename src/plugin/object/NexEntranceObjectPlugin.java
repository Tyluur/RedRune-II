package plugin.object;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.plugin.type.ObjectPlugin;
import com.rs.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
public class NexEntranceObjectPlugin extends ObjectPlugin {
	
	@Override
	public void handle(Player player, WorldObject object, ClickOption option) {
		player.getDialogueManager().startDialogue("NexEntrance");
	}
	
	@Override
	public void register() {
		register(57225);
	}
}
