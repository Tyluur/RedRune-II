package com.rs.game.content.dialogue.varrock;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.utility.game.player.ShopsHandler;

/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Aug 12, 2014 at 10:43:18 PM.
 */

public class Lowe extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(
				SEND_2_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Welcome to Lowe's Archery Emporium.",
						"Do you want to see my wares?" },
				IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		switch (stage) {
		case -1:
			stage = 0;
			sendDialogue(SEND_2_OPTIONS, SEND_DEFAULT_OPTIONS_TITLE, "Yes, please.", "No, I prefer to bash things close up.");
			break;
		case 0:
			if (componentId == 1) {
				stage = 1;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "Yes please." },
						IS_PLAYER, player.getIndex(), 9827);
				break;
			}
			else if (componentId == 2) {
				stage = 2;
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(), "No, I prefer to bash things close up." },
						IS_PLAYER, player.getIndex(), 9827);
				break;
				}
		case 1:
			end();
			ShopsHandler.openShop(player, 3);
			break;
		case 2:
			stage = 3;	
			sendEntityDialogue(SEND_1_TEXT_CHAT,new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Humph, philistine." },
					IS_NPC, npcId, 9827);
			break;
		case 3:
			default:
				end();
				break;
		}
	}

	@Override
	public void finish() {
		
	}

}
