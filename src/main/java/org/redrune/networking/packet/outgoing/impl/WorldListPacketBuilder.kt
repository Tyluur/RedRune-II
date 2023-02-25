package org.redrune.networking.packet.outgoing.impl

import org.redrune.global.wordlist.WorldList.worlds
import org.redrune.networking.packet.Packet
import org.redrune.networking.packet.PacketBuilder
import org.redrune.networking.packet.PacketType
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
class WorldListPacketBuilder(
        /**
         * If the request is for a full update
         */
        private val full: Boolean,
) : OutgoingPacketBuilder(PacketBuilder(88, PacketType.VAR_SHORT)) {

    override fun build(): Packet {
        bldr.writeByte(1) // This was 0
        bldr.writeByte(2)
        bldr.writeByte(if (full) 1 else 0)
        val size = worlds.size
        if (full) {
            bldr.writeSmart(size)
            for (world in worlds.values) {
                bldr.writeSmart(world.countryId)
                bldr.writeGJString(world.countryName)
            }
            bldr.writeSmart(0)
            bldr.writeSmart(size + 1)
            bldr.writeSmart(size)
            for (world in 1..worlds.size) {
                bldr.writeSmart(world) // wid
                bldr.writeByte(0) // loc (idx in list) ^ KEEP THIS 0
                bldr.writeInt(worlds[world]!!.flag)
                bldr.writeGJString(worlds[world]!!.activity) // activity
                bldr.writeGJString(worlds[world]!!.ip) // ip
            }
            bldr.writeInt(-0x6b25b579)
        }
        for (world in 1..worlds.size) {
            bldr.writeSmart(world) // wid
            bldr.writeShort(1337 /*WorldList.INSTANCE.getWorlds().get(world)*/)
        }
        return bldr.toPacket()
    }
}