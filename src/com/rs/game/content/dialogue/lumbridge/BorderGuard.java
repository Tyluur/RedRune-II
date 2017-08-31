package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;

/**
 * Dialogue for Border Guards.
 * @author Gircat <gircat101@gmail.com>
 * @author Feather RuneScape 2011
 */
public class BorderGuard extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { player.getDisplayName(), "Can I come through this gate?" },
				IS_PLAYER, player.getIndex(), 9827);
}
	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"You must pay a toll of 10 gold coins to pass." }, IS_NPC,
					npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendDialogue(SEND_2_TEXT_INFO, "I'm sorry,", "This feature isn't added yet.");
			stage =3;
		} else if (stage == 3) {
			end();
			}
		}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		}
	}