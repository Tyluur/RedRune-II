package org.redrune.rs2.world;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.redrune.engine.EngineWorkingSet;
import org.redrune.rs2.node.InitializingNodeList;
import org.redrune.rs2.node.entity.player.Player;

import lombok.Getter;

/**
 * The sequence for an update in the world. This occurs on every tick.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class SequencialUpdate {
	
	/**
	 * The players that are renderable
	 */
	@Getter
	private static final InitializingNodeList<Player> renderablePlayers = new InitializingNodeList<>();
	
	/**
	 * Starts the sequence
	 */
	public void start() {
		for (Player player : getRenderablePlayers()) {
			player.tick();
			player.getWalkingQueue().updateMovement();
			player.getUpdateMasks().prepare(player);
		}
	}
	
	/**
	 * Executes the updating part of the sequence
	 */
	public void execute() {
		final CountDownLatch latch = new CountDownLatch(getRenderablePlayers().size());
		for (Player player : getRenderablePlayers()) {
			EngineWorkingSet.submitEngineWork(() -> {
				try {
					player.sendUpdating();
					latch.countDown();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			});
		}
		try {
			latch.await(1000L, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Finishes the sequence
	 */
	public void end() {
		for (Player player : getRenderablePlayers()) {
			player.getUpdateMasks().finish();
			player.getRenderData().updateInformation();
			player.getHitMap().getHitList().clear();
		}
		renderablePlayers.sync();
	}
	
}
