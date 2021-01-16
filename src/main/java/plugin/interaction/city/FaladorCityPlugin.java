package plugin.interaction.city;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.player.skills.agility.Agility;
import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceMovement;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.SkillConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-31
 */
public class FaladorCityPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		int id = object.getId();
		switch(id) {
			case 11844: // crumbling wall
				if (!Agility.hasLevel(player, 5)) {
					return true;
				}
				player.getLocks().lock();
				final WorldTile toTile = new WorldTile(player.getX() >= 2936 ? 2934 : 2936, 3355, 0);
				player.setNextForceMovement(new ForceMovement(player.getWorldTile(), 1, toTile, 2, player.getX() == 2934 ? ForceMovement.EAST : ForceMovement.WEST));
				player.setNextAnimation(new Animation(839));
				WorldTasksManager.schedule(new WorldTask() {
					int stage;
					@Override
					public void run() {
						if (stage == 1) {
							player.setNextWorldTile(toTile);
							player.getSkills().addXp(SkillConstants.AGILITY, 0.5);
							player.setNextAnimation(new Animation(-1));
							player.getLocks().unlock();
							stop();
						}
						stage++;
					}
				}, 0, 1);
				break;
		}
		return true;
	}
	
	@Override
	public void register() {
		registerObject(11844, "Climb-over");
	}
}
