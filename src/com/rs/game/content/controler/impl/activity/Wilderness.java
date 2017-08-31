package com.rs.game.content.controler.impl.activity;

import com.rs.cache.loaders.ObjectDefinitions;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.ForceMovement;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.Skills;
import com.rs.game.content.controler.Controler;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.utility.Misc;

public class Wilderness extends Controler {

	private boolean showingSkull;

	@Override
	public void start() {
		checkBoosts(player);
	}

	public static void checkBoosts(Player player) {
		boolean changed = false;
		int level = player.getSkills().getLevelForXp(Skills.ATTACK);
		int maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.ATTACK)) {
			player.getSkills().set(Skills.ATTACK, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.STRENGTH);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.STRENGTH)) {
			player.getSkills().set(Skills.STRENGTH, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.DEFENCE);
		maxLevel = (int) (level + 5 + (level * 0.15));
		if (maxLevel < player.getSkills().getLevel(Skills.DEFENCE)) {
			player.getSkills().set(Skills.DEFENCE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.RANGE);
		maxLevel = (int) (level + 5 + (level * 0.1));
		if (maxLevel < player.getSkills().getLevel(Skills.RANGE)) {
			player.getSkills().set(Skills.RANGE, maxLevel);
			changed = true;
		}
		level = player.getSkills().getLevelForXp(Skills.MAGIC);
		maxLevel = level + 5;
		if (maxLevel < player.getSkills().getLevel(Skills.MAGIC)) {
			player.getSkills().set(Skills.MAGIC, maxLevel);
			changed = true;
		}
		if (changed)
			player.getPackets().sendGameMessage(
					"Your extreme potion bonus has been reduced.");
	}

	@Override
	public boolean login() {
		moved();
		return false;
	}

	@Override
	public boolean keepCombating(Actor target) {
		if (target instanceof NPC)
			return true;
		if (!canAttack(target))
			return false;
		if (target.getAttackedBy() != player
				&& player.getAttackedBy() != target)
			player.setWildernessSkull();
		return true;
	}

	@Override
	public boolean canAttack(Actor target) {
		if (target instanceof Player) {
			Player p2 = (Player) target;
			if (player.isCanPvp() && !p2.isCanPvp()) {
				player.getPackets().sendGameMessage(
						"That player is not in the wilderness.");
				return false;
			}
			if (canHit(target))
				return true;

			// warning message here
			return false;
		}
		return true;
	}

	@Override
	public boolean canHit(Actor target) {
		if (target instanceof NPC)
			return true;
		Player p2 = (Player) target;
		return Math.abs(player.getSkills().getCombatLevel() - p2.getSkills().getCombatLevel()) <= getWildLevel();
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		if (getWildLevel() > 20) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;

	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		if (getWildLevel() > 20) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		if (player.getTeleBlockDelay() > Misc.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		Long teleblock = (Long) player.getTemporaryAttributtes().get(
				"TeleBlocked");
		if (teleblock != null && teleblock > Misc.currentTimeMillis()) {
			player.getPackets().sendGameMessage(
					"A mysterious force prevents you from teleporting.");
			return false;
		}
		return true;
	}

	public void showSkull() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 19,
				381);
	}

	public static boolean isDitch(int id) {
		return id >= 1440 && id <= 1444 || id >= 65076 && id <= 65087;
	}

	@Override
	public boolean processObjectClick1(final WorldObject object) {
		if (isDitch(object.getId())) {
			player.setInfiniteStopDelay();
			player.setNextAnimation(new Animation(6132));
			final WorldTile toTile = new WorldTile(player.getX(),
					object.getY() - 1, object.getPlane());
			player.setNextForceMovement(new ForceMovement(
					new WorldTile(player), 1, toTile, 2, 2));
			final ObjectDefinitions objectDef = object.getDefinitions();
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					player.setNextWorldTile(toTile);
					player.setNextFaceWorldTile(new WorldTile(
							object.getCoordFaceX(objectDef.getSizeX(),
									objectDef.getSizeY(), object.getRotation()),
							object.getCoordFaceY(objectDef.getSizeX(),
									objectDef.getSizeY(), object.getRotation()),
							object.getPlane()));
					removeIcon();
					removeControler();
					player.resetLockDelay();
				}
			}, 2);
			return false;
		}
		return true;
	}

	@Override
	public void sendInterfaces() {
		if (isAtWild(player))
			showSkull();
	}

	@Override
	public boolean sendDeath() {
		removeIcon();
		removeControler();
		return true; // TODO custom dead without graves
	}

	@Override
	public void moved() {
		boolean isAtWild = isAtWild(player);
		boolean isAtWildSafe = isAtWildSafe();
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setCanPvp(true);
			showSkull();
			player.getAppearence().generateAppearenceData();
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			removeIcon();
		} else if (!isAtWildSafe && !isAtWild) {
			player.setCanPvp(false);
			removeIcon();
			removeControler();
		}
	}

	public void removeIcon() {
		if (showingSkull) {
			showingSkull = false;
			player.setCanPvp(false);
			player.getPackets()
					.closeInterface(
							player.getInterfaceManager().hasRezizableScreen() ? 10
									: 19);
			player.getAppearence().generateAppearenceData();
			player.getEquipment().refresh(null);
		}
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public void forceClose() {
		removeIcon();
	}

	public static final boolean isAtWild(WorldTile tile) {
		return (tile.getX() >= 2940 && tile.getX() <= 3395
				&& tile.getY() >= 3525 && tile.getY() <= 4000)
				|| (tile.getX() >= 3264 && tile.getX() <= 3279
						&& tile.getY() >= 3279 && tile.getY() <= 3672)
				|| (tile.getX() >= 2756 && tile.getX() <= 2875
						&& tile.getY() >= 5512 && tile.getY() <= 5627)
				|| (tile.getX() >= 3158 && tile.getX() <= 3181
						&& tile.getY() >= 3679 && tile.getY() <= 3697)
				|| (tile.getX() >= 3280 && tile.getX() <= 3183
						&& tile.getY() >= 3883 && tile.getY() <= 3888);
	}

	public boolean isAtWildSafe() {
		return (player.getX() >= 2940 && player.getX() <= 3395
				&& player.getY() <= 3524 && player.getY() >= 3523);
	}

	public int getWildLevel() {
		return (player.getY() - 3520) / 8 + 1;
	}

	@Override
	public boolean handleItemOption1(Player playerr, int slotId, int itemId,
			Item item) {
		if (itemId != item.getId())
			return false;
		switch (itemId) {
		case -1: // Noobs
			return false;
		}
		return true;
	}
}