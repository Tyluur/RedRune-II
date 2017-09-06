package com.rs.game.plugin.combat;

import com.rs.game.content.combat.player.AbstractCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.Plugin;
import com.rs.game.plugin.PluginRepository;
import com.rs.utility.constants.EquipmentConstants;
import com.rs.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public abstract class SpecialAttackPlugin extends Plugin implements SkillConstants, EquipmentConstants {
	
	/**
	 * Gets the ids of the weapons that this special attack is registered for
	 */
	public abstract int[] getWeaponIds();
	
	/**
	 * Fires the special attack
	 */
	public abstract void fire(Player source, Actor target, AbstractCombatStyle style);
	
	/**
	 * If the special attack is instant
	 */
	public boolean isInstant() {
		return false;
	}
	
	/**
	 * If the special attack requires you to be in a fight, nearly all attacks do.
	 */
	public boolean requiresFight() {
		return true;
	}
	
	@Override
	public void register() {
		PluginRepository.register(this, getWeaponIds());
	}
}
