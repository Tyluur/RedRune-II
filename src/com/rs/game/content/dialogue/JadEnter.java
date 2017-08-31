package com.rs.game.content.dialogue;

import com.rs.game.entity.actor.mask.ForceTalk;
import com.rs.game.entity.WorldTile;
import com.rs.game.content.Magic;

public class JadEnter extends Dialogue {

	private int npcId;

	@Override
	public void start() {
		sendDialogue(SEND_1_TEXT_CHAT, "TzTok-Jad",
				"AHZARGHZ! WHO DARE DISTURB ME!");
		player.getPackets().sendCameraShake(3, 25, 50, 25, 50);
		sentEarthquake = true;

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			sendDialogue(SEND_2_OPTIONS, "What would you like to do?",
					"Sorry master jad ill be on my way ..",
					"Me you son of a bitch show your self !");
			stage = 1;
		} else if (stage == 1) {
			if (componentId == 1) {
				player.getPackets().sendStopCameraShake();
				end();
			} else if (componentId == 2) {
				if (sentEarthquake) {
					teleportPlayer(2402, 5084, 0);
				}
				player.setNextForceTalk(new ForceTalk(
						"Here goes nothing.. ARGGHHH!"));
				player.getPackets().sendStopCameraShake();
				end();
			}

		}
	}

	private boolean sentEarthquake;

	private void teleportPlayer(int x, int y, int z) {
		Magic.sendNormalTeleportSpell(player, 0, 0.0D, new WorldTile(x, y, z));
	}

	public void init() throws InterruptedException {
		player.getPackets().sendCameraShake(1, 1, 1, 1, 1);
		Thread.sleep(90000); // Put the seconds you want the interval to be, *
								// 1000
		player.getPackets().sendStopCameraShake();
	}

	@Override
	public void finish() {
	}

	public void doTrip() {
	}
}
