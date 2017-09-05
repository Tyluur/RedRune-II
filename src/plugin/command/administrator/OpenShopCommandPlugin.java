package plugin.command.administrator;

import com.rs.game.content.market.ShopRepository;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.type.CommandPlugin;
import plugin.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
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
