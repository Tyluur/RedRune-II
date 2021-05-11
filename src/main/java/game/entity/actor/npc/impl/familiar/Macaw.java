package game.entity.actor.npc.impl.familiar;

import game.content.entity.actor.player.skills.herblore.HerbCleaning.Herbs;
import game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.Graphics;
import game.entity.actor.player.Player;
import game.entity.item.Item;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.functions.Misc;

public class Macaw extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = -7805271915467121215L;

	public Macaw(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Herbcall";
	}

	@Override
	public String getSpecialDescription() {
		return "Creates a random herb.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		Herbs herb;
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		// TODO too lazy to find anims and gfx
		if (Misc.getRandom(100) == 0) {
			herb = Herbs.values()[Misc.random(Herbs.values().length)];
		} else {
			herb = Herbs.values()[Misc.getRandom(3)];
		}
		RegionManager.addGroundItem(new Item(herb.getHerbId(), 1), player);
		return true;
	}
}
