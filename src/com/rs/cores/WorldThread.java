package com.rs.cores;

import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.World;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.networking.NetworkConstants;
import com.rs.utility.Misc;

public final class WorldThread extends Thread {
	
	public static long LAST_CYCLE_CTM;
	
	WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}
	
	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			long currentTime = Misc.currentTimeMillis();
			try {
				WorldTasksManager.processTasks();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished()) {
						continue;
					}
					if (currentTime - player.getPacketsDecoderPing() > NetworkConstants.MAX_PACKETS_DECODER_PING_DELAY && player.getSession().getChannel().isOpen()) {
						player.getSession().getChannel().close();
					}
					player.processEntity();
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished()) {
						continue;
					}
					npc.processEntity();
				}
				
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished()) {
						continue;
					}
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				}
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished()) {
						continue;
					}
					player.resetMasks();
					player.getSession().flush();
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished()) {
						continue;
					}
					npc.resetMasks();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			LAST_CYCLE_CTM = Misc.currentTimeMillis();
			long sleepTime = 600 + currentTime - LAST_CYCLE_CTM;
			if (sleepTime <= 0) {
				continue;
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
