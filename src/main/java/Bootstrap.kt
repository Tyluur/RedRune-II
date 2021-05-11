
import cache.Cache
import cache.codec.huffman.Huffman
import cache.codec.loaders.ItemEquipIds
import com.github.michaelbull.logging.InlineLogger
import engine.SystemManager
import engine.worker.boot.BootHandler
import game.GameFlags
import game.content.entity.actor.combat.npc.CombatScriptsHandler
import game.content.entity.actor.npc.FishingSpotsHandler
import game.content.entity.actor.player.controller.ControllerHandler
import game.content.entity.actor.player.cutscene.CutscenesHandler
import game.content.entity.actor.player.dialogue.DialogueHandler
import game.content.entity.actor.player.market.ShopRepository
import game.content.entity.actor.player.market.exchange.ExchangeManager
import game.content.entity.actor.player.skills.PresetHandler
import game.content.plugin.PluginRepository
import game.entity.actor.npc.data.extension.NPCExtensionHolder
import game.entity.actor.player.link.FriendChatsManager
import game.global.map.MapMerger
import game.global.map.region.RegionBuilder
import game.global.punishment.PunishmentRepository
import kotlinx.coroutines.runBlocking
import network.NetworkBinder
import network.packet.incoming.IncomingPacketRepository
import org.koin.core.context.startKoin
import utility.constants.PacketConstants
import utility.game.entity.`object`.ObjectRemoval
import utility.game.entity.`object`.ObjectSpawning
import utility.game.entity.actor.player.Censor
import utility.game.entity.item.priceLoaderModule
import utility.game.map.MapArchiveKeys
import utility.game.repository.`object`.climbable.ClimbableObjectRepository
import utility.game.repository.`object`.door.DoorRepository
import utility.game.repository.npc.NPCWalkingFlag
import utility.getBoolProperty
import utility.getIntProperty
import utility.getProperty
import utility.global.wordlist.WorldList
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
     * This method uses [BootHandler] to prepare all requirements for the game to start efficiently. All tasks
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