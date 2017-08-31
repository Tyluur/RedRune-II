package com.rs.game.content.controler.impl.minigame;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.object.WorldObject;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.item.Item;
import com.rs.game.entity.actor.player.data.Equipment;
import com.rs.game.content.action.impl.PlayerCombatAction;
import com.rs.game.content.node.item.Foods.Food;
import com.rs.game.content.node.item.Pots.Pot;
import com.rs.game.content.controler.Controler;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;

public class Duelarena extends Controler {

	private final Item[] FUN_WEAPONS = {};

	@Override
	public void start() {
		player.stopAll();
		player.addLockDelay(2); // fixes mass click steps
		player.getTemporaryAttributtes().put("startedDuel", true);
		player.getTemporaryAttributtes().put("canFight", false);
		player.reset();
		player.setCanPvp(true);
		player.getHintIconsManager().addHintIcon(
				player.getDuelConfigurations().getOther(player), 1, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			int count = 3;

			@Override
			public void run() {
				if (count > 0)
					player.setNextForceTalk(new ForceTalk("" + count));

				if (count == 0) {
					player.getTemporaryAttributtes().put("canFight", true);
					player.setNextForceTalk(new ForceTalk("FIGHT!"));
					this.stop();
				}
				count--;
			}
		}, 0, 2);
	}

	@Override
	public boolean canEat(Food food) {
		if (player.getDuelConfigurations().getRule(4)) {
			player.getPackets().sendGameMessage(
					"You cannot eat during this duel.", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canPot(Pot pot) {
		if (player.getDuelConfigurations().getRule(3)) {
			player.getPackets().sendGameMessage(
					"You cannot drink during this duel.", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean canMove(int dir) {
		if (player.getDuelConfigurations().getRule(25)) {
			player.getPackets().sendGameMessage(
					"You cannot move during this duel!", true);
			return false;
		}
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager()
				.startDialogue("SimpleMessage",
						"A magical force prevents you from teleporting from the arena.");
		return false;
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		switch (interfaceId) {
		case 271:
			if (player.getDuelConfigurations().getRule(5)) {
				player.getPackets().sendGameMessage(
						"You can't use prayers in this duel.");
				return false;
			}
			return true;
		case 193:
		case 430:
		case 192:
			return !player.getDuelConfigurations().getRule(2);
			case 884:
			switch (componentId) {
			case 4:
				if (player.getDuelConfigurations().getRule(9)) {
					player.getPackets().sendGameMessage(
							"You can't use special attacks in this duel.");
					return false;
				}
				return true;
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		player.getDialogueManager().startDialogue("ForfeitDialouge");
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.addLockDelay(7);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage(
							"Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.setCanPvp(false);
					player.getDuelConfigurations().reward(
							player.getDuelConfigurations().getOther(player));
					// player.getDuelConfigurations().addSpoils(player);
					player.getDuelConfigurations()
							.endDuel(player, false, false);
					player.getDuelConfigurations().endDuel(
							player.getDuelConfigurations().getOther(player),
							false, false);
					removeControler();
					player.getControlerManager()
							.startControler("DuelControler");
					player.getDuelConfigurations().getOther(player)
							.getControlerManager()
							.startControler("DuelControler");
					this.stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {
		return true;
	}

	@Override
	public boolean logout() {
		player.getDuelConfigurations().endDuel(player, false, true);
		player.getDuelConfigurations().endDuel(
				player.getDuelConfigurations().getOther(player), false, false); // other
																				// player
		return false;
	}

	@Override
	public boolean keepCombating(Actor victim) {
		boolean isRanging = PlayerCombatAction.isRanging(player) != 0;
		if (player.getTemporaryAttributtes().get("canFight") == Boolean.FALSE) {
			player.getPackets().sendGameMessage("The duel hasn't started yet.",
					true);
			return false;
		}
		if (player.getDuelConfigurations().getOther(player) != victim)
			return false;
		if (player.getCombatDefinitions().getSpellId() > 0
				&& player.getDuelConfigurations().getRule(2)) {
			player.getPackets().sendGameMessage(
					"You cannot use Magic in this duel!", true);
			return false;
		} else if (isRanging && player.getDuelConfigurations().getRule(0)) {
			player.getPackets().sendGameMessage(
					"You cannot use Range in this duel!", true);
			return false;
		} else if (!isRanging && player.getDuelConfigurations().getRule(1)
				&& player.getCombatDefinitions().getSpellId() <= 0) {
			player.getPackets().sendGameMessage(
					"You cannot use Melee in this duel!", true);
			return false;
		} else {
			for (Item item : FUN_WEAPONS) {
				if (player.getDuelConfigurations().getRule(4)
						&& !player.getInventory().containsItem(item.getId(),
								item.getAmount())) {
					player.getPackets().sendGameMessage(
							"You can only use fun weapons in this duel!");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canEquip(int slotId, int itemId) {
		if (player.getDuelConfigurations().getRule(10 + slotId)) {
			player.getPackets().sendGameMessage(
					"You can't equip "
							+ ItemDefinitions.getItemDefinitions(itemId)
									.getName().toLowerCase()
							+ " during this duel.");
			return false;
		}
		if (slotId == 3 && Equipment.isTwoHandedWeapon(new Item(itemId))
				&& player.getDuelConfigurations().getRule(15)) {
			player.getPackets().sendGameMessage(
					"You can't equip "
							+ ItemDefinitions.getItemDefinitions(itemId)
									.getName().toLowerCase()
							+ " during this duel.");
			return false;
		}
		return true;
	}
}
