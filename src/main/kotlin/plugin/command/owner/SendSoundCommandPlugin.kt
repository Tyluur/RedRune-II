package plugin.command.owner;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import plugin.command.CommandManifest;

/**
 * @author Tyluur
 * @since 2019-05-01
 */
@CommandManifest(description = "Plays a sound by the id", types = { Integer.class })
public class SendSoundCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int soundId = intParam(args, 1);
		int delay = intParamOrDefault(args, 2, 0);
		int effectType = intParamOrDefault(args, 3, 1);
		player.getPackets().sendSound(soundId, delay, effectType);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("sound");
	}
}
