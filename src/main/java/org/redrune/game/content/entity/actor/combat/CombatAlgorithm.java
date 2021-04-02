package org.redrune.game.content.entity.actor.combat;

import com.google.common.base.Preconditions;
import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.engine.SystemManager;
import org.redrune.game.content.entity.actor.combat.player.CombatStyle;
import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.game.global.map.region.Region;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.BonusConstants;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * This class handles all the functions/algorithms that remain static throughout player combat.
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public final class CombatAlgorithm implements BonusConstants, EquipmentConstants, MagicConstants {

    /**
     * Gets the style of combat we're engaging in
     *
     * @param player The player
     */
    public static CombatStyle findCombatStyle(Player player) {
        // magic gets first priority
        int spellId = player.getCombatDefinitions().getRealSpellId();
        if (spellId > 0) {
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
     * @param player The player to check.
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
        int ammoId = player.getEquipment().getIdInSlot(SLOT_ARROWS);
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
     * @param weaponId    The id of the weapon equipped
     * @param attackStyle The style used.
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
     * @param style The attack style
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

    public static double getSpecialMaxModifier(int itemId) {
        switch (itemId) {
            case 11694:
                return 1.375;
            default:
                return 1;
        }
    }

    /**
     * Gets the accuracy multiplier of a weapon when on special
     *
     * @param itemId The weapon
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
     * @param weaponId    The id of the weapon
     * @param attackStyle The players attack style
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
        return !player.isDead() && !player.isFinished() && !target.isDead() && !target.isFinished();
    }

    /**
     * Checks if we have an armour set equipped. This uses lowercase naming. <br> Example usage:
     * <br>armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_chest, SLOT_LEGS, SLOT_WEAPON }, "dharok", "dharok",
     * "dharok", "dharok");// armourSetEquipped(player, new int[] { SLOT_HAT, SLOT_AMMY, SLOT_LEGS, SLOT_WEAPON },
     * "dharok", "dharok", "dharok", "dharok");
     *
     * @param player    The player
     * @param slots     The slots
     * @param nameFlags The name flags, with the indexes corresponding to the slots indexes.
     */
    public static boolean armourSetEquipped(Player player, int[] slots, String... nameFlags) {
        Preconditions.checkArgument(slots.length == nameFlags.length, "Name flags and slot length must be equal!");
        for (int i = 0; i < slots.length; i++) {
            int slot = slots[i];
            int itemInSlot = player.getEquipment().getIdInSlot(slot);
            // theres no item in the slot, so its not possible to match the name
            if (itemInSlot == -1) {
                return false;
            }
            String itemInSlotName = ItemDefinitions.getItemDefinitions(itemInSlot).getName().toLowerCase();

            String nameFlag = nameFlags[i].toLowerCase();
            // the item in that slot's name didn't have the expected flag
            if (!itemInSlotName.contains(nameFlag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if we have full void equipped, with the possible helmet ids
     *
     * @param player    The player to check on
     * @param helmetIds The helmet id
     */
    public static boolean fullVoidEquipped(Player player, int... helmetIds) {
        boolean hasDeflector = player.getEquipment().getIdInSlot(SLOT_SHIELD) == 19712;
        if (player.getEquipment().getIdInSlot(SLOT_HANDS) != 8842) {
            if (hasDeflector) {
                hasDeflector = false;
            } else {
                return false;
            }
        }
        int legsId = player.getEquipment().getIdInSlot(SLOT_LEGS);
        boolean hasLegs = legsId != -1 && (legsId == 8840 || legsId == 19786 || legsId == 19788 || legsId == 19790);
        if (!hasLegs) {
            if (hasDeflector) {
                hasDeflector = false;
            } else {
                return false;
            }
        }
        int torsoId = player.getEquipment().getIdInSlot(SLOT_CHEST);
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
        int helmId = player.getEquipment().getIdInSlot(SLOT_HAT);
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
     * Checks if the target is in a good distance to fight, based on the combat type.
     *
     * @param player The player fighting
     * @param target The target
     * @param style  The combat style
     */
    public static boolean isWithinDistance(Player player, Actor target, CombatStyle style) {
        // the distance change
        int distance = player.isRunModeOn() && target.isRunModeOn() ? 2 : 1;
        int weaponId = player.getEquipment().getWeaponId();
        String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
        boolean halberd = weaponName.contains("halberd");
        if (style == CombatStyle.MELEE && halberd) {
            distance += 1;
        }
        // if we should check closeby tiles [close 1v1 melee only]
        final boolean checkClose = style == CombatStyle.MELEE && !checkAttackPathAsRange(target);
        // the distance modifier
        final int modifier = player.hasWalkSteps() && target.hasWalkSteps() ? distance : 0;
        // if we can't clip to the target
        // or the target is too far away
        // or we're colliding with the target
        return player.clipedProjectile(target, checkClose) && Misc.isOnRange(player, target, getMinimumDistance(player, style) + modifier) && !Misc.colides(player, target);
        // otherwise we can fight
    }

    /**
     * In the case that a target is above  water, melee will never reach; we must check the clip as if its a range/magic
     * projectile.
     *
     * @param target The target
     */
    public static boolean checkAttackPathAsRange(Actor target) {
        return false;
    }

    /**
     * Gets the attack distance the player must be at with their weapon to attack
     *
     * @param player The player
     * @param style  The style of combat the player is using
     */
    public static int getMinimumDistance(Player player, CombatStyle style) {
        final int weaponId = player.getEquipment().getIdInSlot(SLOT_WEAPON);
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
     * @param player The player
     * @param target The target
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
     * @param weaponId    The id of the weapon
     * @param attackStyle The attack style being used
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
     * @param actor The actor
     */
    public static int getDefenceEmote(Actor actor) {
        if (actor.isPlayer()) {
            Player player = actor.toPlayer();
            int shieldId = player.getEquipment().getIdInSlot(SLOT_SHIELD);
            String shieldName = shieldId == -1 ? null : ItemDefinitions.getItemDefinitions(shieldId).getName().toLowerCase();
            if (shieldId == -1 || (shieldName.contains("book") && shieldId != 18346)) {
                int weaponId = player.getEquipment().getIdInSlot(SLOT_WEAPON);
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

    /**
     * Performing some checks to toggle the special attack bar. It must be done after players stop switching if they are
     * to make combat smooth.
     *
     * @param player  The player
     * @param attempt The attempt number
     */
    public static void checkSpecialToggle(Player player, final int attempt) {
        if (!player.getAttributes().getSwitchItemCache().isEmpty() && attempt <= 3) {
            SystemManager.SLOW_EXECUTOR.schedule(() -> checkSpecialToggle(player, attempt + 1), 100, TimeUnit.MILLISECONDS);
            return;
        }
        if (player.removeTemporaryAttribute("special_attack_toggled", false)) {
            player.getCombatDefinitions().switchUsingSpecialAttack();
        }
        if (player.getCombatDefinitions().isUsingSpecialAttack()) {
            int weaponId = player.getEquipment().getWeaponId();
            Optional<SpecialAttackPlugin> optional = PluginRepository.getSpecialPlugin(weaponId);
            if (!optional.isPresent()) {
                return;
            }
            SpecialAttackPlugin plugin = optional.get();
            if (!plugin.isInstant()) {
                return;
            }
            final boolean energyRequired = player.getCombatDefinitions().getSpecialAttackPercentage() < getSpecialAmount(player.getEquipment().getWeaponId());
            // not enough energy
            if (energyRequired) {
                player.getPackets().sendMessage("You don't have enough special attack energy.");
                player.getCombatDefinitions().setUsingSpecialAttack(false);
                return;
            }
            // the combat action
            PlayerCombatAction action = player.getActionManager().getAction() instanceof PlayerCombatAction ? (PlayerCombatAction) player.getActionManager().getAction() : null;
            Actor target = player.getTemporaryAttribute("combat_target", action == null ? null : action.getTarget());
            // no target and it was necessary
            if (target == null && plugin.requiresFight()) {
                player.getCombatDefinitions().setUsingSpecialAttack(false);
                return;
            }
            // we can't allow a swing on dead target
            if (target != null && target.isDead()) {
                player.getCombatDefinitions().setUsingSpecialAttack(false);
                return;
            }
            if (plugin.requiresFight()) {
                if (!canFight(player, target)) {
                    return;
                }

                // just in case [nearly certain all instant specs are melee...]
                CombatStyle style = findCombatStyle(player);
                if (style == null) {
                    return;
                }
                // we are far away
                if (!isWithinDistance(player, target, style)) {
                    return;
                }
                plugin.fire(player, target, style.getStyle());
                player.setNextFaceActor(target);
            } else {
                plugin.fire(player, target, null);
            }
            // dropping the special attack amount
            player.getCombatDefinitions().decreaseSpecialEnergy(getSpecialAmount(player.getEquipment().getWeaponId()));
            // we used spec so it is triggered off
            player.getCombatDefinitions().setUsingSpecialAttack(false);
        }
    }

    /**
     * Gets the amount of special energy a weapon uses
     */
    public static int getSpecialAmount(int weaponId) {
        ItemDefinitions defs = ItemDefinitions.getItemDefinitions(weaponId);
        if (defs.isLended()) {
            weaponId = defs.getLendId();
        }
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
            case 11716:
                return 25;
            case 15442:// whip start
            case 15443:
            case 15444:
            case 15441:
            case 4151:
            case 23691:
            case 11698: // sgs
            case 23681:
            case 11694: // ags
            case 23679:
            case 13904:
            case 13905: // vesta spear
            case 13907:
            case 14484: // d claws
            case 23695:
            case 10887: // anchor
            case 3204: // d hally
            case 4153: // granite maul
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
            case 23690:
            case 11696: // bgs
            case 23680:
            case 11700: // zgs
            case 23682:
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
            case 7158: // d2h
            case 21371: // vine whip
                return 60;
            case 14684: // zanik cbow
                return 50;
            case 13902: // statius hammer
                return 35;
            default:
                return 0;
        }
    }

    public static List<Actor> getMultiAttackTargets(Player player, Actor target) {
        return getMultiAttackTargets(player, target, 1, 9);
    }

    public static List<Actor> getMultiAttackTargets(Player player, Actor target, int maxDistance, int maxAmtTargets) {
        List<Actor> possibleTargets = new ArrayList<>();
        possibleTargets.add(target);
        if (target.isInMultiArea()) {
            y:
            for (int regionId : target.getMapRegionsIds()) {
                Region region = RegionManager.getRegion(regionId);
                if (target instanceof Player) {
                    List<Integer> playerIndexes = region.getPlayerIndexes();
                    if (playerIndexes == null) {
                        continue;
                    }
                    for (int playerIndex : playerIndexes) {
                        Player p2 = World.getPlayers().get(playerIndex);
                        if (p2 == null || p2 == player || p2 == target || p2.isDead() || !p2.hasStarted() || p2.isFinished() || !p2.getAttributes().isCanPvp() || !p2.isInMultiArea() || !p2.withinDistance(target, maxDistance) || !player.getControllerManager().canHit(p2)) {
                            continue;
                        }
                        possibleTargets.add(p2);
                        if (possibleTargets.size() == maxAmtTargets) {
                            break y;
                        }
                    }
                } else {
                    List<Integer> npcIndexes = region.getNPCsIndexes();
                    if (npcIndexes == null) {
                        continue;
                    }
                    for (int npcIndex : npcIndexes) {
                        NPC n = World.getNPCs().get(npcIndex);
                        if (n == null || n == target || n == player.getFamiliar() || n.isDead() || n.isFinished() || !n.isInMultiArea() || !n.withinDistance(target, maxDistance) || !n.getDefinitions().hasAttackOption() || !player.getControllerManager().canHit(n)) {
                            continue;
                        }
                        possibleTargets.add(n);
                        if (possibleTargets.size() == maxAmtTargets) {
                            break y;
                        }
                    }
                }
            }
        }
        return possibleTargets;
    }

    public static boolean hasAntiDragProtection(Actor target) {
        if (target instanceof NPC) {
            return false;
        }
        Player p2 = (Player) target;
        int shieldId = p2.getEquipment().getShieldId();
        return shieldId == 1540 || shieldId == 11283 || shieldId == 11284;
    }

    /**
     * Gets the graphics id of the thrown weapon
     *
     * @param weaponId The id of the weapon
     */
    public static int getKnifeThrowGfxId(int weaponId) {
        // knives
        if (weaponId == 868) { // rune
            return 218;
        } else if (weaponId == 867) { // addy
            return 217;
        } else if (weaponId == 866) {  // mith
            return 216;
        } else if (weaponId == 869) { // black
            return 215;
        } else if (weaponId == 865) { // steel
            return 214;
        } else if (weaponId == 863) { // iron
            return 213;
        } else if (weaponId == 864) { // bronze
            return 212;
        }
        // darts
        if (weaponId == 806) { // bronze
            return 226;
        } else if (weaponId == 807) { // iron
            return 227;
        } else if (weaponId == 808) { // steel
            return 228;
        } else if (weaponId == 3093) { // black
            return 34;
        } else if (weaponId == 809) { // mithril
            return 229;
        } else if (weaponId == 810) { // addy
            return 230;
        } else if (weaponId == 811) { // rune
            return 231;
        } else if (weaponId == 11230) { // dragon
            return 1122;
        } else if (weaponId >= 13954 && weaponId <= 13956 || weaponId >= 13879 && weaponId <= 13882) {// morrjavelins
            return 1837;
        } else if (weaponId == 13883 || weaponId == 13957) { // morr thrownaxe
            return 1839;
        } else if (weaponId == 6522) { // obby rings
            return 442;
        } else if (weaponId == 800) {
            return 43;
        }
        return 219;
    }

    /**
     * Gets the graphics id of an arrow
     *
     * @param arrowId The arrow
     */
    public static int getArrowThrowGfxId(int arrowId) {
        if (arrowId == 884) {
            return 18;
        } else if (arrowId == 886) {
            return 20;
        } else if (arrowId == 888) {
            return 21;
        } else if (arrowId == 890) {
            return 22;
        } else if (arrowId == 892) {
            return 24;
        }
        return 19; // bronze default
    }

    /**
     * Gets the projectile id based on the weapon and the arrow
     *
     * @param weaponId The weapon
     * @param arrowId  The arrow
     */
    public static int getArrowProjectileGfxId(int weaponId, int arrowId) {
        if (arrowId == 882) {
            return 9;
        } else if (arrowId == 884) {
            return 10;
        } else if (arrowId == 886) {
            return 11;
        } else if (arrowId == 888) {
            return 12;
        } else if (arrowId == 890) {
            return 13;
        } else if (arrowId == 892) {
            return 15;
        } else if (arrowId == 11212) {
            return 1120;
        } else if (weaponId == 20171) {
            return 1066;
        }
        return 10;// bronze default
    }

    /**
     * Checks that we can cast a spell and sets it if we can
     *
     * @param player  The player
     * @param spellId The id of the spell
     * @param set     The set value [0 = autocast, 1 = regular cast]
     * @param delete  If we should delete runes
     */
    public static boolean checkCombatSpell(Player player, int spellId, int set, boolean delete) {
        switch (player.getCombatDefinitions().getMagicBook()) {
            case REGULAR:
                switch (spellId) {
                    case 98:
                        if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 2)) {
                            return false;
                        }
                        break;
                    case 25:
                        if (!checkSpellRequirements(player, 1, delete, AIR_RUNE, 1, MIND_RUNE, 1)) {
                            return false;
                        }
                        break;
                    case 26:
                        if (!checkSpellRequirements(player, 1, delete, WATER_RUNE, 3, EARTH_RUNE, 2, BODY_RUNE, 1)) {
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
                    case 31:
                        if (!checkSpellRequirements(player, 1, delete, WATER_RUNE, 3, EARTH_RUNE, 2, BODY_RUNE, 1)) {
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
                    case 35: // curse
                        if (!checkSpellRequirements(player, 1, delete, WATER_RUNE, 2, EARTH_RUNE, 3, BODY_RUNE, 1)) {
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
                            player.getPackets().sendMessage("You need to be equipping a Saradomin staff to cast this spell.", true);
                            return false;
                        }
                        if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
                            return false;
                        }
                        break;
                    case 67: // Guthix Claws
                        if (player.getEquipment().getWeaponId() != 2416) {
                            player.getPackets().sendMessage("You need to be equipping a Guthix Staff or Void Mace to cast this spell.", true);
                            return false;
                        }
                        if (!checkSpellRequirements(player, 60, delete, AIR_RUNE, 4, FIRE_RUNE, 1, BLOOD_RUNE, 2)) {
                            return false;
                        }
                        break;
                    case 68: // Flame of Zammy
                        if (player.getEquipment().getWeaponId() != 2417) {
                            player.getPackets().sendMessage("You need to be equipping a Zamorak Staff to cast this spell.", true);
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
                    case 75:
                        if (!checkSpellRequirements(player, 1, delete, EARTH_RUNE, 5, WATER_RUNE, 5, SOUL_RUNE, 1)) {
                            return false;
                        }
                        break;
                    case 78:
                        if (!checkSpellRequirements(player, 1, delete, EARTH_RUNE, 8, WATER_RUNE, 8, SOUL_RUNE, 1)) {
                            return false;
                        }
                        break;
                    case 82:
                        if (!checkSpellRequirements(player, 1, delete, EARTH_RUNE, 12, WATER_RUNE, 12, SOUL_RUNE, 1)) {
                            return false;
                        }
                        break;
                }
                break;
            case ANCIENTS:
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
                    case 36: // Miasmic rush.
                        if (!checkSpellRequirements(player, 61, delete, CHAOS_RUNE, 2, EARTH_RUNE, 1, SOUL_RUNE, 1)) {
                            return false;
                        }
                        int weaponId = player.getEquipment().getWeaponId();
                        if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
                            player.getPackets().sendMessage("You need a Zuriel's staff to cast this spell.");
                            return false;
                        }
                        break;
                    case 38: // Miasmic burst.
                        if (!checkSpellRequirements(player, 73, delete, CHAOS_RUNE, 4, EARTH_RUNE, 2, SOUL_RUNE, 2)) {
                            return false;
                        }
                        weaponId = player.getEquipment().getWeaponId();
                        if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
                            player.getPackets().sendMessage("You need a Zuriel's staff to cast this spell.");
                            return false;
                        }
                        break;
                    case 37: // Miasmic blitz.
                        if (!checkSpellRequirements(player, 85, delete, BLOOD_RUNE, 2, EARTH_RUNE, 3, SOUL_RUNE, 3)) {
                            return false;
                        }
                        weaponId = player.getEquipment().getWeaponId();
                        if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
                            player.getPackets().sendMessage("You need a Zuriel's staff to cast this spell.");
                            return false;
                        }
                        break;
                    case 39: // Miasmic barrage.
                        if (!checkSpellRequirements(player, 97, delete, BLOOD_RUNE, 4, EARTH_RUNE, 4, SOUL_RUNE, 4)) {
                            return false;
                        }
                        weaponId = player.getEquipment().getWeaponId();
                        if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
                            player.getPackets().sendMessage("You need a Zuriel's staff to cast this spell.");
                            return false;
                        }
                        break;
                }
                break;
            default:
                System.out.println("Spell # " + spellId + " did not have rune requirements in place.");
                return false;
        }
        if (set >= 0) {
            if (set == 0) {
                player.getCombatDefinitions().setAutoCastSpell(spellId);
            } else {
                player.getTemporaryAttributes().put("tempCastSpell", spellId);
            }
        }
        return true;
    }

    /**
     * Checks to make sure the player has the requirements to cast a spell
     *
     * @param player The player
     * @param level  The level of the spell
     * @param delete If we should delete runes
     * @param runes  The runes
     */
    public static boolean checkSpellRequirements(Player player, int level, boolean delete, int... runes) {
        if (player.getSkills().getLevel(SkillConstants.MAGIC) < level) {
            player.getPackets().sendMessage("Your Magic level is not high enough for this spell.");
            return false;
        }
        return checkRunes(player, delete, runes);
    }

    /**
     * Checks that we have enough runes to cast the spell
     *
     * @param player The player
     * @param delete If we should delete the runes
     * @param runes  The runes to delete
     */
    public static boolean checkRunes(Player player, boolean delete, int... runes) {
        int weaponId = player.getEquipment().getWeaponId();
        int shieldId = player.getEquipment().getIdInSlot(SLOT_SHIELD);
        int runesCount = 0;
        while (runesCount < runes.length) {
            int runeId = runes[runesCount++];
            int ammount = runes[runesCount++];
            if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
                continue;
            }
            if (hasStaffOfLight(weaponId) && Misc.getRandom(8) == 0 && runeId != 21773) {
                continue;
            }
            if (!player.getInventory().containsItem(runeId, ammount)) {
                player.getPackets().sendMessage("You do not have enough " + ItemDefinitions.getItemDefinitions(runeId).getName().replace("rune", "Rune") + "s to cast this spell.");
                return false;
            }
        }
        if (delete) {
            runesCount = 0;
            while (runesCount < runes.length) {
                int runeId = runes[runesCount++];
                int amount = runes[runesCount++];
                if (hasInfiniteRunes(runeId, weaponId, shieldId)) {
                    continue;
                }
                player.getInventory().deleteItem(runeId, amount);
            }
        }
        return true;
    }

    /**
     * Checks if we have infinite runes, based on the staffs
     *
     * @param runeId   The rune to check for
     * @param weaponId The weapon
     * @param shieldId The shield
     */
    private static boolean hasInfiniteRunes(int runeId, int weaponId, int shieldId) {
        if (runeId == AIR_RUNE) {
            // air staff
            return weaponId == 1381 || weaponId == 21777;
        } else if (runeId == WATER_RUNE) {
            // water staff
            return weaponId == 1383 || shieldId == 18346;
        } else if (runeId == EARTH_RUNE) {
            // earth staff
            return weaponId == 1385;
        } else if (runeId == FIRE_RUNE) {
            // fire staff
            return weaponId == 1387;
        }
        return false;
    }

    /**
     * If we have a staff of light
     *
     * @param weaponId The id of the weapon equipped
     */
    private static boolean hasStaffOfLight(int weaponId) {
        return weaponId == 15486 || weaponId == 22207 || weaponId == 22209 || weaponId == 22211 || weaponId == 22213;
    }

}
