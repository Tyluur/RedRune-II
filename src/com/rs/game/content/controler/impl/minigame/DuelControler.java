package com.rs.game.content.controler.impl.minigame;

import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.player.data.DuelConfigurations;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.controler.Controler;

public class DuelControler extends Controler {

	@Override
	public void start() {
		sendInterfaces();
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendPlayerOption("Challenge", 1, false);
		moved();
	}

	@Override
	public boolean login() {
		start();
		return false;
	}

	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {
		remove();
	}

	public boolean processMagicTeleport(WorldTile toTile) {
		return true;
	}

	public boolean processItemTeleport(WorldTile toTile) {
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeControler();
		remove();
	}

	@Override
	public void moved() {
		if (!isAtDuelArena(player)) {
			removeControler();
			remove();
		}
	}

	@Override
	public boolean canPlayerOption1(Player target) {
		player.stopAll(true);
		if (target.getInterfaceManager().containsScreenInter()) {
			player.getPackets().sendGameMessage("The other player is busy.");
			return false;
		}
		if (target.getTemporaryAttributtes().get("DuelChallenged") == player) {
			target.getTemporaryAttributtes().remove("DuelChallenged");
			DuelConfigurations duelConfigurations = new DuelConfigurations(
					player, target, (Boolean) target.getTemporaryAttributtes()
							.remove("DuelFriendly"));
			player.setDuelConfigurations(duelConfigurations);
			target.setDuelConfigurations(duelConfigurations);
			return false;
		}
		player.getTemporaryAttributtes().put("DuelTarget", target);
		player.getInterfaceManager().sendInterface(640);
		player.getTemporaryAttributtes().put("WillDuelFriendly", true); // default
																		// must
																		// be
																		// friendly
		player.getPackets().sendConfig(283, 67108864);
		return false;
	}

	public static void challenge(Player player) {
		player.closeInterfaces();
		Boolean friendly = (Boolean) player.getTemporaryAttributtes().remove(
				"WillDuelFriendly");
		if (friendly == null)
			return;
		Player target = (Player) player.getTemporaryAttributtes().remove(
				"DuelTarget");
		if (target == null
				|| target.hasFinished()
				|| !target.withinDistance(player, 14)
				|| !(target.getControlerManager().getControler() instanceof DuelControler)) {
			player.getPackets().sendGameMessage(
					"Unable to find "
							+ (target == null ? "your target" : target
									.getDisplayName()));
			return;
		}
		player.getTemporaryAttributtes().put("DuelChallenged", target);
		player.getTemporaryAttributtes().put("DuelFriendly", friendly);
		player.getPackets().sendGameMessage(
				"Sending " + target.getDisplayName() + " a request...");
		target.getPackets().sendDuelChallengeRequestMessage(player, friendly);
	}

	public void remove() {
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 10 : 8);
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendPlayerOption("null", 1, false);
	}

	@Override
	public void sendInterfaces() {
		if (isAtDuelArena(player)) {
			player.getInterfaceManager().sendTab(
					player.getInterfaceManager().hasRezizableScreen() ? 10 : 8,
					638);
		}
	}

	public static boolean isAtDuelArena(WorldTile player) {
		return (player.getX() >= 3355 && player.getX() <= 3360
				&& player.getY() >= 3267 && player.getY() <= 3279)
				|| (player.getX() >= 3355 && player.getX() <= 3379
						&& player.getY() >= 3272 && player.getY() <= 3279)
				|| (player.getX() >= 3374 && player.getX() <= 3379
						&& player.getY() >= 3267 && player.getY() <= 3271);
	}

}
