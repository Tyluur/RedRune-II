package com.rs.game.content.controler.impl.activity;

import com.rs.game.content.controler.Controler;

public class GodWars extends Controler {

	@Override
	public void start() {
		// place, count1,count2,count3,count4,count5
		setArguments(new Object[] { 0, 0, 0, 0, 0, 0 });
		sendInterfaces();
	}

	public boolean logout() {
		return false; // so doesnt remove script
	}

	public boolean login() {
		sendInterfaces();
		return false; // so doesnt remove script
	}

	// @Override
	// public boolean processObjectClick1(final WorldObject object) {
	// if (object.getId() == 57225) {
	// player.getDialogueManager().startDialogue("NexEntrance");
	// return false;
	// }
	// return true;
	// }

	@Override
	public void sendInterfaces() {
		player.getInterfaceManager().sendTab(
				player.getInterfaceManager().hasRezizableScreen() ? 9 : 8,
				getInterface());
		player.getPackets().sendIComponentText(601, 8, "" + player.ArmadylKC);
		player.getPackets().sendIComponentText(601, 9, "" + player.BandosKC);
		player.getPackets()
				.sendIComponentText(601, 10, "" + player.SaradominKC);
		player.getPackets().sendIComponentText(601, 11, "" + player.ZamorakKC);
	}

	private int getInterface() {
		return 601;
	}

	@Override
	public boolean sendDeath() {
		remove();
		removeControler();
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		remove();
		removeControler();
	}

	@Override
	public void forceClose() {
		player.BandosKC = 0;
		player.ZamorakKC = 0;
		remove();
	}

	public void remove() {
		player.BandosKC = 0;
		player.ZamorakKC = 0;
		player.getPackets().closeInterface(
				player.getInterfaceManager().hasRezizableScreen() ? 9 : 8);
	}

}
