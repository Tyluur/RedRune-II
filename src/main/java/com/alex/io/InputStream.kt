package com.alex.io

class InputStream : Stream {
    constructor(capacity: Int) {
        buffer = ByteArray(capacity)
    }

    constructor(buffer: ByteArray) {
        this.buffer = buffer
        length = buffer.size
    }

    fun initBitAccess() {
        bitPosition = offset * 8
    }

    fun finishBitAccess() {
        offset = (7 + bitPosition) / 8
    }

    fun readBits(bitOffset: Int): Int {
        var bitOffset = bitOffset
        var bytePos = bitPosition shr 1779819011
        var i_8_ = -(0x7 and bitPosition) + 8
        bitPosition += bitOffset
        var value = 0
        while ( /**/bitOffset xor -0x1 < i_8_ xor -0x1) {
            value += BIT_MASK[i_8_] and buffer[bytePos++].toInt() shl -i_8_ + bitOffset
            bitOffset -= i_8_
            i_8_ = 8
        }
        value += if (i_8_ xor -0x1 == bitOffset xor -0x1) {
            buffer[bytePos].toInt() and BIT_MASK[i_8_]
        } else {
            buffer[bytePos].toInt() shr -bitOffset + i_8_ and BIT_MASK[bitOffset]
        }
        return value
    }

    fun skip(length: Int) {
        offset += length
    }

    override var length: Int = 0
        get() = super.length
    override var offset: Int = 0
        get() = super.offset

    fun addBytes(b: ByteArray?, offset: Int, length: Int) {
        checkCapacity(length - offset)
        System.arraycopy(b, offset, buffer, this.offset, length)
        this.length += length - offset
    }

    fun checkCapacity(length: Int) {
        if (offset + length >= buffer.size) {
            val newBuffer = ByteArray((offset + length) * 2)
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.size)
            buffer = newBuffer
        }
    }

    fun readPacket(): Int {
        return readUnsignedByte()
    }

    fun readUnsignedByte(): Int {
        return readByte() and 0xff
    }

    fun readByte(): Int {
        return if (remaining > 0) buffer[offset++].toInt() else 0
    }

    val remaining: Int
        get() = if (offset < length) length - offset else 0

    @JvmOverloads
    fun readBytes(buffer: ByteArray, off: Int = 0, len: Int = buffer.size) {
        for (k in off until len + off) {
            buffer[k] = readByte().toByte()
        }
    }

    fun readSmart2(): Int {
        var i = 0
        var i_33_ = readUnsignedSmart()
        while (i_33_ xor -0x1 == -32768) {
            i_33_ = readUnsignedSmart()
            i += 32767
        }
        i += i_33_
        return i
    }

    fun readByte128(): Int {
        return (readByte() - 128).toByte().toInt()
    }

    fun readByteC(): Int {
        return -readByte().toByte()
    }

    fun read128Byte(): Int {
        return (128 - readByte()).toByte().toInt()
    }

    fun readUnsignedByte128(): Int {
        return readUnsignedByte() - 128 and 0xff
    }

    fun readUnsignedByteC(): Int {
        return -readUnsignedByte() and 0xff
    }

    fun readUnsigned128Byte(): Int {
        return 128 - readUnsignedByte() and 0xff
    }

    fun readShortLE(): Int {
        var i = readUnsignedByte() + (readUnsignedByte() shl 8)
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }

    fun readShort128(): Int {
        var i = (readUnsignedByte() shl 8) + (readByte() - 128 and 0xff)
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }

    fun readShortLE128(): Int {
        var i = (readByte() - 128 and 0xff) + (readUnsignedByte() shl 8)
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }

    fun read128ShortLE(): Int {
        var i = (128 - readByte() and 0xff) + (readUnsignedByte() shl 8)
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }

    fun readShort(): Int {
        var i = (readUnsignedByte() shl 8) + readUnsignedByte()
        if (i > 32767) {
            i -= 0x10000
        }
        return i
    }

    fun readUnsignedShortLE(): Int {
        return readUnsignedByte() + (readUnsignedByte() shl 8)
    }

    fun readUnsignedShort(): Int {
        return (readUnsignedByte() shl 8) + readUnsignedByte()
    }

    fun readUnsignedShort128(): Int {
        return (readUnsignedByte() shl 8) + (readByte() - 128 and 0xff)
    }

    fun readUnsignedShortLE128(): Int {
        return (readByte() - 128 and 0xff) + (readUnsignedByte() shl 8)
    }

    fun read24BitInt(): Int {
        return (readUnsignedByte() shl 16) + (readUnsignedByte() shl 8) + readUnsignedByte()
    }

    fun readIntV1(): Int {
        return (readUnsignedByte() shl 8) + readUnsignedByte() + (readUnsignedByte() shl 24) + (readUnsignedByte() shl 16)
    }

    fun readIntV2(): Int {
        return (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24) + readUnsignedByte() + (readUnsignedByte() shl 8)
    }

    fun readIntLE(): Int {
        return readUnsignedByte() + (readUnsignedByte() shl 8) + (readUnsignedByte() shl 16) + (readUnsignedByte() shl 24)
    }

    fun readLong(): Long {
        val l = readInt().toLong() and 0xffffffffL
        val l1 = readInt().toLong() and 0xffffffffL
        return (l shl 32) + l1
    }

    fun readInt(): Int {
        return (readUnsignedByte() shl 24) + (readUnsignedByte() shl 16) + (readUnsignedByte() shl 8) + readUnsignedByte()
    }

    fun readString(): String {
        var s = ""
        var b: Int
        while (readByte().also { b = it } != 0) {
            s += b.toChar()
        }
        return s
    }

    fun readJagString(): String {
        readByte()
        var s = ""
        var b: Int
        while (readByte().also { b = it } != 0) {
            s += b.toChar()
        }
        return s
    }

    fun readBigSmart(): Int {
        if (buffer[offset].toInt() xor -0x1 <= -1) {
            val value = readUnsignedShort()
            return if (value == 32767) {
                -1
            } else value
        }
        return readInt() and 0x7fffffff
    }

    fun readUnsignedSmart(): Int {
        val i = 0xff and buffer[offset].toInt()
        return if (i >= 128) {
            -32768 + readUnsignedShort()
        } else readUnsignedByte()
    }

    companion object {

        private val BIT_MASK = intArrayOf(
            0,
            1,
            3,
            7,
            15,
            31,
            63,
            127,
            255,
            511,
            1023,
            2047,
            4095,
            8191,
            16383,
            32767,
            65535,
            131071,
            262143,
            524287,
            1048575,
            2097151,
            4194303,
            8388607,
            16777215,
            33554431,
            67108863,
            134217727,
            268435455,
            536870911,
            1073741823,
            2147483647,
            -1
        )
    }
}