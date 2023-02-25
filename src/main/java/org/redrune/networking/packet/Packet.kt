package org.redrune.networking.packet

import io.netty.buffer.ByteBuf
import org.redrune.utility.functions.BufferUtils

/**
 * Contains all the data inside a packet. Netty 4 usage only.
 *
 * @author 'Mystic Flow
 * @author Tyluur <itstyluur@icloud.com>
 * @since 7/19/2017
 */
class Packet(
    /**
     * The opcode of the packet
     */
    val opcode: Int,
    /**
     * The type of packet this is
     */
    val type: PacketType,
    /**
     * The buffer of the packet
     */
    @JvmField val buffer: ByteBuf
) {

    /**
     * The length of the packet
     */
    val length: Int

    /**
     * Constructs a new packet
     */
    init {
        length = buffer.readableBytes()
    }

    override fun toString(): String {
        return "Packet{opcode=$opcode, type=$type, length=$length}"
    }

    val isRaw: Boolean
        /**
         * If the packet is raw, meaning it was built with no opcode
         */
        get() = opcode == -1

    /**
     * Reads an integer.
     *
     * @return An integer.
     */
    fun readInt(): Int {
        return buffer.readInt()
    }

    /**
     * Reads a long.
     *
     * @return A long.
     */
    fun readLong(): Long {
        return buffer.readLong()
    }

    /**
     * Reads a type C byte.
     *
     * @return A type C byte.
     */
    fun readByteC(): Byte {
        return (-readByte()).toByte()
    }

    fun readByte(): Byte {
        return buffer.readByte()
    }

    fun readUnsignedByteC(): Int {
        return -readUnsignedByte() and 0xff
    }

    fun readUnsignedByte(): Int {
        return buffer.readUnsignedByte().toInt()
    }

    fun readIntLE(): Int {
        return readUnsignedByte() + (readUnsignedByte() shl 8) + (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24)
    }

    fun readByte128(): Int {
        return (readByte() - 128).toByte().toInt()
    }

    fun read128Byte(): Int {
        return (128 - readByte()).toByte().toInt()
    }

    fun readUnsignedByte128(): Int {
        return readUnsignedByte() - 128 and 0xff
    }

    fun readUnsignedShort128(): Int {
        return (readUnsignedByte() shl 8) + (readByte() - 128 and 0xff)
    }

    /**
     * Reads a little-endian type A short.
     *
     * @return A little-endian type A short.
     */
    fun readShortLE128(): Int {
        return buffer.readByte() - 128 and 0xFF or (buffer.readByte().toInt() and 0xFF shl 8)
    }

    /**
     * Reads a little-endian short.
     *
     * @return A little-endian short.
     */
    fun readLEShort(): Int {
        return buffer.readByte().toInt() and 0xFF or (buffer.readByte().toInt() and 0xFF shl 8)
    }

    /**
     * Reads a V1 integer.
     *
     * @return A V1 integer.
     */
    fun readIntV1(): Int {
        return (buffer.readUnsignedByte().toInt() shl 8) + buffer.readUnsignedByte() + (buffer.readUnsignedByte()
            .toInt() shl 24) + (buffer.readUnsignedByte().toInt() shl 16)
    }

    /**
     * Reads a V2 integer.
     *
     * @return A V2 integer.
     */
    fun readInt2(): Int {
        val b1 = buffer.readByte().toInt() and 0xFF
        val b2 = buffer.readByte().toInt() and 0xFF
        val b3 = buffer.readByte().toInt() and 0xFF
        val b4 = buffer.readByte().toInt() and 0xFF
        return b2 shl 24 or (b1 shl 16) or (b4 shl 8) or b3
    }

    fun readIntV2(): Int {
        return (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24) + readUnsignedByte() + (readUnsignedByte() shl 8)
    }

    /**
     * reads a 3-byte integer.
     *
     * @return The 3-byte integer.
     */
    fun readTriByte(): Int {
        return buffer.readByte().toInt() shl 16 and 0xFF or (buffer.readByte()
            .toInt() shl 8 and 0xFF) or (buffer.readByte().toInt() and 0xFF)
    }

    /**
     * Reads a type A short.
     *
     * @return A type A short.
     */
    fun readShortA(): Int {
        return buffer.readByte().toInt() and 0xFF shl 8 or (buffer.readByte() - 128 and 0xFF)
    }

    /**
     * Reads a series of bytes in reverse.
     *
     * @param bytes  The tarread byte array.
     * @param offset The offset.
     * @param length The length.
     */
    fun readReverse(bytes: ByteArray, offset: Int, length: Int) {
        for (i in offset + length - 1 downTo offset) {
            bytes[i] = buffer.readByte()
        }
    }

    /**
     * Reads a series of type A bytes in reverse.
     *
     * @param bytes  The tarread byte array.
     * @param offset The offset.
     * @param length The length.
     */
    fun readReverseA(bytes: ByteArray, offset: Int, length: Int) {
        for (i in offset + length - 1 downTo offset) {
            bytes[i] = readByteA()
        }
    }

    /**
     * Reads a type A byte.
     *
     * @return A type A byte.
     */
    fun readByteA(): Byte {
        return (readByte() - 128).toByte()
    }

    /**
     * Reads a series of bytes.
     *
     * @param bytes  The to read byte array.
     * @param offset The offset.
     * @param length The length.
     */
    fun read(bytes: ByteArray, offset: Int, length: Int) {
        for (i in 0 until length) {
            bytes[offset + i] = buffer.readByte()
        }
    }

    /**
     * reads a smart.
     *
     * @return The smart.
     */
    fun readSmart(): Int {
        val peek = buffer.getByte(buffer.readerIndex()).toInt()
        return if (peek < 128) {
            readByte().toInt() and 0xFF
        } else {
            (readShort() and 0xFFFF) - 32768
        }
    }

    fun readShortLE(): Int {
        var i = readUnsignedByte() + (readUnsignedByte() shl 8)
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }

    /**
     * Reads a short.
     *
     * @return A short.
     */
    fun readShort(): Int {
        return buffer.readShort().toInt()
    }

    fun remaining(): Int {
        return buffer.readableBytes()
    }

    fun readBytes(textBuffer: ByteArray?, length: Int) {
        buffer.readBytes(textBuffer, 0, length)
    }

    fun readBytes(textBuffer: ByteArray?) {
        buffer.readBytes(textBuffer)
    }

    fun readJagString(): String {
        readByte()
        return readRS2String()
    }

    /**
     * Reads a RuneScape string.
     *
     * @return The string.
     */
    fun readRS2String(): String {
        return BufferUtils.readRS2String(buffer)
    }

    fun readLEInt(): Int {
        return readUnsignedByte() + (readUnsignedByte() shl 8) + (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24)
    }

    fun readUnsignedSmart(): Int {
        val i = 0xff and buffer.arrayOffset()
        return if (i >= 128) {
            -32768 + readUnsignedShort()
        } else readUnsignedByte()
    }

    /**
     * Reads an unsigned short.
     *
     * @return An unsigned short.
     */
    fun readUnsignedShort(): Int {
        return buffer.readUnsignedShort()
    }

    fun readUnsignedShortLE128(): Int {
        return (buffer.readByte() - 128 and 0xff) + (buffer.readUnsignedByte().toInt() shl 8)
    }

    fun readUnsignedShortLE(): Int {
        return readUnsignedByte() + (readUnsignedByte() shl 8)
    }

    fun readShort128(): Int {
        var i = (readUnsignedByte() shl 8) + (readByte() - 128 and 0xff)
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }
}