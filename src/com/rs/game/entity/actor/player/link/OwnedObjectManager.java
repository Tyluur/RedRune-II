package com.rs.game.entity.actor.player.link;

import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.world.region.RegionManager;
import com.rs.utility.Misc;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OwnedObjectManager {
	
	public static final AtomicLong keyMaker = new AtomicLong();
	
	private static final Map<String, OwnedObjectManager> ownedObjects = new ConcurrentHashMap<String, OwnedObjectManager>();
	
	private Player player;
	
	private WorldObject[] objects;
	
	private int count;
	
	private long cycleTime;
	
	private long lifeTime;
	
	private String managerKey;
	
	private OwnedObjectManager(Player player, WorldObject[] objects, long cycleTime) {
		managerKey = player.getUsername() + "_" + keyMaker.getAndIncrement();
		this.cycleTime = cycleTime;
		this.objects = objects;
		this.player = player;
		spawnObject();
		player.getOwnedObjectManagerKeys().add(managerKey);
		ownedObjects.put(managerKey, this);
	}
	
	public static void processAll() {
		for (OwnedObjectManager object : ownedObjects.values()) {
			object.process();
		}
	}
	
	public void process() {
		if (Misc.currentTimeMillis() > lifeTime) {
			forceMoveNextStage();
		}
	}
	
	public boolean forceMoveNextStage() {
		if (count != -1) {
			destroyObject(objects[count]);
		}
		count++;
		if (count == objects.length) {
			remove();
			return false;
		}
		spawnObject();
		return true;
	}
	
	public void destroyObject(WorldObject object) {
		RegionManager.removeObject(object);
	}
	
	private void remove() {
		ownedObjects.remove(managerKey);
		if (player != null) {
			player.getOwnedObjectManagerKeys().remove(managerKey);
		}
	}
	
	private void spawnObject() {
		RegionManager.spawnObject(objects[count]);
		resetLifeTime();
	}
	
	public void resetLifeTime() {
		this.lifeTime = Misc.currentTimeMillis() + cycleTime;
	}
	
	public static boolean isPlayerObject(Player player, WorldObject object) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY() && manager.getCurrentObject().getPlane() == object.getPlane() && manager.getCurrentObject().getId() == object.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public WorldObject getCurrentObject() {
		return objects[count];
	}
	
	public static boolean convertIntoObject(WorldObject object, WorldObject toObject, ConvertEvent event) {
		for (OwnedObjectManager manager : ownedObjects.values()) {
			if (manager.getCurrentObject().getX() == toObject.getX() && manager.getCurrentObject().getY() == toObject.getY() && manager.getCurrentObject().getPlane() == toObject.getPlane() && manager.getCurrentObject().getId() == object.getId()) {
				if (event != null && !event.canConvert(manager.player)) {
					return false;
				}
				manager.convertIntoObject(toObject);
				return true;
			}
		}
		return false;
	}
	
	public void convertIntoObject(WorldObject object) {
		destroyObject(objects[count]);
		objects[count] = object;
		spawnObject();
	}
	
	public static boolean removeObject(Player player, WorldObject object) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			if (manager.getCurrentObject().getX() == object.getX() && manager.getCurrentObject().getY() == object.getY() && manager.getCurrentObject().getPlane() == object.getPlane() && manager.getCurrentObject().getId() == object.getId()) {
				manager.delete();
				return true;
			}
		}
		return false;
	}
	
	public void delete() {
		destroyObject(objects[count]);
		remove();
	}
	
	public static void linkKeys(Player player) {
		for (Iterator<String> it = player.getOwnedObjectManagerKeys().iterator(); it.hasNext(); ) {
			OwnedObjectManager manager = ownedObjects.get(it.next());
			if (manager == null) {
				it.remove();
				continue;
			}
			manager.player = player;
		}
	}
	
	public static void addOwnedObjectManager(Player player, WorldObject[] objects, long cycleTime) {
		new OwnedObjectManager(player, objects, cycleTime);
	}
	
	public void reset() {
		for (OwnedObjectManager object : ownedObjects.values()) {
			object.delete();
		}
	}
	
	public interface ConvertEvent {
		
		boolean canConvert(Player player);
		
	}
	
}
