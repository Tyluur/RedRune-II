package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;

public class Unicornstallion extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -1291968400159646829L;

	public Unicornstallion(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getHitpoints() == player.getMaxHitpoints()) {
			player.getPackets().sendMessage("You need to have at least some damage before being able to heal yourself.");
			return false;
		} else {
			player.setNextAnimation(new Animation(7660));
			player.setNextGraphics(new Graphics(1300));
			int percentHealed = player.getMaxHitpoints() / 15;
			player.heal(percentHealed);
		}
		return true;
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public String getSpecialName() {
		return "Healing Aura";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals 15% of your health points.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean isAgressive() {
		return false;
	}

}
