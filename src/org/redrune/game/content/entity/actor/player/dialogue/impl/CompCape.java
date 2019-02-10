package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.global.WorldTile;

/**
 * @author Gircat <gircat101@gmail.com> Created on Aug 11, 2014 at 8:20:40 PM.
 */
public class CompCape extends Dialogue {
	
	int itemId;
	
	@Override
	public void start() {
		itemId = (Integer) parameters[0];
		sendItemDialogue(itemId, 1, "Congratulations on your cape.", "What would you like to do?");
	}
	
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendOptions(DEFAULT_OPTION, "Open my bank.", "Home teleport.", "Change Display Name(?)", "Close features.");
		} else if (stage == 0) {
			if (componentId == 1) {
				player.getBank().openBank();
			} else if (componentId == 2) {
				Magic.sendNormalTeleportSpell(player, 0, 0, new WorldTile(2965, 3383, 0));
			} else if (componentId == 3) {
				player.getPackets().sendGameMessage("Name changing is disabled at the moment.");
			}
			end();
		}
	}
	
	@Override
	public void finish() {
	
	}
	
}
