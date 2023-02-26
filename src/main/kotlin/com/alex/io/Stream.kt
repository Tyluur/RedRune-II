package com.alex.io

abstract class Stream {

    open var offset: Int = 0
    open var length: Int = 0
    open var bitPosition: Int = 0
    open var buffer: ByteArray = ByteArray(length)

    @JvmOverloads
    fun decodeXTEA(keys: IntArray, start: Int = 5, end: Int = length) {
        val l = offset
        offset = start
        val i1 = (end - start) / 8
        for (j1 in 0 until i1) {
            var k1 = readInt()
            var l1 = readInt()
            var sum = -0x3910c8e0
            val delta = -0x61c88647
            var k2 = 32
            while (k2-- > 0) {
                l1 -= keys[sum and 0x1c84 ushr 11] + sum xor (k1 ushr 5 xor (k1 shl 4)) + k1
                sum -= delta
                k1 -= (l1 ushr 5 xor (l1 shl 4)) + l1 xor keys[sum and 3] + sum
            }
            offset -= 8
            writeInt(k1)
            writeInt(l1)
        }
        offset = l
    }

    private fun readInt(): Int {
        offset += 4
        return (0xff and buffer[-3 + offset].toInt() shl 16) + ((0xff and buffer[-4 + offset].toInt() shl 24) + (buffer[-2 + offset].toInt() and 0xff shl 8) + (buffer[-1 + offset].toInt() and 0xff))
    }

    open fun writeInt(value: Int) {
        buffer[offset++] = (value shr 24).toByte()
        buffer[offset++] = (value shr 16).toByte()
        buffer[offset++] = (value shr 8).toByte()
        buffer[offset++] = value.toByte()
    }

    fun encodeXTEA(keys: IntArray, start: Int, end: Int) {
        val o = offset
        val j = (end - start) / 8
        offset = start
        for (k in 0 until j) {
            var l = readInt()
            var i1 = readInt()
            var sum = 0
            val delta = -0x61c88647
            var l1 = 32
            while (l1-- > 0) {
                l += sum + keys[3 and sum] xor i1 + (i1 ushr 5 xor (i1 shl 4))
                sum += delta
                i1 += l + (l ushr 5 xor (l shl 4)) xor keys[0x1eec and sum ushr 11] + sum
            }
            offset -= 8
            writeInt(l)
            writeInt(i1)
        }
        offset = o
    }

    fun getBytes(data: ByteArray, off: Int, len: Int) {
        for (k in off until len + off) {
            data[k] = buffer[offset++]
        }
    }
}