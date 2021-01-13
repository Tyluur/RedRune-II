package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;

public class SpiritKalphite extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 6110983138725755250L;

	public SpiritKalphite(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		return 6;
	}

	@Override
	public String getSpecialName() {
		return "Sandstorm";
	}

	@Override
	public String getSpecialDescription() {
		return "Attacks up to five opponents with a max damage of 50.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}
}
