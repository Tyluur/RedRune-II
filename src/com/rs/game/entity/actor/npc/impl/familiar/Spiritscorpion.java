package com.rs.game.entity.actor.npc.impl.familiar;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.skills.summoning.Summoning.Pouches;

public class Spiritscorpion extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6369083931716875985L;

	public Spiritscorpion(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Venom Shot";
	}

	@Override
	public String getSpecialDescription() {
		return "Chance of next Ranged attack becoming mildly poisonous, given that the Ranged weapon being used can be poisoned";
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
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}
}
