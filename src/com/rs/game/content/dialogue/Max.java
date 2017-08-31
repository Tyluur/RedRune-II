package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.Magic;

// Referenced classes of package com.rs.game.entity.actor.player.dialogues:
//            Dialogue

public class Max extends Dialogue {

	public Max() {
	}

	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue((short) 243,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Hello brave adventurer!",
						"My Names is Max, Would you like to tele",
						"to one of my skilling locations?" }, (byte) 1, npcId,
				9850);
	}

	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendEntityDialogue((short) 241,
					new String[] { player.getDisplayName(),
							"Well that depends on where I can go!" }, (byte) 0,
					player.getIndex(), 9827);
			stage = 1;
		} else if (stage == 1) {
			sendDialogue((short) 238, "Max's Skill Teleports", "Theiving", "Fishing", "Mining", "Rune Craft", "More Options");
			stage = 2;
		} else if (stage == 2) {
			if (componentId == 1)
				teleportPlayer(3493, 3488, 0);
			else if (componentId == 2)
				teleportPlayer(2590, 3423, 0);
			else if (componentId == 3)
				teleportPlayer(3298, 3299, 0);
			else if (componentId == 4)
				teleportPlayer(3186, 5718, 0);
			else if (componentId == 5) {
				stage = 3;
				sendDialogue(SEND_5_OPTIONS, "Max's Skill Teleports",
						"Woodcutting", "Gnome Agility", "Smith", "Barrows",
						"Nevermind");
			}
		} else if (stage == 3) {
			if (componentId == 1)
				teleportPlayer(2725, 3491, 0);
			else if (componentId == 2)
				teleportPlayer(2470, 3436, 0);
			else if (componentId == 3)
				teleportPlayer(3277, 3254, 0);
			else if (componentId == 4)
				teleportPlayer(3241, 9365, 0);
			else if (componentId == 5)
				end();
		}
	}

	private void teleportPlayer(int x, int y, int z) {
		Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(x, y, z));
	}

	public void finish() {
	}

	private int npcId;
}
