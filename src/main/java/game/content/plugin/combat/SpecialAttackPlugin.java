package game.content.plugin.combat;

import game.content.entity.actor.combat.player.AbstractCombatStyle;
import game.content.plugin.Plugin;
import game.content.plugin.PluginRepository;
import game.entity.actor.Actor;
import game.entity.actor.player.Player;
import utility.constants.EquipmentConstants;
import utility.constants.SkillConstants;

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
		PluginRepository.INSTANCE.register(this, getWeaponIds());
	}
}
