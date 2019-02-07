package plugin.command.owner;

import org.redrune.cache.loaders.AnimationDefinitions;
import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/12/2017
 */
public class DebugCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		AnimationDefinitions def1 = AnimationDefinitions.getAnimationDefinitions(1979); // ice barrage cast
		AnimationDefinitions def2 = AnimationDefinitions.getAnimationDefinitions(4177); // sol block
		
		System.out.println("ICE BARRAGE: [priority=" + def1.priority + ", ticks=" + def1.getEmoteTime() + "]");
		System.out.println("BLOCK EMOTE: [priority=" + def2.priority + ", ticks=" + def2.getEmoteTime() + "]");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
}
