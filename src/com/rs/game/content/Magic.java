package com.rs.game.content;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.content.action.impl.HomeTeleportAction;
import com.rs.game.content.controller.impl.activity.Wilderness;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.world.World;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;

import java.util.concurrent.TimeUnit;

/*
 * content package used for static stuff
 */
public class Magic {
	
	public static final int MAGIC_TELEPORT = 0, ITEM_TELEPORT = 1, OBJECT_TELEPORT = 2;
	
	@SuppressWarnings("unused")
	private static final int AIR_RUNE = 556, WATER_RUNE = 555, EARTH_RUNE = 557, FIRE_RUNE = 554, MIND_RUNE = 558, NATURE_RUNE = 561, CHAOS_RUNE = 562, DEATH_RUNE = 560, BLOOD_RUNE = 565, SOUL_RUNE = 566, ASTRAL_RUNE = 9075, LAW_RUNE = 563, STEAM_RUNE = 4694, MIST_RUNE = 4695, DUST_RUNE = 4696, SMOKE_RUNE = 4697, MUD_RUNE = 4698, LAVA_RUNE = 4699, ARMADYL_RUNE = 21773, BANANA = 1963;
	
	private final static WorldTile[] TABS = { new WorldTile(3217, 3426, 0), new WorldTile(3222, 3218, 0), new WorldTile(2965, 3379, 0), new WorldTile(2758, 3478, 0), new WorldTile(2660, 3306, 0) };
	
	private Magic() {
	
	}
	
	public static final boolean checkCombatSpell(Player player, int spellId, int set, boolean delete) {
		switch (player.getCombatDefinitions().getSpellBook()) {
			case 193:
				switch (spellId) {
					case 28:
						if (!checkSpellRequirements(player, 50, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1)) {
							return false;
						}
						break;
					case 32:
						if (!checkSpellRequirements(player, 52, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 1)) {
							return false;
						}
						break;
					case 24:
						if (!checkSpellRequirements(player, 56, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 20:
						if (!checkSpellRequirements(player, 58, delete, CHAOS_RUNE, 2, DEATH_RUNE, 2, WATER_RUNE, 2)) {
							return false;
						}
						break;
					case 30:
						if (!checkSpellRequirements(player, 62, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2)) {
							return false;
						}
						break;
					case 34:
						if (!checkSpellRequirements(player, 64, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, AIR_RUNE, 1, SOUL_RUNE, 2)) {
							return false;
						}
						break;
					case 26:
						if (!checkSpellRequirements(player, 68, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 22:
						if (!checkSpellRequirements(player, 70, delete, CHAOS_RUNE, 4, DEATH_RUNE, 2, WATER_RUNE, 4)) {
							return false;
						}
						break;
					case 29:
						if (!checkSpellRequirements(player, 74, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, FIRE_RUNE, 2, AIR_RUNE, 2)) {
							return false;
						}
						break;
					case 33:
						if (!checkSpellRequirements(player, 76, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, AIR_RUNE, 2, SOUL_RUNE, 2)) {
							return false;
						}
						break;
					case 25:
						if (!checkSpellRequirements(player, 80, delete, DEATH_RUNE, 2, BLOOD_RUNE, 4)) {
							return false;
						}
						break;
					case 21:
						if (!checkSpellRequirements(player, 82, delete, DEATH_RUNE, 2, BLOOD_RUNE, 2, WATER_RUNE, 3)) {
							return false;
						}
						break;
					case 31:
						if (!checkSpellRequirements(player, 86, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, FIRE_RUNE, 4, AIR_RUNE, 4)) {
							return false;
						}
						break;
					case 35:
						if (!checkSpellRequirements(player, 88, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, AIR_RUNE, 4, SOUL_RUNE, 3)) {
							return false;
						}
						break;
					case 27:
						if (!checkSpellRequirements(player, 92, delete, DEATH_RUNE, 4, BLOOD_RUNE, 4, SOUL_RUNE, 1)) {
							return false;
						}
						break;
					case 23:
						if (!checkSpellRequirements(player, 94, delete, DEATH_RUNE, 4, BLOOD_RUNE, 2, WATER_RUNE, 6)) {
							return false;
						}
						break;
					default:
						return false;
				}
				break;
			case 192:
				switch (spellId) {
					case 25:
						if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 28:
						if (!checkSpellRequirements(player, 5, delete, WATER_RUNE, 1, AIR_RUNE, 1, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 30:
						if (!checkSpellRequirements(player, 9, delete, EARTH_RUNE, 2, AIR_RUNE, 1, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 32:
						if (!checkSpellRequirements(player, 13, delete, FIRE_RUNE, 3, AIR_RUNE, 2, MIND_RUNE, 1)) {
							return false;
						}
						break;
					case 34: // air bolt
						if (!checkSpellRequirements(player, 17, delete, AIR_RUNE, 2, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 36:// bind
						if (!checkSpellRequirements(player, 20, delete, EARTH_RUNE, 3, WATER_RUNE, 3, NATURE_RUNE, 2)) {
							return false;
						}
						break;
					case 55: // snare
						if (!checkSpellRequirements(player, 50, delete, EARTH_RUNE, 4, WATER_RUNE, 4, NATURE_RUNE, 3)) {
							return false;
						}
						break;
					case 81:// entangle
						if (!checkSpellRequirements(player, 79, delete, EARTH_RUNE, 5, WATER_RUNE, 5, NATURE_RUNE, 4)) {
							return false;
						}
						break;
					case 39: // water bolt
						if (!checkSpellRequirements(player, 23, delete, WATER_RUNE, 2, AIR_RUNE, 2, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 42: // earth bolt
						if (!checkSpellRequirements(player, 29, delete, EARTH_RUNE, 3, AIR_RUNE, 2, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 45: // fire bolt
						if (!checkSpellRequirements(player, 35, delete, FIRE_RUNE, 4, AIR_RUNE, 3, CHAOS_RUNE, 1)) {
							return false;
						}
						break;
					case 49: // air blast
						if (!checkSpellRequirements(player, 41, delete, AIR_RUNE, 3, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 52: // water blast
						if (!checkSpellRequirements(player, 47, delete, WATER_RUNE, 3, AIR_RUNE, 3, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 58: // earth blast
						if (!checkSpellRequirements(player, 53, delete, EARTH_RUNE, 4, AIR_RUNE, 3, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 63: // fire blast
						if (!checkSpellRequirements(player, 59, delete, FIRE_RUNE, 5, AIR_RUNE, 4, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 70: // air wave
						if (!checkSpellRequirements(player, 62, delete, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 73: // water wave
						if (!checkSpellRequirements(player, 65, delete, WATER_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 77: // earth wave
						if (!checkSpellRequirements(player, 70, delete, EARTH_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 80: // fire wave
						if (!checkSpellRequirements(player, 75, delete, FIRE_RUNE, 7, AIR_RUNE, 5, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 84:
						if (!checkSpellRequirements(player, 81, delete, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 87:
						if (!checkSpellRequirements(player, 85, delete, WATER_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 89:
						if (!checkSpellRequirements(player, 85, delete, EARTH_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 66: // Sara Strike
						if (player.getEquipment().getWeaponId() != 2415) {
							player.getPackets().sendGameMessage("You need to be equipping a Saradomin staff to cast this spell.", true);
							return false;
						}
						if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 67: // Guthix Claws
						if (player.getEquipment().getWeaponId() != 2416) {
							player.getPackets().sendGameMessage("You need to be equipping a Guthix Staff or Void Mace to cast this spell.", true);
							return false;
						}
						if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 68: // Flame of Zammy
						if (player.getEquipment().getWeaponId() != 2417) {
							player.getPackets().sendGameMessage("You need to be equipping a Zamorak Staff to cast this spell.", true);
							return false;
						}
						if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 4, BLOOD_RUNE, 2)) {
							return false;
						}
						break;
					case 91:
						if (!checkSpellRequirements(player, 85, delete, FIRE_RUNE, 10, AIR_RUNE, 7, DEATH_RUNE, 1, BLOOD_RUNE, 1)) {
							return false;
						}
						break;
					case 86: // teleblock
						if (!checkSpellRequirements(player, 85, delete, CHAOS_RUNE, 1, LAW_RUNE, 1, DEATH_RUNE, 1)) {
							return false;
						}
						break;
					case 99: // Storm of Armadyl
						if (!checkSpellRequirements(player, 77, delete, ARMADYL_RUNE, 1)) {
							return false;
						}
						break;
					default:
						return false;
				}
				break;
			default:
				return false;
		}
		if (set >= 0) {
			if (set == 0) {
				player.getCombatDefinitions().setAutoCastSpell(spellId);
			} else {
				player.getTemporaryAttributtes().put("tempCastSpell", spellId);
			}
		}
		return true;
	}
	
	public static final void setCombatSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutoCastSpell() == spellId) {
			player.getCombatDefinitions().resetSpells(true);
		} else {
			checkCombatSpell(player, spellId, 0, false);
		}
		player.stopAll();
	}
	
	public static void processLunarSpell(Player player, int spellId, int packetId) {
		switch (spellId) {
			case 37:
				Long lastTimeCast = player.getAttribute("last_veng_time", -1L);
				if (player.getSkills().getLevel(PlayerSkills.MAGIC) < 94) {
					player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
					return;
				} else if (lastTimeCast != null && lastTimeCast + 30_000 > System.currentTimeMillis()) {
					player.getPackets().sendGameMessage("You must wait " + (TimeUnit.MILLISECONDS.toSeconds((lastTimeCast + 30_000) - System.currentTimeMillis())) + " more seconds to cast vengeance.");
					return;
				} else if (!checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2, EARTH_RUNE, 10)) {
					return;
				}
				player.setNextGraphics(new Graphics(726, 0, 100));
				player.setNextAnimation(new Animation(4410));
				player.putAttribute("cast_veng", true);
				player.putAttribute("last_veng_time", System.currentTimeMillis());
				player.getPackets().sendGameMessage("You cast a vengeance.");
				break;
			case 39:
				useHomeTele(player);
				break;
		}
	}
	
	public static final boolean checkRunes(Player player, boolean delete, int... runes) {
		int weaponId = player.getEquipment().getWeaponId();
		int shieldId = player.getEquipment().getShieldId();
		int runesCount = 0;
		while (runesCount < runes.length) {
			int runeId = runes[runesCount++];
			int ammount = runes[runesCount++];
			if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
				continue;
			}
			if (hasStaffOfLight(weaponId) && Misc.getRandom(8) == 0 && runeId != 21773)// 1 in eight chance of keeping runes
			{
				continue;
			}
			if (!player.getInventory().containsItem(runeId, ammount)) {
				player.getPackets().sendGameMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(runeId).getName().replace("rune", "Rune") + "s to cast this spell.");
				return false;
			}
		}
		if (delete) {
			runesCount = 0;
			while (runesCount < runes.length) {
				int runeId = runes[runesCount++];
				int ammount = runes[runesCount++];
				if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
					continue;
				}
				player.getInventory().deleteItem(runeId, ammount);
			}
		}
		return true;
	}
	
	private static void useHomeTele(Player player) {
		player.getActionManager().setAction(new HomeTeleportAction());
	}
	
	public static final boolean hasInfiniteRunes(int runeId, int weaponId, int shieldId) {
		if (runeId == AIR_RUNE) {
			if (weaponId == 1381 || weaponId == 21777) // air staff
			{
				return true;
			}
		} else if (runeId == WATER_RUNE) {
			if (weaponId == 1383 || shieldId == 18346) // water staff
			{
				return true;
			}
		} else if (runeId == EARTH_RUNE) {
			if (weaponId == 1385) // earth staff
			{
				return true;
			}
		} else if (runeId == FIRE_RUNE) {
			if (weaponId == 1387) // fire staff
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasStaffOfLight(int weaponId) {
		return weaponId == 15486 || weaponId == 22207 || weaponId == 22209 || weaponId == 22211 || weaponId == 22213;
	}
	
	public static final void processAncientSpell(Player player, int spellId, int packetId) {
		switch (spellId) {
			case 28:
			case 32:
			case 24:
			case 20:
			case 30:
			case 34:
			case 26:
			case 22:
			case 29:
			case 33:
			case 25:
			case 21:
			case 31:
			case 35:
			case 27:
			case 23:
				setCombatSpell(player, spellId);
				break;
			case 40:
				sendAncientTeleportSpell(player, 54, 64, new WorldTile(3099, 9882, 0), LAW_RUNE, 2, FIRE_RUNE, 1, AIR_RUNE, 1);
				break;
			case 41:
				sendAncientTeleportSpell(player, 60, 70, new WorldTile(3222, 3336, 0), LAW_RUNE, 2, SOUL_RUNE, 1);
				break;
			case 42:
				sendAncientTeleportSpell(player, 66, 76, new WorldTile(3492, 3471, 0), LAW_RUNE, 2, BLOOD_RUNE, 1);
				
				break;
			case 43:
				sendAncientTeleportSpell(player, 72, 82, new WorldTile(3006, 3471, 0), LAW_RUNE, 2, WATER_RUNE, 4);
				break;
			case 44:
				sendAncientTeleportSpell(player, 78, 88, new WorldTile(2990, 3696, 0), LAW_RUNE, 2, FIRE_RUNE, 3, AIR_RUNE, 2);
				break;
			case 45:
				sendAncientTeleportSpell(player, 84, 94, new WorldTile(3217, 3677, 0), LAW_RUNE, 2, SOUL_RUNE, 2);
				break;
			case 46:
				sendAncientTeleportSpell(player, 90, 100, new WorldTile(3288, 3886, 0), LAW_RUNE, 2, BLOOD_RUNE, 2);
				break;
			case 47:
				sendAncientTeleportSpell(player, 96, 106, new WorldTile(2977, 3873, 0), LAW_RUNE, 2, WATER_RUNE, 8);
				break;
			case 48:
				useHomeTele(player);
				break;
		}
	}
	
	public static final void processNormalSpell(Player player, int spellId, int packetId) {
		switch (spellId) {
			case 25: // air strike
			case 28: // water strike
			case 30: // earth strike
			case 32: // fire strike
			case 34: // air bolt
			case 39: // water bolt
			case 42: // earth bolt
			case 45: // fire bolt
			case 49: // air blast
			case 52: // water blast
			case 58: // earth blast
			case 63: // fire blast
			case 70: // air wave
			case 73: // water wave
			case 77: // earth wave
			case 80: // fire wave
			case 99:
			case 84:
			case 87:
			case 89:
			case 91:
			case 36:
			case 55:
			case 81:
			case 66:
			case 67:
			case 68:
				setCombatSpell(player, spellId);
				break;
			case 27: // crossbow bolt enchant
				if (player.getSkills().getLevel(PlayerSkills.MAGIC) < 4) {
					player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
					return;
				}
				player.stopAll();
				player.getInterfaceManager().sendInterface(432);
				break;
			case 24:
				useHomeTele(player);
				break;
			case 37: // citadel
				
				sendNormalTeleportSpell(player, 10, 15, new WorldTile(5888, 4621, 0), WATER_RUNE, 1, AIR_RUNE, 1, LAW_RUNE, 1);
				
				break;
			case 40: // Minigame/varrock
				
				sendNormalTeleportSpell(player, 25, 30, new WorldTile(3212, 3429, 0), FIRE_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
				
				// player.getDialogueManager().startDialogue("TeleportMinigame");
				break;
			case 43: // Training/lumb
				
				sendNormalTeleportSpell(player, 31, 36, new WorldTile(3223, 3218, 0), EARTH_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
				
				// player.getDialogueManager().startDialogue("TeleportTraining");
				break;
			case 46: // Boss/fala
				
				sendNormalTeleportSpell(player, 37, 41, new WorldTile(2964, 3381, 0), WATER_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
				player.getPackets().sendGameMessage("Your Magic level is not high enough to cast this teleport.");
				
				// player.getDialogueManager().startDialogue("TeleportBosses");
				break;
			case 51: // camelot
				
				sendNormalTeleportSpell(player, 45, 49, new WorldTile(2757, 3478, 0), AIR_RUNE, 5, LAW_RUNE, 1);
				
				break;
			case 57: // ardy
				
				sendNormalTeleportSpell(player, 51, 55, new WorldTile(2664, 3305, 0), WATER_RUNE, 2, LAW_RUNE, 2);
				
				break;
			case 62: // watch
				
				sendNormalTeleportSpell(player, 58, 64, new WorldTile(2547, 3113, 2), EARTH_RUNE, 2, LAW_RUNE, 2);
				
				break;
			case 69: // troll
				
				sendNormalTeleportSpell(player, 61, 65, new WorldTile(2888, 3674, 0), FIRE_RUNE, 2, LAW_RUNE, 2);
				
				break;
			case 72: // ape
				
				sendNormalTeleportSpell(player, 64, 68, new WorldTile(2804, 2786, 0), FIRE_RUNE, 2, WATER_RUNE, 2, LAW_RUNE, 2, BANANA, 1);
				
				break;
		}
	}
	
	public static final boolean checkSpellRequirements(Player player, int level, boolean delete, int... runes) {
		if (player.getSkills().getLevelForXp(PlayerSkills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}
	
	public static final boolean checkSpellRequirements2(Player player, int level, boolean delete, int... runes) {
		if (player.getSkills().getLevelForXp(PlayerSkills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		return checkRunes(player, delete, runes);
	}
	
	public static final void sendAncientTeleportSpell(Player player, int level, double xp, WorldTile tile, int... runes) {
		sendTeleportSpell(player, 1979, -1, 1681, -1, level, xp, tile, 5, true, MAGIC_TELEPORT, runes);
	}
	
	public static final void sendNormalTeleportSpell(Player player, int level, double xp, WorldTile tile, int... runes) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3, true, MAGIC_TELEPORT, runes);
	}
	
	public static final boolean sendItemTeleportSpell(Player player, boolean randomize, int upEmoteId, int upGraphicId, int delay, WorldTile tile) {
		return sendTeleportSpell(player, upEmoteId, -2, upGraphicId, -1, 0, 0, tile, delay, randomize, ITEM_TELEPORT);
	}
	
	public static final boolean sendTeleportSpell(final Player player, int upEmoteId, final int downEmoteId, int upGraphicId, final int downGraphicId, int level, final double xp, final WorldTile tile, int delay, final boolean randomize, final int teleType, int... runes) {// TODO, fix wilderness
		long currentTime = Misc.currentTimeMillis();
		if (player.getLockDelay() > currentTime) {
			return false;
		}
		if (player.getSkills().getLevel(PlayerSkills.MAGIC) < level) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!checkRunes(player, false, runes)) {
			return false;
		}
		if (teleType == MAGIC_TELEPORT) {
			if (!player.getControllerManager().processMagicTeleport(tile)) {
				return false;
			}
		} else if (teleType == ITEM_TELEPORT) {
			if (!player.getControllerManager().processItemTeleport(tile)) {
				return false;
			}
		} else if (teleType == OBJECT_TELEPORT) {
			if (!player.getControllerManager().processObjectTeleport(tile)) {
				return false;
			}
		}
		checkRunes(player, true, runes);
		player.stopAll();
		if (upEmoteId != -1) {
			player.setNextAnimation(new Animation(upEmoteId));
		}
		if (upGraphicId != -1) {
			player.setNextGraphics(new Graphics(upGraphicId));
		}
		if (teleType == MAGIC_TELEPORT) {
			player.getPackets().sendSound(5527, 0, 2);
		}
		player.addLockDelay(3 + delay);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				WorldTile teleTile = tile;
				if (randomize) {
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new WorldTile(tile, 2);
						if (World.canMoveNPC(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize())) {
							break;
						}
						teleTile = tile;
					}
				}
				player.setNextWorldTile(teleTile);
				player.getControllerManager().magicTeleported(teleType);
				if (player.getControllerManager().getController() == null) {
					teleControllersCheck(player, teleTile);
				}
				if (xp != 0) {
					player.getSkills().addXp(PlayerSkills.MAGIC, xp);
				}
				if (downEmoteId != -1) {
					player.setNextAnimation(new Animation(downEmoteId == -2 ? -1 : downEmoteId));
				}
				if (downGraphicId != -1) {
					player.setNextGraphics(new Graphics(downGraphicId));
				}
				if (teleType == MAGIC_TELEPORT) {
					player.getPackets().sendSound(5524, 0, 2);
					player.setNextFaceWorldTile(new WorldTile(teleTile.getX(), teleTile.getY() - 1, teleTile.getPlane()));
					player.setDirection(6);
				}
			}
		}, delay);
		return true;
	}
	
	public static void teleControllersCheck(Player player, WorldTile teleTile) {
		if (Wilderness.isAtWild(teleTile)) {
			player.getControllerManager().startController("Wilderness");
		}
	}
	
	public static void pushLeverTeleport(final Player player, final WorldTile tile) {
		if (!player.getControllerManager().processObjectTeleport(tile)) {
			return;
		}
		player.setNextAnimation(new Animation(2140));
		player.setInfiniteStopDelay();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.resetLockDelay();
				Magic.sendObjectTeleportSpell(player, false, tile);
			}
		}, 1);
	}
	
	public static final void sendObjectTeleportSpell(Player player, boolean randomize, WorldTile tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, 3, randomize, OBJECT_TELEPORT);
	}
	
	public static final void sendDelayedObjectTeleportSpell(Player player, int delay, boolean randomize, WorldTile tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, delay, randomize, OBJECT_TELEPORT);
	}
	
	public static boolean useTabTeleport(final Player player, final int itemId) {
		if (itemId < 8007 || itemId > 8007 + TABS.length - 1) {
			return false;
		}
		if (useTeleTab(player, TABS[itemId - 8007])) {
			player.getInventory().deleteItem(itemId, 1);
		}
		return true;
	}
	
	public static boolean useTeleTab(final Player player, final WorldTile tile) {
		if (!player.getControllerManager().processItemTeleport(tile)) {
			return false;
		}
		player.setInfiniteStopDelay();
		player.setNextAnimation(new Animation(9597));
		player.setNextGraphics(new Graphics(1680));
		WorldTasksManager.schedule(new WorldTask() {
			boolean teled;
			
			@Override
			public void run() {
				if (!teled) {
					player.setNextAnimation(new Animation(4731));
					teled = true;
				} else {
					WorldTile teleTile = tile;
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new WorldTile(tile, 2);
						if (World.canMoveNPC(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize())) {
							break;
						}
						teleTile = tile;
					}
					player.setNextWorldTile(teleTile);
					player.getControllerManager().magicTeleported(ITEM_TELEPORT);
					if (player.getControllerManager().getController() == null) {
						teleControllersCheck(player, teleTile);
					}
					player.setNextFaceWorldTile(new WorldTile(teleTile.getX(), teleTile.getY() - 1, teleTile.getPlane()));
					player.setDirection(6);
					player.setNextAnimation(new Animation(-1));
					player.resetLockDelay();
					stop();
				}
			}
		}, 2, 1);
		return true;
	}
}
