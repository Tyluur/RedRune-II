package org.redrune.engine.cycle

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.redrune.engine.SystemManager
import org.redrune.engine.tick.task.WorldTasksManager
import org.redrune.game.entity.actor.ActorList
import org.redrune.game.entity.actor.npc.NPC
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.utility.constants.NetworkConstants
import org.redrune.utility.functions.Misc
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * This class represents a single update cycle in the game
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
class UpdateSequence : Runnable {

    /**
     * Fires the update sequence
     */
    suspend fun fire(lobbyPlayers: ActorList<Player>, gamePlayers: ActorList<Player>, npcs: ActorList<NPC>) {
        start(lobbyPlayers, gamePlayers, npcs)
        run(gamePlayers)
        finish(lobbyPlayers, gamePlayers, npcs)
    }

    /**
     * This is the start of the update sequence
     */
    fun start(lobbyPlayers: ActorList<Player>, gamePlayers: ActorList<Player>, npcs: ActorList<NPC>) {
        val currentTime = Misc.currentTimeMillis()
        SystemManager.SCHEDULER.pulse()
        WorldTasksManager.processTasks()

        lobbyPlayers.stream().filter { p: Player? -> p != null && !p.session.isInLobby }
            .forEach { p: Player -> p.session.processContextQueue() }
        lobbyPlayers.stream().filter { player: Player? -> player != null && player.session.isInLobby }
            .forEach { player: Player -> player.session.processContextQueue() }
        gamePlayers.stream().filter { player: Player? -> player != null && player.hasStarted() && !player.isFinished }
            .forEach { player: Player ->
                if (currentTime - player.attributes.packetsDecoderPing > NetworkConstants.MAX_PACKETS_DECODER_PING_DELAY && player.session.channel.isOpen) {
                    player.session.channel.close()
                }
                player.processEntity()
            }
        npcs.stream().filter { npc: NPC? -> npc != null && !npc.isFinished }.forEach { obj: NPC -> obj.processEntity() }
    }

    /**
     * Runs the updating part of the sequence
     */
    suspend fun run(players: ActorList<Player>) {
        val latch = CountDownLatch(players.size)
        for (player in players) {
            val async = GlobalScope.async {
                try {
                    if (player.hasStarted() && !player.isFinished) {
                        player.packets.sendLocalPlayersUpdate()
                        player.packets.sendLocalNPCsUpdate()
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
                latch.countDown()
            }
            async.await()
        }
        try {
            latch.await(600, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * Finishes the update sequence
     */
    fun finish(lobbyPlayers: ActorList<Player>, players: ActorList<Player>, npcs: ActorList<NPC>) {
        players.stream().filter { player: Player? -> player != null && player.hasStarted() && !player.isFinished }
            .forEach { obj: Player -> obj.resetMasks() }
        npcs.stream().filter { npc: NPC? -> npc != null && !npc.isFinished }.forEach { obj: NPC -> obj.resetMasks() }
        lobbyPlayers.stream().filter { obj: Player? -> Objects.nonNull(obj) }
            .forEach { player: Player -> player.session.flush() }
        players.stream().filter { player: Player? -> player != null && player.hasStarted() && !player.isFinished }
            .forEach { player: Player -> player.session.flush() }
    }

    companion object {

        /**
         * The executor used for parallel execution of player updating and npc updating
         */
        private val EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    override fun run() {
        while (!SystemManager.shutdown) {
            val currentTime = Misc.currentTimeMillis()
            runBlocking {
                try {
                    fire(World.getLobbyPlayers(), World.getPlayers(), World.getNPCs())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                sleepThread(currentTime)
            }
        }
    }

    /**
     * Handles the sleeping of the thread
     */
    private fun sleepThread(startTime: Long) {
        GameCycleWorker.lastCycleTime = Misc.currentTimeMillis()
        val sleepTime = 600 + (startTime - GameCycleWorker.lastCycleTime)
        if (sleepTime <= 0) {
            return
        }
        GameCycleWorker.ticksPassed++
        try {
            Thread.sleep(sleepTime)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}