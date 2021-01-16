package org.redrune.networking.packet.context.impl;

import org.redrune.game.GameFlags;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.context.PacketContext;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-04
 */
public class InterfaceInteractionPacketContext extends PacketContext {
	
	private final int interfaceId;
	
	private final int componentId;
	
	private final int itemId;
	
	private final int slotId;
	
	private final int packetId;
	
	public InterfaceInteractionPacketContext(int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.itemId = itemId;
		this.slotId = slotId;
		this.packetId = packetId;
	}
	
	@Override
	public void handle(Player player) {
		if (!player.getControllerManager().processButtonClick(interfaceId, componentId, slotId, packetId)) {
			return;
		}
		InterfacePlugin plugin = PluginRepository.handleInterface(player, interfaceId, componentId, itemId, slotId, packetId);
		if (GameFlags.debugMode) {
			if (plugin != null) {
				System.out.println("[" + plugin.getClass().getSimpleName() + "] handled [" + interfaceId + ", " + componentId + ", " + packetId + "]");
			} else {
				System.out.println("[N/A] handled [" + interfaceId + ", " + componentId + ", " + packetId + "]");
			}
		}
	}
}
