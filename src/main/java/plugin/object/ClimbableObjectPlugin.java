package plugin.object;

import org.redrune.game.content.entity.object.ClimbActionHandler;
import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.game.repository.object.climbable.ClimbableObjectRepository;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-21
 */
public class ClimbableObjectPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		ClimbActionHandler.climbLadder(player, object, option.toLowerCase());
		return true;
	}
	
	@Override
	public void register() {
		for (int objectId : ClimbableObjectRepository.getObjectIds()) {
			registerObject(objectId, "Climb");
			registerObject(objectId, "Climb-up");
			registerObject(objectId, "Climb-down");
		}
	}
}
