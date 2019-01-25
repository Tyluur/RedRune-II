package org.redrune.engine.thread;

import org.redrune.engine.SystemManager;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.Misc;

public final class WorldThread extends Thread {
	
	public static long LAST_CYCLE_CTM;
	
	private static int ticksPassed = 0;
	
	public WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}
	
	@Override
	public final void run() {
		while (!SystemManager.shutdown) {
			long currentTime = Misc.currentTimeMillis();
			try {
				SystemManager.SCHEDULER.pulse();
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
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.hasFinished()) {
						continue;
					}
					npc.resetMasks();
				}
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted() || player.hasFinished()) {
						continue;
					}
					player.getSession().flush();
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			LAST_CYCLE_CTM = Misc.currentTimeMillis();
			long sleepTime = 600 + currentTime - LAST_CYCLE_CTM;
			if (sleepTime <= 0) {
				continue;
			}
			ticksPassed++;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int getTicksPassed() {
		return ticksPassed;
	}
}
