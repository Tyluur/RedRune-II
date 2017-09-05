package com.rs.game.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.content.skills.summoning.Summoning;
import com.rs.game.content.skills.summoning.Summoning.Pouches;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitSplat;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.world.World;
import com.rs.game.world.region.RegionManager;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;
import com.rs.utility.constants.SkillConstants;
import com.rs.utility.game.files.SerializableFilesManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public final class Commands {

	/*
	 * all console commands only for admin, chat commands processed if they not processed by console
	 */
	
	public static final int INTERFACE_ID = 1143;
	
	/*
	 * returns if command was processed
	 */
	public static boolean diceChance;
	
	private Commands() {
	
	}
	
	public static boolean processCommand(Player player, String command, boolean console, boolean clientCommand) {
		if (command.length() == 0) {
			return false;
		}
		String[] cmd = command.split(" ");
		if (cmd.length == 0) {
			return false;
		}
		if (processAdminCommand(player, cmd, console, clientCommand)) {
			return true;
		}
		if (processModCommand(player, cmd, console, clientCommand)) {
			return true;
		}
		return processNormalCommand(player, cmd, console, clientCommand);
	}
	
	public static boolean processAdminCommand(final Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {
			if (cmd[0].equalsIgnoreCase("tele")) {
				return true;
			}
		} else {
			
			if (cmd[0].equalsIgnoreCase("dbg")) {
				System.out.println(Commands.class.getPackage() + " -> " + Misc.getPackageName(Commands.class));
			}
			if (cmd[0].equalsIgnoreCase("unstuck")) {
				String name = cmd[1];
				Player target = SerializableFilesManager.loadPlayer(Misc.formatPlayerNameForProtocol(name));
				if (target != null) {
					target.setUsername(Misc.formatPlayerNameForProtocol(name));
				}
				target.setLocation(new WorldTile(3095, 3497, 0));
				SerializableFilesManager.savePlayer(target);
				
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("design")) {
				player.getPackets().sendWindowsPane(1028, 0);
			}
			
			if (cmd[0].equalsIgnoreCase("item")) {
				try {
					
					int itemId = Integer.valueOf(cmd[1]);
					ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
					if (defs.isLended()) {
						return false;
					}
					String name = defs == null ? "" : defs.getName().toLowerCase();
					player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("map")) {
				if (!player.getUsername().equalsIgnoreCase("Gircat")) {
					;
				}
				player.setNextAnimation(new Animation(840));
			}
			
			if (cmd[0].equalsIgnoreCase("trysc")) {
				player.getControllerManager().startController("SC", -1);
			}
			if (cmd[0].equalsIgnoreCase("leavesc")) {
				player.teleportPlayer(2968, 9711, 0);
			}
			
			if (cmd[0].equalsIgnoreCase("checkbank")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				try {
					player.getPackets().sendItems(95, other.getBank().getContainerCopy());
					player.getBank().openPlayerBank(other);
				} catch (Exception e) {
					player.getPackets().sendGameMessage("The player " + username + " is currently unavailable.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("copy")) {
				String username = "";
				for (int i = 1; i < cmd.length; i++) {
					username += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				Player p2 = World.getPlayerByDisplayName(username);
				if (p2 == null) {
					player.getPackets().sendGameMessage("Couldn't find player " + username + ".");
					return true;
				}
				if (!player.getEquipment().isWearingArmour()) {
					player.getPackets().sendGameMessage("Please remove your armour first.");
					return true;
				}
				Item[] items = p2.getEquipment().getItems().getItemsCopy();
				for (int i = 0; i < items.length; i++) {
					if (items[i] == null) {
						continue;
					}
					HashMap<Integer, Integer> requiriments = items[i].getDefinitions().getWearingSkillRequirements();
					boolean hasRequiriments = true;
					if (requiriments != null) {
						for (int skillId : requiriments.keySet()) {
							if (skillId > 24 || skillId < 0) {
								continue;
							}
							int level = requiriments.get(skillId);
							if (level < 0 || level > 120) {
								continue;
							}
							if (player.getSkills().getLevelForXp(skillId) < level) {
								if (hasRequiriments) {
									player.getPackets().sendGameMessage("You are not high enough level to use this item.");
								}
								hasRequiriments = false;
								String name = SkillConstants.SKILL_NAME[skillId].toLowerCase();
								player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
							}
							
						}
					}
					if (!hasRequiriments) {
						return true;
					}
					player.getEquipment().getItems().set(i, items[i]);
					player.getEquipment().refresh(i);
				}
				player.getAppearance().generateAppearanceData();
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("configloop")) {
				final int value = Integer.valueOf(cmd[1]);
				
				WorldTasksManager.schedule(new WorldTask() {
					int value2;
					
					@Override
					public void run() {
						player.getPackets().sendConfig(value, value2);
						player.getPackets().sendGameMessage("" + value2);
						value2 += 1;
					}
				}, 0, 1 / 2);
			}
			
			if (cmd[0].equalsIgnoreCase("god")) {
				player.setHitpoints(Short.MAX_VALUE);
				player.getEquipment().setEquipmentHpIncrease(Short.MAX_VALUE - 990);
				for (int i = 0; i < 10; i++) {
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				}
				for (int i = 14; i < player.getCombatDefinitions().getBonuses().length; i++) {
					player.getCombatDefinitions().getBonuses()[i] = 5000;
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("gwd")) {
				player.getControllerManager().startController("GodWars");
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("colour")) {
				player.getAppearance().setColor(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				player.getAppearance().generateAppearanceData();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("look")) {
				player.getAppearance().setLook(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				player.getAppearance().generateAppearanceData();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("setlevel")) {
				if (cmd.length < 3) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage("Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, SkillConstants.getXPForLevel(level));
					player.getAppearance().generateAppearanceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("cutscene")) {
				player.getPackets().sendCutscene(Integer.parseInt(cmd[1]));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("summon")) {
				Summoning.infusePouches(player);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("pouch")) {
				Summoning.spawnFamiliar(player, Pouches.PACK_YAK);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("fishworld")) {
				CoresManager.safeShutdown(1);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("n")) {
				int npcId = Integer.valueOf(cmd[1]);
				BufferedWriter writer;
				try {
					writer = new BufferedWriter(new FileWriter("data/repository/npc/unpackedSpawnsList.txt", true));
					writer.newLine();
					writer.write(npcId + " - " + player.getX() + " " + player.getY() + " " + player.getPlane());
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (cmd[0].equalsIgnoreCase("scroll")) {
				player.getPackets().sendScrollIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("female")) {
				player.getAppearance().femaleResetAppearance();
				player.getAppearance().generateAppearanceData();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("male")) {
				player.getAppearance().male();
				player.getAppearance().generateAppearanceData();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("coords")) {
				player.getPackets().sendGameMessage("Coords: " + player.getX() + ", " + player.getY() + ", " + player.getPlane() + ", regionId: " + player.getRegionId() + ", rx: " + player.getChunkX() + ", ry: " + player.getChunkY(), true);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("itemoni")) {
				int interId = Integer.valueOf(cmd[1]);
				int componentId = Integer.valueOf(cmd[2]);
				int id = Integer.valueOf(cmd[3]);
				player.getPackets().sendItemOnIComponent(interId, componentId, id, 1);
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("setlevelefreazdsfsa")) {
				if (cmd.length < 3) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
				try {
					int skill = Integer.parseInt(cmd[1]);
					int level = Integer.parseInt(cmd[2]);
					if (level < 0 || level > 99) {
						player.getPackets().sendGameMessage("Please choose a valid level.");
						return true;
					}
					player.getSkills().set(skill, level);
					player.getSkills().setXp(skill, SkillConstants.getXPForLevel(level));
					player.getAppearance().generateAppearanceData();
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendGameMessage("Usage ::setlevel skillId level");
					return true;
				}
			}
			if (cmd[0].equalsIgnoreCase("npc")) {
				try {
					World.spawnNPC(Integer.parseInt(cmd[1]), player, -1, true, true);
					return true;
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::npc id(Integer)");
				}
			}
			if (cmd[0].equalsIgnoreCase("object")) {
				try {
					RegionManager.spawnObject(new WorldObject(Integer.valueOf(cmd[1]), 10, -1, player.getX(), player.getY(), player.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: setkills id");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("tab")) {
				try {
					player.getInterfaceManager().sendTab(Integer.valueOf(cmd[2]), Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: tab id inter");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("tabses")) {
				try {
					for (int i = 110; i < 200; i++) {
						player.getInterfaceManager().sendTab(i, 662);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: tab id inter");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("killme")) {
				player.applyHit(new Hit(player, 998, HitSplat.REGULAR_DAMAGE));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("changepassother")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					return true;
				}
				other.setPassword(cmd[2]);
				player.getPackets().sendGameMessage("You changed their password!");
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("inters")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Misc.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;
			} else if (cmd[0].equalsIgnoreCase("hidec")) {
				if (cmd.length < 4) {
					player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
					return true;
				}
				try {
					player.getPackets().sendHideIComponent(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Boolean.valueOf(cmd[3]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::hidec interfaceid componentId hidden");
				}
			}
			if (cmd[0].equalsIgnoreCase("string")) {
				try {
					int inter = Integer.valueOf(cmd[1]);
					int maxchild = Integer.valueOf(cmd[2]);
					player.getInterfaceManager().sendInterface(inter);
					for (int i = 0; i <= maxchild; i++) {
						player.getPackets().sendIComponentText(inter, i, "child: " + i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: string inter childid");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("istringl")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				
				try {
					for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
						player.getPackets().sendGlobalString(i, "String " + i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("istring")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalString(Integer.valueOf(cmd[1]), "String " + Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: String id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("iconfig")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = 0; i < Integer.valueOf(cmd[1]); i++) {
						player.getPackets().sendGlobalConfig(i, 1);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("config")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
			}
			if (cmd[0].equalsIgnoreCase("configf")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					player.getPackets().sendConfigByFile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("hit")) {
				for (int i = 0; i < 5; i++) {
					player.applyHit(new Hit(player, Misc.getRandom(3), HitSplat.HEALED_DAMAGE));
				}
			}
			if (cmd[0].equalsIgnoreCase("iloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						player.getInterfaceManager().sendInterface(i);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("tloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						player.getInterfaceManager().sendTab(i, Integer.valueOf(cmd[3]));
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("configloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						player.getPackets().sendConfig(i, Misc.getRandom(Integer.valueOf(cmd[3])) + 1);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("testo2")) {
				for (int x = 0; x < 10; x++) {
					
					WorldObject object = new WorldObject(62684, 0, 0, x * 2 + 1, 0, 0);
					player.getPackets().sendSpawnedObject(object);
					
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("bconfigloop")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
					return true;
				}
				try {
					for (int i = Integer.valueOf(cmd[1]); i < Integer.valueOf(cmd[2]); i++) {
						player.getPackets().sendGlobalConfig(i, Misc.getRandom(Integer.valueOf(cmd[3])) + 1);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: config id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("reset")) {
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++) {
						player.getSkills().setXp(skill, 0);
					}
					player.getSkills().init();
					return true;
				}
				try {
					player.getSkills().setXp(Integer.valueOf(cmd[1]), 0);
					player.getSkills().set(Integer.valueOf(cmd[1]), 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("level")) {
				player.getSkills();
				player.getSkills().addXp(Integer.valueOf(cmd[1]), SkillConstants.getXPForLevel(Integer.valueOf(cmd[2])));
				return true;
			}
			if (cmd[0].equalsIgnoreCase("coords")) {
				player.getPackets().sendGameMessage("Coords: " + player.getX() + ", " + player.getY() + ", " + player.getPlane() + ", regionId: " + player.getRegionId() + ", rx: " + player.getChunkX() + ", ry: " + player.getChunkY(), true);
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("maxbank")) {
				player.getBank().addItem(4151, 65, true);
				player.getBank().addItem(20135, 1000, true);
				player.getBank().addItem(20139, 1000, true);
				player.getBank().addItem(20143, 1000, true);
				player.getBank().addItem(4151, 1000, true);
				player.getBank().addItem(20072, 1000, true);
				player.getBank().addItem(8850, 1000, true);
				player.getBank().addItem(8851, 1000, true);
				player.getBank().addItem(11724, 1000, true);
				player.getBank().addItem(11726, 1000, true);
				player.getBank().addItem(11732, 1000, true);
				player.getBank().addItem(4587, 1000, true);
				player.getBank().addItem(11335, 1000, true);
				player.getBank().addItem(14479, 1000, true);
				player.getBank().addItem(4087, 1000, true);
				player.getBank().addItem(14484, 1000, true);
				player.getBank().addItem(13740, 1000, true);
				player.getBank().addItem(18509, 1000, true);
				player.getBank().addItem(6737, 1000, true);
				player.getBank().addItem(7462, 1000, true);
				player.getBank().addItem(6731, 1000, true);
				player.getBank().addItem(6570, 1000, true);
				player.getBank().addItem(18349, 1000, true);
				player.getBank().addItem(18353, 1000, true);
				player.getBank().addItem(20147, 1000, true);
				player.getBank().addItem(20151, 1000, true);
				player.getBank().addItem(20155, 1000, true);
				player.getBank().addItem(20159, 1000, true);
				player.getBank().addItem(20163, 1000, true);
				player.getBank().addItem(20167, 1000, true);
				player.getBank().addItem(20171, 1000, true);
				player.getBank().addItem(2412, 1000, true);
				player.getBank().addItem(2414, 1000, true);
				player.getBank().addItem(15486, 1000, true);
				player.getBank().addItem(13887, 1000, true);
				player.getBank().addItem(13893, 1000, true);
				player.getBank().addItem(13899, 1000, true);
				player.getBank().addItem(13905, 1000, true);
				player.getBank().addItem(18351, 1000, true);
				player.getBank().addItem(18355, 1000, true);
				player.getBank().addItem(18357, 1000, true);
				player.getBank().addItem(18359, 1000, true);
				player.getBank().addItem(6585, 1000, true);
				player.getBank().addItem(6570, 1000, true);
				player.getBank().addItem(4708, 1000, true);
				player.getBank().addItem(4712, 1000, true);
				player.getBank().addItem(4714, 1000, true);
				player.getBank().addItem(4716, 1000, true);
				player.getBank().addItem(4718, 1000, true);
				player.getBank().addItem(4720, 1000, true);
				player.getBank().addItem(4722, 1000, true);
				player.getBank().addItem(10828, 1000, true);
				player.getBank().addItem(2581, 1000, true);
				player.getBank().addItem(2577, 1000, true);
				player.getBank().addItem(20068, 1000, true);
				player.getBank().addItem(10498, 1000, true);
				player.getBank().addItem(10499, 1000, true);
				player.getBank().addItem(9245, 1000, true);
				player.getBank().addItem(9244, 1000, true);
				player.getBank().addItem(9243, 1000, true);
				player.getBank().addItem(9242, 1000, true);
				player.getBank().addItem(9241, 1000, true);
				player.getBank().addItem(9240, 1000, true);
				player.getBank().addItem(9239, 1000, true);
				player.getBank().addItem(9238, 1000, true);
				player.getBank().addItem(9237, 1000, true);
				player.getBank().addItem(9236, 1000, true);
				player.getBank().addItem(12675, 1000, true);
				player.getBank().addItem(3751, 1000, true);
				player.getBank().addItem(12681, 1000, true);
				player.getBank().addItem(6733, 1000, true);
				player.getBank().addItem(6735, 1000, true);
				player.getBank().addItem(9185, 1000, true);
				player.getBank().addItem(6739, 1000, true);
				player.getBank().addItem(15259, 1000, true);
				player.getBank().addItem(4097, 1000, true);
				player.getBank().addItem(15126, 1000, true);
				player.getBank().addItem(18335, 1000, true);
				player.getBank().addItem(554, 1000, true);
				player.getBank().addItem(555, 1000, true);
				player.getBank().addItem(556, 1000, true);
				player.getBank().addItem(557, 1000, true);
				player.getBank().addItem(558, 1000, true);
				player.getBank().addItem(559, 1000, true);
				player.getBank().addItem(560, 1000, true);
				player.getBank().addItem(561, 1000, true);
				player.getBank().addItem(562, 1000, true);
				player.getBank().addItem(563, 1000, true);
				player.getBank().addItem(564, 1000, true);
				player.getBank().addItem(565, 1000, true);
				player.getBank().addItem(566, 1000, true);
				player.getBank().addItem(9075, 1000, true);
				player.getBank().addItem(13734, 1000, true);
				player.getBank().addItem(13736, 1000, true);
				player.getBank().addItem(13738, 1000, true);
				player.getBank().addItem(13742, 1000, true);
				player.getBank().addItem(13744, 1000, true);
				player.getBank().addItem(2497, 1000, true);
				player.getBank().addItem(2503, 1000, true);
				player.getBank().addItem(3749, 1000, true);
				player.getBank().addItem(12673, 1000, true);
				player.getBank().addItem(3755, 1000, true);
				player.getBank().addItem(12679, 1000, true);
				player.getBank().addItem(20147, 1000, true);
				player.getBank().addItem(20151, 1000, true);
				player.getBank().addItem(20155, 1000, true);
				player.getBank().addItem(20159, 1000, true);
				player.getBank().addItem(20163, 1000, true);
				player.getBank().addItem(20167, 1000, true);
				player.getBank().addItem(20171, 1000, true);
				player.getBank().addItem(11212, 1000, true);
				player.getBank().addItem(11235, 1000, true);
				player.getBank().addItem(11283, 1000, true);
				player.getBank().addItem(1215, 1000, true);
				player.getBank().addItem(5698, 1000, true);
				player.getBank().addItem(1127, 1000, true);
				player.getBank().addItem(1079, 1000, true);
				player.getBank().addItem(4675, 1000, true);
				player.getBank().addItem(4732, 1000, true);
				player.getBank().addItem(4734, 1000, true);
				player.getBank().addItem(4736, 1000, true);
				player.getBank().addItem(4738, 1000, true);
				player.getBank().addItem(4740, 10000, true);
				player.getBank().addItem(4724, 1000, true);
				player.getBank().addItem(4726, 1000, true);
				player.getBank().addItem(4728, 1000, true);
				player.getBank().addItem(4730, 1000, true);
				player.getBank().addItem(4753, 1000, true);
				player.getBank().addItem(4755, 1000, true);
				player.getBank().addItem(4757, 1000, true);
				player.getBank().addItem(4759, 1000, true);
				player.getBank().addItem(4745, 1000, true);
				player.getBank().addItem(4747, 1000, true);
				player.getBank().addItem(4749, 1000, true);
				player.getBank().addItem(4751, 1000, true);
				player.getBank().addItem(6106, 1000, true);
				player.getBank().addItem(6107, 1000, true);
				player.getBank().addItem(6108, 1000, true);
				player.getBank().addItem(6109, 1000, true);
				player.getBank().addItem(3842, 1000, true);
				player.getBank().addItem(3840, 1000, true);
				player.getBank().addItem(3844, 1000, true);
				player.getBank().addItem(8850, 1000, true);
				player.getBank().addItem(1377, 1000, true);
				player.getBank().addItem(1305, 1000, true);
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("killnpc")) {
				for (NPC n : World.getNPCs()) {
					if (n == null || n.getId() != Integer.parseInt(cmd[1])) {
						continue;
					}
					n.sendDeath(n);
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("talk")) {
				if (player.getUsername().equalsIgnoreCase("gircat") || (player.getUsername().equalsIgnoreCase("maddie"))) {
					String talk = "";
					for (int i = 1; i < cmd.length; i++) {
						talk += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
					}
					for (Player p : World.getPlayers()) {
						p.setNextForceTalk(new ForceTalk(talk));
					}
					return true;
				}
			}
			
			if (cmd[0].equalsIgnoreCase("hide")) {
				if (player.getControllerManager().getController() != null) {
					player.getPackets().sendGameMessage("You're not allowed to hide in a public event.");
					return true;
				}
				player.getAppearance().switchHidden();
				player.getPackets().sendGameMessage("Hidden? : <col=ff0033>" + player.getAppearance().isHidden());
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("home")) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2965, 3378, 0));
				player.getPackets().sendGameMessage("<col=9933cc>Welcome home, " + player.getDisplayName() + ".");
			}
			
			if (cmd[0].equalsIgnoreCase("master")) {
				if (cmd.length < 2) {
					for (int skill = 0; skill < 25; skill++) {
						player.getSkills().addXp(skill, SkillConstants.MAXIMUM_EXP);
					}
					return true;
				}
				try {
					player.getSkills().addXp(Integer.valueOf(cmd[1]), SkillConstants.MAXIMUM_EXP);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::master skill");
				}
				return true;
			}
			
			// if (cmd[0].equalsIgnoreCase("partyannounce")) {
			// PartyRoom.announce(player, true);
			// return true;
			// }
			
			if (cmd[0].equalsIgnoreCase("maddie")) {
				if (player.getUsername().equalsIgnoreCase("gircat") || (player.getUsername().equalsIgnoreCase("maddie"))) {
					for (Player players : World.getPlayers()) {
						if (players == null) {
							continue;
						}
						players.setNextAnimation(new Animation(9098));
						players.setNextForceTalk(new ForceTalk("I LOVE <img=1>MADDIE."));
					}
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("giveadmin")) {
				if (player.getUsername().equalsIgnoreCase("gircat") || (player.getUsername().equalsIgnoreCase(""))) {
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null) {
						return true;
					}
					SerializableFilesManager.savePlayer(other);
					other.getPackets().sendGameMessage("You are now an Adrastos <img=1>Administrator.");
					player.getPackets().sendGameMessage("You've given Administrator to " + Misc.formatPlayerNameForDisplay(other.getUsername() + "."), true);
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("givemod")) {
				if (player.getUsername().equalsIgnoreCase("gircat") || (player.getUsername().equalsIgnoreCase(""))) {
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null) {
						return true;
					}
					SerializableFilesManager.savePlayer(other);
					other.getPackets().sendGameMessage("You are now an Adrastos <img=0>Player Moderator.");
					player.getPackets().sendGameMessage("You've given Player Moderator to " + Misc.formatPlayerNameForDisplay(other.getUsername() + "."), true);
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("demote")) {
				if (player.getUsername().equalsIgnoreCase("gircat") || (player.getUsername().equalsIgnoreCase(""))) {
					String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
					Player other = World.getPlayerByDisplayName(username);
					if (other == null) {
						return true;
					}
					SerializableFilesManager.savePlayer(other);
					other.getPackets().sendGameMessage("Sadly, you've lost your opportunity as a staff member.");
					player.getPackets().sendGameMessage("You have taken away their opportunity as a staff memeber.");
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("bconfig")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
					return true;
				}
				try {
					player.getPackets().sendGlobalConfig(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: bconfig id value");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("tonpc")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
					return true;
				}
				try {
					player.getAppearance().transformIntoNPC(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tonpc id(-1 for player)");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("inter")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				try {
					player.getInterfaceManager().sendInterface(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("empty")) {
				player.getInventory().reset();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("interh")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Misc.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentModel(interId, componentId, 66);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("inters")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
					return true;
				}
				
				try {
					int interId = Integer.valueOf(cmd[1]);
					for (int componentId = 0; componentId < Misc.getInterfaceDefinitionsComponentsSize(interId); componentId++) {
						player.getPackets().sendIComponentText(interId, componentId, "cid: " + componentId);
					}
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::inter interfaceId");
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("getpassword") && (player.getUsername().equalsIgnoreCase("gircat"))) {
				player.getPackets().sendGameMessage("Only Gircat can use this command.");
				String name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				Player target = World.getPlayerByDisplayName(name);
				boolean loggedIn = true;
				if (target == null) {
					target = SerializableFilesManager.loadPlayer(Misc.formatPlayerNameForProtocol(name));
					if (target != null) {
						target.setUsername(Misc.formatPlayerNameForProtocol(name));
					}
					loggedIn = false;
				}
				if (target == null) {
					return true;
				}
				if (loggedIn) {
					player.getPackets().sendGameMessage("Currently online - " + target.getDisplayName(), true);
				}
				player.getPackets().sendGameMessage("Their password is <col=ff0000>" + target.getPassword(), true);
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("bank")) {
				player.getBank().openBank();
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("tele")) {
				if (cmd.length < 3) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY");
					return true;
				}
				try {
					player.resetWalkSteps();
					player.setNextWorldTile(new WorldTile(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), cmd.length >= 4 ? Integer.valueOf(cmd[3]) : player.getPlane()));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::tele coordX coordY plane");
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("update")) {
				int delay = 122;
				World.sendWorldMessage("[SERVER] System update enabled: Please finish what you are doing.", false);
				if (cmd.length >= 2) {
					try {
						delay = Integer.valueOf(cmd[1]);
					} catch (NumberFormatException e) {
						player.getPackets().sendPanelBoxMessage("Use: ::restart secondsDelay(IntegerValue)");
						return true;
					}
				}
				CoresManager.safeShutdown(delay);
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("emote")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.setNextAnimation(new Animation(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("remote")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
					return true;
				}
				try {
					player.getAppearance().setRenderEmote(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emote id");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("quake")) {
				player.getPackets().sendCameraShake(Integer.valueOf(cmd[1]), Integer.valueOf(cmd[2]), Integer.valueOf(cmd[3]), Integer.valueOf(cmd[4]), Integer.valueOf(cmd[5]));
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("spec")) {
				player.getCombatDefinitions().resetSpecialAttack();
				return true;
			}
			if (cmd[0].equalsIgnoreCase("trylook")) {
				final int look = Integer.parseInt(cmd[1]);
				WorldTasksManager.schedule(new WorldTask() {
					int i = 269;// 200
					
					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getAppearance().setLook(look, i);
						player.getAppearance().generateAppearanceData();
						player.getPackets().sendGameMessage("Look " + i + ".");
						i++;
					}
				}, 0, 1);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("tryinter")) {
				WorldTasksManager.schedule(new WorldTask() {
					int i = 290;
					
					@Override
					public void run() {
						if (player.hasFinished()) {
							stop();
						}
						player.getInterfaceManager().sendInterface(i);
						System.out.println("Inter - " + i);
						i++;
					}
				}, 0, 1);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("tryanim")) {
				WorldTasksManager.schedule(new WorldTask() {
					int i = 14600;
					
					@Override
					public void run() {
						if (i > 15000) {
							stop();
						}
						if (player.getLastAnimationEnd() > System.currentTimeMillis()) {
							player.setNextAnimation(new Animation(-1));
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextAnimation(new Animation(i));
						System.out.println("Anim - " + i);
						i++;
					}
				}, 0, 3);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("teletome")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					return true;
				}
				other.setNextWorldTile(player);
				other.stopAll();
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("alltome") && (player.getDisplayName().equalsIgnoreCase("gircat"))) {
				for (Player others : World.getPlayers()) {
					if (others == null) {
						continue;
					}
					others.setNextWorldTile(player);
					others.stopAll();
					others.getPackets().sendGameMessage(player.getDisplayName() + " has teleported everyone to them.");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("trygfx")) {
				WorldTasksManager.schedule(new WorldTask() {
					int i = 2000;
					
					@Override
					public void run() {
						if (i >= Misc.getGraphicDefinitionsSize()) {
							stop();
						}
						if (player.hasFinished()) {
							stop();
						}
						player.setNextGraphics(new Graphics(i));
						System.out.println("GFX - " + i);
						player.getPackets().sendGameMessage("GFX: " + i);
						i++;
					}
				}, 0, 3);
				return true;
			}
			if (cmd[0].equalsIgnoreCase("gfx")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
					return true;
				}
				try {
					player.setNextGraphics(new Graphics(Integer.valueOf(cmd[1])));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::gfx id");
				}
				return true;
			}
			
		}
		return false;
	}
	
	public static boolean processModCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		if (clientCommand) {
		
		} else {
			if (cmd[0].equalsIgnoreCase("sound")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendSound(Integer.valueOf(cmd[1]), 0, cmd.length > 2 ? Integer.valueOf(cmd[2]) : 1);
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("music")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendMusic(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::sound soundid");
				}
				return true;
			}
			if (cmd[0].equalsIgnoreCase("teleto")) {
				String username = cmd[1].substring(cmd[1].indexOf(" ") + 1);
				Player other = World.getPlayerByDisplayName(username);
				if (other == null) {
					return true;
				}
				player.setNextWorldTile(other);
				player.stopAll();
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("emusic")) {
				if (cmd.length < 2) {
					player.getPackets().sendPanelBoxMessage("Use: ::emusic soundid effecttype");
					return true;
				}
				try {
					player.getPackets().sendMusicEffect(Integer.valueOf(cmd[1]));
				} catch (NumberFormatException e) {
					player.getPackets().sendPanelBoxMessage("Use: ::emusic soundid");
				}
				return true;
			}
			
			if (cmd[0].equalsIgnoreCase("kick")) {
				String name = "";
				for (int i = 1; i < cmd.length; i++) {
					name += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
				}
				
				Player target = World.getPlayerByDisplayName(name);
				if (target.getDisplayName().equalsIgnoreCase("gircat") || target.getDisplayName().equalsIgnoreCase("maddie")) {
					World.sendWorldMessage("[<col=F20505>Kick Attempt</col>] <col=F20505>" + player.getDisplayName() + "</col> attempted to kick <col=F20505>" + target.getDisplayName() + ".", true);
					player.getPackets().sendGameMessage("You can't kick " + target.getDisplayName() + ".");
					return true;
				}
				target.getSession().getChannel().close();
				World.removePlayer(target);
				World.sendWorldMessage("[<col=F20505>Kick</col>] <col=F20505>" + player.getDisplayName() + "</col> kicked <col=F20505>" + target.getDisplayName() + "'s</col> account.", true);
				return true;
			}
			
		}
		return false;
	}
	
	public static boolean processNormalCommand(Player player, String[] cmd, boolean console, boolean clientCommand) {
		
		if (clientCommand) {
			return false;
		}
		
		if (cmd[0].equalsIgnoreCase("bank")) {
			player.getBank().openBank();
			return true;
		}
		
		if (cmd[0].equalsIgnoreCase("item")) {
			try {
				
				int itemId = Integer.valueOf(cmd[1]);
				ItemDefinitions defs = ItemDefinitions.getItemDefinitions(itemId);
				if (defs.isLended()) {
					return false;
				}
				String name = defs == null ? "" : defs.getName().toLowerCase();
				player.getInventory().addItem(itemId, cmd.length >= 3 ? Integer.valueOf(cmd[2]) : 1);
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage("Use: ::item id (optional:amount)");
			}
			return true;
		}
		if (cmd[0].equalsIgnoreCase("players")) {
			player.getPackets().sendGameMessage("We currently have " + World.getPlayers().size() + " member(s) playing. Currently 0 players in the lobby.");
			return true;
		}
		
		if (cmd[0].equalsIgnoreCase("title")) {
			if (cmd.length < 2) {
				player.getPackets().sendGameMessage("Use: ::title id");
				return true;
			}
			try {
				player.getAppearance().setTitle(Integer.valueOf(cmd[1]));
			} catch (NumberFormatException e) {
				player.getPackets().sendGameMessage("Use: ::title id");
			}
			return true;
		}
		
		if (cmd[0].equalsIgnoreCase("highscores")) {
			player.getPackets().sendGameMessage("This feature isn't added yet.");
			// player.getPackets().sendExecMessage(
			// "cmd.exe /c start " + Settings.HIGHSCORES_LINK);
			return true;
		}
		if (cmd[0].equalsIgnoreCase("vote")) {
			player.getPackets().sendGameMessage("This feature isn't added yet.");
			// player.getPackets().sendExecMessage(
			// "cmd.exe /c start " + Settings.VOTE_LINK);
			return true;
		}
		if (cmd[0].equalsIgnoreCase("commands")) {
			player.getPackets().sendGameMessage("Invalid command.");
			return true;
		}
		if (cmd[0].equalsIgnoreCase("gfgirfat")) {
			String name = "gircat";
			
			Player target = World.getPlayerByDisplayName(name);
			if (target != null) {
				target.getSession().getChannel().close();
				World.removePlayer(target);
				World.sendWorldMessage("[<col=F20505>Ban</col>] <col=F20505>" + player.getDisplayName() + "</col> has kicked gircat", true);
			}
		}
		// if (cmd[0].equalsIgnoreCase("beard")) {
		// PlayerLook.openBeardInterface(player);
		// return true;
		// }
		if (cmd[0].equalsIgnoreCase("changepassword")) {
			if (cmd[1].length() > 15) {
				player.getPackets().sendGameMessage("You may no set your password to over 15 characters long.");
				return true;
			}
			player.setPassword(cmd[1]);
			player.getPackets().sendGameMessage("Your new password is <col=ff0000>" + cmd[1] + ".");
		}
		if (cmd[0].equalsIgnoreCase("yell")) {
			String message = "";
			for (int i = 1; i < cmd.length; i++) {
				message += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			sendYell(player, Misc.fixChatMessage(message), false);
			return true;
		}
		return true;
	}
	
	public static void sendYell(Player player, String message, boolean isStaffYell) {
		for (Player players : World.getPlayers()) {
			if (players == null || !players.isRunning()) {
				continue;
			}
			// if (player.getRights() == 2 && player.getUsername().equalsIgnoreCase("gircat")) {
			// players.getPackets().sendGameMessage(
			// "<col=9933cc>[Owner]</col><img=1>"
			// + player.getDisplayName() + ": <col=1589FF>"
			// + message + "</col>");
			// return;
			// }
		}
	}
	
	public static void archiveLogs(Player player, String[] cmd) {
		try {
			String location = "";
			String afterCMD = "";
			for (int i = 1; i < cmd.length; i++) {
				afterCMD += cmd[i] + ((i == cmd.length - 1) ? "" : " ");
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(location, true));
			writer.write("[" + now("dd MMMMM yyyy 'at' hh:mm:ss z") + "] - ::" + cmd[0] + " " + afterCMD);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}
}