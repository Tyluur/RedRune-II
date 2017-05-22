package org.redrune.rs2.world;

import org.redrune.engine.EngineWorkingSet;
import org.redrune.rs2.node.InitializingNodeList;
import org.redrune.rs2.node.entity.player.Player;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * The sequence for an update in the world. This occurs on every tick.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class SequencialUpdate {
	
	/**
	 * Starts the sequence
	 */
	public void start() {
		System.out.println(getRenderablePlayers());
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
		World.get().getRenderablePlayers().sync();
	}
	
	/**
	 * Gets the list of renderable players
	 */
	private InitializingNodeList<Player> getRenderablePlayers() {
		return World.get().getRenderablePlayers();
	}
	
}
