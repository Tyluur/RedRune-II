package org.redrune.game.content.entity.item;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.SkillConstants;

import java.util.HashMap;
import java.util.Map;

public class Burying {
	
	public static boolean bury(final Player player, int slotId) {
		final Item item = player.getInventory().getItem(slotId);
		if (item == null || Bone.forId(item.getId()) == null) {
			return false;
		}
		if (player.getBoneDelay() > Misc.currentTimeMillis()) {
			return true;
		}
		final Bone bone = Bone.forId(item.getId());
		final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
		player.addBoneDelay(3000);
		player.getPackets().sendSound(2738, 0, 1);
		player.setNextAnimation(new Animation(827));
		player.getPackets().sendGameMessage("You dig a hole in the ground...");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage("You bury the " + itemDef.getName().toLowerCase());
				player.getInventory().deleteItem(item.getId(), 1);
				player.getSkills().addXp(SkillConstants.PRAYER, bone.getExperience());
				stop();
			}
			
		}, 2);
		return false;
	}
	
	public enum Bone {
		NORMAL(526, 100),
		
		BURNT(528, 100),
		
		WOLF(2859, 100),
		
		MONKEY(3183, 125),
		
		BAT(530, 125),
		
		BIG(532, 200),
		
		JOGRE(3125, 200),
		
		ZOGRE(4812, 250),
		
		SHAIKAHAN(3123, 300),
		
		BABY(534, 350),
		
		WYVERN(6812, 400),
		
		DRAGON(536, 500),
		
		FAYRG(4830, 525),
		
		RAURG(4832, 550),
		
		DAGANNOTH(6729, 650),
		
		OURG(4834, 750),
		
		FROST_DRAGON(18830, 850);
		
		private static Map<Integer, Bone> bones = new HashMap<Integer, Bone>();
		
		static {
			for (Bone bone : Bone.values()) {
				bones.put(bone.getId(), bone);
			}
		}
		
		private int id;
		
		private double experience;
		
		Bone(int id, double experience) {
			this.id = id;
			this.experience = experience;
		}
		
		public static void bury(final Player player, int inventorySlot) {
			final Item item = player.getInventory().getItem(inventorySlot);
			if (item == null || Bone.forId(item.getId()) == null) {
				return;
			}
			if (player.getBoneDelay() > Misc.currentTimeMillis()) {
				return;
			}
			final Bone bone = Bone.forId(item.getId());
			final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
			player.addBoneDelay(3000);
			player.getPackets().sendSound(2738, 0, 1);
			player.setNextAnimation(new Animation(827));
			player.getPackets().sendGameMessage("You dig a hole in the ground...");
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.getPackets().sendGameMessage("You bury the " + itemDef.getName().toLowerCase());
					player.getInventory().deleteItem(item.getId(), 1);
					player.getSkills().addXp(SkillConstants.PRAYER, bone.getExperience());
					stop();
				}
				
			}, 2);
		}
		
		public static Bone forId(int id) {
			return bones.get(id);
		}
		
		public double getExperience() {
			return experience;
		}
		
		public int getId() {
			return id;
		}
	}
}
