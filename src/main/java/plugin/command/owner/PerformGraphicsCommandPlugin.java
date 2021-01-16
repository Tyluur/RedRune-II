package plugin.command.owner;

import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
@CommandManifest(description = "Performs a graphic", types = { Integer.class })
public class PerformGraphicsCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int id = intParam(args, 1);
		int speed = intParamOrDefault(args, 2, 0);
		int height = intParamOrDefault(args, 3, 0);
		int rotation = intParamOrDefault(args, 4, 0);
		player.setNextGraphics(new Graphics(id, speed, height, rotation));
	}
	
	@Override
	public String[] identifiers() {
		return arguments("gfx");
	}
}
