package com.rs.game.content.dialogue;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.item.Item;
import com.rs.game.content.Magic;

// Referenced classes of package com.rs.game.entity.actor.player.dialogues:
//            Dialogue

public class Tzhaar extends Dialogue {

	public Tzhaar() {
	}

	@Override
	public void start() {
		npcId = ((Integer) parameters[0]).intValue();
		sendEntityDialogue(
				(short) 243,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"I am the leader of all fighting and Tzhaar Items",
						"I can teleport you to fight pits/Tzhaar Arena",
						"or, I can give you a cape for Fighting masters only. " },
				(byte) 1, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 0;
			sendDialogue((short) 237, "What would you like to say?", "Teleport me to Fight pits", "Teleport me so I can fight TzHaar!", "May I have a Fire cape? I think I'm worthy.", "Never Mind.");
		} else if (stage == 0) {
			if (componentId == 1) {
				player.closeInterfaces();
				teleportPlayer(4600, 5062, 0);
			} else if (componentId == 2) {
				player.closeInterfaces();
				teleportPlayer(4673, 5126, 0);
			} else if (componentId == 3) {
				if (player.getInventory().containsItem(6529, 60000)) {
					player.getInventory().removeItems(new Item(6529, 60000));
					player.getInventory().addItem(6570, 1);
					// player.sendMessage("You are worthy and paid 60,000 tokkul for your Fire cape");
					player.closeInterfaces();
				} else {
					player.closeInterfaces();
					// player.sendMessage("You do not have 60,000 tokkul and are not worthy for a Fire cape");
				}
			} else if (componentId == 4) {
				end();
			} else {
				end();
			}
		}
	}

	private void teleportPlayer(int x, int y, int z) {
		Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(x, y, z));
	}

	@Override
	public void finish() {
	}

	int npcId;
}
