package game.entity.actor.npc.impl.familiar;

import game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import game.entity.actor.player.Player;
import game.global.WorldTile;

public class Spiritscorpion extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 6369083931716875985L;

	public Spiritscorpion(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
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
		return "Venom Shot";
	}

	@Override
	public String getSpecialDescription() {
		return "Chance of next Ranged attack becoming mildly poisonous, given that the Ranged weapon being used can be poisoned";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}
}
