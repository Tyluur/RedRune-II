package com.rs.game.content.dialogue.impl;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.ForceMovement;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

public class WildernessDitch extends Dialogue {
	
	private WorldObject ditch;
	
	@Override
	public void start() {
		ditch = (WorldObject) parameters[0];
		player.getInterfaceManager().sendInterface(382);
	}
	
	@Override
	public void run(int interfaceId, int componentId) {
		if (interfaceId == 382 && componentId == 19) {
			player.stopAll();
			player.getLocks().lock(4);
			player.setNextAnimation(new Animation(6132));
			final WorldTile toTile = new WorldTile(player.getX(), ditch.getY() + 2, ditch.getPlane());
			player.setNextForceMovement(new ForceMovement(new WorldTile(player), 1, toTile, 2, 0));
			final ObjectDefinitions objectDef = ditch.getDefinitions();
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(toTile);
					player.setNextFaceWorldTile(new WorldTile(ditch.getCoordFaceX(objectDef.getSizeX(), objectDef.getSizeY(), ditch.getRotation()), ditch.getCoordFaceY(objectDef.getSizeX(), objectDef.getSizeY(), ditch.getRotation()), ditch.getPlane()));
					player.getControllerManager().startController("Wilderness");
				}
			}, 2);
		} else {
			player.closeInterfaces();
		}
		end();
	}
	
	@Override
	public void finish() {
	
	}
	
}
