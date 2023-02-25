package org.redrune.networking.packet

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.redrune.utility.functions.BufferUtils

/**
 * @author 'Mystic Flow
 */
class PacketBuilder @JvmOverloads constructor(
    private val opcode: Int = -1,
    private val type: PacketType = PacketType.STANDARD
) {

    @JvmField
    val buffer = Unpooled.buffer()
    private var bitPosition = 0
    fun writeBytes(other: ByteBuf?): PacketBuilder {
        buffer.writeBytes(other)
        return this
    }

    fun writeLong(l: Long): PacketBuilder {
        buffer.writeLong(l)
        return this
    }

    fun toPacket(): Packet {
        return Packet(opcode, type, Unpooled.copiedBuffer(buffer))
    }

    fun writeString(string: String): PacketBuilder {
        buffer.writeBytes(string.toByteArray())
        buffer.writeByte(0.toByte().toInt())
        return this
    }

    fun writeShort128(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` shr 8).toByte().toInt())
        buffer.writeByte((`val` + 128).toByte().toInt())
        return this
    }

    fun writeByteA(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` + 128).toByte().toInt())
        return this
    }

    fun writeShortLE128(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` + 128).toByte().toInt())
        buffer.writeByte((`val` shr 8).toByte().toInt())
        return this
    }

    fun startBitAccess(): PacketBuilder {
        bitPosition = buffer.writerIndex() * 8
        return this
    }

    fun finishBitAccess(): PacketBuilder {
        buffer.writerIndex((bitPosition + 7) / 8)
        return this
    }

    fun writeBits(numBits: Int, value: Int): PacketBuilder {
        var numBits = numBits
        var bytePos = bitPosition shr 3
        var bitOffset = 8 - (bitPosition and 7)
        bitPosition += numBits
        val pos = (bitPosition + 7) / 8
        buffer.ensureWritable(pos + 1) //pos + 1
        buffer.writerIndex(pos)
        var b: Byte
        while (numBits > bitOffset) {
            b = buffer.getByte(bytePos)
            buffer.setByte(bytePos, (b.toInt() and BIT_MASK_OUT[bitOffset].inv()).toByte().toInt())
            buffer.setByte(
                bytePos++, (b.toInt() or (value shr numBits - bitOffset and BIT_MASK_OUT[bitOffset])).toByte()
                    .toInt()
            )
            numBits -= bitOffset
            bitOffset = 8
        }
        b = buffer.getByte(bytePos)
        if (numBits == bitOffset) {
            buffer.setByte(bytePos, (b.toInt() and BIT_MASK_OUT[bitOffset].inv()).toByte().toInt())
            buffer.setByte(bytePos, (b.toInt() or (value and BIT_MASK_OUT[bitOffset])).toByte().toInt())
        } else {
            buffer.setByte(
                bytePos, (b.toInt() and (BIT_MASK_OUT[numBits] shl bitOffset - numBits).inv()).toByte()
                    .toInt()
            )
            buffer.setByte(
                bytePos, (b.toInt() or (value and BIT_MASK_OUT[numBits] shl bitOffset - numBits)).toByte()
                    .toInt()
            )
        }
        return this
    }

    fun writeByteC(`val`: Int): PacketBuilder {
        writeByte((-`val`).toByte())
        return this
    }

    fun writeByte(b: Byte): PacketBuilder {
        buffer.writeByte(b.toInt())
        return this
    }

    fun writeShortLE(`val`: Int): PacketBuilder {
        buffer.writeByte(`val`.toByte().toInt())
        buffer.writeByte((`val` shr 8).toByte().toInt())
        return this
    }

    fun writeIntV1(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` shr 8).toByte().toInt())
        buffer.writeByte(`val`.toByte().toInt())
        buffer.writeByte((`val` shr 24).toByte().toInt())
        buffer.writeByte((`val` shr 16).toByte().toInt())
        return this
    }

    fun writeIntV2(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` shr 16).toByte().toInt())
        buffer.writeByte((`val` shr 24).toByte().toInt())
        buffer.writeByte(`val`.toByte().toInt())
        buffer.writeByte((`val` shr 8).toByte().toInt())
        return this
    }

    fun writeIntLE(`val`: Int): PacketBuilder {
        buffer.writeByte(`val`.toByte().toInt())
        buffer.writeByte((`val` shr 8).toByte().toInt())
        buffer.writeByte((`val` shr 16).toByte().toInt())
        buffer.writeByte((`val` shr 24).toByte().toInt())
        return this
    }

    fun writeSomeInt(`val`: Int): PacketBuilder {
        buffer.writeByte(`val`.toByte().toInt())
        buffer.writeByte((`val` shr 16).toByte().toInt())
        buffer.writeByte((`val` shr 24).toByte().toInt())
        buffer.writeByte((`val` shr 8).toByte().toInt())
        return this
    }

    fun writeByteC(`val`: Byte): PacketBuilder {
        buffer.writeByte((-`val`).toByte().toInt())
        return this
    }

    fun write128Byte(`val`: Int): PacketBuilder {
        buffer.writeByte((128 - `val`).toByte().toInt())
        return this
    }

    fun writeReverse(`is`: ByteArray, offset: Int, length: Int): PacketBuilder {
        for (i in offset + length - 1 downTo offset) {
            buffer.writeByte(`is`[i].toInt())
        }
        return this
    }

    fun writeReverseA(`is`: ByteArray, offset: Int, length: Int): PacketBuilder {
        for (i in offset + length - 1 downTo offset) {
            writeByte128(`is`[i].toInt())
        }
        return this
    }

    fun writeByte128(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` + 128).toByte().toInt())
        return this
    }

    fun write24BitInteger(`val`: Int): PacketBuilder {
        buffer.writeByte((`val` shr 16).toByte().toInt())
        buffer.writeByte((`val` shr 8).toByte().toInt())
        buffer.writeByte(`val`.toByte().toInt())
        return this
    }

    fun writeSmart(`val`: Int): PacketBuilder {
        if (`val` >= 128) {
            writeShort(`val` + 32768)
        } else {
            writeByte(`val`.toByte())
        }
        return this
    }

    fun writeShort(s: Int): PacketBuilder {
        buffer.writeShort(s.toShort().toInt())
        return this
    }

    /**
     * Puts a smart.
     *
     * @param val The value.
     * @return This instance for chaining.
     */
    fun writeIntSmart(`val`: Int): PacketBuilder {
        if (`val` >= 32768) {
            writeInt(`val` + 32768)
        } else {
            writeShort(`val`)
        }
        return this
    }

    fun writeInt(i: Int): PacketBuilder {
        buffer.writeInt(i)
        return this
    }

    fun writeMediumInt(i: Int): PacketBuilder {
        buffer.writeByte((i shl 16 and 0xFF).toByte().toInt())
        buffer.writeByte((i shl 8 and 0xFF).toByte().toInt())
        buffer.writeByte(i.toByte().toInt())
        return this
    }

    fun writeLEMedium(i: Int): PacketBuilder {
        buffer.writeByte(i.toByte().toInt())
        buffer.writeByte((i shr 8).toByte().toInt())
        buffer.writeByte((i shr 16).toByte().toInt())
        return this
    }

    fun skip(skip: Int) {
        for (i in 0 until skip) {
            buffer.writeByte(0.toByte().toInt())
        }
    }

    fun writeGJString2(string: String?): PacketBuilder {
        val packed = ByteArray(256)
        val length = BufferUtils.packGJString2(0, packed, string)
        writeByte(0).writeBytes(packed, 0, length).writeByte(0)
        return this
    }

    fun writeByte(i: Int): PacketBuilder {
        writeByte(i.toByte())
        return this
    }

    fun writeBytes(data: ByteArray?, offset: Int, length: Int): PacketBuilder {
        buffer.writeBytes(data, offset, length)
        return this
    }

    /**
     * Puts an GJ-String on the buffer.
     *
     * @param string The value.
     * @return This OutgoingPacket instance, for chaining.
     */
    fun writeGJString(string: String): PacketBuilder {
        writeByte(0)
        writeBytes(string.toByteArray())
        writeByte(0)
        return this
    }

    fun writeBytes(b: ByteArray?): PacketBuilder {
        buffer.writeBytes(b)
        return this
    }

    /**
     * Writes A-type bytes on the array.
     *
     * @param data   The byte-array.
     * @param offset The offset.
     * @param len    The length.
     * @return This OutgoingPacket instance, for chaining.
     */
    fun writeBytesA(data: ByteArray, offset: Int, len: Int): PacketBuilder {
        for (k in offset until len) {
            buffer.writeByte((data[k] + 128).toByte().toInt())
        }
        return this
    }

    fun position(): Int {
        return buffer.writerIndex()
    }

    fun addBytes128(buffer: ByteBuf) {
        for (k in 0 until buffer.writerIndex()) {
            writeByte((buffer.readByte() + 128).toByte())
        }
    }

    fun writeByte5(value: Long) {
        writeByte((value shr 32).toByte())
        writeInt(value.toInt())
    }

    companion object {

        val BIT_MASK_OUT = IntArray(32)

        init {
            for (i in BIT_MASK_OUT.indices) {
                BIT_MASK_OUT[i] = (1 shl i) - 1
            }
        }
    }
}