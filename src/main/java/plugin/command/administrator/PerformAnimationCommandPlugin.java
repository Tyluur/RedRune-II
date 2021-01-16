package plugin.command.administrator;

import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
@CommandManifest(description = "Performs an animation", types = { Integer.class })
public class PerformAnimationCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int id = intParam(args, 1);
		int speed = intParamOrDefault(args, 2, 0);
		player.setNextAnimation(new Animation(id, speed));
	}
	
	@Override
	public String[] identifiers() {
		return arguments("anim");
	}
}
