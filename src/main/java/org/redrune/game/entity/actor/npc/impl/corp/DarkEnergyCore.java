package org.redrune.game.entity.actor.npc.impl.corp;

import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class DarkEnergyCore extends NPC {

	private CorporealBeast beast;

	private Actor target;

	private int changeTarget;

	private int delay;

	public DarkEnergyCore(CorporealBeast beast) {
		super(8127, beast, -1, true, true);
		setForceMultiArea(true);
		this.beast = beast;
		changeTarget = 2;
	}

	@Override
	public void processNPC() {
		if (isDead() || isFinished()) {
			return;
		}
		if (delay > 0) {
			delay--;
			return;
		}
		if (changeTarget > 0) {
			if (changeTarget == 1) {
				ArrayList<Actor> possibleTarget = beast.getPossibleTargets(true, true);
				if (possibleTarget.isEmpty()) {
					finish();
					beast.removeDarkEnergyCore();
					return;
				}
				target = possibleTarget.get(Misc.getRandom(possibleTarget.size() - 1));
				setNextWorldTile(new WorldTile(target));
				RegionManager.sendProjectile(this, this, target, 1828, 0, 0, 40, 40, 20, 0);
			}
			changeTarget--;
			return;
		}
		if (target == null || target.getX() != getX() || target.getY() != getY() || target.getPlane() != getPlane()) {
			changeTarget = 3;
			return;
		}
		int damage = Misc.getRandom(50) + 50;
		target.applyHit(new Hit(this, damage, HitSplat.REGULAR_DAMAGE));
		beast.heal(damage);
		delay = getPoisonManager().isPoisoned() ? 10 : (int) 0.5D;
		if (target instanceof Player) {
			Player player = (Player) target;
			player.getPackets().sendMessage("The dark core creature steals some life from you for its master.");
		}
	}

	@Override
	public void sendDeath(Actor source) {
		super.sendDeath(source);
		beast.removeDarkEnergyCore();
	}

}
