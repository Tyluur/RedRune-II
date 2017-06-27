package org.redrune.game.content.action.combat.player.registry.range;

import org.redrune.game.content.action.combat.StaticCombatFormulae;
import org.redrune.game.content.action.combat.player.CombatTypeSwing;
import org.redrune.game.content.action.combat.player.registry.BowFireEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class DarkBowEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("dark bow");
	}
	
	@Override
	public void fire(Player attacker, org.redrune.game.node.entity.Entity target, CombatTypeSwing swing, int weaponId, int ammoId) {
		int speed = (int) (46 + (attacker.getLocation().distance(target.getLocation()) * 5));
		int speed2 = (int) (55 + (attacker.getLocation().distance(target.getLocation()) * 10));
		attacker.sendGraphics(StaticCombatFormulae.getArrowThrowGfxId(ammoId), 100, 0);
		
		for (int i = 1; i <= 2; i++) {
			sendDamage(attacker, target, swing, weaponId);
			attacker.getRegion().sendProjectile(new Projectile(attacker, target, StaticCombatFormulae.getArrowProjectileGfxId(weaponId, ammoId), 41, 35, 41, i == 1 ? speed : speed2, i == 1 ? 5 : 25, 0));
			dropAmmo(attacker, target.getLocation(), EquipConstants.SLOT_ARROWS, ammoId, false);
		}
	}
}
