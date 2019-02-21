package org.redrune.engine.cycle;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.entity.actor.ActorList;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.Misc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This class represents a single update cycle in the game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-25
 */
public class UpdateSequence {
	
	/**
	 * The executor used.
	 */
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	/**
	 * Fires the update sequence
	 */
	public void fire(ActorList<Player> lobbyPlayers, ActorList<Player> gamePlayers, ActorList<NPC> npcs) {
		start(lobbyPlayers, gamePlayers, npcs);
		run(gamePlayers);
		finish(lobbyPlayers, gamePlayers, npcs);
	}
	
	/**
	 * This is the start of the update sequence
	 */
	public void start(ActorList<Player> lobbyPlayers, ActorList<Player> gamePlayers, ActorList<NPC> npcs) {
		long currentTime = Misc.currentTimeMillis();
		SystemManager.SCHEDULER.pulse();
		WorldTasksManager.processTasks();
		for (Player player : lobbyPlayers) {
			if (player == null || !player.getSession().isInLobby()) {
				continue;
			}
			player.getSession().processContextQueue();
		}
		for (Player player : gamePlayers) {
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			if (currentTime - player.getAttributes().getPacketsDecoderPing() > NetworkConstants.MAX_PACKETS_DECODER_PING_DELAY && player.getSession().getChannel().isOpen()) {
				player.getSession().getChannel().close();
			}
			player.processEntity();
		}
		for (NPC npc : npcs) {
			if (npc == null || npc.isFinished()) {
				continue;
			}
			npc.processEntity();
		}
	}
	
	/**
	 * Runs the updating part of the sequence
	 */
	public void run(ActorList<Player> players) {
		final CountDownLatch latch = new CountDownLatch(players.size());
		for (final Player player : players) {
			EXECUTOR.execute(() -> {
				try {
					if (player != null && player.hasStarted() && !player.isFinished()) {
						player.getPackets().sendLocalPlayersUpdate();
						player.getPackets().sendLocalNPCsUpdate();
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
				latch.countDown();
			});
		}
		try {
			latch.await(600, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finishes the update sequence
	 */
	public void finish(ActorList<Player> lobbyPlayers, ActorList<Player> players, ActorList<NPC> npcs) {
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			player.resetMasks();
		}
		for (NPC npc : npcs) {
			if (npc == null || npc.isFinished()) {
				continue;
			}
			npc.resetMasks();
		}
		for (Player player : lobbyPlayers) {
			if (player == null) {
				continue;
			}
			player.getSession().flush();
		}
		for (Player player : players) {
			if (player == null || !player.hasStarted() || player.isFinished()) {
				continue;
			}
			player.getSession().flush();
		}
	}
	
}
