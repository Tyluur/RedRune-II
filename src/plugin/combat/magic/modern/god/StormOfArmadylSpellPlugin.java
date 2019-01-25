package plugin.combat.magic.modern.god;

import org.redrune.game.content.combat.player.style.MagicCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.projectile.ProjectileManager;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class StormOfArmadylSpellPlugin extends CombatSpellPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(457);
	
	@Override
	public int delay(Player player) {
		return player.getEquipment().getWeaponId() == 21777 ? 4 : 5;
	}
	
	@Override
	public int animationId() {
		return 10546;
	}
	
	@Override
	public int hitGfx() {
		return 1019;
	}
	
	@Override
	public int maxHit(Player player, Actor target) {
		return 160 + minimumHit(player);
	}
	
	@Override
	public void cast(Player source, Actor target, MagicCombatStyle style) {
		source.setNextGraphics(GRAPHICS);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(source, target, 1019, 30, 26, 52, 0, 0));
		if (target.isPlayer()) {
			target.toPlayer().getSkills().set(DEFENCE, target.toPlayer().getSkills().getLevel(DEFENCE) - 1);
		}
		style.sendSpell(source, target, this);
	}
	
	@Override
	public int minimumHit(Player player) {
		int start = 77;
		int level = player.getSkills().getLevelForXp(SkillConstants.MAGIC);
		int difference = level - start;
		return (difference / 2) * 10;
	}
	
	@Override
	public int spellId() {
		return 99;
	}
	
	@Override
	public double exp() {
		return 70;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
