package plugin.interaction.combat.magic.ancient.blood;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class BloodBlitzSpellPlugin implements CombatSpellPlugin {
	
	@Override
	public int delay(Player player) {
		return 4;
	}
	
	@Override
	public int animationId() {
		return 1978;
	}
	
	@Override
	public int hitGfx() {
		return 375;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 250;
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 374, 18, 9, 52, 15, 0));
		style.sendSpell(source, target, this, null, null).consume(detail -> {
			if (detail.getHit().getDamage() != 0) {
				source.getPackets().sendGameMessage("You drain some of your opponents' life points.");
				source.heal(detail.getHit().getDamage() / 4);
			}
		});
	}
	
	@Override
	public int spellId() {
		return 25;
	}
	
	@Override
	public double exp() {
		return 45;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
	
}
