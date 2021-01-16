package plugin.interaction.combat.range;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.engine.cycle.GameCycleWorker;
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.content.plugin.combat.RangeWeaponPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.functions.RandomFunction;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/6/2017
 */
public class CrossbowPlugin extends RangeWeaponPlugin {
	
	@Override
	public String[] getWeaponNames() {
		return arguments("* crossbow");
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int weaponId = source.getEquipment().getWeaponId();
		int ammoId = source.getEquipment().getAmmoId();
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 27, 38, 36, 41, 5, 0));
		Optional<BoltSpecial> optional = BoltSpecial.getBoltSpecial(ammoId);
		// found a possible bolt
		if (optional.isPresent()) {
			BoltSpecial special = optional.get();
			// the bolt was fired so we don't need to send another hit
			if (special.canFire(source, target)) {
				special.fire(source, target, style, weaponId);
				return;
			}
		}
		if (!name.contains("karil's crossbow")) {
			dropAmmo(source, target, 1);
		} else {
			source.getEquipment().removeAmmo(ammoId, 1);
		}
		style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
	}
	
	private enum BoltSpecial {
		
		JADE_BOLT(9237, 755) {
			@Override
			public double getDamageModifier() {
				return 1;
			}
			
			@Override
			public CombatSwingDetail fire(Player source, Actor target, AbstractCombatStyle style, int weaponId) {
				if (target.isNPC()) {
					target.toNPC().getCombat().setTarget(null);
				} else {
					target.toPlayer().stopAll();
				}
				return super.fire(source, target, style, weaponId);
			}
		},
		
		RUBY_BOLT(9242, 754) {
			@Override
			public double getDamageModifier() {
				return 1;
			}
			
			@Override
			public CombatSwingDetail fire(Player source, Actor target, AbstractCombatStyle style, int weaponId) {
				target.setNextGraphics(new Graphics(getGraphicsId(), getGraphicsHeight(), 0));
				source.applyHit(new Hit(target, source.getHitpoints() > 20 ? (int) (source.getHitpoints() * 0.10) : 1, HitSplat.REFLECTED_DAMAGE));
				return style.sendHit(source, target, style.getCalculator().getMaximumHit(source,1), (int) (target.getHitpoints() * 0.20), ProjectileManager.getProjectileDelay(source, target));
			}
		},
		
		DIAMOND_BOLT(9243, 758) {
			@Override
			public double getDamageModifier() {
				return 1.05;
			}
		},
		
		DRAGON_BOLT(9244, 756) {
			@Override
			public double getDamageModifier() {
				return 1.45;
			}
			
			@Override
			public boolean canFire(Player source, Actor target) {
				return !CombatAlgorithm.hasAntiDragProtection(target) && super.canFire(source, target);
			}
		},
		
		ONYX_BOLT(9245, 753) {
			@Override
			public double getDamageModifier() {
				return 1.25;
			}
			
			@Override
			public boolean canFire(Player source, Actor target) {
				return source.getTemporaryAttribute("onyx-effect", 0L) <= GameCycleWorker.getTicksPassed() && super.canFire(source, target);
			}
			
			@Override
			public CombatSwingDetail fire(Player source, Actor target, AbstractCombatStyle style, int weaponId) {
				return super.fire(source, target, style, weaponId).consume(detail -> {
					source.putTemporaryAttribute("onyx-effect", GameCycleWorker.getTicksPassed() + 12);
					source.heal((int) (detail.getHit().getDamage() * 0.25));
				});
			}
		},;
		
		/**
		 * The id of the bolt used for this special
		 */

		private final int boltId;
		
		/**
		 * The id of the graphics
		 */

		private final int graphicsId;
		
		/**
		 * The height of the graphics
		 */

		private final int graphicsHeight;
		
		BoltSpecial(int boltId, int graphicsId) {
			this(boltId, graphicsId, 0);
		}
		
		BoltSpecial(int boltId, int graphicsId, int graphicsHeight) {
			this.boltId = boltId;
			this.graphicsId = graphicsId;
			this.graphicsHeight = graphicsHeight;
		}
		
		/**
		 * The damage modifier of the bolt special
		 */
		public abstract double getDamageModifier();
		
		/**
		 * Checks if the bolt special can be fired
		 *
		 * @param source
		 * 		The source of the special
		 */
		public boolean canFire(Player source, Actor target) {
			return RandomFunction.random(13) == 5;
		}
		
		/**
		 * When the style is fired
		 */
		public CombatSwingDetail fire(Player source, Actor target, AbstractCombatStyle style, int weaponId) {
			target.setNextGraphics(new Graphics(graphicsId, graphicsHeight, 0));
			return style.sendHit(source, target, style.getCalculator().getMaximumHit(source, 1), style.getRandomDamage(source, target, 1), ProjectileManager.getProjectileDelay(source, target));
		}
		
		/**
		 * Gets the bolt special by the id of the bolt we're using
		 *
		 * @param boltId
		 * 		The id of the bolt
		 */
		public static Optional<BoltSpecial> getBoltSpecial(int boltId) {
			for (BoltSpecial special : values()) {
				if (special.getBoltId() == boltId) {
					return Optional.of(special);
				}
			}
			return Optional.empty();
		}

		public int getBoltId() {
			return boltId;
		}

		public int getGraphicsHeight() {
			return graphicsHeight;
		}

		public int getGraphicsId() {
			return graphicsId;
		}
	}
}
