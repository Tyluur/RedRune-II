package com.rs.game.content.dialogue.varrock;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.content.dialogue.Dialogue;
import com.rs.utility.game.player.ShopsHandler;


public class Aubury extends Dialogue {

	int npcId;
	public NPC npc;
	
	public NPC findNPC(int id) {
		for (NPC npc : World.getNPCs()) {
			if (npc == null || npc.getId() != id)
				continue;
			return npc;
		}
		return null;
	}

	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
						"Do you want to buy some runes?" }, IS_NPC, npcId, 9827);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_OPTIONS, player.getDisplayName(), "Yes please.", "Can you teleport me to rune essence, please?");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == 1) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Yes please." },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 2;
			} else if (componentId == 2) {
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Can you teleport me to rune essence, please?" },
						IS_PLAYER, player.getIndex(), 9827);
				stage = 3;
			}
		} else if (stage == 2) {
			ShopsHandler.openShop(player, 2);
			end();
		} else if (stage == 3) {
			stage = 4;
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { NPCDefinitions.getNPCDefinitions(npcId).name,
							"Sure thing!" }, IS_NPC,
					npcId, 9827);
		} else if (stage == 4) {
			NPC aubury = findNPC(5913);
			aubury.setNextForceTalk(new ForceTalk("Senventior disthine molenko!"));
			aubury.setNextAnimation(new Animation(1818));
			aubury.faceEntity(player);
			World.sendProjectile(aubury, player, 110, 1, 1, 1, 1, 1, 1);
			player.setNextGraphics(new Graphics(110));
			player.setNextWorldTile(new WorldTile(2910, 4832, 0));
			end();
		}
	}

	@Override
	public void finish() {

	}
}
