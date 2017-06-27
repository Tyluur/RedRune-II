package org.redrune.game.content.action.combat.player.registry.special;

import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.registry.SpecialAttackEvent;
import org.redrune.game.node.entity.data.Hit;
import org.redrune.game.node.entity.data.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class ArmadylGodswordSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("armadyl godsword");
	}
	
	@Override
	public double accuracyIncrease() {
		return 1.10;
	}
	
	@Override
	public int energyRequired() {
		return 50;
	}
	
	@Override
	public void fire(Player player, org.redrune.game.node.entity.Entity target, CombatTypeSwing swing, int combatStyle) {
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, accuracyIncrease());
		
		player.sendAnimation(11989);
		player.sendGraphics(2113);
		swing.applyHit(player, target, new Hit(player, swing.randomizeHit(maxHit, attackBonus, defenceBonus), HitSplat.MELEE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, 1);
	}
}
