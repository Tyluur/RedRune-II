package org.redrune.networking.packet.context.impl;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.handler.ObjectHandler;
import org.redrune.utility.game.ClickOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-04
 */
public class ObjectClickInteractionPacketContext extends PacketContext {
	
	private final WorldObject object;
	
	private final ClickOption option;
	
	public ObjectClickInteractionPacketContext(WorldObject object, ClickOption option) {
		this.object = object;
		this.option = option;
	}
	
	@Override
	public void handle(Player player) {
		switch (option) {
			case FIRST:
				ObjectHandler.handleOption1(player, object);
				break;
			case SECOND:
				ObjectHandler.handleOption2(player, object);
				break;
			case THIRD:
				ObjectHandler.handleOption3(player, object);
				break;
			case EXAMINE:
				ObjectHandler.handleExamine(player, object);
				break;
		}
	}
}
