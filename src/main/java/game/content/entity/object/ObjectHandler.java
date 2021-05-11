package game.content.entity.object;

import cache.codec.loaders.ObjectDefinitions;
import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.GameFlags;
import game.content.entity.actor.combat.CombatAlgorithm;
import game.content.entity.actor.combat.function.Magic;
import game.content.entity.actor.player.action.Action;
import game.content.entity.actor.player.market.exchange.ExchangeManager;
import game.content.entity.actor.player.skills.hunter.Hunter.HunterNPC;
import game.content.entity.actor.player.skills.mining.EssenceMining;
import game.content.entity.actor.player.skills.mining.EssenceMining.EssenceDefinitions;
import game.content.entity.actor.player.skills.mining.Mining;
import game.content.entity.actor.player.skills.mining.Mining.RockDefinitions;
import game.content.entity.actor.player.skills.runecrafting.Runecrafting;
import game.content.entity.actor.player.skills.summoning.Summoning;
import game.content.entity.actor.player.skills.thieving.Thieving;
import game.content.entity.actor.player.skills.woodcutting.Woodcutting;
import game.content.entity.actor.player.skills.woodcutting.Woodcutting.TreeDefinitions;
import game.content.plugin.PluginRepository;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.ForceMovement;
import game.entity.actor.mask.Graphics;
import game.entity.actor.player.Player;
import game.entity.actor.player.data.RouteEvent;
import game.entity.actor.player.link.OwnedObjectManager;
import game.entity.object.WorldObject;
import game.global.WorldTile;
import game.global.map.region.RegionManager;
import utility.constants.GameConstants;
import utility.constants.SkillConstants;
import utility.functions.Misc;
import utility.game.entity.object.ObjectRemoval;
import plugin.command.owner.StopObjectSpawnCommandPlugin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utility.game.ClickOption.*;

public class ObjectHandler {

    public static void handleOption1(final Player player, final WorldObject object) {
        final String option = object.getDefinitions().getOption(1);
        final ObjectDefinitions objectDef = object.getDefinitions();
        player.setRouteEvent(new RouteEvent(object, () -> {
            player.setNextFaceWorldTile(object.getFaceLocation());
            if (!player.getControllerManager().canEntityClick(object, FIRST)) {
                return;
            }
            HunterNPC hunterNpc = HunterNPC.forObjectId(object.getId());
            if (hunterNpc != null) {
                if (OwnedObjectManager.removeObject(player, object)) {
                    player.setNextAnimation(hunterNpc.getEquipment().getPickUpAnimation());
                    player.getInventory().addItem(hunterNpc.getItem(), 1);
                    player.getInventory().addItem(hunterNpc.getEquipment().getId(), 1);
                    player.getSkills().addXp(SkillConstants.HUNTER, hunterNpc.getXp());
                    player.getAttributes().setTrapAmount(player.getAttributes().getTrapAmount() - 1);
                } else {
                    player.getPackets().sendMessage("This isn't your trap.");
                }
            } else if (object.getId() == 2350 && (object.getX() == 3352 && object.getY() == 3417 && object.getPlane() == 0)) {
                player.useStairs(832, new WorldTile(3177, 5731, 0), 1, 2);
            } else if (object.getId() == 2353 && (object.getX() == 3177 && object.getY() == 5730 && object.getPlane() == 0)) {
                player.useStairs(828, new WorldTile(3353, 3416, 0), 1, 2);
            } else if (object.getId() == 10949 || object.getId() == 18994 || object.getId() == 18995 || object.getId() == 3043 || object.getId() == 18996 || object.getId() == 3038 || object.getId() == 3245 || object.getId() == 11933 || object.getId() == 11934 || object.getId() == 11935 || object.getId() == 11957 || object.getId() == 11958 || object.getId() == 11959) {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Tin_Ore));
            } else if (object.getId() == 37312 || object.getId() == 11952 || object.getId() == 37310) // gold ore
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Gold_Ore));
            } else if (object.getId() == 19000 || object.getId() == 19001 || object.getId() == 19002 || object.getId() == 37309 || object.getId() == 37307 || object.getId() == 11954 || object.getId() == 11955 || object.getId() == 11956) // iron ore
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Iron_Ore));
            } else if (object.getId() == 37306 || object.getId() == 2311 || object.getId() == 37304 || object.getId() == 37305) // silver ore
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Silver_Ore));
            } else if (object.getId() == 10948 || object.getId() == 18997 || object.getId() == 18998 || object.getId() == 18999 || object.getId() == 14850 || object.getId() == 14851 || object.getId() == 3233 || object.getId() == 3032 || object.getId() == 11930 || object.getId() == 11931 || object.getId() == 11932) // coal ore
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Coal_Ore));
            } else if (object.getId() == 18991 || object.getId() == 18992 || object.getId() == 18993 || object.getId() == 3042 || object.getId() == 3027 || object.getId() == 3229 || object.getId() == 11936 || object.getId() == 11937 || object.getId() == 11938 || object.getId() == 11960 || object.getId() == 11961 || object.getId() == 11962) // copper
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Copper_Ore));
            } else if (object.getId() == 3041 || object.getId() == 3280 || object.getId() == 11942 || object.getId() == 11944) // mithril
            // ore
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Mithril_Ore));
            } else if (object.getId() == 3273 || object.getId() == 3040 || object.getId() == 11939 || object.getId() == 11941) // adamant
            // ore
            {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Adamant_Ore));
            } else if (object.getId() == 14860 || object.getId() == 14861) {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Runite_Ore));
            } else if (object.getId() == 10947) {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Granite_Ore));
            } else if (object.getId() == 10946) {
                player.getActionManager().setAction(new Mining(object, RockDefinitions.Sandstone_Ore));
            } else if (object.getId() == 11554 || object.getId() == 11552) {
                player.getPackets().sendMessage("That rock is currently unavailable.");
            } else if (object.getId() == 2491) {
                Action skill = new EssenceMining(object, player.getSkills().getLevel(SkillConstants.MINING) < 30 ? EssenceDefinitions.Rune_Essence : EssenceDefinitions.Pure_Essence);
                player.getActionManager().setAction(skill);
            } else if (object.getId() == 2478) {
                Runecrafting.craftEssence(player, 556, 1, 5, false, 11, 2, 22, 3, 34, 4, 44, 5, 55, 6, 66, 7, 77, 88, 9, 99, 10);
            } else if (object.getId() == 2479) {
                Runecrafting.craftEssence(player, 558, 2, 5.5, false, 14, 2, 28, 3, 42, 4, 56, 5, 70, 6, 84, 7, 98, 8);
            } else if (object.getId() == 2480) {
                Runecrafting.craftEssence(player, 555, 5, 6, false, 19, 2, 38, 3, 57, 4, 76, 5, 95, 6);
            } else if (object.getId() == 2481) {
                Runecrafting.craftEssence(player, 557, 9, 6.5, false, 26, 2, 52, 3, 78, 4);
            } else if (object.getId() == 2482) {
                Runecrafting.craftEssence(player, 554, 14, 7, false, 35, 2, 70, 3);
            } else if (object.getId() == 2483) {
                Runecrafting.craftEssence(player, 559, 20, 7.5, false, 46, 2, 92, 3);
            } else if (object.getId() == 2484) {
                Runecrafting.craftEssence(player, 564, 27, 8, true, 59, 2);
            } else if (object.getId() == 2487) {
                Runecrafting.craftEssence(player, 562, 35, 8.5, true, 74, 2);
            } else if (object.getId() == 17010) {
                Runecrafting.craftEssence(player, 9075, 40, 8.7, true, 82, 2);
            } else if (object.getId() == 2486) {
                Runecrafting.craftEssence(player, 561, 45, 9, true, 91, 2);
            } else if (object.getId() == 2485) {
                Runecrafting.craftEssence(player, 563, 50, 9.5, true);
            } else if (object.getId() == 2488) {
                Runecrafting.craftEssence(player, 560, 65, 10, true);
            } else if (object.getId() == 30624) {
                Runecrafting.craftEssence(player, 565, 77, 10.5, true);
            } else if (object.getId() == 2452) {
                int hatId = player.getEquipment().getHatId();
                if (hatId == Runecrafting.AIR_TIARA || hatId == Runecrafting.OMNI_TIARA) {
                    Runecrafting.enterAirAltar(player);
                }
            } else if (object.getId() == 2455) {
                int hatId = player.getEquipment().getHatId();
                if (hatId == Runecrafting.EARTH_TIARA || hatId == Runecrafting.OMNI_TIARA) {
                    Runecrafting.enterEarthAltar(player);
                }
            } else if (object.getId() == 2456) {
                int hatId = player.getEquipment().getHatId();
                if (hatId == Runecrafting.FIRE_TIARA || hatId == Runecrafting.OMNI_TIARA) {
                    Runecrafting.enterFireAltar(player);
                }
            } else if (object.getId() == 2454) {
                int hatId = player.getEquipment().getHatId();
                if (hatId == Runecrafting.WATER_TIARA || hatId == Runecrafting.OMNI_TIARA) {
                    Runecrafting.enterWaterAltar(player);
                }
            } else if (object.getId() == 2457) {
                int hatId = player.getEquipment().getHatId();
                if (hatId == Runecrafting.BODY_TIARA || hatId == Runecrafting.OMNI_TIARA) {
                    Runecrafting.enterBodyAltar(player);
                }
            } else if (object.getId() == 2453) {
                int hatId = player.getEquipment().getHatId();
                if (hatId == Runecrafting.MIND_TIARA || hatId == Runecrafting.OMNI_TIARA) {
                    Runecrafting.enterMindAltar(player);
                }
                //Start of leaving portals
            } else if (object.getId() == 2465) {
                player.teleportPlayer(3128, 3408, 0); //air altar portal

            } else if (object.getId() == 2466) {
                player.teleportPlayer(2980, 3512, 0); //mind altar portal

            } else if (object.getId() == 2467) {
                player.teleportPlayer(3184, 3167, 0); //water altar portal

            } else if (object.getId() == 2468) {
                player.teleportPlayer(3304, 3475, 0); //earth altar portal

            } else if (object.getId() == 2469) {
                player.teleportPlayer(3311, 3256, 0); //fire altar portal

            } else if (object.getId() == 2470) {
                player.teleportPlayer(3055, 3444, 0); //body altar portal

            } else if (object.getId() == 2474) {
                player.teleportPlayer(3062, 3591, 0); //chaos altar portal
            } else if (object.getId() == 36972) {
                player.setNextAnimation(new Animation(712));
                player.setNextGraphics(new Graphics(624));
                player.getPackets().sendMessage("You pray to the gods.");
                player.getInventory().deleteItem(536, 1);
            } else if (object.getId() == 36972) {
                player.setNextAnimation(new Animation(712));
                player.setNextGraphics(new Graphics(624));
                player.getPackets().sendMessage("You pray to the gods.");
                player.getInventory().deleteItem(18830, 1);
            } else if (object.getId() == 36786) {
                player.getDialogueManager().startDialogue("Banker", 4907);
            } else if (object.getId() == 42377 || object.getId() == 42378) {
                player.getDialogueManager().startDialogue("Banker", 2759);
            } else if (object.getId() == 42217 || object.getId() == 782 || object.getId() == 34752 || object.getId() == 4369) {
                player.getDialogueManager().startDialogue("Banker", 553);
            } else if (object.getId() == 57437 || object.getId() == 6084 || object.getId() == 22819 || object.getId() == 25808) {
                player.getBank().openBank();
            } else if (object.getId() == 42425 && object.getX() == 3220 && object.getY() == 3222) { // zaros portal
                player.useStairs(10256, new WorldTile(3353, 3416, 0), 4, 5, "And you find yourself into a digsite.");
                player.addWalkSteps(3222, 3223, -1, false);
                player.getPackets().sendMessage("You examine portal and it aborves you...");
            } else if (object.getId() == 46500 && object.getX() == 3351 && object.getY() == 3415) { // zaros portal
                player.useStairs(-1, new WorldTile(GameConstants.RESPAWN_TILE.getX(), GameConstants.RESPAWN_TILE.getY(), GameConstants.RESPAWN_TILE.getPlane()), 2, 3, "You found your way back to home.");
                player.addWalkSteps(3351, 3415, -1, false);
            } else if (object.getId() == 9293) {
                if (player.getSkills().getLevel(SkillConstants.AGILITY) < 70) {
                    player.getPackets().sendMessage("You need an agility level of 70 to use this obstacle.", true);
                    return;
                }
                int x1 = player.getX() == 2886 ? 2892 : 2886;
                WorldTasksManager.schedule(new WorldTask() {
                    int count = 0;

                    @Override
                    public void run() {
                        player.setNextAnimation(new Animation(844));
                        if (count++ == 1) {
                            stop();
                        }
                    }

                }, 0, 0);
                player.setNextForceMovement(new ForceMovement(new WorldTile(x1, 9799, 0), 3, player.getX() == 2886 ? 1 : 3));
                player.useStairs(-1, new WorldTile(x1, 9799, 0), 3, 4);
            } else if (object.getId() == 42611) {// Magic Portal
                player.getDialogueManager().startDialogue("MagicPortal");
            } else if (object.getId() == 27254) {// Edgeville portal
                player.getPackets().sendMessage("You enter the portal...");
                player.useStairs(10584, new WorldTile(3087, 3488, 0), 2, 3, "..and are transported to Edgeville.");
                player.addWalkSteps(1598, 4506, -1, false);
            } else if (object.getId() == 15522) {// portal sign
                if (player.withinDistance(new WorldTile(1598, 4504, 0), 1)) {
                    player.getInterfaceManager().sendInterface(327);
                    player.getPackets().sendIComponentText(327, 13, "Edgeville");
                    player.getPackets().sendIComponentText(327, 14, "This portal will take you to edgeville. There " + "you can multi pk once past the wilderness ditch.");
                }
                if (player.withinDistance(new WorldTile(1598, 4508, 0), 1)) {// PORTAL
                    // 2
                    player.getInterfaceManager().sendInterface(327);
                    player.getPackets().sendIComponentText(327, 13, "Mage Bank");
                    player.getPackets().sendIComponentText(327, 14, "This portal will take you to the mage bank. " + "The mage bank is a 1v1 deep wilderness area.");
                }
                if (player.withinDistance(new WorldTile(1598, 4513, 0), 1)) {// PORTAL
                    // 3
                    player.getInterfaceManager().sendInterface(327);
                    player.getPackets().sendIComponentText(327, 13, "Magic's Portal");
                    player.getPackets().sendIComponentText(327, 14, "This portal will allow you to teleport to areas that " + "will allow you to change your magic spell book.");
                }
            } else if (object.getId() == 37929) {// corp beast
                if (object.getX() == 2971 && object.getY() == 4382 && object.getPlane() == 0) {
                    player.getInterfaceManager().sendInterface(650);
                } else if (object.getX() == 2918 && object.getY() == 4382 && object.getPlane() == 0) {
                    player.stopAll();
                    player.setNextWorldTile(new WorldTile(player.getX() == 2921 ? 2917 : 2921, player.getY(), player.getPlane()));
                }
            } else if (object.getId() == 37928 && object.getX() == 2883 && object.getY() == 4370 && object.getPlane() == 0) {
                player.stopAll();
                player.setNextWorldTile(new WorldTile(3214, 3782, 0));
                player.getControllerManager().startController("Wilderness");
            } else if (object.getId() == 38815 && object.getX() == 3209 && object.getY() == 3780 && object.getPlane() == 0) {
                if (player.getSkills().getLevelForXp(SkillConstants.WOODCUTTING) < 37 || player.getSkills().getLevelForXp(SkillConstants.MINING) < 45 || player.getSkills().getLevelForXp(SkillConstants.SUMMONING) < 23 || player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) < 47 || player.getSkills().getLevelForXp(SkillConstants.PRAYER) < 55) {
                    player.getPackets().sendMessage("You need 23 Summoning, 37 Woodcutting, 45 Mining, 47 Firemaking and 55 Prayer to enter this dungeon.");
                    return;
                }
                player.stopAll();
                player.setNextWorldTile(new WorldTile(2885, 4372, 0));
                player.getControllerManager().forceStop();
            } else if (object.getId() == 9369) {
                player.getControllerManager().startController("FightPits");
            } else if (object.getId() == 20602) {
                player.teleportPlayer(2954, 9675, 0);
                player.getPackets().sendMessage("You enter the dark cave and arrive to Gamers' Grotto.");
            } else if (object.getId() == 20604) {
                player.teleportPlayer(3018, 3405, 0);
                player.getPackets().sendMessage("You leave the mysterious cave and you return to the surface.");
            } else if (object.getId() == 50205) {
                Summoning.infusePouches(player);
            } else if (object.getId() == 1817 && object.getX() == 2273 && object.getY() == 4680) { // kbd lever
                Magic.pushLeverTeleport(player, new WorldTile(3067, 10254, 0));
            } else if (object.getId() == 1816 && object.getX() == 3067 && object.getY() == 10252) { // kbd out lever
                Magic.pushLeverTeleport(player, new WorldTile(2273, 4681, 0));
            } else if (object.getId() == 32015 && object.getX() == 3069 && object.getY() == 10256) { // kbd stairs
                player.useStairs(828, new WorldTile(3017, 3848, 0), 1, 2);
                player.getControllerManager().startController("Wilderness");
            } else if (object.getId() == 1765 && object.getX() == 3017 && object.getY() == 3849) { // kbd out stairs
                player.stopAll();
                player.setNextWorldTile(new WorldTile(3069, 10255, 0));
                player.getControllerManager().forceStop();
            } else if (object.getId() == 5959) {
                Magic.pushLeverTeleport(player, new WorldTile(2539, 4712, 0));
            } else if (object.getId() == 5960) {
                Magic.pushLeverTeleport(player, new WorldTile(3089, 3957, 0));
            } else if (object.getId() == 2273) {
                player.setNextWorldTile(new WorldTile(2851, 5933, 0));
                player.getPackets().sendMessage("Use your fire cape on the floating orb to bring out Har'Arken.");
                player.getPackets().sendMessage("WARNING     WARNING     WARNING     WARNING     WARNING     WARNING     WARNING");
                player.getPackets().sendMessage("You will lose your fire cape and not be able to get it back, but gain the kiln cape if you win!");
            } else if (object.getId() == 62676) { // dominion exit
                player.useStairs(-1, new WorldTile(3374, 3093, 0), 0, 1);
            } else if (object.getId() == 62674) { // dominion entrance
                player.useStairs(-1, new WorldTile(3744, 6405, 0), 0, 1);
            } else {
                switch (objectDef.getName().toLowerCase()) {
                    case "web":
                        if (objectDef.containsOption(0, "Slash")) {
                            player.setNextAnimation(new Animation(CombatAlgorithm.getWeaponAttackEmote(player.getEquipment().getWeaponId(), player.getCombatDefinitions().getAttackStyle())));
                            slashWeb(player, object);
                        }
                        break;
                    case "bank booth":
                        if (objectDef.containsOption(0, "Bank") || objectDef.containsOption(0, "Use")) {
                            player.getBank().openBank();
                        }
                        break;
                    case "bank chest":
                        if (objectDef.containsOption(0, "Use")) {
                            player.getBank().openBank();
                        }
                    case "bank deposit box":
                        if (objectDef.containsOption(0, "Deposit")) {
                            player.getBank().openDepositBox();
                        }
                        break;
                    case "bank":
                        player.getBank().openBank();
                        break;
                    // Woodcutting start
                    case "tree":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.NORMAL));
                        }
                        break;
                    case "dead tree":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.DEAD));
                        }
                        break;
                    case "oak":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK));
                        }
                        break;
                    case "willow":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW));
                        }
                        break;
                    case "maple tree":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE));
                        }
                        break;
                    case "ivy":
                        if (objectDef.containsOption(0, "Chop")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.IVY));
                        }
                        break;
                    case "yew":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW));
                        }
                        break;
                    case "magic tree":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC));
                        }
                        break;
                    case "cursed magic tree":
                        if (objectDef.containsOption(0, "Chop down")) {
                            player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.CURSED_MAGIC));
                        }
                        break;
                    case "altar":
                        if (objectDef.containsOption(0, "Pray-at")) {
                            final int maxPrayer = player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 10;
                            if (player.getPrayer().getPrayerpoints() < maxPrayer) {
                                player.getLocks().lock((int) (long) 5);
                                player.getPackets().sendMessage("You pray to the gods...", true);
                                player.setNextAnimation(new Animation(645));
                                WorldTasksManager.schedule(new WorldTask() {
                                    @Override
                                    public void run() {
                                        player.getPrayer().restorePrayer(maxPrayer);
                                        player.getPackets().sendMessage("...and recharged your prayer.", true);
                                    }
                                }, 2);
                            } else {
                                player.getPackets().sendMessage("You already have full prayer.", true);
                            }
                        }
                        break;
                    default:
                        if (PluginRepository.handleObject(player, object, option)) {
                            return;
                        }
                        player.getPackets().sendMessage("Nothing interesting happens.");
                        if (GameFlags.debugMode) {
                            System.out.println("No plugin registered for option " + option + " on object " + object);
                        }
                        break;
                }
            }
        }));
    }

    public static void handleOption2(final Player player, final WorldObject object) {
        final String option = object.getDefinitions().getOption(2);
        final ObjectDefinitions objectDef = object.getDefinitions();
        player.setRouteEvent(new RouteEvent(object, () -> {
            player.setNextFaceWorldTile(object.getFaceLocation());
            if (!player.getControllerManager().canEntityClick(object, SECOND)) {
                return;
            }
            if (object.getId() == 36786 || object.getId() == 42378 || object.getId() == 42377 || object.getId() == 42217 || object.getId() == 27663 || object.getId() == 57437 || object.getId() == 6084 || object.getId() == 22819 || object.getId() == 25808) {
                player.getBank().openBank();
            } else if (object.getDefinitions().getName().equalsIgnoreCase("furnace")) {
                player.getDialogueManager().startDialogue("SmeltingD", object);
            } else if (object.getId() == 11758 || object.getId() == 782 || object.getDefinitions().getName().toLowerCase().contains("bank")) {
                player.getBank().openBank();
            } else if (object.getId() == 34384 || object.getId() == 34383 || object.getId() == 14011 || object.getId() == 7053 || object.getId() == 34387 || object.getId() == 34386 || object.getId() == 34385) {
                Thieving.handleStalls(player, object);
            } else {
                if (PluginRepository.handleObject(player, object, option)) {
                    return;
                }
                player.getPackets().sendMessage("Nothing interesting happens.");
                if (GameFlags.debugMode) {
                    System.out.println("Second clicked object [" + object + "]");
                }
            }
        }));
    }

    public static void handleOption3(final Player player, final WorldObject object) {
        final String option = object.getDefinitions().getOption(3);
        final ObjectDefinitions objectDef = object.getDefinitions();
        player.setRouteEvent(new RouteEvent(object, () -> {
            player.setNextFaceWorldTile(object.getFaceLocation());
            if (!player.getControllerManager().canEntityClick(object, THIRD)) {
                return;
            }
            player.setNextFaceWorldTile(object);
            if (PluginRepository.handleObject(player, object, option)) {
                return;
            } else if (object.getDefinitions().getName().toLowerCase().contains("bank")) {
                ExchangeManager.INSTANCE.openCollectionBox(player);
            } else {
                player.getPackets().sendMessage("Nothing interesting happens.");
                if (GameFlags.debugMode) {
                    System.out.println("No plugin registered for option 3 " + option + " on object " + object);
                }
            }
        }));
    }

    public static void handleExamine(final Player player, final WorldObject object) {
        if (player.getTemporaryAttribute("door_finding", false)) {
            int doorId = object.getId();
            ObjectDefinitions base = ObjectDefinitions.getObjectDefinitions(doorId);
            String name = base.getName().toLowerCase();
            List<ObjectDefinitions> results = new ArrayList<>();
            for (int i = doorId - 1000; i < doorId + 1000; i++) {
                ObjectDefinitions o = ObjectDefinitions.getObjectDefinitions(i);
                if (o == null) {
                    continue;
                }
                String oName = o.getName().toLowerCase();
                if (oName.equalsIgnoreCase(name) && o.containsOption("Close")) {
                    results.add(o);
                }
            }
            results.forEach(result -> System.out.println("[" + object.getId() + ", " + object.getType() + ", " + object.getRotation() + "] Possible Door: [" + result.getId() + ", " + result.getName() + " ," + Arrays.toString(result.getOptions())));
        }
        if (player.getTemporaryAttribute("removing_objects", false)) {
            try {
                if (object.isSpawned()) {
                    player.getPackets().sendMessage("Unable to remove; spawned object[" + object + "]");
                    return;
                }
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ObjectRemoval.NONSPAWNING_OBJECTS_FILE, true)));
                out.println(object.getId() + " " + object.getType() + " " + object.getRotation() + " " + object.getX() + " " + object.getY() + " " + object.getPlane());
                out.close();
                RegionManager.removeObject(object);
                StopObjectSpawnCommandPlugin.STOPPED_OBJECTS.add(object);
                System.out.println("Added " + object.getId() + " to be removed from spawns.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        player.getPackets().sendMessage("It's a " + object.getDefinitions().getName() + ".");
        if (GameFlags.debugMode) {
            player.getPackets().sendMessage(object.toString());
        }
    }

    public static void slashWeb(Player player, WorldObject object) {
        if (Misc.getRandom(1) == 0) {
            RegionManager.spawnTemporaryObject(new WorldObject(object.getId() + 1, object.getType(), object.getRotation(), object.getX(), object.getY(), object.getPlane()), 60000);
            player.getPackets().sendMessage("You slash through the web!");
        } else {
            player.getPackets().sendMessage("You fail to cut through the web.");
        }
    }

}