package org.redrune.networking

import com.github.michaelbull.logging.InlineLogger
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import org.redrune.game.entity.actor.player.Player
import org.redrune.networking.packet.PacketBuilder
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.session.ISAACCipher

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class NetworkSession(
    /**
     * The channel instance.
     */
    var channel: Channel
) {
    /**
     * The player affiliated with this network session
     */
    var player: Player? = null

    /**
     * The mac address affiliated with the session
     */
    val macAddress: String? = null

    /**
     * If the session is in the lobby
     */
    var isInLobby = false

    /**
     * The ISAAC cipher for incoming data.
     */
    private var inCipher: ISAACCipher? = null

    /**
     * The ISAAC cipher for outgoing data
     */
    private var outCipher: ISAACCipher? = null

    /**
     * This method is invoked when the session is registered
     */
    fun onRegistration() {
        logger.debug { "Session has registered successfully [session=${toString()}]" }
    }

    override fun toString(): String {
        return "NetworkSession{" + "player=" + player + ", inLobby=" + isInLobby + '}'
    }

    /**
     * This method is invoked when the session is deregistered
     */
    fun onDeregistration() {
        if (player != null) {
            if (isInLobby) {
                player!!.finishLobby()
            } else {
                player!!.finish()
            }
        }
        logger.debug { "Session has de-registered [session=${toString()}]" }
    }

    /**
     * Writes a packet to the channel and flushes it
     *
     * @param bldr The builder of the packet to flush
     */
    @Synchronized
    fun write(bldr: OutgoingPacketBuilder): ChannelFuture {
        val build = bldr.build()
        //		System.out.println("Wrote packet " + build);
        return channel.write(build)
    }

    /**
     * Writes a packet to the channel and flushes it
     *
     * @param bldr The builder of the packet to flush
     */
    @Synchronized
    fun write(bldr: PacketBuilder): ChannelFuture {
        val msg = bldr.toPacket()

        return channel.write(msg)
    }

    /**
     * Flushes all the outgoing buffers
     */
    @Synchronized
    fun flush(): Channel {
        return channel.flush()
    }

    /**
     * Gets the ip
     */
    val iPAddress: String
        get() = Misc.getIpAddress(channel)

    /**
     * Builds the ciphers
     *
     * @param inCipher  The incoming cipher
     * @param outCipher The outgoing cipher
     */
    fun buildCiphers(inCipher: ISAACCipher?, outCipher: ISAACCipher?) {
        setInCipher(inCipher)
        setOutCipher(outCipher)
    }

    private fun setInCipher(inCipher: ISAACCipher?) {
        this.inCipher = inCipher
    }

    private fun setOutCipher(outCipher: ISAACCipher?) {
        this.outCipher = outCipher
    }

    companion object {

        private val logger = InlineLogger()

    }
}