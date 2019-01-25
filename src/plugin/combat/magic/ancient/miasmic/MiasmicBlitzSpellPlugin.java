package plugin.combat.magic.ancient.miasmic;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.constants.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class MiasmicBlitzSpellPlugin extends CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 10524;
	}
	
	@Override
	public int hitGfx() {
		return 1851;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 280;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 1852, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, null, () -> {
			if (!target.isPlayer() || target.getAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
				return;
			}
			Player p = target.toPlayer();
			p.getPackets().sendGameMessage("You feel slowed down.");
			target.putAttribute(AttributeKey.MIASMIC_IMMUNITY, true);
			target.putAttribute(AttributeKey.MIASMIC_EFFECT, true);
			SystemManager.SCHEDULER.schedule(new ScheduledTask(1, 75) {
				@Override
				public void run() {
					if (getTicksPassed() == 60) {
						target.removeAttribute(AttributeKey.MIASMIC_EFFECT);
					} else if (getTicksPassed() == 75) {
						target.removeAttribute(AttributeKey.MIASMIC_IMMUNITY);
					}
				}
			});
		});
	}
	
	@Override
	public int spellId() {
		return 37;
	}
	
	@Override
	public double exp() {
		return 48;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
