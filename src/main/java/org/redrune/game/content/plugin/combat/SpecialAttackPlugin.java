package org.redrune.game.content.plugin.combat;

import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.content.plugin.Plugin;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
public abstract class SpecialAttackPlugin implements Plugin, SkillConstants, EquipmentConstants {
	
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
