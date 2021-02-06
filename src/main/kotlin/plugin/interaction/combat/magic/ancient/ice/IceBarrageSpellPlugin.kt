package plugin.interaction.combat.magic.ancient.ice;

import org.redrune.game.content.entity.actor.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 6/28/2017
 */
public class IceBarrageSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int spellId() {
		return 23;
	}
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1979;
	}
	
	// we don't store a static gfx because it is modifiable
	@Override
	public int hitGfx() {
		return -1;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 300;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		final boolean freezeDelayed = target.freezeDelayed();
		final boolean frozenTarget = target.isFrozen();
		style.sendMultiSpell(source, target, this, () -> {
			if (frozenTarget || freezeDelayed) {
				return;
			}
			// only freeze the player if they are unfreezeable when the spell is cast.
			target.freeze(source, TimeUnit.SECONDS.toMillis(20), "You have been frozen!");
		}, () -> {
			int gfx;
			int height;
			if (target.getSize() >= 2 || freezeDelayed || frozenTarget) {
				gfx = 1677;
				height = 100;
			} else {
				gfx = 369;
				height = 0;
			}
			target.setNextGraphics(new Graphics(gfx, 0, height));
		});
	}
	
	@Override
	public double exp() {
		return 52;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
	@Override
	public int castSoundId() {
		return 171;
	}
	
	@Override
	public int impactSoundId() {
		return 168;
	}
}
