package plugin.object;

import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-31
 */
public class DoorObjectPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		return true;
	}
	
	@Override
	public void register() {
	
	}
}
