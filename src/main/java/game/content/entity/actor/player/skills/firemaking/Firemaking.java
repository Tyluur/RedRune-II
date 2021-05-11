package game.content.entity.actor.player.skills.firemaking;

import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.content.entity.actor.player.action.Action;
import game.content.entity.item.InventoryOptionsHandler;
import game.entity.actor.mask.Animation;
import game.entity.actor.npc.impl.familiar.Familiar;
import game.entity.actor.player.Player;
import game.entity.item.FloorItem;
import game.entity.item.Item;
import game.entity.object.WorldObject;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.constants.SkillConstants;
import utility.functions.Misc;

public class Firemaking extends Action {
	
	private final Fire fire;
	
	public Firemaking(Fire fire) {
		this.fire = fire;
	}
	
	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getPackets().sendMessage("You attempt to light the logs.", true);
		player.getInventory().deleteItem(fire.getLogId(), 1);
		RegionManager.addGroundItem(new Item(fire.getLogId(), 1), new WorldTile(player), player, false, 180, true);
		Long time = (Long) player.getTemporaryAttributes().remove("Fire");
		boolean quickFire = time != null && time > Misc.currentTimeMillis();
		setActionDelay(player, quickFire ? 1 : Misc.getRandom(5) + 4);
		if (!quickFire) {
			player.setNextAnimation(new Animation(733));
		}
		player.getPackets().sendSound(2599, 0, 1);
		return true;
	}
	
	public boolean checkAll(Player player) {
		if (!player.getInventory().containsItem(590, 1)) {
			player.getPackets().sendMessage("You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.FIREMAKING) < fire.getLevel()) {
			player.getPackets().sendMessage("You do not have the required level to light this.");
			return false;
		}
		if (!RegionManager.canMoveNPC(player.getPlane(), player.getX(), player.getY(), 1) || RegionManager.getRegion(player.getRegionId()).getSpawnedObject(player) != null) {
			player.getPackets().sendMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}
	
	@Override
	public int processWithDelay(final Player player) {
		final WorldTile tile = new WorldTile(player);
		if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1)) {
			if (!player.addWalkSteps(player.getX() + 1, player.getY(), 1)) {
				if (!player.addWalkSteps(player.getX(), player.getY() + 1, 1)) {
					player.addWalkSteps(player.getX(), player.getY() - 1, 1);
				}
			}
		}
		player.getPackets().sendMessage("The fire catches and the logs begin to burn.", true);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				final FloorItem item = RegionManager.getRegion(tile.getRegionId()).getGroundItem(fire.getLogId(), tile, player);
				if (item == null) {
					return;
				}
				if (!RegionManager.removeGroundItem(player, item, false)) {
					return;
				}
				RegionManager.spawnTempGroundObject(new WorldObject(fire.getFireId(), 10, 0, tile.getX(), tile.getY(), tile.getPlane()), 592, fire.getLife());
				player.getPackets().sendSound(2594, 0, 1);
				player.getSkills().addXp(SkillConstants.FIREMAKING, fire.getExperience());
				player.setNextFaceWorldTile(tile);
			}
		}, 1);
		player.getTemporaryAttributes().put("Fire", Misc.currentTimeMillis() + 1800);
		return -1;
	}
	
	@Override
	public void stop(Player player) {
	
	}
	
	public static boolean isFiremaking(Player player, Item item1, Item item2) {
		Item log = InventoryOptionsHandler.contains(590, item1, item2);
		if (log == null) {
			return false;
		}
		return isFiremaking(player, log.getId());
	}
	
	public static boolean isFiremaking(Player player, int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				player.getActionManager().setAction(new Firemaking(fire));
				return true;
			}
		}
		return false;
		
	}
	
	public static void startFamiliarFire(Player player, Familiar familiar, Fire fire) {
		if (player.getFamiliar().getId() == 7378 || player.getFamiliar().getId() == 7377) {
		}
	}
	
	public enum Fire {
		NORMAL(1511, 1, 30, 2732, 40, 20),
		ACHEY(2862, 1, 30, 2732, 40, 1),
		OAK(1521, 15, 45, 2732, 60, 1),
		WILLOW(1519, 30, 45, 2732, 90, 1),
		TEAK(6333, 35, 45, 2732, 105, 1),
		ARCTIC_PINE(10810, 42, 50, 2732, 125, 1),
		MAPLE(1517, 45, 50, 2732, 135, 1),
		MAHOGANY(6332, 50, 70, 2732, 157.5, 1),
		EUCALYPTUS(12581, 58, 70, 2732, 193.5, 1),
		YEW(1515, 60, 80, 2732, 202.5, 1),
		MAGIC(1513, 75, 90, 2732, 303.8, 1),
		CURSED_MAGIC(13567, 82, 100, 2732, 303.8, 1);
		
		private final int logId;
		
		private final int level;
		
		private final int life;
		
		private final int fireId;
		
		private final int time;
		
		private final double xp;
		
		Fire(int logId, int level, int life, int fireId, double xp, int time) {
			this.logId = logId;
			this.level = level;
			this.life = life;
			this.fireId = fireId;
			this.xp = xp;
			this.time = time;
		}
		
		public int getLogId() {
			return logId;
		}
		
		public int getLevel() {
			return level;
		}
		
		public int getLife() {
			return (life * 600);
		}
		
		public int getFireId() {
			return fireId;
		}
		
		public double getExperience() {
			return xp;
		}
		
		public int getTime() {
			return time;
		}
	}
	
}
