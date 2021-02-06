package plugin.interaction.combat.special.melee;

import org.redrune.utility.constants.GameConstants;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerSkills;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/5/2017
 */
public class ExcaliburSpecialAttackPlugin extends SpecialAttackPlugin {
	
	private static final Animation ANIMATION = new Animation(1168);
	
	private static final Graphics GRAPHICS = new Graphics(247);
	
	private static final ForceTalk FORCE_TALK = new ForceTalk("For " + GameConstants.SERVER_NAME.toUpperCase() + "!");
	
	@Override
	public int[] getWeaponIds() {
		return arguments(35, 8280, 14632);
	}
	
	@Override
	public void fire(Player source, Actor target, AbstractCombatStyle style) {
		int weaponId = source.getEquipment().getWeaponId();
		source.setNextAnimation(ANIMATION);
		source.setNextGraphics(GRAPHICS);
		source.setNextForceTalk(FORCE_TALK);
		final boolean enhanced = weaponId == 14632;
		PlayerSkills skills = source.getSkills();
		skills.set(SkillConstants.DEFENCE, enhanced ? (int) (skills.getLevelForXp(SkillConstants.DEFENCE) * 1.15D) : (skills.getLevel(SkillConstants.DEFENCE) + 8));
		WorldTasksManager.schedule(new WorldTask() {
			int count = 5;
			
			@Override
			public void run() {
				if (source.isDead() || source.isFinished() || source.getHitpoints() >= source.getMaxHitpoints()) {
					stop();
					return;
				}
				source.heal(enhanced ? 80 : 40);
				if (count-- == 0) {
					stop();
				}
			}
		}, 4, 2);
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
