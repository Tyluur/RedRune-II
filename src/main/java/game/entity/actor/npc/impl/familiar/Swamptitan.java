package game.entity.actor.npc.impl.familiar;

import game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import game.entity.actor.player.Player;
import game.global.WorldTile;

public class Swamptitan extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -6073150798974730997L;

	public Swamptitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		return "Swamp Plague";
	}

	@Override
	public String getSpecialDescription() {
		return "Inflicts a magical attack on near by opponents and attempts to poison them as well.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

}
