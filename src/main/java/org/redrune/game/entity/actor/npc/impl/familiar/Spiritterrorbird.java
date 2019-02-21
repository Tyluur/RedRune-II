package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.SkillConstants;

public class Spiritterrorbird extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 5259052583696765531L;

	public Spiritterrorbird(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		if (player.getAttributes().getRunEnergy() == 100) {
			player.getPackets().sendMessage("This wouldn't effect you at all.");
			return false;
		}
		int newLevel = getOwner().getSkills().getLevel(SkillConstants.AGILITY) + 2;
		int runEnergy = player.getAttributes().getRunEnergy() + (Math.round(newLevel / 2));
		if (newLevel > getOwner().getSkills().getLevelForXp(SkillConstants.AGILITY) + 2) {
			newLevel = getOwner().getSkills().getLevelForXp(SkillConstants.AGILITY) + 2;
		}
		setNextAnimation(new Animation(8229));
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getSkills().set(SkillConstants.AGILITY, newLevel);
		player.getAttributes().setRunEnergy(runEnergy > 100 ? 100 : runEnergy);
		return true;
	}

	@Override
	public int getBOBSize() {
		return 12;
	}

	@Override
	public int getSpecialAmount() {
		return 8;
	}

	@Override
	public String getSpecialName() {
		return "Tireless Run";
	}

	@Override
	public String getSpecialDescription() {
		return "Restores the player's run energy, by half the players agility level rounded up.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}
}
