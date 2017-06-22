package org.redrune.game.content.action.combat;

import com.google.common.base.Preconditions;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.combat.player.CombatType;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.Misc;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import static org.redrune.utility.rs.constant.BonusConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class StaticCombatFormulae {
	
	/**
	 * Gets the type of combat we're engaging in
	 *
	 * @param player
	 * 		The player
	 */
	public static CombatType getCombatType(Player player) {
		return CombatType.MELEE;
	}
	
	/**
	 * Gets the combat style, based on the weapon id and the attack style selected of the weapon.
	 *
	 * @param weaponId
	 * 		The id of the weapon equipped
	 * @param attackStyle
	 * 		The style used.
	 */
	public static int getMeleeBonusStyle(int weaponId, int attackStyle) {
		if (weaponId == -1) {
			return CRUSH_ATTACK;
		} else {
			if (weaponId == -2) {
				return CRUSH_ATTACK;
			}
			String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
			if (weaponName.contains("whip")) {
				return SLASH_ATTACK;
			}
			if (weaponName.contains("staff of light")) {
				switch (attackStyle) {
					case 0:
						return STAB_ATTACK;
					case 1:
						return SLASH_ATTACK;
					default:
						return CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("staff") || weaponName.contains("granite mace") || weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em") || weaponName.contains("tzhaar-ket-om") || weaponName.contains("maul")) {
				return CRUSH_ATTACK;
			}
			if (weaponName.contains("godsword") || weaponName.contains("greataxe") || weaponName.contains("2h sword") || weaponName.equals("saradomin sword")) {
				switch (attackStyle) {
					case 2:
						return CRUSH_ATTACK;
					default:
						return SLASH_ATTACK;
				}
			}
			if (weaponName.contains("scimitar") || weaponName.contains("hatchet") || weaponName.contains("claws") || weaponName.contains(" sword") || weaponName.contains("longsword")) {
				switch (attackStyle) {
					case 2:
						return STAB_ATTACK;
					default:
						return SLASH_ATTACK;
				}
			}
			if (weaponName.contains("mace") || weaponName.contains("anchor")) {
				switch (attackStyle) {
					case 2:
						return STAB_ATTACK;
					default:
						return CRUSH_ATTACK;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
					case 1:
						return SLASH_ATTACK;
					default:
						return STAB_ATTACK;
				}
			}
			if (weaponName.contains("spear")) {
				switch (attackStyle) {
					case 1:
						return SLASH_ATTACK;
					case 2:
						return CRUSH_ATTACK;
					default:
						return STAB_ATTACK;
				}
			}
			if (weaponName.contains("pickaxe")) {
				switch (attackStyle) {
					case 2:
						return CRUSH_ATTACK;
					default:
						return STAB_ATTACK;
				}
			}
			
			if (weaponName.contains("dagger") || weaponName.contains("rapier")) {
				switch (attackStyle) {
					case 2:
						return SLASH_ATTACK;
					default:
						return STAB_ATTACK;
				}
			}
			
		}
		switch (weaponId) {
			default:
				return CRUSH_ATTACK;
		}
	}
	
	/**
	 * Gets the defence bonus based on the attack bonus
	 *
	 * @param bonusId
	 * 		The attack bonus id
	 */
	public static int getMeleeDefenceBonus(int bonusId) {
		return bonusId == STAB_ATTACK ? STAB_DEFENCE : bonusId == STAB_DEFENCE ? SLASH_DEFENCE : CRUSH_DEFENCE;
	}
	
	/**
	 * Checks if we have full void equipped, with the possible helmet ids
	 *
	 * @param player
	 * 		The player to check on
	 * @param helmetIds
	 * 		The helmet id
	 */
	public static boolean fullVoidEquipped(Player player, int... helmetIds) {
		boolean hasDeflector = player.getEquipment().getIdInSlot(EquipConstants.SLOT_SHIELD) == 19712;
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_HANDS) != 8842) {
			if (hasDeflector) {
				hasDeflector = false;
			} else {
				return false;
			}
		}
		int legsId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_LEGS);
		boolean hasLegs = legsId != -1 && (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
		if (!hasLegs) {
			if (hasDeflector) {
				hasDeflector = false;
			} else {
				return false;
			}
		}
		int torsoId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_CHEST);
		boolean hasTorso = torsoId != -1 && (torsoId == 8839 || torsoId == 10611 || torsoId == 19785 || torsoId == 19787 || torsoId == 19789);
		if (!hasTorso) {
			if (hasDeflector) {
				hasDeflector = false;
			} else {
				return false;
			}
		}
		if (hasDeflector) {
			return true;
		}
		int helmId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_HAT);
		if (helmId == -1) {
			return false;
		}
		boolean hasHelm = false;
		for (int id : helmetIds) {
			if (helmId == id) {
				hasHelm = true;
				break;
			}
		}
		return hasHelm;
	}
	
	/**
	 * Checks if we have an armour set equipped. This uses lowercase naming.
	 * <br>
	 * Example usage:
	 * <br>armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_AMMY, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok",
	 * "dharok", "dharok");// armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_AMMY, SLOT_LEGS, SLOT_WEAPON },
	 * "dharok", "dharok", "dharok", "dharok");
	 *
	 * @param player
	 * 		The player
	 * @param slots
	 * 		The slots
	 * @param nameFlags
	 * 		The name flags, with the indexes corresponding to the slots indexes.
	 */
	public static boolean armourSetEquipped(Player player, int[] slots, String... nameFlags) {
		Preconditions.checkArgument(slots.length == nameFlags.length, "Name flags and slot length must be equal!");
		for (int slot : slots) {
			int itemInSlot = player.getEquipment().getIdInSlot(slot);
			// theres no item in the slot, so its not possible to match the name
			if (itemInSlot == -1) {
				return false;
			}
			String itemInSlotName = ItemDefinitionParser.forId(itemInSlot).getName().toLowerCase();
			String nameFlag = nameFlags[slot].toLowerCase();
			// the item in that slot's name didn't have the expected flag
			if (!itemInSlotName.contains(nameFlag)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the experience style based on the weapon and the attack style selected. -1 means shared
	 *
	 * @param weaponId
	 * 		The id of the weapon
	 * @param attackStyle
	 * 		The players attack style
	 */
	public static int getXpStyle(int weaponId, int attackStyle) {
		if (weaponId != -1 && weaponId != -2) {
			String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
			if (weaponName.contains("whip")) {
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return -1;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			}
			if (weaponName.contains("halberd")) {
				switch (attackStyle) {
					case 0:
						return -1;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			}
			if (weaponName.contains("staff")) {
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			}
			if (weaponName.contains("godsword") || weaponName.contains("sword") || weaponName.contains("2h")) {
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
						return SkillConstants.STRENGTH;
					case 3:
					default:
						return SkillConstants.DEFENCE;
				}
			}
		}
		switch (weaponId) {
			case -1:
			case -2:
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
					default:
						return SkillConstants.DEFENCE;
				}
			default:
				switch (attackStyle) {
					case 0:
						return SkillConstants.ATTACK;
					case 1:
						return SkillConstants.STRENGTH;
					case 2:
						return -1;
					case 3:
					default:
						return SkillConstants.DEFENCE;
				}
		}
	}
	
	/**
	 * Gets the attack emote for a weapon
	 *
	 * @param weaponId
	 * 		The id of the weapon
	 * @param attackStyle
	 * 		The attack style being used
	 */
	public static int getWeaponAttackEmote(int weaponId, int attackStyle) {
		if (weaponId != -1) {
			if (weaponId == -2) {
				// punch/block:14393 kick:14307 spec:14417
				switch (attackStyle) {
					case 1:
						return 14307;
					default:
						return 14393;
				}
			}
			String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
			if (!weaponName.equals("null")) {
				if (weaponName.contains("crossbow")) {
					return weaponName.contains("karil's crossbow") ? 2075 : 4230;
				}
				if (weaponName.contains("bow")) {
					return 426;
				}
				if (weaponName.contains("chinchompa")) {
					return 2779;
				}
				if (weaponName.contains("staff of light")) {
					switch (attackStyle) {
						case 0:
							return 15072;
						case 1:
							return 15071;
						case 2:
							return 414;
					}
				}
				if (weaponName.contains("staff") || weaponName.contains("wand")) {
					return 419;
				}
				if (weaponId == 6522) {
					return 2614;
				}
				if (weaponName.contains("dart")) {
					return 6600;
				}
				if (weaponName.contains("knife")) {
					return 9055;
				}
				if (weaponName.contains("scimitar") || weaponName.contains("korasi's sword")) {
					switch (attackStyle) {
						case 2:
							return 15072;
						default:
							return 15071;
					}
				}
				if (weaponName.contains("granite mace")) {
					return 400;
				}
				if (weaponName.contains("mace")) {
					switch (attackStyle) {
						case 2:
							return 400;
						default:
							return 401;
					}
				}
				if (weaponName.contains("hatchet") || weaponName.contains("battleaxe")) {
					switch (attackStyle) {
						case 2:
							return 401;
						default:
							return 395;
					}
				}
				if (weaponName.contains("warhammer")) {
					switch (attackStyle) {
						default:
							return 401;
					}
				}
				if (weaponName.contains("claws")) {
					switch (attackStyle) {
						case 2:
							return 1067;
						default:
							return 393;
					}
				}
				if (weaponName.contains("whip")) {
					switch (attackStyle) {
						case 1:
							return 11969;
						case 2:
							return 11970;
						default:
							return 11968;
					}
				}
				if (weaponName.contains("anchor")) {
					switch (attackStyle) {
						default:
							return 5865;
					}
				}
				if (weaponName.contains("tzhaar-ket-em")) {
					switch (attackStyle) {
						default:
							return 401;
					}
				}
				if (weaponId == 20084 || weaponName.contains("tzhaar-ket-om")) {
					switch (attackStyle) {
						default:
							return 13691;
					}
				}
				if (weaponName.contains("halberd")) {
					switch (attackStyle) {
						case 1:
							return 440;
						default:
							return 428;
					}
				}
				if (weaponName.contains("zamorakian spear")) {
					switch (attackStyle) {
						case 1:
							return 12005;
						case 2:
							return 12009;
						default:
							return 12006;
					}
				}
				if (weaponName.equals("training sword")) {
					switch (attackStyle) {
						case 2:
						case 3:
							return 12311;
						default:
							return 12310;
					}
				}
				if (weaponName.contains("spear")) {
					switch (attackStyle) {
						case 1:
							return 440;
						case 2:
							return 429;
						default:
							return 428;
					}
				}
				if (weaponName.contains("flail")) {
					return 2062;
				}
				if (weaponName.contains("javelin")) {
					return 10501;
				}
				if (weaponName.contains("morrigan's throwing axe")) {
					return 10504;
				}
				if (weaponName.contains("pickaxe")) {
					switch (attackStyle) {
						case 2:
							return 400;
						default:
							return 401;
					}
				}
				if (weaponName.contains("dagger")) {
					switch (attackStyle) {
						case 2:
							return 377;
						default:
							return 376;
					}
				}
				if (weaponName.contains("2h sword") || weaponName.equals("dominion sword") || weaponName.equals("thok's sword") || weaponName.equals("saradomin sword")) {
					switch (attackStyle) {
						case 2:
							return 7048;
						case 3:
							return 7049;
						default:
							return 7041;
					}
				}
				if (weaponName.contains(" sword") || weaponName.contains("saber") || weaponName.contains("longsword") || weaponName.contains("light") || weaponName.contains("excalibur")) {
					switch (attackStyle) {
						case 2:
							return 12310;
						default:
							return 12311;
					}
				}
				if (weaponName.contains("rapier") || weaponName.contains("brackish")) {
					switch (attackStyle) {
						case 2:
							return 13048;
						default:
							return 13049;
					}
				}
				if (weaponName.contains("katana")) {
					switch (attackStyle) {
						case 2:
							return 1882;
						default:
							return 1884;
					}
				}
				if (weaponName.contains("godsword")) {
					switch (attackStyle) {
						case 2:
							return 11980;
						case 3:
							return 11981;
						default:
							return 11979;
					}
				}
				if (weaponName.contains("greataxe")) {
					switch (attackStyle) {
						case 2:
							return 12003;
						default:
							return 12002;
					}
				}
				if (weaponName.contains("granite maul")) {
					switch (attackStyle) {
						default:
							return 1665;
					}
				}
				
			}
		}
		switch (weaponId) {
			case 16405:// novite maul
			case 16407:// Bathus maul
			case 16409:// Maramaros maul
			case 16411:// Kratonite maul
			case 16413:// Fractite maul
			case 16415:// Zephyrium maul
			case 16417:// Argonite maul
			case 16419:// Katagon maul
			case 16421:// Gorgonite maul
			case 16423:// Promethium maul
			case 16425:// primal maul
				return 2661; // maul
			case 18353:// chaotic maul
				return 13055;
			case 13883: // morrigan thrown axe
				return 10504;
			case 15241:
				return 12174;
			default:
				switch (attackStyle) {
					case 1:
						return 423;
					default:
						return 422;
				}
		}
	}
	
	/**
	 * Gets the defence emote of a player
	 *
	 * @param player
	 * 		The player
	 */
	public static int getDefenceEmote(Player player) {
		int shieldId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_SHIELD);
		String shieldName = shieldId == -1 ? null : ItemDefinitionParser.forId(shieldId).getName().toLowerCase();
		if (shieldId == -1 || (shieldName.contains("book") && shieldId != 18346)) {
			int weaponId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_WEAPON);
			if (weaponId == -1) {
				return 424;
			}
			String weaponName = ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
			if (!weaponName.equals("null")) {
				if (weaponName.contains("scimitar") || weaponName.contains("korasi sword")) {
					return 15074;
				}
				if (weaponName.contains("whip")) {
					return 11974;
				}
				if (weaponName.contains("staff of light")) {
					return 12806;
				}
				if (weaponName.contains("longsword") || weaponName.contains("darklight") || weaponName.contains("silverlight") || weaponName.contains("excalibur")) {
					return 388;
				}
				if (weaponName.contains("dagger")) {
					return 378;
				}
				if (weaponName.contains("rapier")) {
					return 13038;
				}
				if (weaponName.contains("pickaxe")) {
					return 397;
				}
				if (weaponName.contains("mace")) {
					return 403;
				}
				if (weaponName.contains("claws")) {
					return 4177;
				}
				if (weaponName.contains("hatchet") || weaponName.contains("battleaxe")) {
					return 397;
				}
				if (weaponName.contains("greataxe")) {
					return 12004;
				}
				if (weaponName.contains("wand")) {
					return 415;
				}
				if (weaponName.contains("chaotic staff")) {
					return 13046;
				}
				if (weaponName.contains("staff")) {
					return 420;
				}
				if (weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em")) {
					return 403;
				}
				if (weaponName.contains("maul") || weaponName.contains("tzhaar-ket-om")) {
					return 1666;
				}
				if (weaponName.contains("zamorakian spear")) {
					return 12008;
				}
				if (weaponName.contains("spear") || weaponName.contains("halberd") || weaponName.contains("hasta")) {
					return 430;
				}
				if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.equals("saradomin sword")) {
					return 7050;
				}
			}
			return 424;
		}
		if (shieldName.contains("shield") || shieldName.contains("toktz-ket-xil")) {
			return 1156;
		}
		if (shieldName.contains("defender")) {
			return 4177;
		}
		switch (shieldId) {
			default:
				return 424;
		}
	}
	
	/**
	 * Gets the attack distance the player must be at with their weapon to attack
	 *
	 * @param player
	 * 		The player
	 * @param type
	 * 		The type of combat the player is using
	 */
	static int getMinimumDistance(Player player, CombatType type) {
		final int weaponId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_WEAPON);
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final String name = weaponId == -1 ? "null" : ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		
		switch (type) {
			// melee must be right next to the player, unless its a halberd
			case MELEE:
				if (name.contains("halberd")) {
					return 1;
				}
				return 0;
			default:
				if (name.contains("dart")) {
					return attackStyle != 2 ? 3 : 5;
				}
				if (name.contains("knife") || name.contains("throwaxe") || name.contains("sling")) {
					return attackStyle != 2 ? 4 : 6;
				}
				if (name.contains("javelin")) {
					return attackStyle != 2 ? 5 : 7;
				}
				if (name.contains("dorgeshuun")) {
					return attackStyle != 2 ? 6 : 8;
				}
				if (name.contains("longbow") || name.contains("dark") || name.contains("chinchompa")) {
					return attackStyle != 2 ? 9 : 10;
				}
				if (name.contains("zaryte") || name.contains("crystal")) {
					return 10;
				}
				return attackStyle != 2 ? 7 : 9;
		}
	}
	
	/**
	 * In the case that a target is above  water, melee will never reach; we must check the clip as if its a range/magic
	 * projectile.
	 *
	 * @param target
	 * 		The target
	 */
	public static boolean checkAttackPathAsRange(Entity target) {
		return false;
	}
	
	/**
	 * Fires combat listeners (post-swing events)
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 */
	public static void fireCombatListeners(Player player, Entity target) {
		if (target.isPlayer()) {
			target.toPlayer().stop(false, false, true, false);
		}
		if (target.getCombatDefinitions().isRetaliating() && !target.fighting()) {
			SystemManager.getScheduler().schedule(new ScheduledTask(2, 1, false) {
				@Override
				public Runnable getTask() {
					return () -> {
						if (!target.isRenderable()) {
							return;
						}
						if (target.isPlayer()) {
							target.toPlayer().getManager().getActions().startAction(new PlayerCombatAction(player));
						} else {
							// TODO: force the npc to attack us
						}
					};
				}
			});
		}
	}
	
	/**
	 * Checks if the target is in a good distance to fight, based on the combat type.
	 *
	 * @param player
	 * 		The player fighting
	 * @param target
	 * 		The target
	 * @param type
	 * 		The combat type
	 */
	public static boolean isWithinDistance(Player player, Entity target, CombatType type) {
		// the distance change
		final int distance = player.getMovement().isRunning() && target.getMovement().isRunning() ? 2 : 1;
		// if we should check closeby tiles [close 1v1 melee only]
		final boolean checkClose = type == CombatType.MELEE && !checkAttackPathAsRange(target);
		// the distance modifier
		final int modifier = player.getMovement().hasWalkSteps() && target.getMovement().hasWalkSteps() ? distance : 0;
		// if we can't clip to the target
		// or the target is too far away
		// or we're colliding with the target
		if (!player.getMovement().clippedProjectileToNode(target, checkClose) || !Misc.isOnRange(player, target, getMinimumDistance(player, type) + modifier) || Misc.colides(player, target)) {
			return false;
		}
		// otherwise we can fight
		return true;
	}
}
