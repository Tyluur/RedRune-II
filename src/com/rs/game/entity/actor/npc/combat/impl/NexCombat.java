package com.rs.game.entity.actor.npc.combat.impl;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.content.cutscene.impl.NexCutScene;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.*;
import com.rs.game.entity.actor.mask.Hit.HitLook;
import com.rs.game.entity.actor.npc.NPC;
import com.rs.game.entity.actor.npc.combat.CombatScript;
import com.rs.game.entity.actor.npc.combat.NPCCombatDefinitions;
import com.rs.game.entity.actor.npc.impl.godwars.zaros.Nex;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.object.WorldObject;
import com.rs.utility.Misc;

import java.util.ArrayList;
import java.util.HashMap;

public class NexCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Nex" };
	}

	public WorldTile[] NO_ESCAPE_TELEPORTS = { new WorldTile(2924, 5213, 0), // north
			new WorldTile(2934, 5202, 0), // east,
			new WorldTile(2924, 5192, 0), // south
			new WorldTile(2913, 5202, 0), }; // west

	@Override
	public int attack(final NPC npc, final Actor target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Nex nex = (Nex) npc;
		int size = npc.getSize();
		// attacks close only
		if (nex.isFollowTarget()) {
			int distanceX = target.getX() - npc.getX();
			int distanceY = target.getY() - npc.getY();
			if (distanceX > size || distanceX < -1 || distanceY > size
					|| distanceY < -1)
				return 0;
			nex.setFollowTarget(Misc.getRandom(1) == 0);
			// first stage close attacks
			if (nex.getAttacksStage() == 0) {
				// virus 1/3 probability every 1min
				if (nex.getLastVirus() < Misc.currentTimeMillis()
						&& Misc.getRandom(2) == 0) {
					nex.setLastVirus(Misc.currentTimeMillis() + 60000);
					npc.setNextForceTalk(new ForceTalk(
							"Let the virus flow through you."));
					nex.playSound(3296, 2);
					npc.setNextAnimation(new Animation(6987));
					nex.sendVirusAttack(new ArrayList<Actor>(),
							npc.getPossibleTargets(), target);
					return defs.getAttackDelay();
				}
			}
			// no escape, 1/10 probability doing it
			if (Misc.getRandom(nex.getStage() == 4 ? 5 : 10) == 0) {
				npc.setNextForceTalk(new ForceTalk("There is..."));
				nex.playSound(3294, 2);
				npc.setCantInteract(true);
				npc.getCombat().removeTarget();
				final int idx = Misc.random(NO_ESCAPE_TELEPORTS.length);
				final WorldTile dir = NO_ESCAPE_TELEPORTS[idx];
				final WorldTile center = new WorldTile(2924, 5202, 0);
				WorldTasksManager.schedule(new WorldTask() {
					private int count;

					@Override
					public void run() {
						if (count == 0) {
							npc.setNextAnimation(new Animation(6321));
							npc.setNextGraphics(new Graphics(1216));
						} else if (count == 1) {
							nex.setNextWorldTile(dir);
							nex.setNextForceTalk(new ForceTalk("NO ESCAPE!"));
							nex.playSound(3292, 2);
							nex.setNextForceMovement(new ForceMovement(dir, 1,
									center, 3, idx == 3 ? 1 : idx == 2 ? 0
											: idx == 1 ? 3 : 2));
							for (Actor actor : nex.calculatePossibleTargets(
									center, dir, idx == 0 || idx == 2)) {
								if (actor instanceof Player) {
									Player player = (Player) actor;
									player.getCutsceneManager().play(
											new NexCutScene(dir, idx));
									player.applyHit(new Hit(
											npc,
											Misc.getRandom(nex.getStage() == 4 ? 800
											                                   : 650),
											HitLook.REGULAR_DAMAGE));
									player.setNextAnimation(new Animation(10070));
									player.setNextForceMovement(new ForceMovement(
											player, 1, idx == 3 ? 3
													: idx == 2 ? 2
															: idx == 1 ? 1 : 0));
								}
							}
						} else if (count == 3) {
							nex.setNextWorldTile(center);
						} else if (count == 4) {
							nex.setTarget(target);
							npc.setCantInteract(false);
							stop();
						}
						count++;
					}
				}, 0, 1);
				return defs.getAttackDelay();
			}
			// normal melee attack
			int damage = getRandomMaxHit(npc, defs.getMaxHit(),
					NPCCombatDefinitions.MELEE, target);
			delayHit(npc, 0, target, getMeleeHit(npc, damage));
			npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			return defs.getAttackDelay();
			// far attacks
		} else {
			nex.setFollowTarget(Misc.getRandom(1) == 0);
			// drag a player to center
			if (Misc.getRandom(15) == 0) {
				int distance = 0;
				Actor settedTarget = null;
				for (Actor t : npc.getPossibleTargets()) {
					if (t instanceof Player) {
						int thisDistance = Misc.getDistance(t.getX(),
								t.getY(), npc.getX(), npc.getY());
						if (settedTarget == null || thisDistance > distance) {
							distance = thisDistance;
							settedTarget = t;
						}
					}
				}
				if (settedTarget != null && distance > 10) {
					final Player player = (Player) settedTarget;
					player.addLockDelay(3);
					player.setNextAnimation(new Animation(14386));
					player.setNextForceMovement(new ForceMovement(nex, 2, Misc
							.getMoveDirection(
									npc.getCoordFaceX(player.getSize())
											- player.getX(),
									npc.getCoordFaceY(player.getSize())
											- player.getY())));
					npc.setNextAnimation(new Animation(6986));
					npc.setTarget(player);
					player.setNextAnimation(new Animation(-1));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.setNextWorldTile(nex);
							player.getPackets()
									.sendGameMessage(
											"You've been injured and you can't use protective curses!");
							player.setPrayerDelay(Misc.getRandom(20000) + 5);// random
																				// 20
																				// seconds
							player.getPackets().sendGameMessage(
									"You're stunned.");
						}
					});
					return defs.getAttackDelay();
				}
			}
			// first stage close attacks
			if (nex.getAttacksStage() == 0) {
				npc.setNextAnimation(new Animation(6987));
				for (Actor t : npc.getPossibleTargets()) {
					World.sendProjectile(npc, t, 471, 41, 16, 41, 35, 16, 0);
					int damage = getRandomMaxHit(npc, defs.getMaxHit(),
							NPCCombatDefinitions.MAGE, t);
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					if (damage > 0 && Misc.getRandom(5) == 0) // 1/6
																// probability
																// poisoning
						t.getPoisonManager().makePoisoned(80);
				}
				return defs.getAttackDelay();
			} else if (nex.getAttacksStage() == 1) {
				if (!nex.isEmbracedShadow()) {
					nex.setEmbracedShadow(true);
					npc.setNextForceTalk(new ForceTalk("Embrace darkness!"));
					nex.playSound(3322, 2);
					npc.setNextAnimation(new Animation(6355));
					npc.setNextGraphics(new Graphics(1217));
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							if (nex.getAttacksStage() != 1 || nex.hasFinished()) {
								for (Actor actor : nex.getPossibleTargets()) {
									if (actor instanceof Player) {
										Player player = (Player) actor;
										player.getPackets().sendGlobalConfig(
												1435, 255);
									}
								}
								stop();
								return;
							}
							if (Misc.getRandom(2) == 0) {
								for (Actor actor : nex.getPossibleTargets()) {
									if (actor instanceof Player) {
										Player player = (Player) actor;
										int distance = Misc.getDistance(
												player.getX(), player.getY(),
												npc.getX(), npc.getY());
										if (distance > 30)
											distance = 30;
										player.getPackets().sendGlobalConfig(
												1435, (distance * 255 / 30));
									}
								}
							}
						}
					}, 0, 0);
					return defs.getAttackDelay();
				}
				if (!nex.isTrapsSettedUp() && Misc.getRandom(2) == 0) {
					nex.setTrapsSettedUp(true);
					npc.setNextForceTalk(new ForceTalk("Fear the Shadow!"));
					nex.playSound(3314, 2);
					npc.setNextAnimation(new Animation(6984));
					npc.setNextGraphics(new Graphics(1215));
					ArrayList<Actor> possibleTargets = nex
							.getPossibleTargets();
					final HashMap<String, int[]> tiles = new HashMap<String, int[]>();
					for (Actor t : possibleTargets) {
						String key = t.getX() + "_" + t.getY();
						if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
							tiles.put(key, new int[] { t.getX(), t.getY() });
							World.spawnTemporaryObject(new WorldObject(57261,
									10, 0, t.getX(), t.getY(), 0), 2400);
						}
					}
					WorldTasksManager.schedule(new WorldTask() {
						private boolean firstCall;

						@Override
						public void run() {
							if (!firstCall) {
								ArrayList<Actor> possibleTargets = nex
										.getPossibleTargets();
								for (int[] tile : tiles.values()) {
									World.sendGraphics(null, new Graphics(383),
											new WorldTile(tile[0], tile[1], 0));
									for (Actor t : possibleTargets)
										if (t.getX() == tile[0]
												&& t.getY() == tile[1])
											t.applyHit(new Hit(npc, Misc
													.getRandom(400) + 400,
													HitLook.REGULAR_DAMAGE));
								}
								firstCall = true;
							} else {
								nex.setTrapsSettedUp(false);
								stop();
							}
						}

					}, 3, 3);
					return defs.getAttackDelay();
				}
				npc.setNextAnimation(new Animation(6987));
				for (final Actor t : npc.getPossibleTargets()) {
					int distance = Misc.getDistance(t.getX(), t.getY(),
							npc.getX(), npc.getY());
					if (distance <= 10) {
						int damage = 800 - (distance * 800 / 11);
						World.sendProjectile(npc, t, 380, 41, 16, 41, 35, 16, 0);
						delayHit(
								npc,
								1,
								t,
								getRangeHit(
										npc,
										getRandomMaxHit(npc, damage,
												NPCCombatDefinitions.RANGE, t)));
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								t.setNextGraphics(new Graphics(471));
							}
						}, 1);
					}
				}
				return defs.getAttackDelay();
			} else if (nex.getAttacksStage() == 2) {
				if (Misc.getRandom(4) == 0 && target instanceof Player) {
					npc.setNextForceTalk(new ForceTalk(
							"I demand a blood sacrifice!"));
					nex.playSound(3293, 2);
					final Player player = (Player) target;
					player.getAppearence().setGlowRed(true);
					player.getPackets().sendGameMessage(
							"Nex has marked you as a sacrifice, RUN!");
					final int x = player.getX();
					final int y = player.getY();
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							player.getAppearence().setGlowRed(false);
							if (x == player.getX() && y == player.getY()) {
								player.getPackets()
										.sendGameMessage(
												"You didn't make it far enough in time - Nex fires a punishing attack!");
								npc.setNextAnimation(new Animation(6987));
								for (final Actor t : npc.getPossibleTargets()) {
									World.sendProjectile(npc, t, 374, 41, 16,
											41, 35, 16, 0);
									final int damage = getRandomMaxHit(npc,
											290, NPCCombatDefinitions.MAGE, t);
									delayHit(npc, 1, t,
											getMagicHit(npc, damage));
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											t.setNextGraphics(new Graphics(376));
											nex.heal(damage / 4);
											if (t instanceof Player) {
												Player p = (Player) t;
												p.getPrayer()
														.drainPrayerOnHalf();
											}
										}
									}, 1);
								}
							}
						}
					}, defs.getAttackDelay());
					return defs.getAttackDelay() * 2;
				}
				if (nex.getLastSiphon() < Misc.currentTimeMillis()
						&& npc.getHitpoints() <= 18000
						&& Misc.getRandom(2) == 0) {
					nex.setLastSiphon(Misc.currentTimeMillis() + 30000);
					nex.killBloodReavers();
					npc.setNextForceTalk(new ForceTalk(
							"A siphon will solve this!"));
					nex.playSound(3317, 2);
					npc.setNextAnimation(new Animation(6948));
					npc.setNextGraphics(new Graphics(1201));
					nex.setDoingSiphon(true);
					int bloodReaverSize = NPCDefinitions
							.getNPCDefinitions(13458).size;
					int respawnedBloodReaverCount = 0;
					int maxMinions = Misc.getRandom(3);
					if (maxMinions != 0) {
						int[][] dirs = Misc
								.getCoordOffsetsNear(bloodReaverSize);
						for (int dir = 0; dir < dirs[0].length; dir++) {
							final WorldTile tile = new WorldTile(new WorldTile(
									target.getX() + dirs[0][dir], target.getY()
											+ dirs[1][dir], target.getPlane()));
							if (World.canMoveNPC(tile.getPlane(), tile.getX(),
									tile.getY(), bloodReaverSize)) { // if found
																		// done
								nex.getBloodReavers()[respawnedBloodReaverCount++] = new NPC(
										13458, tile, -1, true, true);
								if (respawnedBloodReaverCount == maxMinions)
									break;
							}
						}
					}
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							nex.setDoingSiphon(false);
						}
					}, 8);
					return defs.getAttackDelay();
				}
				npc.setNextAnimation(new Animation(6986));
				World.sendProjectile(npc, target, 374, 41, 16, 41, 35, 16, 0);
				delayHit(
						npc,
						1,
						target,
						getMagicHit(
								npc,
								getRandomMaxHit(npc, defs.getMaxHit(),
										NPCCombatDefinitions.MAGE, target)));
				return defs.getAttackDelay();
			} else if (nex.getAttacksStage() == 3) {
				npc.setNextAnimation(new Animation(6986));
				for (final Actor t : npc.getPossibleTargets()) {
					World.sendProjectile(npc, t, 362, 41, 16, 41, 35, 16, 0);
					int damage = getRandomMaxHit(npc, defs.getMaxHit(),
							NPCCombatDefinitions.MAGE, t);
					delayHit(npc, 1, t, getMagicHit(npc, damage));
					if (damage > 0 && Misc.getRandom(5) == 0) {// 1/6
																// probability
																// freezing
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								t.addFreezeDelay(18000);
								t.setNextGraphics(new Graphics(369));
							}
						}, 2);

					}
				}
				return defs.getAttackDelay();
			} else if (nex.getAttacksStage() == 4) {
				npc.setNextAnimation(new Animation(6987));
				for (Actor t : npc.getPossibleTargets()) {
					World.sendProjectile(npc, t, 471, 41, 16, 41, 35, 16, 0);
					int damage = getRandomMaxHit(npc, defs.getMaxHit(),
							NPCCombatDefinitions.MAGE, t);
					delayHit(npc, 1, t, getMagicHit(npc, damage));
				}
				return defs.getAttackDelay();
			}
		}
		return defs.getAttackDelay();
	}
}
