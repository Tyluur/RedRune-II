package org.redrune.game.content.entity.actor.combat.function;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.player.controller.impl.activity.Wilderness;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.spell.SpellPlugin;
import org.redrune.game.content.plugin.combat.spell.type.CombatSpellPlugin;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.WorldTile;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.constants.MagicConstants;
import org.redrune.utility.constants.MagicConstants.MagicBook;
import org.redrune.utility.constants.SkillConstants;

import java.util.Optional;

/**
 * This class contains all references for magic spells
 *
 * @author Matrix Team
 * @author Tyluur <itstyluur@icloud.com>
 */
public class Magic {
	
	public static void setCombatSpell(Player player, int spellId) {
		if (player.getCombatDefinitions().getAutoCastSpell() == spellId) {
			player.getCombatDefinitions().resetSpells(true);
		} else {
			CombatAlgorithm.checkCombatSpell(player, spellId, 0, false);
		}
		player.stopAll();
	}
	
	public static void processNormalSpell(Player player, int spellId) {
		Optional<SpellPlugin> optional = PluginRepository.getSpellPlugin(MagicBook.REGULAR, spellId);
		if (!optional.isPresent()) {
			player.getPackets().sendMessage("This spell has not yet been added.");
			return;
		}
		SpellPlugin plugin = optional.get();
		if (plugin instanceof CombatSpellPlugin) {
			setCombatSpell(player, spellId);
		} else {
			plugin.cast(player, null);
		}
	}
	
	public static void processAncientSpell(Player player, int spellId) {
		Optional<SpellPlugin> optional = PluginRepository.getSpellPlugin(MagicBook.ANCIENTS, spellId);
		if (!optional.isPresent()) {
			player.getPackets().sendMessage("This spell has not yet been added.");
			return;
		}
		SpellPlugin plugin = optional.get();
		if (plugin instanceof CombatSpellPlugin) {
			setCombatSpell(player, spellId);
		} else {
			plugin.cast(player, null);
		}
	}
	
	public static void processLunarSpell(Player player, int spellId) {
		Optional<SpellPlugin> optional = PluginRepository.getSpellPlugin(MagicBook.LUNAR, spellId);
		if (!optional.isPresent()) {
			player.getPackets().sendMessage("This spell has not yet been added.");
			return;
		}
		SpellPlugin plugin = optional.get();
		if (plugin instanceof CombatSpellPlugin) {
			setCombatSpell(player, spellId);
		} else {
			plugin.cast(player, null);
		}
	}
	
	public static void sendNormalTeleportSpell(Player player, int level, double xp, WorldTile tile, int... runes) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, level, xp, tile, 3, true, MagicConstants.MAGIC_TELEPORT, runes);
	}
	
	public static boolean sendItemTeleportSpell(Player player, boolean randomize, int upEmoteId, int upGraphicId, int delay, WorldTile tile) {
		return sendTeleportSpell(player, upEmoteId, -2, upGraphicId, -1, 0, 0, tile, delay, randomize, MagicConstants.ITEM_TELEPORT);
	}
	
	public static boolean sendTeleportSpell(final Player player, int upEmoteId, final int downEmoteId, int upGraphicId, final int downGraphicId, int level, final double xp, final WorldTile tile, int delay, final boolean randomize, final int teleType, int... runes) {// TODO, fix wilderness
		if (player.getLocks().isTeleportLocked()) {
			return false;
		}
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < level) {
			player.getPackets().sendMessage("Your Magic level is not high enough for this spell.");
			return false;
		}
		if (!CombatAlgorithm.checkRunes(player, false, runes)) {
			return false;
		}
		if (teleType == MagicConstants.MAGIC_TELEPORT) {
			if (!player.getControllerManager().processMagicTeleport(tile)) {
				return false;
			}
		} else if (teleType == MagicConstants.ITEM_TELEPORT) {
			if (!player.getControllerManager().processItemTeleport(tile)) {
				return false;
			}
		} else if (teleType == MagicConstants.OBJECT_TELEPORT) {
			if (!player.getControllerManager().processObjectTeleport(tile)) {
				return false;
			}
		}
		CombatAlgorithm.checkRunes(player, true, runes);
		player.stopAll();
		if (upEmoteId != -1) {
			player.setNextAnimation(new Animation(upEmoteId));
		}
		if (upGraphicId != -1) {
			player.setNextGraphics(new Graphics(upGraphicId));
		}
		if (teleType == MagicConstants.MAGIC_TELEPORT) {
			player.getPackets().sendSound(5527, 0, 2);
		}
		player.getLocks().lockTeleport(3 + delay);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				WorldTile teleTile = tile;
				if (randomize) {
					// attemps to randomize tile by 4x4 area
					for (int trycount = 0; trycount < 10; trycount++) {
						teleTile = new WorldTile(tile, 2);
						if (RegionManager.canMoveNPC(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize())) {
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
					player.getSkills().addXp(SkillConstants.MAGIC, xp);
				}
				if (downEmoteId != -1) {
					player.setNextAnimation(new Animation(downEmoteId == -2 ? -1 : downEmoteId));
				}
				if (downGraphicId != -1) {
					player.setNextGraphics(new Graphics(downGraphicId));
				}
				if (teleType == MagicConstants.MAGIC_TELEPORT) {
					player.getPackets().sendSound(5524, 0, 2);
					player.setNextFaceWorldTile(new WorldTile(teleTile.getX(), teleTile.getY() - 1, teleTile.getPlane()));
					player.setFaceDirection(6);
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
		player.getLocks().lock();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getLocks().unlock();
				Magic.sendObjectTeleportSpell(player, false, tile);
			}
		}, 1);
	}
	
	public static void sendObjectTeleportSpell(Player player, boolean randomize, WorldTile tile) {
		sendTeleportSpell(player, 8939, 8941, 1576, 1577, 0, 0, tile, 3, randomize, MagicConstants.OBJECT_TELEPORT);
	}
	
	public static boolean useTabTeleport(final Player player, final int itemId) {
		if (itemId < 8007 || itemId > 8007 + MagicConstants.TABS.length - 1) {
			return false;
		}
		if (useTeleTab(player, MagicConstants.TABS[itemId - 8007])) {
			player.getInventory().deleteItem(itemId, 1);
		}
		return true;
	}
	
	public static boolean useTeleTab(final Player player, final WorldTile tile) {
		if (!player.getControllerManager().processItemTeleport(tile)) {
			return false;
		}
		player.getLocks().lock();
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
						if (RegionManager.canMoveNPC(tile.getPlane(), teleTile.getX(), teleTile.getY(), player.getSize())) {
							break;
						}
						teleTile = tile;
					}
					player.setNextWorldTile(teleTile);
					player.getControllerManager().magicTeleported(MagicConstants.ITEM_TELEPORT);
					if (player.getControllerManager().getController() == null) {
						teleControllersCheck(player, teleTile);
					}
					player.setNextFaceWorldTile(new WorldTile(teleTile.getX(), teleTile.getY() - 1, teleTile.getPlane()));
					player.setFaceDirection(6);
					player.setNextAnimation(new Animation(-1));
					player.getLocks().unlock();
					stop();
				}
			}
		}, 2, 1);
		return true;
	}
}
