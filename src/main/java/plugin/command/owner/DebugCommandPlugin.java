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
		AnimationDefinitions def1 = AnimationDefinitions.getAnimationDefinitions(92); // ice barrage cast
		
		System.out.println("ICE BARRAGE: [priority=" + def1.priority + ", ticks=" + def1.getEmoteGameTickets() + "]");
		
		//		player.getInterfaceManager().sendChatBoxInterface(intParam(args, 1));
	}
	
	@Override
	public String[] identifiers() {
		return arguments("dbg");
	}
}
