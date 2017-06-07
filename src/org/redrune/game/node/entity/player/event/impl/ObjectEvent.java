package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.context.ObjectEventContext;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.entity.player.render.flag.impl.FaceLocationUpdate;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class ObjectEvent extends Event<ObjectEventContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public ObjectEvent(ObjectEventContext context) {
		super(context);
	}
	
	@Override
	public boolean canStart(Player player) {
		return !player.getManager().getLocks().isLocked(LockType.OBJECT_INTERACTION);
	}
	
	@Override
	public void run(Player player) {
		player.getUpdateMasks().register(new FaceLocationUpdate(player, getContext().getObject().getLocation()));
		
		if (!ModuleRepository.handle(player, getContext().getObject(), getContext().getOption())) {
			player.getTransmitter().sendMessage("Nothing interesting happens.");
		}
	}
}
