package plugin.object.agility;

import org.redrune.game.content.entity.actor.player.skills.agility.WildernessAgility;
import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-29
 */
public class WildernessAgilityCoursePlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		int id = object.getId();
		switch (id) {
			case 2297:
				WildernessAgility.walkAcrossLogBalance(player, object);
				break;
			case 37704:
				WildernessAgility.jumpSteppingStones(player, object);
				break;
			case 2288:
				WildernessAgility.enterWildernessPipe(player, object.getX(), object.getY());
				break;
			case 2328:
				WildernessAgility.climbUpWall(player, object);
				break;
			case 2283:
				WildernessAgility.swingOnRopeSwing(player, object);
				break;
			case 2309:
				WildernessAgility.enterWildernessCourse(player);
				break;
			case 2307:
			case 2308:
				WildernessAgility.exitWildernessCourse(player);
				break;
		}
		return true;
	}
	
	@Override
	public void register() {
		registerSpecifiedOptionVarags(ClickOption.FIRST, 2297, 37704, 2288, 2328, 2283, 2309, 2307, 2308);
	}
}
