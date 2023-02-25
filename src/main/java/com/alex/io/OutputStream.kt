package com.alex.io

import com.alex.utils.Constants
import java.math.BigInteger

class OutputStream : Stream {



    private var opcodeStart = 0

    constructor(capacity: Int) {
        buffer = ByteArray(capacity)
    }

    constructor() {
        buffer = ByteArray(16)
    }

    constructor(buffer: ByteArray) {
        this.buffer = buffer
        offset = buffer.size
        length = buffer.size
    }

    constructor(buffer: IntArray) {
        this.buffer = ByteArray(buffer.size)
        for (value in buffer) {
            writeByte(value)
        }
    }

    @JvmOverloads
    fun writeByte(i: Int, position: Int = offset++) {
        checkCapacityPosition(position)
        buffer[position] = i.toByte()
    }

    fun checkCapacityPosition(position: Int) {
        if (position >= buffer!!.size) {
            val newBuffer = ByteArray(position + 16)
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.size)
            buffer = newBuffer
        }
    }

    override fun writeInt(i: Int) {
        writeByte(i shr 24)
        writeByte(i shr 16)
        writeByte(i shr 8)
        writeByte(i)
    }

    fun skip(length: Int) {
        offset = offset + length
    }

    fun writeBytes(b: ByteArray) {
        val offset = 0
        val length = b.size
        checkCapacityPosition(this.offset + length - offset)
        System.arraycopy(b, offset, buffer, this.offset, length)
        this.offset = this.offset + (length - offset)
    }

    fun addBytes128(data: ByteArray, offset: Int, len: Int) {
        for (k in offset until len) {
            writeByte((data[k] + 128).toByte().toInt())
        }
    }

    fun addBytesS(data: ByteArray, offset: Int, len: Int) {
        for (k in offset until len) {
            writeByte((-128 + data[k]).toByte().toInt())
        }
    }

    fun addBytes_Reverse(data: ByteArray, offset: Int, len: Int) {
        for (i in len - 1 downTo 0) {
            writeByte(data[i].toInt())
        }
    }

    fun addBytes_Reverse128(data: ByteArray, offset: Int, len: Int) {
        for (i in len - 1 downTo 0) {
            writeByte((data[i] + 128).toByte().toInt())
        }
    }

    fun writeNegativeByte(i: Int) {
        writeByte(-i, offset++)
    }

    fun writeByte128(i: Int) {
        writeByte(i + 128)
    }

    fun writeByteC(i: Int) {
        writeByte(-i)
    }

    fun write3Byte(i: Int) {
        writeByte(i shr 16)
        writeByte(i shr 8)
        writeByte(i)
    }

    fun write128Byte(i: Int) {
        writeByte(128 - i)
    }

    fun writeShortLE128(i: Int) {
        writeByte(i + 128)
        writeByte(i shr 8)
    }

    fun writeShort128(i: Int) {
        writeByte(i shr 8)
        writeByte(i + 128)
    }

    @Suppress("unused")
    fun writeBigSmart(i: Int) {
        if (Constants.CLIENT_BUILD < 670) {
            writeShort(i)
            return
        }
        if (i >= Short.MAX_VALUE && i >= 0) {
            writeInt(i - Int.MAX_VALUE - 1)
        } else {
            writeShort(if (i >= 0) i else 32767)
        }
    }

    fun writeSmart(i: Int) {
        if (i >= 128) {
            writeShort(i + 32768)
        } else {
            writeByte(i)
        }
    }

    fun writeShort(i: Int) {
        writeByte(i shr 8)
        writeByte(i)
    }

    fun writeShortLE(i: Int) {
        writeByte(i)
        writeByte(i shr 8)
    }

    fun write24BitInt(i: Int) {
        writeByte(i shr 16)
        writeByte(i shr 8)
        writeByte(i)
    }

    fun writeIntV1(i: Int) {
        writeByte(i shr 8)
        writeByte(i)
        writeByte(i shr 24)
        writeByte(i shr 16)
    }

    fun writeIntV2(i: Int) {
        writeByte(i shr 16)
        writeByte(i shr 24)
        writeByte(i)
        writeByte(i shr 8)
    }

    fun writeIntLE(i: Int) {
        writeByte(i)
        writeByte(i shr 8)
        writeByte(i shr 16)
        writeByte(i shr 24)
    }

    fun writeLong(l: Long) {
        writeByte((l shr 56).toInt())
        writeByte((l shr 48).toInt())
        writeByte((l shr 40).toInt())
        writeByte((l shr 32).toInt())
        writeByte((l shr 24).toInt())
        writeByte((l shr 16).toInt())
        writeByte((l shr 8).toInt())
        writeByte(l.toInt())
    }

    fun writePSmarts(i: Int) {
        if (i < 128) {
            writeByte(i)
            return
        }
        if (i < 32768) {
            writeShort(32768 + i)
        } else {
            println("Error psmarts out of range:")
        }
    }

    fun writeString(s: String) {
        checkCapacityPosition(offset + s.length + 1)
        System.arraycopy(s.toByteArray(), 0, buffer, offset, s.length)
        offset = offset + s.length
        writeByte(0)
    }

    fun writeGJString(s: String) {
        writeByte(0)
        writeString(s)
    }

    fun putGJString3(s: String) {
        writeByte(0)
        writeString(s)
        writeByte(0)
    }

    fun writePacket(id: Int) {
        writeByte(id)
    }

    fun writePacketVarByte(id: Int) {
        writePacket(id)
        writeByte(0)
        opcodeStart = offset - 1
    }

    fun writePacketVarShort(id: Int) {
        writePacket(id)
        writeShort(0)
        opcodeStart = offset - 2
    }

    /*
     * public void writePacketShort(int id) { writeByte(id); writeShort(0);
     * opcodeStart = getOffset() - 2; }
     */
    fun endPacketVarByte() {
        writeByte(offset - (opcodeStart + 2) + 1, opcodeStart)
    }

    fun endPacketVarShort() {
        val size = offset - (opcodeStart + 2)
        writeByte(size shr 8, opcodeStart++)
        writeByte(size, opcodeStart)
    }

    fun initBitAccess() {
        bitPosition = offset * 8
    }

    fun finishBitAccess() {
        offset = (bitPosition + 7) / 8
    }

    fun getBitPos(i: Int): Int {
        return 8 * i - bitPosition
    }

    fun writeBits(numBits: Int, value: Int) {
        var numBits = numBits
        var bytePos = bitPosition shr 3
        var bitOffset = 8 - (bitPosition and 7)
        bitPosition += numBits
        while (numBits > bitOffset) {
            checkCapacityPosition(bytePos)
            buffer!![bytePos] = (buffer!![bytePos].toInt() and BIT_MASK[bitOffset].inv()).toByte()
            buffer!![bytePos++] = (buffer!![bytePos++]
                .toInt() or (value shr numBits - bitOffset and BIT_MASK[bitOffset])).toByte()
            numBits -= bitOffset
            bitOffset = 8
        }
        checkCapacityPosition(bytePos)
        if (numBits == bitOffset) {
            buffer!![bytePos] = (buffer!![bytePos].toInt() and BIT_MASK[bitOffset].inv()).toByte()
            buffer!![bytePos] = (buffer!![bytePos].toInt() or (value and BIT_MASK[bitOffset])).toByte()
        } else {
            buffer!![bytePos] = (buffer!![bytePos]
                .toInt() and (BIT_MASK[numBits] shl bitOffset - numBits).inv()).toByte()
            buffer!![bytePos] = (buffer!![bytePos]
                .toInt() or (value and BIT_MASK[numBits] shl bitOffset - numBits)).toByte()
        }
    }

    fun rsaEncode(key: BigInteger?, modulus: BigInteger?) {
        val length = offset
        offset = 0
        val data = ByteArray(length)
        getBytes(data, 0, length)
        val biginteger2 = BigInteger(data)
        val biginteger3 = biginteger2.modPow(key, modulus)
        val out = biginteger3.toByteArray()
        offset = 0
        writeBytes(out, 0, out.size)
    }

    fun writeBytes(b: ByteArray?, offset: Int, length: Int) {
        checkCapacityPosition(this.offset + length - offset)
        System.arraycopy(b, offset, buffer, this.offset, length)
        this.offset = this.offset + (length - offset)
    }

    companion object {

        private val BIT_MASK = IntArray(32)

        init {
            for (i in 0..31) {
                BIT_MASK[i] = (1 shl i) - 1
            }
        }
    }
}