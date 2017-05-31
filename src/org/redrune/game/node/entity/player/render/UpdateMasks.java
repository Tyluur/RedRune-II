package org.redrune.game.node.entity.player.render;

import lombok.Getter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.render.flag.UpdateFlag;
import org.redrune.game.node.entity.player.render.flag.impl.HitUpdate;
import org.redrune.game.node.entity.player.render.flag.impl.MovementUpdate;
import org.redrune.utility.backend.Priority;

import java.util.LinkedList;
import java.util.List;
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
	 * A queue holding all update flags
	 */
	private final List<UpdateFlag> queuedUpdates = new LinkedList<>();
	
	/**
	 * The mask data.
	 */
	private int maskData = 0;
	
	/**
	 * If we're updating this Entity.
	 */
	private boolean updating;
	
	/**
	 * The current animation priority.
	 */
	private Priority animationPriority;
	
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
			if (e.getWalkingQueue().getWalkDir() != -1 || e.getWalkingQueue().getRunDir() != -1) {
				register(new MovementUpdate(e.toPlayer()));
			}
		}
		if (e.getHitMap().getHitList().size() > 0) {
			register(new HitUpdate(e));
		}
		updating = true;
	}
	
	/**
	 * Registers an update flag.
	 *
	 * @param updateFlag
	 * 		The update flag.
	 */
	public void register(UpdateFlag updateFlag) {
		if (updating) {
			queuedUpdates.add(updateFlag);
			return;
		}
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
		updating = false;
		for (UpdateFlag flag : queuedUpdates) {
			register(flag);
		}
		queuedUpdates.clear();
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
	
	/**
	 * Gets the mask data.
	 *
	 * @return The mask data.
	 */
	public int getMaskData() {
		return maskData;
	}
	
	/**
	 * @return the animationPriority
	 */
	public Priority getAnimationPriority() {
		return animationPriority;
	}
	
	/**
	 * @param animationPriority
	 * 		the animationPriority to set
	 */
	public void setAnimationPriority(Priority animationPriority) {
		this.animationPriority = animationPriority;
	}
}