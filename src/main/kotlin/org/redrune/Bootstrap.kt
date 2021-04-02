package org.redrune

import com.github.michaelbull.logging.InlineLogger
import org.koin.core.context.startKoin
import org.koin.logger.slf4jLogger
import org.redrune.cache.Cache
import org.redrune.cache.huffman.Huffman
import org.redrune.cache.loaders.ItemEquipIds
import org.redrune.engine.SystemManager
import org.redrune.engine.worker.boot.BootHandler
import org.redrune.game.GameFlags
import org.redrune.game.content.entity.actor.combat.npc.CombatScriptsHandler
import org.redrune.game.content.entity.actor.npc.FishingSpotsHandler
import org.redrune.game.content.entity.actor.player.controller.ControllerHandler
import org.redrune.game.content.entity.actor.player.cutscene.CutscenesHandler
import org.redrune.game.content.entity.actor.player.dialogue.DialogueHandler
import org.redrune.game.content.entity.actor.player.market.ShopRepository
import org.redrune.game.content.plugin.PluginRepository
import org.redrune.game.entity.actor.npc.data.extension.NPCExtensionHolder
import org.redrune.game.entity.actor.player.link.FriendChatsManager
import org.redrune.game.global.map.MapMerger
import org.redrune.game.global.map.region.RegionBuilder
import org.redrune.game.global.punishment.PunishmentRepository
import org.redrune.global.wordlist.WorldList
import org.redrune.networking.NetworkBinder
import org.redrune.networking.packet.incoming.IncomingPacketRepository
import org.redrune.utility.constants.PacketConstants
import org.redrune.utility.game.entity.`object`.ObjectRemoval
import org.redrune.utility.game.entity.`object`.ObjectSpawning
import org.redrune.utility.game.entity.actor.player.Censor
import org.redrune.utility.game.map.MapArchiveKeys
import org.redrune.utility.game.repository.`object`.climbable.ClimbableObjectRepository
import org.redrune.utility.game.repository.`object`.door.DoorRepository
import org.redrune.utility.game.repository.npc.NPCWalkingFlag
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * This class runs the server
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since January 25th, 2019
 */
object Bootstrap {

    /**
     * The main method invoked by the jvm
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // startup work
        initialize()
    }

    /**
     * This method uses [BootHandler] to prepare all requirements for the game to start efficiently. All tasks
     * that require each other are performed in the same parallel instance, other ones can be performed
     * individually.
     *
     * This is a blocking method due to [BootHandler.await]
     */
    private fun initialize() {
        startKoin {
            slf4jLogger()
            fileProperties("/game.properties")
        }
        try {
            Cache.initialize()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        BootHandler.addWork(Runnable {
            try {
                SystemManager.initialize()
                ShopRepository.registerAll()
                ItemEquipIds.initialize()
                Huffman.initialize()
                RegionBuilder.initialize()
                MapArchiveKeys.initialize()
                IncomingPacketRepository.initialize()
                PacketConstants.loadPacketSizes()
                Censor.initialize()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, Runnable {
            NPCWalkingFlag.registerFlags()
            NPCExtensionHolder.initialize()
            ObjectSpawning.initialize()
        }, Runnable {
            WorldList.initialize()
            // door ids loaded before plugins bc of referencing
            DoorRepository.initialize()
            ClimbableObjectRepository.initialize()
            PluginRepository.registerAll()
            PunishmentRepository.loadAll()
        }, Runnable {
            FishingSpotsHandler.initialize()
            CombatScriptsHandler.registerAll()
            DialogueHandler.initialize()
            ControllerHandler.registerAll()
            CutscenesHandler.init()
            ObjectRemoval.initialize()
            FriendChatsManager.initialize()
        }, Runnable {
            MapMerger.start()
        })
        BootHandler.await()
        try {
            logger.info {
                "Startup completed in " + BootHandler.getSTOPWATCH()
                    .elapsed(TimeUnit.MILLISECONDS) + " milliseconds [hostMode=" + GameFlags.hostMode + ", debugMode=" + GameFlags.debugMode + ", pvpWorld=" + GameFlags.pvpWorld + "]"
            }
            NetworkBinder.bind()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private val logger = InlineLogger()
}