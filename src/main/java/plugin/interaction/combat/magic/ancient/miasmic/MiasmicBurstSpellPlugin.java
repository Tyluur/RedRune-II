package plugin.interaction.combat.magic.ancient.miasmic;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.utility.constants.key.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class MiasmicBurstSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 10516;
	}
	
	@Override
	public int hitGfx() {
		return 1849;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 240;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		style.sendMultiSpell(source, target, this, null, null).forEach(spellContext -> {
			Actor spellTarget = spellContext.getTarget();
			if (!spellTarget.isPlayer() || spellTarget.getTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, false)) {
				return;
			}
			Player p = spellTarget.toPlayer();
			p.getPackets().sendMessage("You feel slowed down.");
			spellTarget.putTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY, true);
			spellTarget.putTemporaryAttribute(AttributeKey.MIASMIC_EFFECT, true);
			SystemManager.SCHEDULER.schedule(new ScheduledTask(1, 55) {
				@Override
				public void run() {
					if (getTicksPassed() == 40) {
						spellTarget.removeTemporaryAttribute(AttributeKey.MIASMIC_EFFECT);
					} else if (getTicksPassed() == 55) {
						spellTarget.removeTemporaryAttribute(AttributeKey.MIASMIC_IMMUNITY);
					}
				}
			});
		});
	}
	
	@Override
	public int spellId() {
		return 38;
	}
	
	@Override
	public double exp() {
		return 42;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
