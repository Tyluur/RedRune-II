package org.redrune

import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
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
import org.redrune.game.content.entity.actor.player.market.exchange.ExchangeManager
import org.redrune.game.content.entity.actor.player.skills.PresetHandler
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
import org.redrune.utility.game.entity.actor.player.Censor
import org.redrune.utility.game.entity.item.priceLoaderModule
import org.redrune.utility.game.entity.`object`.ObjectRemoval
import org.redrune.utility.game.entity.`object`.ObjectSpawning
import org.redrune.utility.game.map.MapArchiveKeys
import org.redrune.utility.game.repository.npc.NPCWalkingFlag
import org.redrune.utility.game.repository.`object`.climbable.ClimbableObjectRepository
import org.redrune.utility.game.repository.`object`.door.DoorRepository
import org.redrune.utility.getBoolProperty
import org.redrune.utility.getIntProperty
import org.redrune.utility.getProperty
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
    fun main(args: Array<String>) = runBlocking {
        boot()
    }

    /**
     * This method uses [BfootHandler] to prepare all requirements for the game to start efficiently. All tasksfd
     * that require each other are performed in the same parallel instance, other ones can be performed
     * individually.
     *
     * This is a blocking method due to [BootHandler.await]
     */
    private fun boot() {
        startKoin {
            fileProperties("/game.properties")
            fileProperties("/parameters.properties")
            fileProperties("/world.properties")

            modules(priceLoaderModule)
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
            PresetHandler.loadPresets()
            ExchangeManager.loadExchangeList()
        })
        BootHandler.await()
        launch()
    }

    private fun launch() {
        val name = getProperty("name")
        val worldId = getIntProperty("world_id")
        val pvpWorld = getBoolProperty("pvp_world")
        val hostMode = getBoolProperty("host_mode")
        val debugMode = getBoolProperty("debug_mode")

        GameFlags.debugMode = debugMode
        GameFlags.hostMode = hostMode
        GameFlags.pvpWorld = pvpWorld

        val elapsed = BootHandler.stopwatch.elapsed(TimeUnit.MILLISECONDS)

        logger.info {
            "Parameters: [hostMode=$hostMode, debugMode=$debugMode, pvpWorld=$pvpWorld]"
        }

        logger.info {
            "$name successfully launched world $worldId in $elapsed ms."
        }

        NetworkBinder.bind()
    }

    private val logger = InlineLogger()
}