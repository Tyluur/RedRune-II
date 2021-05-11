package game.content.entity.actor.player.dialogue.impl;

import cache.codec.loaders.ObjectDefinitions;
import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.content.entity.actor.player.dialogue.Dialogue;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.ForceMovement;
import game.entity.object.WorldObject;
import game.global.WorldTile;

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
