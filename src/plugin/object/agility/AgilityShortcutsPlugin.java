package plugin.object.agility;

import org.redrune.game.content.entity.actor.player.skills.agility.Shortcuts;
import org.redrune.game.content.plugin.type.ObjectPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;

import static org.redrune.utility.game.ClickOption.FIRST;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-29
 */
public class AgilityShortcutsPlugin implements ObjectPlugin {
	
	@Override
	public boolean handle(Player player, WorldObject object, String option) {
		int id = object.getId();
		if (id == 9311 || id == 9312) {
			Shortcuts.handleEdgevilleUnderwallTunnel(player, object);
		}
		return true;
	}
	
	@Override
	public void register() {
		registerSpecifiedOptionVarags(FIRST, 9311, 9312);
	}
}
