package org.redrune.networking.packet.outgoing.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.networking.packet.PacketType;
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class LoginConfigurationPacketBuilder extends OutgoingPacketBuilder {

    private final Player player;

    public LoginConfigurationPacketBuilder(Player player) {
        super(new PacketBuilder(2, PacketType.VAR_BYTE));
        this.player = player;
    }

    @Override
    public Packet build() {
        bldr.writeByte(player.getDominantRight().getClientRight());
        bldr.writeByte(0);
        bldr.writeByte(0);
        bldr.writeByte(0);
        bldr.writeByte(1);
        bldr.writeByte(0);
        bldr.writeShort(player.getIndex());
        bldr.writeByte(1);
        bldr.write24BitInteger(0);
        bldr.writeByte(1);
        bldr.writeString(player.getDisplayName());
        return bldr.toPacket();
    }
}
