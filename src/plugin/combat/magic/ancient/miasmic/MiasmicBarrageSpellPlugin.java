package plugin.combat.magic.ancient.miasmic;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.constants.key.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class MiasmicBarrageSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 10518;
	}
	
	@Override
	public int hitGfx() {
		return 1854;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 320;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellContext -> {
			Actor spellTarget = spellContext.getTarget();
			if (!spellTarget.isPlayer() || spellTarget.getTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
				return;
			}
			Player p = spellTarget.toPlayer();
			p.getPackets().sendGameMessage("You feel slowed down.");
			spellTarget.putAttribute(AttributeKey.MIASMIC_IMMUNITY, true);
			spellTarget.putAttribute(AttributeKey.MIASMIC_EFFECT, true);
			SystemManager.SCHEDULER.schedule(new ScheduledTask(1, 95) {
				@Override
				public void run() {
					if (getTicksPassed() == 80) {
						spellTarget.removeTemporaryAttribute(AttributeKey.MIASMIC_EFFECT);
					} else if (getTicksPassed() == 95) {
						spellTarget.removeTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY);
					}
				}
			});
		});
	}
	
	@Override
	public int spellId() {
		return 39;
	}
	
	@Override
	public double exp() {
		return 54;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
