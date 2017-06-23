package org.redrune.game.node.entity.player.render;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.game.node.entity.player.render.flag.impl.HitUpdate;
import org.redrune.game.node.entity.player.render.flag.impl.MovementUpdate;
import org.redrune.utility.backend.Priority;

import java.util.PriorityQueue;

/**
 * Represents an Entity's update masks.
 *
 * @author Emperor
 */
public class UpdateMasks {
	
	/**
	 * Our priority queue used.
	 */
	@Getter
	private final PriorityQueue<UpdateFlag> flagQueue = new PriorityQueue<>();
	
	/**
	 * The mask data.
	 */
	@Getter
	private int maskData = 0;
	
	/**
	 * The current animation priority.
	 */
	@Getter
	@Setter
	private Priority animationPriority;
	
	/**
	 * The time the last animation ended
	 */
	@Getter
	@Setter
	private long lastAnimationEndTime = -1;
	
	/**
	 * Prepares the outgoing packet for updating.
	 *
	 * @param e
	 * 		The entity who's using this update mask instance.
	 */
	public void prepare(Entity e) {
		if (e.isPlayer()) {
			if (e.toPlayer().getDetails().getAppearance() != null) {
				e.toPlayer().getDetails().getAppearance().prepareBodyData(e.toPlayer());
			}
			if (e.getMovement().getNextWalkDirection() != -1 || e.getMovement().getNextRunDirection() != -1) {
				register(new MovementUpdate(e.toPlayer()));
			}
		}
		if (e.getHitMap().getHitList().size() > 0) {
			register(new HitUpdate(e));
		}
	}
	
	/**
	 * Registers an update flag.
	 *
	 * @param updateFlag
	 * 		The update flag.
	 */
	public void register(UpdateFlag updateFlag) {
		if (!updateFlag.canRegister(this)) {
			return;
		}
		if ((maskData & updateFlag.getMaskData()) != 0) {
			flagQueue.remove(updateFlag);
		}
		maskData |= updateFlag.getMaskData();
		flagQueue.add(updateFlag);
	}
	
	/**
	 * Finishes the updating.
	 */
	public void finish() {
		animationPriority = Priority.LOWEST;
		maskData = 0;
		flagQueue.clear();
	}
	
	/**
	 * Checks if an update is required.
	 *
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isUpdateRequired() {
		return maskData != 0;
	}
	
	/**
	 * Checks if an update flag was registered.
	 *
	 * @param data
	 * 		The mask data of the update flag.
	 * @return {@code True} if the update flag was registered, {@code false} if not.
	 */
	public boolean get(int data) {
		return (maskData & data) != 0;
	}
	
}