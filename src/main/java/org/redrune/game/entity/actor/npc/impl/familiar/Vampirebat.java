package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.player.Player;

public class Vampirebat extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 586089784797828590L;

	public Vampirebat(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 4;
	}

	@Override
	public String getSpecialName() {
		return "Vampyre Touch";
	}

	@Override
	public String getSpecialDescription() {
		return "Deals damage to your opponents, with a maximum hit of 120. It also has a chance of healing your lifepoints by 20. ";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}
}
