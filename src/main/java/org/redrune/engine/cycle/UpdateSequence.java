package org.redrune.engine.cycle;

import org.redrune.engine.SystemManager;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.entity.actor.ActorList;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.NetworkConstants;
import org.redrune.utility.functions.Misc;

import java.util.Objects;
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
	 * The executor used for parallel execution of player updating and npc updating
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
		lobbyPlayers.stream().filter(p -> p != null && ! p.getSession().isInLobby()).forEach(p -> p.getSession().processContextQueue());
		lobbyPlayers.stream().filter(player -> player != null && player.getSession().isInLobby()).forEach(player -> player.getSession().processContextQueue());
		gamePlayers.stream().filter(player -> player != null && player.hasStarted() && !player.isFinished()).forEach(player -> {
			if (currentTime - player.getAttributes().getPacketsDecoderPing() > NetworkConstants.MAX_PACKETS_DECODER_PING_DELAY && player.getSession().getChannel().isOpen()) {
				player.getSession().getChannel().close();
			}
			player.processEntity();
		});
		npcs.stream().filter(npc -> npc != null && !npc.isFinished()).forEach(NPC::processEntity);
	}
	
	/**
	 * Runs the updating part of the sequence
	 */
	public void run(ActorList<Player> players) {
		final CountDownLatch latch = new CountDownLatch(players.size());
		players.stream().<Runnable>map(player -> () -> {
			try {
				if (player != null && player.hasStarted() && !player.isFinished()) {
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
			latch.countDown();
		}).forEach(EXECUTOR::execute);
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
		players.stream().filter(player -> player != null && player.hasStarted() && !player.isFinished()).forEach(Player::resetMasks);
		npcs.stream().filter(npc -> npc != null && !npc.isFinished()).forEach(NPC::resetMasks);
		lobbyPlayers.stream().filter(Objects::nonNull).forEach(player -> player.getSession().flush());
		players.stream().filter(player -> player != null && player.hasStarted() && !player.isFinished()).forEach(player -> player.getSession().flush());
	}
	
}
