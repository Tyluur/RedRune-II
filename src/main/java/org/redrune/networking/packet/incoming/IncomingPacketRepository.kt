package org.redrune.networking.packet.incoming

import com.github.michaelbull.logging.InlineLogger
import org.redrune.game.GameFlags
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.Packet
import org.redrune.utility.functions.Misc
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 2019-02-04
 */
object IncomingPacketRepository {
    /**
     * The map of `IncomingPacketDecoder`s
     */
    private val PACKET_MAP = ConcurrentHashMap<Int, IncomingPacketReader?>()

    /**
     * Initializes all decoders
     */
    fun initialize() {
        Misc.getClasses(IncomingPacketRepository::class.java.getPackage().name + ".impl").stream()
            .filter { obj: Any? -> IncomingPacketReader::class.java.isInstance(obj) }
            .forEach { clazz: Any -> include(clazz as IncomingPacketReader) }
        logger.info { "Initialized " + PACKET_MAP.size + " incoming packet decoders" }
    }

    /**
     * Includes the decoder
     *
     * @param decoder The decoder instance
     */
    private fun include(decoder: IncomingPacketReader) {
        Arrays.stream(decoder.bindings()).forEach { key: Int ->
            check(!PACKET_MAP.containsKey(key)) { "Defined incoming packet [" + PACKET_MAP[key] + "] #" + key + " already and attempted to store " + decoder + " ahead of it." }
            PACKET_MAP[key] = decoder
        }
    }

    /**
     * Handling the decoding of the packet received
     *
     * @param player The player whose session received the packet
     * @param packet The packet that will be read
     */
    fun handlePacket(player: Player, packet: Packet) {
        try {
            val packetId = packet.opcode
            val reader = PACKET_MAP[packetId]
            if (reader == null) {
                logger.debug { "Received game packet #$packetId, unidentified handler." }
                return
            }
            val context = reader.read(player, packet)
            if (context == null) {
                if (GameFlags.debugMode) {
                    logger.debug { "Unable to get packet context for incoming packet " + packet + " with [reader=" + reader.javaClass.simpleName + "]!" }
                }
                return
            }
            context.handle(player)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val logger = InlineLogger()
}