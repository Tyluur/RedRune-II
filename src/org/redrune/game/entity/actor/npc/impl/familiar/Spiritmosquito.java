package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.skills.summoning.Summoning.Pouches;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.player.Player;

public class Spiritmosquito extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 3249731229258558109L;

	public Spiritmosquito(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		return 3;
	}

	@Override
	public String getSpecialName() {
		return "Pester";
	}

	@Override
	public String getSpecialDescription() {
		return "Sends a mosquito to your opponent.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

}
