package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.SkillConstants;

public class Icetitan extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 8361882652135063488L;

	public Icetitan(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		int newLevel = getOwner().getSkills().getLevel(SkillConstants.DEFENCE) + (getOwner().getSkills().getLevelForXp(SkillConstants.DEFENCE) / (int) 12.5);
		if (newLevel > getOwner().getSkills().getLevelForXp(SkillConstants.DEFENCE) + (int) 12.5) {
			newLevel = getOwner().getSkills().getLevelForXp(SkillConstants.DEFENCE) + (int) 12.5;
		}
		getOwner().setNextGraphics(new Graphics(2011));
		getOwner().setNextAnimation(new Animation(7660));
		getOwner().getSkills().set(SkillConstants.DEFENCE, newLevel);
		return true;
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public String getSpecialName() {
		return "Titan's Constitution ";
	}

	@Override
	public String getSpecialDescription() {
		return "Defence by 12.5%, and it can also increase a player's Life Points 80 points higher than their max Life Points.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}
}
