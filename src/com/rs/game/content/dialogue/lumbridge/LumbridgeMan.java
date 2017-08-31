package com.rs.game.content.dialogue.lumbridge;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.dialogue.Dialogue;


/**
 * @author Gircat <gircat101@gmail.com> 
 * Created on Jul 12, 2014 at 3:55:42 PM.
 *
 */

public class LumbridgeMan extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Quickly - Tell me, is it still there? " }, IS_NPC,
				npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Is what still where? " }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"The THING, the THING! It was just outside my house!",
							"Has it gone away yet? Or is it still lurking out",
							"there, waiting for me to go outside? ", }, IS_NPC,
					npcId, 9827);
			stage = 2;
		} else if (stage == 2) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I didn't see any THING out there, just a ",
							"couple of guards. What did it look like? " },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 3;
		} else if (stage == 3) {
			sendEntityDialogue(SEND_2_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Ohhh, it was HORRIBLE! It was an enormous THING,",
							"with TEETH and EYES and... and... and THINGS!", },
					IS_NPC, npcId, 9827);
			stage = 4;
		} else if (stage == 4) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Um... would you care to be more specific?" },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 5;
		} else if (stage == 5) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I can't. I only saw it in the dark. ", }, IS_NPC,
					npcId, 9827);
			stage = 6;
		} else if (stage == 6) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"You only saw this THING in the dark? " },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 7;
		} else if (stage == 7) {
			sendEntityDialogue(
					SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"I was sleeping peacefully one night, when suddenly",
							"I woke up and saw it through the window. It was",
							"LOOKING at me! I haven't dared go out since then.",
							"It's had me trapped in here for days! I've packed my ", },
					IS_NPC, npcId, 9827);
			stage = 8;
		} else if (stage == 8) {
			sendEntityDialogue(
					SEND_3_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"bags so I can escape, but the THING's still out there",
							"waiting for me to come out! If I have to stay in here",
							"much longer I'll go mad! MAD!! MAD!! ", }, IS_NPC,
					npcId, 9827);
			stage = 9;
		} else if (stage == 9) {
			sendEntityDialogue(
					SEND_2_TEXT_CHAT,
					new String[] {
							player.getDisplayName(),
							"There's no THING outside. Just come out and get some",
							"fresh air before you go funny in the head. " },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 10;
		} else if (stage == 10) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You want me to go outside?  ", }, IS_NPC, npcId,
					9827);
			stage = 11;
		} else if (stage == 11) {
			sendEntityDialogue(
					SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							" I think you might just have dreamed about the THING." },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 12;
		} else if (stage == 12) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"You want me to believe that it's not real?", },
					IS_NPC, npcId, 9827);
			stage = 13;
		} else if (stage == 13) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"Please come outside! " }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 14;
		} else if (stage == 14) {
			sendEntityDialogue(SEND_4_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"No! No! I know what you are! You're in league",
							"with the THING! It keeps sending people in here",
							"to trick me into going outside! They keep ",
							"stealing from me too! GO AWAY! ", }, IS_NPC,
					npcId, 9827);
			stage = 15;
		} else if (stage == 15) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(),
							"I'm not trying to trick you!" }, IS_PLAYER,
					player.getIndex(), 9827);
			stage = 16;
		} else if (stage == 16) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] {
							NPCDefinitions.getNPCDefinitions(npcId).name,
							"Get thee gone, trickster!", }, IS_NPC, npcId, 9827);
			stage = 17;
		} else if (stage == 17) {
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Sheesh... " },
					IS_PLAYER, player.getIndex(), 9827);
			stage = 18;
		} else if (stage == 18) {
			end();

		}

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
	}
}