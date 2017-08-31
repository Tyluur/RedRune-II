package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;

public class RewardsMysticDialogue extends Dialogue {

	int npcId;

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendDialogue(SEND_2_OPTIONS, player.getDisplayName(), "Can you tell me about your rewards shop?", "Nothing, nevermind.");
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
				"Can you tell me about your rewards shop?" },
				IS_PLAYER, player.getIndex(), 9827);
				stage = 1;
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(),
				"Nothing, nevermind." },
				IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
				}
			
			} else if (stage == 1) {
			sendEntityDialogue(SEND_4_TEXT_CHAT, 
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
					"Most certainly I can, " + player.getDisplayName() +". The rewards shop allows",
					"users like you obtain Sacred items which increase the",
					"amount of experiene you receive when you are training",
					"your skills." }, IS_NPC, npcId, 9827);
			stage = 2;
			} else if (stage == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"It's a great opportunity to take!" }, IS_NPC, npcId, 9827);
				stage = 3;
			} else if (stage == 3) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
						"Are there any disadvantages to the Sacred items?" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 4;
			} else if (stage == 4) {
				sendEntityDialogue(SEND_3_TEXT_CHAT, new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Unfortunately there is one. After a certain amount",
						"of time your Sacred items become worn and they",
						"end up turning into dust." }, IS_NPC, npcId, 9827);
				stage = 5;
			} else if (stage == 5) {
				sendEntityDialogue(SEND_1_TEXT_CHAT, new String[] { player.getDisplayName(),
				"That's sad to hear. Thanks for your time sir!" }, IS_PLAYER, player.getIndex(), 9827);
				stage = 100;
			} else if (stage == 100) {
				end();
		} 
	}

@Override
public void finish() {

}
}
