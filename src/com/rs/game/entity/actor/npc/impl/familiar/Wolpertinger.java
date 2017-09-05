package com.rs.game.entity.actor.npc.impl.familiar;

import com.rs.game.content.skills.summoning.Summoning.Pouches;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.constants.SkillConstants;

public class Wolpertinger extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 4097036858996221680L;

	public Wolpertinger(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		int newLevel = player.getSkills().getLevel(SkillConstants.MAGIC) + 7;
		if (newLevel > player.getSkills().getLevelForXp(SkillConstants.MAGIC) + 7) {
			newLevel = player.getSkills().getLevelForXp(SkillConstants.MAGIC) + 7;
		}
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getSkills().set(SkillConstants.MAGIC, newLevel);
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
		return "Magic Focus";
	}

	@Override
	public String getSpecialDescription() {
		return "Boosts your restistance towards magic by 5% while also boosting your magic by 7%.";
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}
}
