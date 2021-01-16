package org.redrune.game.content.entity.actor.player.dialogue.impl.object;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.schedule.ScheduledTask;
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.content.entity.object.ClimbActionHandler;
import org.redrune.game.entity.object.WorldObject;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-21
 */
public class ClimbDialogue extends Dialogue {
	
	private WorldObject object;
	
	@Override
	public void start() {
		object = getParam(0);
		sendOptions("What would you like to do?", "Climb Up", "Climb Down");
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
			case -1:
				end();
				if (componentId == first) {
					climb(true);
				} else if (componentId == second) {
					climb(false);
				}
				break;
		}
	}
	
	@Override
	public void finish() {
	
	}
	
	private void climb(boolean up) {
		player.getLocks().lock(1);
		SystemManager.SCHEDULER.schedule(new ScheduledTask(2) {
			@Override
			public void run() {
				ClimbActionHandler.climbLadder(player, object, up ? "climb-up" : "climb-down");
			}
		});
	}
	
}
