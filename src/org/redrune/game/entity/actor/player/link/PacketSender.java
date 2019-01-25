package org.redrune.game.entity.actor.player.link;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.FloorItem;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.map.region.RegionManager;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/31/2017
 */
public final class PacketSender {
	
	/**
	 * The player this packet sender is for
	 */
	private final Player player;
	
	public PacketSender(Player player) {
		this.player = player;
	}
	
	public void sendRunButtonConfig() {
		player.getPackets().sendConfig(173, player.isResting() ? 3 : player.getRun() ? 1 : 0);
	}
	
	public void refreshSpawnedObjects() {
		for (int regionId : player.getMapRegionsIds()) {
			List<WorldObject> removedObjects = RegionManager.getRegion(regionId).getRemovedObjects();
			if (removedObjects != null) {
				for (WorldObject object : removedObjects) {
					player.getPackets().sendDestroyObject(object);
				}
			}
			List<WorldObject> spawnedObjects = RegionManager.getRegion(regionId).getSpawnedObjects();
			if (spawnedObjects != null) {
				for (WorldObject object : spawnedObjects) {
					if (object.getPlane() == player.getPlane()) {
						player.getPackets().sendSpawnedObject(object);
					}
				}
			}
		}
	}
	
	public void refreshSpawnedItems() {
		for (int regionId : player.getMapRegionsIds()) {
			List<FloorItem> floorItems = RegionManager.getRegion(regionId).getFloorItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && player != item.getOwner() || item.getTile().getPlane() != player.getPlane()) {
					continue;
				}
				player.getPackets().sendRemoveGroundItem(item);
			}
		}
		for (int regionId : player.getMapRegionsIds()) {
			List<FloorItem> floorItems = RegionManager.getRegion(regionId).getFloorItems();
			if (floorItems == null) {
				continue;
			}
			for (FloorItem item : floorItems) {
				if ((item.isInvisible() || item.isGrave()) && player != item.getOwner() || item.getTile().getPlane() != player.getPlane()) {
					continue;
				}
				player.getPackets().sendGroundItem(item);
			}
		}
	}
	
	public void switchMouseButtons() {
		player.setMouseButtons(!player.isMouseButtons());
		refreshMouseButtons();
	}
	
	public void refreshMouseButtons() {
		player.getPackets().sendConfig(170, player.isMouseButtons() ? 0 : 1);
	}
	
	public void refreshPrivateChatSetup() {
		player.getPackets().sendConfig(287, player.getPrivateChatSetup());
	}
	
	public void sendDefaultPlayersOptions() {
		player.getPackets().sendPlayerOption("Follow", 2, false);
		player.getPackets().sendPlayerOption("Trade with", 3, false);
		//		getPackets().sendPlayerOption("Req Assist", 4, false);
	}
	
	public void switchAllowChatEffects() {
		player.setAllowChatEffects(!player.isAllowChatEffects());
		refreshAllowChatEffects();
	}
	
	public void refreshAllowChatEffects() {
		player.getPackets().sendConfig(171, player.isAllowChatEffects() ? 0 : 1);
	}
}
