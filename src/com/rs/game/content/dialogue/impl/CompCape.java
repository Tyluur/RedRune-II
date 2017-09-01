package com.rs.game.content.dialogue.impl;

import com.rs.game.content.Magic;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.game.entity.WorldTile;

/**
 * @author Gircat <gircat101@gmail.com> Created on Aug 11, 2014 at 8:20:40 PM.
 */
public class CompCape extends Dialogue {
	
	@Override
	public void start() {
		sendDialogue(SEND_2_TEXT_INFO, "Congratulations on your Completionist cape.", "What would you like to do?");
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue(SEND_4_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE, "Open my bank.", "Home teleport.", "Change Display Name(?)", "Close features.");
		} else if (stage == 0) {
			if (componentId == 1) {
				player.getBank().openBank();
			}
			if (componentId == 2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2965, 3383, 0));
			}
			if (componentId == 3) {
				player.getPackets().sendGameMessage("Name changing is disabled at the moment.");
			}
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
