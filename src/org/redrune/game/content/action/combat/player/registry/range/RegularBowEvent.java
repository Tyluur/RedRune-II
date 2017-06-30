package org.redrune.game.content.action.combat.player.registry.range;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.registry.BowFireEvent;
import org.redrune.game.content.action.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class RegularBowEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("* shortbow", "* longbow");
	}
	
	@Override
	public void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId) {
		sendDamage(attacker, target, swing, weaponId);
		attacker.sendGraphics(StaticCombatFormulae.getArrowThrowGfxId(ammoId), 100, 0);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(attacker, target, StaticCombatFormulae.getArrowProjectileGfxId(weaponId, ammoId), 40, 30, 41, 15, 0));
		swing.dropAmmo(attacker, target.getLocation(), EquipConstants.SLOT_ARROWS, ammoId, false);
	}
	
}
