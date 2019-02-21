package org.redrune.game.entity.actor.npc.impl.familiar;

import org.redrune.game.content.entity.actor.player.skills.summoning.Summoning.Pouches;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.SkillConstants;

public class Bullant extends Familiar {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 4667052662212699631L;
	
	public Bullant(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}
	
	@Override
	public String getSpecialName() {
		return "Unburden";
	}
	
	@Override
	public String getSpecialDescription() {
		return "Restores the owner's run energy by half of their Agility level.";
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
		if (player.getAttributes().getRunEnergy() == 100) {
			player.getPackets().sendMessage("This wouldn't effect you at all.");
			return false;
		}
		int agilityLevel = getOwner().getSkills().getLevel(SkillConstants.AGILITY);
		int runEnergy = player.getAttributes().getRunEnergy() + (Math.round(agilityLevel / 2));
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		player.getAttributes().setRunEnergy(runEnergy > 100 ? 100 : runEnergy);
		return true;
	}
}
