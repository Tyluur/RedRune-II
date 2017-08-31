package com.rs.game.content.cutscene;

import com.rs.cores.CoresManager;
import com.rs.game.GameFlags;
import com.rs.game.world.region.RegionBuilder;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.link.InterfaceManager;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.cutscene.actions.CutsceneAction;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

public abstract class Cutscene {
	
	public abstract boolean hiddenMinimap();
	
	public abstract CutsceneAction[] getActions(Player player);
	
	private int stage;
	
	private Object[] cache;
	
	private CutsceneAction[] actions;
	
	private int delay;
	
	private boolean constructingRegion;
	
	private int[] currentMapData;
	
	public Cutscene() {
	
	}
	
	private WorldTile endTile;
	
	public final void stopCutscene(Player player) {
		if (player.getX() != endTile.getX() || player.getY() != endTile.getY() || player.getPlane() != endTile.getPlane()) {
			player.setNextWorldTile(endTile);
		}
		if (hiddenMinimap()) {
			player.getPackets().sendBlackOut(0); // unblack
		}
		player.getPackets().sendConfig(1241, 0);
		player.getPackets().sendResetCamera();
		player.resetLockDelay();
		deleteCache();
		if (currentMapData != null) {
			CoresManager.slowExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						if (currentMapData != null) {
							RegionBuilder.destroyMap(currentMapData[0], currentMapData[1], currentMapData[1], currentMapData[2]);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	public final void startCutscene(Player player) {
		if (hiddenMinimap()) {
			player.getPackets().sendBlackOut(2); // minimap
		}
		player.getPackets().sendConfig(1241, 1);
		player.setInfiniteStopDelay();
		player.stopAll(true, false);
	}
	
	public void constructArea(final Player player, final int baseChunkX, final int baseChunkY, final int widthChunks, final int heightChunks) {
		constructingRegion = true;
		player.getPackets().sendWindowsPane(56, 0);
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final int[] oldData = currentMapData;
					int[] mapBaseChunks = RegionBuilder.findEmptyMap(widthChunks, heightChunks);
					RegionBuilder.copyAllPlanesMap(baseChunkX, baseChunkY, mapBaseChunks[0], mapBaseChunks[1], widthChunks, heightChunks);
					currentMapData = new int[] { mapBaseChunks[0], mapBaseChunks[1], widthChunks, heightChunks };
					player.setNextWorldTile(new WorldTile(getBaseX() + widthChunks * 4, +getBaseY() + heightChunks * 4, 0));
					constructingRegion = false;
					if (GameFlags.debugMode) {
						System.out.println("Bases: " + getBaseX() + ", " + getBaseY());
					}
					WorldTasksManager.schedule(new WorldTask() {
						
						@Override
						public void run() {
							CoresManager.slowExecutor.execute(() -> {
								player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? InterfaceManager.RESIZABLE_WINDOW_ID : InterfaceManager.FIXED_WINDOW_ID, 0);
								if (oldData != null) {
									RegionBuilder.destroyMap(oldData[0], oldData[1], oldData[1], oldData[2]);
								}
							});
						}
						
					}, 1);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public int getLocalX(Player player, int x) {
		if (currentMapData == null) {
			return x;
		}
		return getX(player, getBaseX() + x);
	}
	
	public int getLocalY(Player player, int y) {
		if (currentMapData == null) {
			return y;
		}
		return getY(player, getBaseY() + y);
	}
	
	public int getBaseX() {
		return currentMapData == null ? 0 : currentMapData[0] << 3;
	}
	
	public int getBaseY() {
		return currentMapData == null ? 0 : currentMapData[1] << 3;
	}
	
	public final void logout(Player player) {
		stopCutscene(player);
	}
	
	public final boolean process(Player player) {
		if (delay > 0) {
			delay--;
			return true;
		}
		while (true) {
			if (constructingRegion) {
				return true;
			}
			if (stage == actions.length) {
				stopCutscene(player);
				return false;
			} else if (stage == 0) {
				startCutscene(player);
			}
			CutsceneAction action = actions[stage++];
			action.process(player, cache);
			int delay = action.getActionDelay();
			if (delay == -1) {
				continue;
			}
			this.delay = delay;
			return true;
		}
	}
	
	public void deleteCache() {
		for (Object object : cache) {
			destroyCache(object);
		}
	}
	
	public void destroyCache(Object object) {
		if (object instanceof NPC) {
			NPC n = (NPC) object;
			n.finish();
		}
	}
	
	public final void createCache(Player player) {
		actions = getActions(player);
		endTile = new WorldTile(player);
		int lastIndex = 0;
		for (CutsceneAction action : actions) {
			if (action.getCachedObjectIndex() > lastIndex) {
				lastIndex = action.getCachedObjectIndex();
			}
		}
		cache = new Object[lastIndex + 1];
		cache[0] = this;
	}
	
	public static int getX(Player player, int x) {
		return new WorldTile(x, 0, 0).getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize());
	}
	
	public static int getY(Player player, int y) {
		return new WorldTile(0, y, 0).getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize());
	}
}
