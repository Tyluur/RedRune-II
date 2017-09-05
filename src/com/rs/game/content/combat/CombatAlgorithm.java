package com.rs.game.content.combat;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.content.combat.player.CombatStyle;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.Misc;
import com.rs.utility.constants.BonusConstants;
import com.rs.utility.constants.EquipmentConstants;
import com.rs.utility.constants.SkillConstants;

/**
 * This class handles all the functions/algorithms that remain static throughout player combat.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/4/2017
 */
public final class CombatAlgorithm implements BonusConstants {
	
	/**
	 * Gets the style of combat we're engaging in
	 *
	 * @param player
	 * 		The player
	 */
	public static CombatStyle findCombatStyle(Player player) {
		System.out.println(player.getCombatDefinitions().getAutoCastSpell());
		// magic gets first priority
		int spellId = player.getAttribute("spell_cast_id", player.getCombatDefinitions().getAutoCastSpell());
		if (spellId != 0) {
			return CombatStyle.MAGIC;
		}
		// then we check if we have range worn
		int rangeResponse = getRangeResponse(player);
		switch (rangeResponse) {
			case 0: // nothing found that symbolizes range
				return CombatStyle.MELEE;
			case 1: // invalid ammo
			case 2: // good range
			case 3: // no ammo
				return CombatStyle.RANGE;
		}
		return CombatStyle.MELEE;
	}
	
	/**
	 * Gets the range response from the player. The options are as follows: <br> <ul> <li>0 - We are doing melee</li>
	 * <li>1 - The ammo being used is incorrect</li> <li>2 - Range should proceed</li> <li>3 - We do not have ammo to
	 * use.</li> </ul>
	 *
	 * @param player
	 * 		The player to check.
	 */
	public static int getRangeResponse(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1) {
			return 0;
		}
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		// those dont need arrows
		if (name.contains("knife") || name.contains("dart") || name.contains("javelin") || name.contains("thrownaxe") || name.contains("throwing axe") || name.contains("crystal bow") || name.equalsIgnoreCase("zaryte bow") || name.contains("chinchompa") || name.contains("bolas") || name.contains("sling") || name.contains("toktz-xil-ul")) {
			return 2;
		}
		int ammoId = player.getEquipment().getIdInSlot(EquipmentConstants.SLOT_ARROWS);
		switch (weaponId) {
			case 15241: // Hand cannon
				switch (ammoId) {
					case -1:
						return 3;
					case 15243: // bronze arrow
						return 2;
					default:
						return 1;
				}
			case 839: // longbow
			case 841: // shortbow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
						return 2;
					default:
						return 1;
				}
			case 843: // oak longbow
			case 845: // oak shortbow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
						return 2;
					default:
						return 1;
				}
			case 847: // willow longbow
			case 849: // willow shortbow
			case 13541: // Willow composite bow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
						return 2;
					default:
						return 1;
				}
			case 851: // maple longbow
			case 853: // maple shortbow
			case 18331: // Maple longbow (sighted)
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
						return 2;
					default:
						return 1;
				}
			case 2883:// ogre bow
				switch (ammoId) {
					case -1:
						return 3;
					case 2866: // ogre arrow
						return 2;
					default:
						return 1;
				}
			case 4827:// Comp ogre bow
				switch (ammoId) {
					case -1:
						return 3;
					case 2866: // ogre arrow
					case 4773: // bronze brutal
					case 4778: // iron brutal
					case 4783: // steel brutal
					case 4788: // black brutal
					case 4793: // mithril brutal
					case 4798: // adamant brutal
					case 4803: // rune brutal
						return 2;
					default:
						return 1;
				}
			case 855: // yew longbow
			case 857: // yew shortbow
			case 10281: // Yew composite bow
			case 14121: // Sacred clay bow
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
			case 6724: // seercull
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
					case 892: // rune arrow
						return 2;
					default:
						return 1;
				}
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
					case 892: // rune arrow
					case 11212: // dragon arrow
						return 2;
					default:
						return 1;
				}
			case 19143: // saradomin bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19152: // saradomin arrow
						return 2;
					default:
						return 1;
				}
			case 19146: // guthix bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19157: // guthix arrow
						return 2;
					default:
						return 1;
				}
			case 19149: // zamorak bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19162: // zamorak arrow
						return 2;
					default:
						return 1;
				}
			case 24338: // Royal crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 24336: // Coral bolts
						return 2;
					default:
						return 1;
				}
			case 24303: // Coral crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 24304: // Coral bolts
						return 2;
					default:
						return 1;
				}
			case 4734: // karil crossbow
			case 4934:
			case 4935:
			case 4936:
			case 4937:
				switch (ammoId) {
					case -1:
						return 3;
					case 4740: // bolt rack
						return 2;
					default:
						return 1;
				}
			case 10156: // hunters crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 10158: // Kebbit bolts
					case 10159: // Long kebbit bolts
						return 2;
					default:
						return 1;
				}
			case 8880: // Dorgeshuun c'bow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 8882: // bone bolts
						return 2;
					default:
						return 1;
				}
			case 14684: // zanik crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9144: // rune bolts
					case 9145: // silver bolts wtf
						return 2;
					default:
						return 1;
				}
			case 767: // phoenix crossbow
			case 837: // crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
						return 2;
					default:
						return 1;
				}
			case 9174: // bronze crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9236: // Opal bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9176: // blurite crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9139: // Blurite bolts
					case 9237: // Jade bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9177: // iron crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9179: // steel crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
						return 2;
					default:
						return 1;
				}
			case 13081: // black crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9181: // Mith crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9145: // silver bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9183: // adam c bow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9185: // rune c bow
			case 18357: // chaotic crossbow
			case 18358:
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9144: // rune bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
					case 9244: // Dragon bolts (e)
					case 9245: // Onyx bolts (e)
					case 24116: // Bakriminel bolts
						return 2;
					default:
						return 1;
				}
			default:
				return 0;
		}
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
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
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
				System.out.println("using style " + attackStyle + " with weapon " + weaponName);
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
	 * @param style
	 * 		The attack style
	 */
	public static int getMeleeDefenceBonusIndex(int style) {
		switch (style) {
			case STAB_ATTACK:
				return STAB_DEFENCE;
			case SLASH_ATTACK:
				return SLASH_DEFENCE;
			case CRUSH_ATTACK:
				return CRUSH_DEFENCE;
			default:
				return STAB_DEFENCE;
		}
	}
	
	/**
	 * Gets the accuracy multiplier of a weapon when on special
	 *
	 * @param itemId
	 * 		The weapon
	 */
	public static double getSpecialAccuracyModifier(int itemId) {
		if (itemId == -1) {
			return 0;
		}
		String name = ItemDefinitions.getItemDefinitions(itemId).getName().toLowerCase();
		if (name.contains("whip") || name.contains("dragon scimitar") || name.contains("dragon dagger") || name.contains("dragon spear") || name.contains("zamorakian spear") || name.contains("dragon halberd") || name.contains("anchor") || name.contains("magic longbow") || name.contains("magic shortbow") || name.contains("dragon longsword")) {
			return 0.25;
		}
		if (name.contains("dragon mace")) {
			return 0.1;
		}
		if (name.contains("korasi") || name.contains("dragon claws")) {
			return 0.6;
		}
		if (name.contains("armadyl godsword")) {
			return 0.10;
		}
		if (name.contains("godsword")) {
			return 0.10;
		}
		if (name.contains("barrelchest anchor")) {
			return 0.10;
		}
		if (name.contains("granite maul") || name.contains("granite mace")) {
			return 0.095;
		}
		if (name.contains("dark bow") || name.contains("zanik")) {
			return 0.5;
		}
		if (name.contains("morrigan's javel")) {
			return 0.4;
		}
		if (name.contains("vesta's spear") || name.contains("statius' warhammer") || name.contains("statius' warhammer (deg)") || name.contains("morrigan's throw") || name.contains("hand cannon")) {
			return 1.7;
		}
		if (name.contains("vesta's longsword")) {
			return 1.5;
		}
		return 1;
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
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
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
	
	public static boolean canFight(Player player, Actor target) {
		if (player.isDead() || player.hasFinished() || target.isDead() || target.hasFinished()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the target is in a good distance to fight, based on the combat type.
	 *
	 * @param player
	 * 		The player fighting
	 * @param target
	 * 		The target
	 * @param style
	 * 		The combat type
	 */
	public static boolean isWithinDistance(Player player, Actor target, CombatStyle style) {
		// the distance change
		int distance = player.getRun() /*&& target.getMovement().isRunning()*/ ? 2 : 1;
		int weaponId = player.getEquipment().getWeaponId();
		String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		boolean halberd = weaponName.contains("halberd");
		if (style == CombatStyle.MELEE && halberd) {
			distance += 1;
		}
		// if we should check closeby tiles [close 1v1 melee only]
		final boolean checkClose = style == CombatStyle.MELEE && !checkAttackPathAsRange(target);
		// the distance modifier
		final int modifier = player.hasWalkSteps() /*&& target.getMovement().hasWalkSteps()*/ ? distance : 0;
		// if we can't clip to the target
		// or the target is too far away
		// or we're colliding with the target
		if (!player.clipedProjectile(target, checkClose) || !Misc.isOnRange(player, target, getMinimumDistance(player, style) + modifier) || Misc.colides(player, target)) {
			return false;
		}
		// otherwise we can fight
		return true;
	}
	
	/**
	 * In the case that a target is above  water, melee will never reach; we must check the clip as if its a range/magic
	 * projectile.
	 *
	 * @param target
	 * 		The target
	 */
	public static boolean checkAttackPathAsRange(Actor target) {
		return false;
	}
	
	/**
	 * Gets the attack distance the player must be at with their weapon to attack
	 *
	 * @param player
	 * 		The player
	 * @param style
	 * 		The style of combat the player is using
	 */
	public static int getMinimumDistance(Player player, CombatStyle style) {
		final int weaponId = player.getEquipment().getIdInSlot(EquipmentConstants.SLOT_WEAPON);
		final int attackStyle = player.getCombatDefinitions().getAttackStyle();
		final String name = weaponId == -1 ? "null" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
		switch (style) {
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
	 * Fires combat listeners (post-swing events)
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 */
	public static void fireCombatListeners(Player player, Actor target) {
		addAttackedByDelay(player, target);
	}
	
	public static void addAttackedByDelay(Actor player, Actor target) {
		target.setAttackedBy(player);
		target.setAttackedByDelay(Misc.currentTimeMillis() + 6000); // 8seconds
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
			String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
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
	 * Gets the defence emote of an entity
	 *
	 * @param actor
	 * 		The actor
	 */
	public static int getDefenceEmote(Actor actor) {
		if (actor.isPlayer()) {
			Player player = actor.toPlayer();
			int shieldId = player.getEquipment().getIdInSlot(EquipmentConstants.SLOT_SHIELD);
			String shieldName = shieldId == -1 ? null : ItemDefinitions.getItemDefinitions(shieldId).getName().toLowerCase();
			if (shieldId == -1 || (shieldName.contains("book") && shieldId != 18346)) {
				int weaponId = player.getEquipment().getIdInSlot(EquipmentConstants.SLOT_WEAPON);
				if (weaponId == -1) {
					return 424;
				}
				String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
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
		} else if (actor.isNPC()) {
			return actor.toNPC().getCombatDefinitions().getDefenceAnim();
		} else {
			System.out.println("Unable to identify actor type: " + actor);
			return -1;
		}
	}
	
	/*
 * 0 not ranging, 1 invalid ammo so stops att, 2 can range, 3 no ammo
 */
	public static final int isRanging(Player player) {
		int weaponId = player.getEquipment().getWeaponId();
		if (weaponId == -1) {
			return 0;
		}
		String name = ItemDefinitions.getItemDefinitions(weaponId).getName();
		if (name != null) { // those dont need arrows
			if (name.contains("knife") || name.contains("dart") || name.contains("javelin") || name.contains("thrownaxe") || name.contains("throwing axe") || name.contains("Crystal bow") || name.contains("Zaryte bow") || name.contains("Polypore staff") || name.equalsIgnoreCase("Polypore staff (degraded)")) {
				return 2;
			}
		}
		int ammoId = player.getEquipment().getAmmoId();
		switch (weaponId) {
			case 15241: // Hand cannon
				switch (ammoId) {
					case -1:
						return 3;
					case 15243: // bronze arrow
						return 2;
					default:
						return 1;
				}
			case 839: // longbow
			case 841: // shortbow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
						return 2;
					default:
						return 1;
				}
			case 843: // oak longbow
			case 845: // oak shortbow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
						return 2;
					default:
						return 1;
				}
			case 847: // willow longbow
			case 849: // willow shortbow
			case 13541: // Willow composite bow
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
						return 2;
					default:
						return 1;
				}
			case 851: // maple longbow
			case 853: // maple shortbow
			case 18331: // Maple longbow (sighted)
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
						return 2;
					default:
						return 1;
				}
			case 2883:// ogre bow
				switch (ammoId) {
					case -1:
						return 3;
					case 2866: // ogre arrow
						return 2;
					default:
						return 1;
				}
			case 4827:// Comp ogre bow
				switch (ammoId) {
					case -1:
						return 3;
					case 2866: // ogre arrow
					case 4773: // bronze brutal
					case 4778: // iron brutal
					case 4783: // steel brutal
					case 4788: // black brutal
					case 4793: // mithril brutal
					case 4798: // adamant brutal
					case 4803: // rune brutal
						return 2;
					default:
						return 1;
				}
			case 855: // yew longbow
			case 857: // yew shortbow
			case 10281: // Yew composite bow
			case 14121: // Sacred clay bow
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
			case 6724: // seercull
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
					case 892: // rune arrow
						return 2;
					default:
						return 1;
				}
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				switch (ammoId) {
					case -1:
						return 3;
					case 882: // bronze arrow
					case 884: // iron arrow
					case 886: // steel arrow
					case 888: // mithril arrow
					case 890: // adamant arrow
					case 892: // rune arrow
					case 11212: // dragon arrow
						return 2;
					default:
						return 1;
				}
			case 19143: // saradomin bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19152: // saradomin arrow
						return 2;
					default:
						return 1;
				}
			case 19146: // guthix bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19157: // guthix arrow
						return 2;
					default:
						return 1;
				}
			case 19149: // zamorak bow
				switch (ammoId) {
					case -1:
						return 3;
					case 19162: // zamorak arrow
						return 2;
					default:
						return 1;
				}
			case 4734: // karil crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 4740: // bolt rack
						return 2;
					default:
						return 1;
				}
			case 10156: // hunters crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 10158: // Kebbit bolts
					case 10159: // Long kebbit bolts
						return 2;
					default:
						return 1;
				}
			case 8880: // Dorgeshuun c'bow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 8882: // bone bolts
						return 2;
					default:
						return 1;
				}
			case 14684: // zanik crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9145: // silver bolts
					case 8882: // bone bolts
						return 2;
					default:
						return 1;
				}
			case 767: // phoenix crossbow
			case 837: // crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
						return 2;
					default:
						return 1;
				}
			case 9174: // bronze crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9236: // Opal bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9176: // blurite crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9139: // Blurite bolts
					case 9237: // Jade bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9177: // iron crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9179: // steel crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
						return 2;
					default:
						return 1;
				}
			case 13081: // black crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9181: // Mith crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9145: // silver bolts
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9183: // adam c bow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
						return 2;
					default:
						return 1;
				}
			case 9185: // rune c bow
			case 18357: // chaotic crossbow
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9144: // rune bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
					case 9244: // Dragon bolts (e)
					case 9245: // Onyx bolts (e)
						return 2;
					default:
						return 1;
				}
			case 24338:
				switch (ammoId) {
					case -1:
						return 3;
					case 877: // bronze bolts
					case 9140: // iron bolts
					case 9141: // steel bolts
					case 13083: // black bolts
					case 9142:// mithril bolts
					case 9143: // adam bolts
					case 9144: // rune bolts
					case 9145: // silver bolts wtf
					case 9236: // Opal bolts (e)
					case 9238: // Pearl bolts (e)
					case 9239: // Topaz bolts (e)
					case 9240: // Sapphire bolts (e)
					case 9241: // Emerald bolts (e)
					case 9242: // Ruby bolts (e)
					case 9243: // Diamond bolts (e)
					case 9244: // Dragon bolts (e)
					case 9245: // Onyx bolts (e)
					case 24336:
						return 2;
					default:
						return 1;
				}
			default:
				return 0;
		}
	}
	
	public static int getSpecialAmmount(int weaponId) {
		switch (weaponId) {
			case 4587: // dragon sci
			case 859: // magic longbow
			case 861: // magic shortbow
			case 10284: // Magic composite bow
			case 18332: // Magic longbow (sighted)
			case 19149:// zamorak bow
			case 19151:
			case 19143:// saradomin bow
			case 19145:
			case 19146:
			case 19148:// guthix bow
				return 55;
			case 11235: // dark bows
			case 15701:
			case 15702:
			case 15703:
			case 15704:
				return 65;
			case 13899: // vls
			case 13901:
			case 1305: // dragon long
			case 1215: // dragon dagger
			case 5698: // dds
			case 1434: // dragon mace
			case 1249:// d spear
			case 1263:
			case 3176:
			case 5716:
			case 5730:
			case 13770:
			case 13772:
			case 13774:
			case 13776:
				return 25;
			case 15442:// whip start
			case 15443:
			case 15444:
			case 15441:
			case 4151:
			case 11698: // sgs
			case 11694: // ags
			case 13902: // statius hammer
			case 13904:
			case 13905: // vesta spear
			case 13907:
			case 14484: // d claws
			case 10887: // anchor
			case 3204: // d hally
			case 4153: // granite maul
			case 14684: // zanik cbow
			case 15241: // hand cannon
			case 13908:
			case 13954:// morrigan javelin
			case 13955:
			case 13956:
			case 13879:
			case 13880:
			case 13881:
			case 13882:
			case 13883:// morigan thrown axe
			case 13957:
				return 50;
			case 11730: // ss
			case 11696: // bgs
			case 11700: // zgs
			case 35:// Excalibur
			case 8280:
			case 14632:
			case 1377:// dragon battle axe
			case 13472:
			case 15486:// staff of lights
			case 22207:
			case 22209:
			case 22211:
			case 22213:
				return 100;
			case 19784: // korasi sword
				return 60;
			default:
				return 0;
		}
	}
}
