package plugin.interaction.combat.special.melee;

import org.redrune.game.content.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerSkills;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/5/2017
 */
public class DragonBattleaxeSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Graphics GRAPHICS = new Graphics(246);
	
	private static final Animation ANIMATION = new Animation(1056);
	
	@Override
	public int[] getWeaponIds() {
		return arguments(1377, 13472);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		source.setNextForceTalk(new ForceTalk("Raarrrrrgggggghhhhhhh!"));
		PlayerSkills skills = source.getSkills();
		int defence = (int) (skills.getLevel(DEFENCE) * 0.90D);
		int attack = (int) (skills.getLevel(ATTACK) * 0.90D);
		int range = (int) (skills.getLevel(RANGE) * 0.90D);
		int magic = (int) (skills.getLevel(MAGIC) * 0.90D);
		int strength = (int) (skills.getLevel(STRENGTH) * 1.2D);
		skills.set(DEFENCE, defence);
		skills.set(ATTACK, attack);
		skills.set(RANGE, range);
		skills.set(MAGIC, magic);
		skills.set(STRENGTH, strength);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public boolean requiresFight() {
		return false;
	}
}

