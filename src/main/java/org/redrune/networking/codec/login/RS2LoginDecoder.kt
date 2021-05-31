package org.redrune.networking.codec.login

import com.alex.utils.Utils
import com.github.michaelbull.logging.InlineLogger
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.redrune.cache.Cache
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.global.World
import org.redrune.game.global.punishment.PunishmentRepository
import org.redrune.game.global.punishment.PunishmentType
import org.redrune.networking.NetworkSession
import org.redrune.networking.codec.RS2PacketDecoder
import org.redrune.networking.packet.outgoing.impl.LobbyConfigurationPacketBuilder
import org.redrune.networking.packet.outgoing.impl.LoginConfigurationPacketBuilder
import org.redrune.networking.packet.outgoing.impl.LoginResponseCodePacketBuilder
import org.redrune.utility.constants.GameConstants
import org.redrune.utility.constants.NetworkConstants
import org.redrune.utility.functions.Misc
import org.redrune.utility.game.entity.actor.player.JacksonFactory.fromFile
import org.redrune.utility.game.entity.actor.player.JacksonFactory.playerExists
import org.redrune.utility.game.entity.actor.player.LoginReturnCode
import org.redrune.utility.game.session.ISAACCipher
import org.redrune.utility.game.stream.buffer.FixedBuffer
import java.util.*

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class RS2LoginDecoder : ByteToMessageDecoder() {

    /**
     * The `NetworkSession` instance created
     */
    private var session: NetworkSession? = null

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, buffer: ByteBuf, out: List<Any>) {
        if (buffer.readableBytes() < 3) {
            return
        }
        val opcode = buffer.readUnsignedByte().toInt()
        val size = buffer.readUnsignedShort()
        if (buffer.readableBytes() != size) {
            ctx.close()
            return
        }
        if (opcode != 16 && opcode != 18 && opcode != 19) {
            logger.error { "Received unexpected world login opcode: $opcode" }
            setSession(ctx.channel())
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val revision = buffer.readInt()
        if (revision != NetworkConstants.PROTOCOL_NUMBER) {
            println("Received unexpected protocol number: $revision")
            setSession(ctx.channel())
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        setSession(ctx.channel())
        val data = ByteArray(size - 4)
        // store the data into the buffer
        buffer.readBytes(data)
        // convert the buffer into a readable object
        val buffer = FixedBuffer(data)

        when (opcode) {
            19 -> {
                decodeLobbyLogin(ctx, buffer, out)
            }
            16 -> {
                decodeWorldLogin(ctx, buffer, out)
            }
            else -> {
                logger.debug { "Received unexpected login request from $session. [opcode=$opcode]" }
                ctx.channel().close()
            }
        }
    }

    /**
     * Decode the lobby login from buffer.
     *
     * @param ctx
     * The context
     * @param buffer
     * the buffer with data
     * @param out
     * The outgoing response
     */
    private fun decodeLobbyLogin(ctx: ChannelHandlerContext, buffer: FixedBuffer, out: List<Any>) {
        val rsaSize = buffer.readUnsignedShort()
        if (rsaSize > buffer.remaining) {
            logger.error { "Unexpected buffer length" }
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val rsaData = ByteArray(rsaSize)
        buffer.read(rsaData)
        val rsaBuffer =
            FixedBuffer(Utils.cryptRSA(rsaData, NetworkConstants.LOGIN_EXPONENT, NetworkConstants.LOGIN_MODULUS))
        if (rsaBuffer.readUnsignedByte() != 10) {
            logger.error { "Unexpected buffer length" }
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val isaacSeed = IntArray(4)
        for (i in isaacSeed.indices) {
            isaacSeed[i] = rsaBuffer.readInt()
        }
        if (rsaBuffer.readLong() != 0L) {
            logger.error { "Unexpected rsa long" }
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val password = rsaBuffer.readString()
        rsaBuffer.readLong()
        rsaBuffer.readLong()
        buffer.decodeXTEA(isaacSeed, buffer.offset, buffer.length)
        val username = Misc.formatPlayerNameForProtocol(buffer.readString())
        val gameType = buffer.readUnsignedByte()
        val language = buffer.readUnsignedByte()
        buffer.skipBefore(24)
        buffer.readString()
        buffer.readInt()
        for (index in 0..35) {
            val crc = if (Cache.STORE.indexes[index] == null) 0 else Cache.STORE.indexes[index].crc
            val receivedCrc = buffer.readInt()
            if (crc != receivedCrc && index < 32) {
                session!!.write(LoginResponseCodePacketBuilder(LoginReturnCode.UPDATED))
                    .addListener(ChannelFutureListener.CLOSE)
                println("index=$index, crc=$crc, receivedCrc=$receivedCrc")
                return
            }
        }
        if (Misc.invalidAccountName(username)) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.INVALID_CREDENTIALS).build().buffer)
            return
        }
        if (World.getLobbyPlayers().size >= GameConstants.PLAYERS_LIMIT - 10) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.FULL_WORLD).build().buffer)
            return
        }
        if (World.containsPlayer(username, false)) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.ALREADY_ONLINE).build().buffer)
            return
        }
        val player: Player?
        if (!playerExists(username)) {
            player = Player(username, password)
        } else {
            player = fromFile(username)
            if (player == null) {
                ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.LOGIN_SERVER_OFFLINE).build().buffer)
                return
            }
        }
        val inCipher = Arrays.copyOf(isaacSeed, isaacSeed.size)
        val outCipher = IntArray(4)
        for (i in isaacSeed.indices) {
            outCipher[i] = isaacSeed[i] + 50
        }

        // builds the isaac ciphers used for packet encryption
        session!!.buildCiphers(ISAACCipher(inCipher), ISAACCipher(outCipher))
        player.initializeLobby(username, session)
        if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN)) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.ACCOUNT_DISABLED).build().buffer)
            return
        }
        // send the player to the lobby
        session!!.write(LobbyConfigurationPacketBuilder(player))

        // change decoders
        ctx.pipeline().replace("decoder", "decoder", RS2PacketDecoder(session!!))
    }

    /**
     * Decode the world login from buffer.
     *
     * @param ctx
     * the channel context.
     * @param buffer
     * the buffer with data
     * @param out
     * The outgoing response
     */
    private fun decodeWorldLogin(ctx: ChannelHandlerContext, buffer: FixedBuffer, out: List<Any>) {
        buffer.readUnsignedByte()
        val rsaSize = buffer.readUnsignedShort()
        if (rsaSize > buffer.remaining) {
            logger.error { "Unexpected buffer length" }
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val rsaData = ByteArray(rsaSize)
        buffer.read(rsaData)
        val rsaBuffer =
            FixedBuffer(Utils.cryptRSA(rsaData, NetworkConstants.LOGIN_EXPONENT, NetworkConstants.LOGIN_MODULUS))
        if (rsaBuffer.readUnsignedByte() != 10) {
            logger.error { "Unexpected rsa length" }
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val isaacSeed = IntArray(4)
        for (i in isaacSeed.indices) {
            isaacSeed[i] = rsaBuffer.readInt()
        }
        if (rsaBuffer.readLong() != 0L) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID).build().buffer)
            return
        }
        val password = rsaBuffer.readString()
        rsaBuffer.readLong()
        rsaBuffer.readLong()
        buffer.decodeXTEA(isaacSeed, buffer.offset, buffer.length)
        val username = Misc.formatPlayerNameForProtocol(buffer.readString())
        buffer.readByte()
        val mode = buffer.readByte()
        val width = buffer.readShort()
        val height = buffer.readShort()
        val displayMode = buffer.readByte()
        val userId = ByteArray(24)
        // that's the content of random.dat , which is generated depending on user's hardware and software.
        for (i in 0..23) {
            userId[i] = buffer.readByte().toByte()
        }
        val settings = buffer.readString()
        val affiliateId = buffer.readInt() // used for adverts.
        val settingsBufferLength = buffer.readByte()
        val settingsBuffer = ByteArray(settingsBufferLength)
        for (i in 0 until settingsBufferLength) {
            settingsBuffer[i] = buffer.readByte().toByte()
        }

        // hardware block
        val hwMagic = buffer.readByte()
        if (hwMagic != 5) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.MALFORMED_LOGIN_PACKET).build().buffer)
            return
        }
        val osId = buffer.readByte() // [1 - windows, 2 - mac, 3 - linux, 4 - other]
        val is64Bit = buffer.readByte() == 1
        val osVersion = buffer.readByte()
        val javaVendorID = buffer.readByte()
        val javaVersion = ByteArray(3)
        for (i in 0..2) {
            javaVersion[i] = buffer.readByte().toByte()
        }
        val webclient = buffer.readByte() == 1
        val heapSize = buffer.readShort()
        val availableProcessors = buffer.readByte()
        buffer.read24BitInt() // raw cpu info MEDINT,USHORT,UBYTE,UBYTE,UBYTE
        buffer.readShort()
        buffer.readByte()
        buffer.readByte()
        buffer.readByte()
        for (i in 0..3) {
            buffer.readVString() // those 4 are always empty and not set in client ( propably for future use )
        }
        buffer.readByte() // same for theese two
        buffer.readShort()
        val specialPacketCounter = buffer.readInt()
        val userFlow = buffer.readLong()
        val additionalExists = buffer.readByte() == 1
        var additionalInfo: String? = ""
        if (additionalExists) {
            additionalInfo = buffer.readString()
        }
        val jagtheoraLoaded = buffer.readByte() == 1
        val supportsJavaScript = buffer.readByte() == 1
        buffer.readByte()
        for (index in 0..35) {
            val crc = if (Cache.STORE.indexes[index] == null) 0 else Cache.STORE.indexes[index].crc
            val receivedCrc = buffer.readInt()
            if (crc != receivedCrc && index < 32) {
                session!!.write(LoginResponseCodePacketBuilder(LoginReturnCode.BAD_SESSION_ID))
                    .addListener(ChannelFutureListener.CLOSE)
                return
            }
        }
        if (Misc.invalidAccountName(username)) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.INVALID_CREDENTIALS).build().buffer)
            return
        }
        if (password.length >= 30) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.INVALID_CREDENTIALS).build().buffer)
            return
        }
        // build the isaac ciphers
        val inCipher = Arrays.copyOf(isaacSeed, isaacSeed.size)
        val outCipher = IntArray(4)
        for (i in isaacSeed.indices) {
            outCipher[i] = isaacSeed[i] + 50
        }
        val player: Player?
        if (!playerExists(username)) {
            player = Player(username, password)
        } else {
            player = fromFile(username)
            if (player == null) {
                ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.LOGIN_SERVER_OFFLINE).build().buffer)
                return
            }
        }

        // builds the isaac ciphers used for packet encryption
        session!!.buildCiphers(ISAACCipher(inCipher), ISAACCipher(outCipher))
        player.username = username
        if (PunishmentRepository.isPunished(player, PunishmentType.PLAYER_BAN, PunishmentType.ADDRESS_BAN)) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.INVALID_CREDENTIALS).build().buffer)
            return
        }
        if (GameConstants.HOSTED && password != player.password) {
            ctx.writeAndFlush(LoginResponseCodePacketBuilder(LoginReturnCode.INVALID_CREDENTIALS).build().buffer)
            return
        }

        // start game session
        player.initializeGameSession(username, session, mode, width, height)
        session!!.write(LoginConfigurationPacketBuilder(player))
        player.start()

        // change decoders
        ctx.pipeline().replace("decoder", "decoder", RS2PacketDecoder(session!!))
    }

    /**
     * Sets the session
     *
     * @param channel
     * The channel
     */
    private fun setSession(channel: Channel) {
        session = NetworkSession(channel)
        channel.attr(NetworkConstants.SESSION_KEY).set(session)
    }

    private val logger = InlineLogger()

}