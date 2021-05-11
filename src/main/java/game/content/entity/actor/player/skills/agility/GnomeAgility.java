package game.content.entity.actor.player.skills.agility;

import engine.tick.task.WorldTask;
import engine.tick.task.WorldTasksManager;
import game.entity.actor.mask.Animation;
import game.entity.actor.mask.ForceMovement;
import game.entity.actor.player.Player;
import game.entity.object.WorldObject;
import game.global.WorldTile;
import utility.constants.SkillConstants;

public class GnomeAgility {

	// gnome course

	public static void walkGnomeLog(final Player player) {
		if (player.getX() != 2474 || player.getY() != 3436)
			return;
		final boolean running = player.isRunModeOn();
		player.setRunHidden(false);
		player.getLocks().lock();
		player.addWalkSteps(2474, 3429, -1, false);
		player.getPackets().sendMessage("You walk carefully across the slippery log...", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					setGnomeStage(player, 0);
					player.getSkills().addXp(SkillConstants.AGILITY, 7.5);
					player.getPackets().sendMessage("... and make it safely to the other side.", true);
					player.getLocks().unlock();
					stop();
				}
			}
		}, 0, 6);
	}

	public static void climbGnomeObstacleNet(final Player player) {
		if (player.getY() != 3426)
			return;
		player.getPackets().sendMessage("You climb the netting.", true);
		player.useStairs(828, new WorldTile(player.getX(), 3423, 1), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 0)
					setGnomeStage(player, 1);
				player.getSkills().addXp(SkillConstants.AGILITY, 7.5);
			}
		}, 1);
	}

	public static void climbUpGnomeTreeBranch(final Player player) {
		player.getPackets().sendMessage("You climb the tree...", true);
		player.useStairs(828, new WorldTile(2473, 3420, 2), 1, 2, "... to the plantaform above.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 1)
					setGnomeStage(player, 2);
				player.getSkills().addXp(SkillConstants.AGILITY, 5);
			}
		}, 1);
	}

	public static void walkBackGnomeRope(final Player player) {
		if (player.getX() != 2483 || player.getY() != 3420 || player.getPlane() != 2)
			return;
		final boolean running = player.isRunModeOn();
		player.setRunHidden(false);
		player.getLocks().lock(12);
		player.addWalkSteps(2477, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(SkillConstants.AGILITY, 7);
					player.getPackets().sendMessage("You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void walkGnomeRope(final Player player) {
		if (player.getX() != 2477 || player.getY() != 3420 || player.getPlane() != 2)
			return;
		final boolean running = player.isRunModeOn();
		player.setRunHidden(false);
		player.getLocks().lock(12);
		player.addWalkSteps(2483, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					if (getGnomeStage(player) == 2)
						setGnomeStage(player, 3);
					player.getSkills().addXp(SkillConstants.AGILITY, 7);
					player.getPackets().sendMessage("You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void climbDownGnomeTreeBranch(final Player player) {
		player.useStairs(828, new WorldTile(2487, 3421, 0), 1, 2, "You climbed the tree branch succesfully.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 3)
					setGnomeStage(player, 4);
				player.getSkills().addXp(SkillConstants.AGILITY, 5);
			}
		}, 1);
	}

	public static void climbGnomeObstacleNet2(final Player player) {
		if (player.getY() != 3425)
			return;
		player.getPackets().sendMessage("You climb the netting.", true);
		player.useStairs(828, new WorldTile(player.getX(), player.getY() == 3425 ? 3428 : 3425, 0), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 4)
					setGnomeStage(player, 5);
				player.getSkills().addXp(SkillConstants.AGILITY, 8);
			}
		}, 1);
	}

	public static void enterGnomePipe(final Player player, int objectX, int objectY) {
		final boolean running = player.isRunModeOn();
		player.setRunHidden(false);
		player.getLocks().lock();
		player.addWalkSteps(objectX, objectY == 3431 ? 3437 : 3430, -1, false);
		player.getPackets().sendMessage("You pulled yourself through the pipes.", true);
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
					player.getSkills().addXp(SkillConstants.AGILITY, 7);
					if (getGnomeStage(player) == 5) {
						removeGnomeStage(player);
						player.getSkills().addXp(SkillConstants.AGILITY, 39.5);
					}
					player.getLocks().unlock();
					stop();
				}
			}
		}, 0, 6);
	}

	public static void removeGnomeStage(Player player) {
		player.getTemporaryAttributes().remove("GnomeCourse");
	}

	public static void setGnomeStage(Player player, int stage) {
		player.getTemporaryAttributes().put("GnomeCourse", stage);
	}

	public static int getGnomeStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributes().get("GnomeCourse");
		if (stage == null)
			return -1;
		return stage;
	}

	public static void climbUpTree(final Player player) {
		if (!Agility.hasLevel(player, 85))
			return;
		player.useStairs(828, new WorldTile(2472, 3419, 3), 1, 2, "You climbed the tree succesfully.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				removeGnomeStage(player);
				player.getSkills().addXp(SkillConstants.AGILITY, 25);
			}
		}, 1);
	}


	public static void jumpDown(final Player player, WorldObject object) {
		player.getLocks().lock(7);
		final WorldTile toTile = new WorldTile(2485, 3436, 0);
		WorldTasksManager.schedule(new WorldTask() {

			boolean secondLoop;

			@Override
			public void run() {

				if (!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, ForceMovement.NORTH));
					player.setNextAnimation(new Animation(2923));
					secondLoop = true;
				} else {
					player.setNextAnimation(new Animation(2924));
					player.setNextWorldTile(toTile);
					player.getSkills().addXp(SkillConstants.AGILITY, 630);
					stop();
				}

			}

		}, 1, 2);
	}

	// advanced

	public static void preSwing(final Player player, final WorldObject object) {
		player.getLocks().lock(7);
		player.setNextAnimation(new Animation(11784));
		final WorldTile toTile = new WorldTile(player.getX(), 3421, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 1) {
					player.setNextWorldTile(toTile);
					player.setNextAnimation(new Animation(11785));
					swing(player, object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	private static void swing(final Player player, final WorldObject object) {
		player.getLocks().lock(7);
		final WorldTile toTile = new WorldTile(player.getX(), 3425, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 1, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 0) {
					player.setNextAnimation(new Animation(11789));
					player.setNextWorldTile(toTile);
				} else if (stage == 1) {
					swing1(player, object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	private static void swing1(final Player player, final WorldObject object) {
		player.getLocks().lock(7);
		final WorldTile NextTile = new WorldTile(player.getX(), 3429, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 2, NextTile, 3, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 3) {
					player.setNextWorldTile(NextTile);
					swing2(player, object);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	private static void swing2(final Player player, final WorldObject object) {
		player.getLocks().lock(7);
		final WorldTile LastTile = new WorldTile(player.getX(), 3432, object.getPlane());
		player.setNextForceMovement(new ForceMovement(player, 0, LastTile, 1, ForceMovement.NORTH));
		WorldTasksManager.schedule(new WorldTask() {
			int stage;

			@Override
			public void run() {
				if (stage == 2) {
					player.getSkills().addXp(SkillConstants.AGILITY, 25);
					player.setNextWorldTile(LastTile);
					stop();
				}
				stage++;
			}

		}, 0, 1);
	}

	public static void runGnomeBoard(final Player player, final WorldObject object) {
		player.getLocks().lock();
		player.setNextAnimation(new Animation(2922));
		final WorldTile toTile = new WorldTile(2484, 3418, object.getPlane());
		player.setNextForceMovement(new ForceMovement(new WorldTile(2477, 3418, 3), 1, toTile, 3, ForceMovement.EAST));
		player.getPackets().sendMessage("You skillfully run across the Board", true);
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextWorldTile(toTile);
				player.getSkills().addXp(SkillConstants.AGILITY, 25);
				player.getLocks().unlock();
			}

		}, 1);
	}
}


