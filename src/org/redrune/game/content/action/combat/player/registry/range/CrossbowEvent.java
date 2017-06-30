package org.redrune.game.content.action.combat.player.registry.range;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.action.combat.player.registry.BowFireEvent;
import org.redrune.game.content.action.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class CrossbowEvent implements BowFireEvent {
	
	@Override
	public String[] bowNames() {
		return arguments("* crossbow");
	}
	
	@Override
	public void fire(Player attacker, Entity target, RangeCombatSwing swing, int weaponId, int ammoId) {
		String name = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		
		sendDamage(attacker, target, swing, weaponId);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(attacker, target, 27, 38, 36, 41, 5, 0));
		//		/players.stream().filter(player -> player != null && player.isRenderable()).forEach(player -> {
		//			player.getNetworkSession().writeNoDelay(new ProjectilePacketBuilder(projectile).build(player));
		//		});
		swing.dropAmmo(attacker, target.getLocation(), EquipConstants.SLOT_ARROWS, ammoId, name.contains("karil"));
	}
	
	// TODO: configure the gfx for (e) bolts
	
	/*
	int damage;
					int ammoId = player.getEquipment().getAmmoId();
					if (ammoId != -1 && Utils.getRandom(10) >= 8) {
						damage = getCrossbowDamage(player, ammoId, attackStyle, weaponId);
					} else {
						damage = getRandomMaxHit(player, weaponId, attackStyle, true);
						checkSwiftGlovesEffect(player, 2, attackStyle, weaponId, damage, 27, 38, 36, 41, 32, 5, 0);
					}
					World.sendProjectile(player, target, 27, 38, 36, 41, 32, 5, 0);
					delayHit(2, weaponId, attackStyle, getRangeHit(player, damage));
					// all non xbows drop ammo
					if (!weaponName.toLowerCase().contains("karil's crossbow")) {
						dropAmmo(player);
					} else {
						player.getEquipment().removeAmmo(ammoId, 1);
					}
					
		private int getCrossbowDamage(Player player, int ammoId, int attackStyle, int weaponId) {
		int damage;
		switch (ammoId) {
			case 9237:
				damage = getRandomMaxHit(player, weaponId, attackStyle, true);
				target.setNextGraphics(new Graphics(755));
				if (target instanceof Player) {
					Player p2 = (Player) target;
					p2.stopAll();
				} else {
					NPC n = (NPC) target;
					n.setTarget(null);
				}
				break;
			case 9242:
				max_hit = Short.MAX_VALUE;
				damage = (int) (target.getHealthPoints() * 0.2);
				target.setNextGraphics(new Graphics(754));
				player.applyHit(new Hit(target, player.getHealthPoints() > 20 ? (int) (player.getHealthPoints() * 0.1) : 1, HitLook.REFLECTED_DAMAGE));
				break;
			case 9243:
				damage = getRandomMaxHit(player, weaponId, attackStyle, true, false, 1.15, true);
				target.setNextGraphics(new Graphics(751));
				break;
			case 9244:
				damage = getRandomMaxHit(player, weaponId, attackStyle, true, false, !Combat.hasAntiDragProtection(target) ? 1.45 : 1.0, true);
				target.setNextGraphics(new Graphics(756));
				break;
			case 9245:
				damage = getRandomMaxHit(player, weaponId, attackStyle, true, false, 1.15, true);
				target.setNextGraphics(new Graphics(753));
				player.heal((int) (damage * 0.25));
				break;
			default:
				damage = getRandomMaxHit(player, weaponId, attackStyle, true);
		}
		return damage;
	}
	 */
}
