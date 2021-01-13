package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class Lavatitan extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -778365732778023700L;

	public Lavatitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		return "Ebon Thunder";
	}

	@Override
	public String getSpecialDescription() {
		return "Possibly drain an enemy's special attack energy by 10%";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

}
