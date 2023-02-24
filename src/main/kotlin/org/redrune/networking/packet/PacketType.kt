package org.redrune.networking.packet

/**
 * @author 'Mystic Flow
 * @author Tyluur
 */
enum class PacketType(private val size: Int) {
    STANDARD(0), VAR_BYTE(1), VAR_SHORT(2)
}