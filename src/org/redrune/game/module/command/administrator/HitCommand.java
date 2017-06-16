package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.utility.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
@CommandManifest(description = "Adds a hit to yourself", types = { Integer.class })
public class HitCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("hit");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		String target = Misc.getArrayEntry(args, 2);
		if (target == null) {
			player.getHitMap().getHitList().add(new Hit(player, intParam(args, 1), HitSplat.MELEE_DAMAGE));
		} else {
			Optional<Player> o = World.get().getPlayerByUsername(target);
			o.ifPresent(p2 -> p2.getHitMap().getHitList().add(new Hit(player, intParam(args, 1))));
		}
	}
}
