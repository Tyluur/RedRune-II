package network.packet.context.impl;

import game.content.plugin.PluginRepository;
import game.entity.actor.player.Player;
import network.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class CommandPacketContext extends PacketContext {

    private final boolean clientCommand;
    private final boolean unknown;
    private final String command;

    public CommandPacketContext(boolean clientCommand, boolean unknown, String command) {
        this.clientCommand = clientCommand;
        this.unknown = unknown;
        this.command = command;
    }

    @Override
    public void handle(Player player) {
        PluginRepository.handleCommand(player, command.toLowerCase().replaceFirst("::", "").split(" "), true, clientCommand);
    }
}
