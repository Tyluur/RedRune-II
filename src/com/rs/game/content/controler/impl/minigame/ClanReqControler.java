package com.rs.game.content.controler.impl.minigame;

import com.rs.game.content.minigame.CommenceWar;
import com.rs.game.content.minigame.War;
import com.rs.game.content.minigame.War.Stage;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.content.controler.Controler;

public class ClanReqControler extends Controler {
	
	CommenceWar commenceWar;
	
	@Override
	public void start() {
		sendInterfaces();
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendPlayerOption("Challenge", 1, false);
		moved();
	}
	
	@Override
	public void moved() {
		if (!isAtClanArea(player)) {
			removeControler();
			remove();
		}
	}
	
	@Override
	public boolean login() {
		start();
		return false;
	}
	
	@Override
	public void forceClose() {
		remove();
	}
	
	@Override
	public void magicTeleported(int type) {
		removeControler();
		remove();
	}
	
	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (interfaceId == 791) {
			War war = player.getOwnedWar();
			Player other = (Player) player.getTemporaryAttributtes().get("challenger");
			if (war != null && war.getStage() == Stage.SETUP) {
				war.onClick(player, other, componentId, slotId);
				player.getPackets().sendConfigByFile(5292, war.getArena().getConfig());
				other.getPackets().sendConfigByFile(5292, war.getArena().getConfig());
			}
		}
		return true;
	}
	
	@Override
	public boolean canPlayerOption1(Player target) {
		player.stopAll(true);
		if (player.getCurrentFriendChatOwner() == null) {
			player.getPackets().sendGameMessage("You need to be in a friend's chat to start a war.");
			return false;
		}
		if (player.getCurrentFriendChat().getRank(0, player.getUsername()) <= 2) {
			player.getPackets().sendGameMessage("You need to be a higher rank to start a war.");
			return false;
		}
		if (target == null || target.hasFinished() || !target.withinDistance(player, 14) || !(target.getControlerManager().getControler() instanceof ClanReqControler)) {
			player.getPackets().sendGameMessage("Unable to find " + (target == null ? "your target" : target.getDisplayName()));
			return false;
		}
		if (target.getInterfaceManager().containsScreenInter()) {
			player.getPackets().sendGameMessage("The other player is busy.");
			return false;
		}
		player.getTemporaryAttributtes().put("target", target);
		player.getTemporaryAttributtes().put("challenger", target);
		if (player == target.getTemporaryAttributtes().get("target")) {
			target.getCurrentFriendChat().setChallenger(true);
			commenceWar = new CommenceWar(player, target, new War());
			player.getTemporaryAttributtes().remove("target");
			target.getTemporaryAttributtes().remove("target");
			return false;
		}
		target.getPackets().sendClanWarsRequestMessage(player);
		player.getPackets().sendGameMessage("Sending " + target.getDisplayName() + " a war request...");
		return true;
	}
	
	public void remove() {
		player.getTemporaryAttributtes().remove("target");
		player.getTemporaryAttributtes().remove("challenger");
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendPlayerOption("null", 1, false);
	}
	
	public static boolean isAtClanArea(Player player) {
		return player.inArea(2980, 9669, 3004, 9690);
	}
	
}
