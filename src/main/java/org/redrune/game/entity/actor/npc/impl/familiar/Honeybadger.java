package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.player.Player;

public class Honeybadger extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -7544357522011400153L;

	public Honeybadger(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		return "Insane ferocity";
	}

	@Override
	public String getSpecialDescription() {
		return "Decreases the owner's Magic, Range, and Defence, but also increasing Strength and Attack, there is also a chance of hitting twice.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}
}
