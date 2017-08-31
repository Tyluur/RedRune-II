package com.rs.game.entity.actor.npc.impl.familiar;

import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.Skills;
import com.rs.game.content.skills.fishing.Fishing.Fish;
import com.rs.game.content.skills.summoning.Summoning.Pouches;
import com.rs.game.content.node.item.Foods.Food;

public class Bunyip extends Familiar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7203440350875823581L;

	public Bunyip(Player owner, Pouches pouch, WorldTile tile,
			int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Swallow Whole";
	}

	@Override
	public String getSpecialDescription() {
		return "Eat an uncooked fish and gain the correct number of life points corresponding to the fish eaten if you have the cooking level to cook the fish.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 7;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Item item = getOwner().getInventory().getItem((Integer) object);
		for (Fish fish : Fish.values()) {
			if (fish.getId() == item.getId()) {
				if (getOwner().getSkills().getLevel(Skills.COOKING) < fish
						.getLevel()) {
					getOwner()
							.getPackets()
							.sendGameMessage(
									"Your cooking level is not high enough for the bunyip to eat this fish.");
					return false;
				} else {
					getOwner().setNextGraphics(new Graphics(1316));
					getOwner().setNextAnimation(new Animation(7660));
					getOwner().heal(Food.forId(item.getId()).getHeal());
					getOwner().getInventory().deleteItem(item.getId(),
							item.getAmount());
					return true;// stop here
				}
			} else {
				getOwner().getPackets().sendGameMessage(
						"Your bunyip cannot eat this.");
				return false;
			}
		}
		return true;
	}
}
