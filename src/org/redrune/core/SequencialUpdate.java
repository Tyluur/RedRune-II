package org.redrune.core;

import lombok.Getter;
import org.redrune.core.system.SystemManager;
import org.redrune.game.node.InitializingNodeList;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

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
	 * The players that are renderable
	 */
	@Getter
	private static final InitializingNodeList<Player> renderablePlayers = new InitializingNodeList<>();
	
	/**
	 * Starts the sequence
	 */
	void start() {
		try {
			SystemManager.getScheduler().pulse();
			for (Player player : getRenderablePlayers()) {
				player.getMovement().processMovement();
				player.getUpdateMasks().prepare(player);
				player.tick();
			}
			for (NPC npc : World.get().getNpcs()) {
				if (npc == null || !npc.isRenderable()) {
					continue;
				}
				npc.getMovement().processMovement();
				npc.getUpdateMasks().prepare(npc);
				npc.tick();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes the updating part of the sequence
	 */
	void execute() {
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
	void end() {
		try {
			for (Player player : getRenderablePlayers()) {
				player.getUpdateMasks().finish();
				player.getRenderData().updateInformation();
				player.getHitMap().getHitList().clear();
			}
			for (NPC npc : World.get().getNpcs()) {
				if (npc == null || !npc.isRenderable()) {
					continue;
				}
				npc.getUpdateMasks().finish();
				npc.getHitMap().getHitList().clear();
			}
			renderablePlayers.sync();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}