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

public class BarbarianOutpostAgility {
	
	public static void enterObstaclePipe(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		player.getLocks().lock();
		player.setNextAnimation(new Animation(10580));
		final WorldTile toTile = new WorldTile(object.getX(), player.getY() >= 3561 ? 3558 : 3561, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, player.getY() >= 3561 ? ForceMovement.SOUTH : ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(SkillConstants.AGILITY, 1 / 20);
				player.getLocks().unlock();
			}
			
		}, 1);
	}
	
	public static void runUpWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.getLocks().lock(2);
		final WorldTile toTile = new WorldTile(2538, 3545, 2);
		WorldTasksManager.schedule(new WorldTask() {
			
			boolean secondLoop;
			
			@Override
			public void run() {
				
				if (!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 7, toTile, 8, ForceMovement.NORTH));
					player.setNextAnimation(new Animation(10492));
					secondLoop = true;
				} else {
					player.setNextAnimation(new Animation(10493));
					player.setNextWorldTile(toTile);
					player.getSkills().addXp(SkillConstants.AGILITY, 15);
					stop();
				}
				
			}
			
		}, 1, 6);
	}
	
	public static void climbUpWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.useStairs(10023, new WorldTile(2536, 3546, 3), 2, 3);
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextAnimation(new Animation(11794));
				player.getSkills().addXp(SkillConstants.AGILITY, 15);
			}
			
		}, 1);
	}
	
	public static void fireSpringDevice(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.getLocks().lock(9);
		player.addWalkSteps(2533, 3547, -1, false);
		final WorldTile toTile = new WorldTile(2532, 3553, 3);
		WorldTasksManager.schedule(new WorldTask() {
			
			boolean secondLoop;
			
			@Override
			public void run() {
				if (!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH));
					player.setNextAnimation(new Animation(4189));
					World.sendObjectAnimation(player, object, new Animation(11819));
					
					secondLoop = true;
				} else {
					player.setNextWorldTile(toTile);
					player.getSkills().addXp(SkillConstants.AGILITY, 15);
					stop();
				}
			}
			
		}, 1, 1);
	}
	
	public static void crossBalanceBeam(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.getLocks().lock(7);
		final WorldTile toTile = new WorldTile(2536, 3553, 3);
		player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.EAST));
		player.getAppearance().setRenderEmote(330);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(SkillConstants.AGILITY, 15);
				player.setNextAnimation(new Animation(-1));
				stop();
			}
			
		}, 2);
	}
	
	public static void jumpOverGap(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.getLocks().lock(2);
		player.setNextAnimation(new Animation(2586));
		player.getAppearance().setRenderEmote(-1);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextWorldTile(new WorldTile(2538, 3553, 2));
				player.setNextAnimation(new Animation(2588));
				player.getSkills().addXp(SkillConstants.AGILITY, 15);
				stop();
			}
			
		}, 0);
	}
	
	public static void slideDownRoof(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 90)) {
			return;
		}
		player.getLocks().lock(10);
		player.setNextAnimation(new Animation(11792));
		final WorldTile toTile = new WorldTile(2544, player.getY(), 0);
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, ForceMovement.EAST));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;
			
			@Override
			public void run() {
				if (stage == 0) {
					player.setNextWorldTile(new WorldTile(2541, player.getY(), 1));
					player.setNextAnimation(new Animation(11790));
					stage = 1;
				} else if (stage == 1) {
					stage = 2;
				} else if (stage == 2) {
					player.setNextAnimation(new Animation(11791));
					stage = 3;
				} else if (stage == 3) {
					player.setNextWorldTile(toTile);
					player.setNextAnimation(new Animation(2588));
					player.getSkills().addXp(SkillConstants.AGILITY, 15);
					if (getStage(player) == 1) {
						removeStage(player);
						player.getSkills().addXp(SkillConstants.AGILITY, 615);
					}
					stop();
				}
			}
			
		}, 0, 0);
	}
	
	public static int getStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributes().get("BarbarianOutpostCourse");
		if (stage == null) {
			return -1;
		}
		return stage;
	}
	
	public static void removeStage(Player player) {
		player.getTemporaryAttributes().remove("BarbarianOutpostCourse");
	}
	
	public static void swingOnRopeSwing(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		if (player.getY() != 3556) {
			player.getPackets().sendGameMessage("You'll need to get closer to make this jump.");
			return;
		}
		player.getLocks().lock();
		player.setNextAnimation(new Animation(751));
		World.sendObjectAnimation(player, object, new Animation(497));
		final WorldTile toTile = new WorldTile(object.getX(), 3549, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 3, ForceMovement.SOUTH));
		player.getSkills().addXp(SkillConstants.AGILITY, 22);
		player.getPackets().sendGameMessage("You skillfully swing across.", true);
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.getLocks().unlock();
				setStage(player, 0);
			}
			
		}, 1);
	}
	
	public static void setStage(Player player, int stage) {
		player.getTemporaryAttributes().put("BarbarianOutpostCourse", stage);
	}
	
	public static void walkAcrossLogBalance(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		if (player.getY() != object.getY()) {
			player.addWalkSteps(2551, 3546, -1, false);
			player.getLocks().lock(3);
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
		player.getLocks().lock(3);
		player.setNextAnimation(new Animation(9908));
		final WorldTile toTile = new WorldTile(2541, object.getY(), object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 16, ForceMovement.WEST));
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(SkillConstants.AGILITY, 13);
				player.getPackets().sendGameMessage("... and make it safely to the other side.", true);
				player.getLocks().unlock();
				if (getStage(player) == 0) {
					setStage(player, 1);
				}
			}
			
		}, 15);
	}
	
	public static void walkAcrossBalancingLedge(final Player player, final WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		player.getPackets().sendGameMessage("You put your food on the ledge and try to edge across...", true);
		player.getLocks().lock(9);
		player.setNextAnimation(new Animation(753));
		player.getAppearance().setRenderEmote(157);
		final WorldTile toTile = new WorldTile(2532, object.getY(), object.getPlane());
		player.setRunModeOn(true);
		player.addWalkSteps(toTile.getX(), toTile.getY(), -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setNextAnimation(new Animation(759));
				player.getAppearance().setRenderEmote(-1);
				player.getSkills().addXp(SkillConstants.AGILITY, 22);
				player.getPackets().sendGameMessage("You skillfully edge across the gap.", true);
				player.getLocks().unlock();
				if (getStage(player) == 2) {
					setStage(player, 3);
				}
			}
		}, 3);
	}
	
	public static void climbObstacleNet(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35) || player.getY() < 3545 || player.getY() > 3547) {
			return;
		}
		player.getPackets().sendGameMessage("You climb the netting...", true);
		player.getSkills().addXp(SkillConstants.AGILITY, 8.2);
		player.useStairs(828, new WorldTile(object.getX() - 1, player.getY(), 1), 1, 2);
		if (getStage(player) == 1) {
			setStage(player, 2);
		}
	}
	
	public static void climbOverCrumblingWall(final Player player, WorldObject object) {
		if (!Agility.hasLevel(player, 35)) {
			return;
		}
		if (player.getX() >= object.getX()) {
			player.getPackets().sendGameMessage("You cannot climb that from this side.");
			return;
		}
		player.getPackets().sendGameMessage("You climb the low wall...", true);
		player.getLocks().lock(5);
		player.setNextAnimation(new Animation(4853));
		final WorldTile toTile = new WorldTile(object.getX() + 1, object.getY(), object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.EAST));
		WorldTasksManager.schedule(new WorldTask() {
			
			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(SkillConstants.AGILITY, 13.7);
				player.getLocks().unlock();
				int stage = getStage(player);
				if (stage == 3) {
					setStage(player, 4);
				} else if (stage == 4) {
					removeStage(player);
					player.getSkills().addXp(SkillConstants.AGILITY, 46.2);
				}
			}
			
		}, 1);
	}
}
