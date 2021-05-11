package game.entity.actor.npc.impl.familiar;

import game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.player.Player;
import game.global.WorldTile;

public class Spiritwolf extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 2691875962052924796L;

	public Spiritwolf(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.setNextAnimation(new Animation(7660));
		player.setNextGraphics(new Graphics(1316));
		return true;
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
		return "Howl";
	}

	@Override
	public String getSpecialDescription() {
		return "Scares non-player opponents, causing them to retreat. However, this lasts for only a few seconds.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}
}
