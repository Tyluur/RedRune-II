package org.redrune.game.content.entity.actor.player.skills.agility;

import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceMovement;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.global.World;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.constants.SkillConstants;

public class WildernessAgility {
	
	public static void swingOnRopeSwing(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		} else if (player.getY() != 3953) {
			player.getPackets().sendGameMessage("You'll need to get closer to make this jump.");
			return;
		}
		player.getLocks().lock(7);
		player.setNextAnimation(new Animation(751));
		World.sendObjectAnimation(player, object, new Animation(497));
		final WorldTile toTile = new WorldTile(object.getX(), 3958, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH));
		player.getSkills().addXp(SkillConstants.AGILITY, 20);
		player.getPackets().sendGameMessage("You skillfully swing across.", true);
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				if (getStage(player) != 1) {
					removeStage(player);
				} else {
					setStage(player, 2);
				}
			}
		}, 1);
	}
	
	public static void walkAcrossLogBalance(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		if (player.getY() != object.getY()) {
			player.addWalkSteps(3001, 3945, -1, false);
			player.getLocks().lock(2);
			WorldTasksManager.schedule(new WorldTask() {
				
				@Override
				public void run() {
					walkAcrossLogBalanceEnd(player, object);
				}
			}, 1);
		} else {
			walkAcrossLogBalanceEnd(player, object);
		}
	}
	
	private static void walkAcrossLogBalanceEnd(final Player player, WorldObject object) {
		player.getPackets().sendGameMessage("You walk carefully across the slippery log...", true);
		player.getLocks().lock();
		player.setNextAnimation(new Animation(9908));
		final WorldTile toTile = new WorldTile(2994, object.getY(), object.getPlane());
		player.setNextForceMovement(new ForceMovement(toTile, 10, ForceMovement.WEST));
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextAnimation(new Animation(-1));
				player.setNextWorldTile(toTile);
				player.getLocks().unlock();
				player.getSkills().addXp(SkillConstants.AGILITY, 20);
				player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
				if (getStage(player) != 3) {
					removeStage(player);
				} else {
					setStage(player, 4);
				}
			}
		}, 9);
	}
	
	public static void jumpSteppingStones(final Player player, final WorldObject object) {
		if (player.getY() != object.getY()) {
			return;
		}
		player.getLocks().lock(6);
		WorldTasksManager.schedule(new WorldTask() {
			
			int x;
			
			@Override
			public void run() {
				if (x++ == 6) {
					stop();
					return;
				}
				final WorldTile toTile = new WorldTile(3002 - x, player.getY(), player.getPlane());
				player.setNextForceMovement(new ForceMovement(toTile, 1, ForceMovement.WEST));
				player.setNextAnimation(new Animation(741));
				WorldTasksManager.schedule(new WorldTask() {
					
					@Override
					public void run() {
						player.setNextWorldTile(toTile);
					}
				}, 0);
			}
		}, 2, 1);
		player.getSkills().addXp(SkillConstants.AGILITY, 20);
		if (getStage(player) != 2) {
			removeStage(player);
		} else {
			setStage(player, 3);
		}
	}
	
	public static void climbUpWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		player.useStairs(3378, new WorldTile(2994, 3932, 0), 7, 9);
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				if (getStage(player) != 4) {
					removeStage(player);
				} else {
					player.getSkills().addXp(SkillConstants.AGILITY, 498.9);
					setStage(player, 0);
				}
			}
		}, 8);
	}
	
	public static void enterWildernessCourse(final Player player) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		WorldObject firstGate = new WorldObject(65365, 10, 1, 2998, 3916, 0);
		final WorldObject secondGate = new WorldObject(65367, 10, 1, 2998, 3930, 0);
		player.setNextWorldTile(new WorldTile(firstGate.getX(), firstGate.getY() + 1, 0));
		player.setNextForceMovement(new ForceMovement(secondGate, 8, ForceMovement.NORTH));
		player.setNextAnimation(new Animation(9908));
		player.getLocks().lock();
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(secondGate);
				player.setNextAnimation(new Animation(-1));
				WorldTasksManager.schedule(new WorldTask() {
					
					@Override
					public void run() {
						player.getLocks().unlock();
						player.setNextWorldTile(new WorldTile(secondGate.getX(), secondGate.getY() + 1, 0));
					}
				});
			}
		}, 7);
	}
	
	public static void exitWildernessCourse(final Player player) {
		if (!Agility.hasLevel(player, 52)) {
			return;
		}
		final WorldObject firstGate = new WorldObject(65365, 10, 1, 2998, 3916, 0);
		final WorldObject secondGate = new WorldObject(65367, 10, 1, 2998, 3930, 0);
		player.setNextWorldTile(new WorldTile(secondGate.getX(), secondGate.getY(), 0));
		player.setNextForceMovement(new ForceMovement(new WorldTile(firstGate.getX(), firstGate.getY() + 1, 0), 8, ForceMovement.SOUTH));
		player.setNextAnimation(new Animation(9908));
		player.getLocks().lock(10);
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(new WorldTile(firstGate.getX(), firstGate.getY() + 1, 0));
				player.setNextAnimation(new Animation(-1));
				WorldTasksManager.schedule(new WorldTask() {
					
					@Override
					public void run() {
						player.setNextWorldTile(new WorldTile(firstGate.getX(), firstGate.getY() - 1, 0));
					}
				});
			}
		}, 7);
	}
	
	public static void enterWildernessPipe(final Player player, int objectX, int objectY) {
		final boolean running = player.isRunModeOn();
		player.setRunHidden(false);
		player.getLocks().lock();
		player.addWalkSteps(objectX, 3950, -1, false);
		player.getPackets().sendGameMessage("You pulled yourself through the pipes.", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;
			
			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(295);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					setStage(player, 1);
					player.getSkills().addXp(SkillConstants.AGILITY, 12.5);
					player.setNextWorldTile(new WorldTile(3004, 3950, 0));
					player.getLocks().unlock();
					stop();
				}
			}
		}, 0, 10);
	}
	
	public static void removeStage(Player player) {
		player.getTemporaryAttributes().remove("WildernessCourse");
	}
	
	public static void setStage(Player player, int stage) {
		player.getTemporaryAttributes().put("WildernessCourse", stage);
	}
	
	public static int getStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributes().get("WildernessCourse");
		if (stage == null) {
			return -1;
		}
		return stage;
	}
}