package com.rs.game.entity.actor.npc.impl.familiar;

import com.rs.game.content.skills.summoning.Summoning.Pouches;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.SkillConstants;

public class Wartortoise extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 5092434230714486203L;

	public Wartortoise(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		int newLevel = player.getSkills().getLevel(SkillConstants.DEFENCE) + 9;
		if (newLevel > player.getSkills().getLevelForXp(SkillConstants.DEFENCE) + 9) {
			newLevel = player.getSkills().getLevelForXp(SkillConstants.DEFENCE) + 9;
		}
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getSkills().set(SkillConstants.DEFENCE, newLevel);
		return true;
	}

	@Override
	public int getBOBSize() {
		return 18;
	}

	@Override
	public int getSpecialAmount() {
		return 20;
	}

	@Override
	public String getSpecialName() {
		return "Testudo";
	}

	@Override
	public String getSpecialDescription() {
		return "Increases defence by nine points.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}
}
