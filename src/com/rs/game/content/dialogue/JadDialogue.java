// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MrEx.java

package com.rs.game.content.dialogue;

import com.rs.game.entity.WorldTile;
import com.rs.game.content.Magic;

// Referenced classes of package com.rs.game.entity.actor.player.dialogues:
//            Dialogue

public class JadDialogue extends Dialogue {

	public JadDialogue() {
	}

	@Override
	public void start() {
		// npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue((short) 243, new String[] { "TzTok-Jad Cave",
				"You DARE DISTURB ME!", "ANSWER ME MORTAL, FEAR MY WRATH!",
				"fIGHT ME MORTAL! UNLESS, YOU FEAR ME!" }, (byte) 1, 2745, 9850);
		player.getPackets().sendCameraShake(3, 25, 50, 25, 50);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue((short) 241,
					new String[] { player.getDisplayName(),
							"ELEMENTAL BEING! I SHALL" }, (byte) 0,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			player.getPackets().sendStopCameraShake();
			sendDialogue((short) 238, "Choose your option...", "", "", "Defeat you!", "", "Leave you in peace, Master Jad.");
			stage = 2;
		} else if (stage == 2) {
			switch (componentId) {
			case 3:
				teleportPlayer(2412, 5117, 0);
				player.closeInterfaces();
				break;
			case 4:
				teleportPlayer(4351, 4897, 0);
				break;
			case 5:
				player.closeInterfaces();
				teleportPlayer(4351, 4897, 0);
				/*
				 * stage = 3; sendDialogue((short) 238, new String[] {
				 * "Where would you like to go?", "Duel Arena.",
				 * "Gnome Agility.", "Dominion Tower.", "Barrows",
				 * "More Options" });
				 */
				break;
			}
		}
	}

	private void teleportPlayer(int x, int y, int z) {
		Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(x, y, z));
		// player.getControllerManager().startControler("GodWars", new
		// Object[0]);
	}

	@Override
	public void finish() {
	}
}
