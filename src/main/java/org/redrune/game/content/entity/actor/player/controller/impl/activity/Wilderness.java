package org.redrune.game.content.entity.actor.player.controller.impl.activity;

import org.redrune.game.content.entity.actor.player.controller.Controller;
import org.redrune.game.entity.Entity;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.ClickOption;
import plugin.object.WildernessDitchObjectPlugin;

public class Wilderness extends Controller {
	
	private boolean showingSkull;
	
	@Override
	public void start() {
		checkBoosts(player);
		moved();
	}
	
	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getLevelForXp(SkillConstants.ATTACK);
		int maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(SkillConstants.ATTACK)) {
			player.getSkills().set(SkillConstants.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(SkillConstants.STRENGTH);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(SkillConstants.STRENGTH)) {
			player.getSkills().set(SkillConstants.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(SkillConstants.DEFENCE);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(SkillConstants.DEFENCE)) {
			player.getSkills().set(SkillConstants.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(SkillConstants.RANGE);
		maxLevel = (int) (level + 5 + (level * 0.1));
		if (maxLevel < player.getSkills().getLevel(SkillConstants.RANGE)) {
			player.getSkills().set(SkillConstants.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(SkillConstants.MAGIC);
		maxLevel = level + 5;
		if (maxLevel < player.getSkills().getLevel(SkillConstants.MAGIC)) {
			player.getSkills().set(SkillConstants.MAGIC, maxLevel);
			changed = true;
		}
		if (changed) {
			player.getPackets().sendMessage("Your extreme potion bonus has been reduced.");
		}
	}
	
	public static boolean isAtWild(WorldTile tile) {
		return (tile.getX() >= 2940 && tile.getX() <= 3395 && tile.getY() >= 3525 && tile.getY() <= 4000) || (tile.getX() >= 3264 && tile.getX() <= 3279 && tile.getY() >= 3279 && tile.getY() <= 3672) || (tile.getX() >= 2756 && tile.getX() <= 2875 && tile.getY() >= 5512 && tile.getY() <= 5627) || (tile.getX() >= 3158 && tile.getX() <= 3181 && tile.getY() >= 3679 && tile.getY() <= 3697) || (tile.getX() >= 3280 && tile.getX() <= 3183 && tile.getY() >= 3883 && tile.getY() <= 3888);
	}
	
	public boolean isAtWildSafe() {
		return (player.getX() >= 2940 && player.getX() <= 3395 && player.getY() <= 3524 && player.getY() >= 3523);
	}
	
	public void showSkull() {
		player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 10 : 19, 381);
	}
	
	public void removeIcon() {
		if (showingSkull) {
			showingSkull = false;
			player.getAttributes().setCanPvp(false);
			player.getPackets().closeInterface(player.getInterfaceManager().hasRezizableScreen() ? 10 : 19);
			player.getAppearance().generateAppearanceData();
			player.getEquipment().refresh(null);
		}
	}
	
	@Override
	public boolean keepCombating(Actor target) {
		if (target instanceof NPC) {
			return true;
		}
		if (!canAttack(target)) {
			return false;
		}
		if (target.getAttackedBy() != player && player.getAttackedBy() != target) {
			player.getAttributes().setWildernessSkull();
		}
		return true;
	}
	
	@Override
	public boolean canAttack(Actor target) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.getAttributes().isCanPvp() && !p2.getAttributes().isCanPvp()) {
				player.getPackets().sendMessage("You can't attack players who aren't in the Wilderness.");
				return false;
			}
			if (canHit(target)) {
				return true;
			}
			player.getPackets().sendMessage("Your level difference is too great!<br>You need to move deeper into the Wilderness.");
			// warning message here
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canHit(Actor target) {
		if (target instanceof NPC) {
			return true;
		}
		Player p2 = (Player) target;
		return Math.abs(player.getSkills().getCombatLevel() - p2.getSkills().getCombatLevel()) <= getWildLevel();
	}
	
	@Override
	public void moved() {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe();
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.getAttributes().setCanPvp(true);
			showSkull();
			player.getAppearance().generateAppearanceData();
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon();
		} else if (!isAtWildSafe && !isAtWild) {
			player.getAttributes().setCanPvp(false);
			removeIcon();
			removeController();
		}
	}
	
	@Override
	public void sendInterfaces() {
		if (isAtWild(player)) {
			showSkull();
		}
	}
	
	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (getWildLevel() > 20) {
			player.getPackets().sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.isTeleblocked()) {
			player.getPackets().sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;

	}
	
	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		if (getWildLevel() > 20) {
			player.getPackets().sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getAttributes().getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.getPackets().sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		Long teleBlockDelay = player.getAttributes().getTeleBlockDelay();
		if (teleBlockDelay != -1 && teleBlockDelay > Misc.currentTimeMillis()) {
			player.getPackets().sendMessage("A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean sendDeath() {
		removeIcon();
		removeController();
		return true;
	}
	
	@Override
	public boolean login() {
		moved();
		return false;
	}
	
	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}
	
	@Override
	public void forceClose() {
		removeIcon();
	}
	
	@Override
	public boolean canEntityClick(Entity entity, ClickOption option) {
		WorldObject object = entity.toObject();
		if (entity.isObject() && isDitch(object.getId())) {
			WildernessDitchObjectPlugin.performJump(player, object, () -> {
				removeIcon();
				removeController();
			});
			return false;
		}
		return super.canEntityClick(entity, option);
	}
	
	public static boolean isDitch(int id) {
		return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
	}
	
	public int getWildLevel() {
		return (player.getY() - 3520) / 8 + 1;
	}
	
}