package plugin.combat.magic.ancient.miasmic;

import com.rs.cores.CoresManager;
import com.rs.cores.schedule.ScheduledTask;
import com.rs.game.content.combat.player.style.MagicCombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.CombatSpellPlugin;
import com.rs.game.world.projectile.ProjectileManager;
import com.rs.utility.constants.AttributeKey;
import com.rs.utility.constants.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class MiasmicRushSpellPlugin extends CombatSpellPlugin {
	
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
			if (!target.isPlayer() || target.getAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
				return;
			}
			Player p = target.toPlayer();
			p.getPackets().sendGameMessage("You feel slowed down.");
			target.putAttribute(AttributeKey.MIASMIC_IMMUNITY, true);
			target.putAttribute(AttributeKey.MIASMIC_EFFECT, true);
			CoresManager.scheduler.schedule(new ScheduledTask(1, 35) {
				@Override
				public void run() {
					if (getTicksPassed() == 20) {
						target.removeAttribute(AttributeKey.MIASMIC_EFFECT);
					} else if (getTicksPassed() == 35) {
						target.removeAttribute(AttributeKey.MIASMIC_IMMUNITY);
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
}
