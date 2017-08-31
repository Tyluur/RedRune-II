package com.rs.game.content.controler.impl.minigame;

import com.rs.game.entity.object.WorldObject;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.minigame.CastleWars;
import com.rs.game.entity.actor.player.data.Equipment;
import com.rs.game.content.controler.Controler;

public class CastleWarsWaiting extends Controler {

	private int team;

	@Override
	public void start() {
		team = (int) getArguments()[0];
		sendInterfaces();
	}

	// You can't leave just like that!

	public void leave() {
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 8);
		CastleWars.removeWaitingPlayer(player, team);
	}

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 8, 57);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId,
			int slotId, int packetId) {
		if (interfaceId == 387 && componentId == 9) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	public boolean canEquip(int slotId, int itemId) {
		if (slotId == Equipment.SLOT_CAPE || slotId == Equipment.SLOT_HAT) {
			player.getPackets().sendGameMessage(
					"You can't remove your team's colours.");
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		removeControler();
		leave();
		return true;
	}

	@Override
	public boolean logout() {
		player.setLocation(new WorldTile(CastleWars.LOBBY, 2));
		return true;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage",
				"You can't leave just like that!");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		int id = object.getId();
		if (id == 4389 || id == 4390) {
			removeControler();
			leave();
			return false;
		}
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		leave();
	}

	@Override
	public void forceClose() {
		leave();
	}
}
