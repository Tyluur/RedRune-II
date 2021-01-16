package plugin.command.administrator;

import org.redrune.game.content.entity.actor.player.market.ShopRepository;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
@CommandManifest(description = "Opens a shop by its id", types = { Integer.class })
public class OpenShopCommandPlugin extends CommandPlugin {
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		int identifier = intParam(args, 1);
		ShopRepository.open(player, identifier);
	}
	
	@Override
	public String[] identifiers() {
		return arguments("openshop");
	}
}
