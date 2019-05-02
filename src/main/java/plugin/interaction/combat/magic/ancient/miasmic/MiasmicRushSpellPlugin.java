package plugin.interaction.combat.magic.ancient.miasmic;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.constants.key.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class MiasmicRushSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 10513;
	}
	
	@Override
	public int hitGfx() {
		return 1847;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 200;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 1846, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, null, () -> {
			if (!target.isPlayer() || target.getTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
				return;
			}
			Player p = target.toPlayer();
			p.getPackets().sendMessage("You feel slowed down.");
			target.putTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, true);
			target.putTemporaryAttribute(AttributeKey.MIASMIC_EFFECT, true);
			SystemManager.SCHEDULER.schedule(new ScheduledTask(1, 35) {
				@Override
				public void run() {
					if (getTicksPassed() == 20) {
						target.removeTemporaryAttribute(AttributeKey.MIASMIC_EFFECT);
					} else if (getTicksPassed() == 35) {
						target.removeTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY);
					}
				}
			});
		});
	}
	
	@Override
	public int spellId() {
		return 36;
	}
	
	@Override
	public double exp() {
		return 35;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public int impactSoundId() {
		return 173;
	}
}
