package org.redrune.game.content.actor.item;

import org.redrune.game.content.controller.impl.activity.Wilderness;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.SkillConstants;

public final class Pots {
	
	private Pots() {
	
	}
	
	public static boolean pot(Player player, Item item, int slot) {
		Pot pot = getPot(item.getId());
		if (pot == null) {
			return false;
		}
		if (player.getPotDelay() > Misc.currentTimeMillis()) {
			return true;
		}
		if (!player.getControllerManager().canPot(pot)) {
			return true;
		}
		if (!pot.effect.canDrink(player)) {
			return true;
		}
		player.addPotDelay(1200);
		String name = item.getDefinitions().getName();
		int index = name.indexOf("(");
		int dosesLeft = 0;
		if (index != -1) {
			dosesLeft = Integer.parseInt(name.substring(index).replace("(", "").replace(")", "")) - 1;
		}
		int toPot = pot.id.length - dosesLeft;
		player.getInventory().getItems().set(slot, new Item(dosesLeft > 0 && toPot < pot.id.length ? pot.id[toPot] : 229, 1));
		player.getInventory().refresh(slot);
		for (int skillId : pot.effect.affectedSkills) {
			player.getSkills().set(skillId, pot.effect.getAffectedSkill(player, skillId, player.getSkills().getLevel(skillId), player.getSkills().getLevelForXp(skillId)));
		}
		pot.effect.extra(player);
		player.setNextAnimation(new Animation(829));
		player.getPackets().sendSound(4580, 0, 1);
		player.getPackets().sendGameMessage(pot.effect.drinkMessage != null ? pot.effect.drinkMessage : "You drink some of your " + name.toLowerCase().replace(" (1)", "").replace(" (2)", "").replace(" (3)", "").replace(" (4)", "") + ".", true);
		
		player.getPackets().sendGameMessage(dosesLeft == 0 ? "You have finished your potion." : "You have " + dosesLeft + " dose of potion left.", true);
		return true;
	}
	
	private static Pot getPot(int id) {
		for (Pot pot : Pot.values()) {
			for (int potionId : pot.id) {
				if (id == potionId) {
					return pot;
				}
			}
		}
		return null;
	}
	
	public static boolean mixPotion(Player player, int usedSlot, int withSlot) {
		/*
		 * Item itemUsed = player.getInventory().getItem(usedSlot); Item
		 * usedWith = player.getInventory().getItem(withSlot); Pot first =
		 * getPot(itemUsed.getId()); Pot second = getPot(usedWith.getId());
		 * String name = usedWith.getDefinitions().getName(); if (first == null
		 * || second == null) return false; if (first.effect != second.effect)
		 * return false; else if (name.contains("4")) {
		 * player.getPackets().sendGameMessage("That potion is full."); return
		 * false; } else if (itemUsed.getId() == 229)// empty vial return false;
		 * int index = 0; int currentId = itemUsed.getId();
		 * player.getInventory().getItems().set(withSlot, new Item(currentId));
		 * player.getInventory().getItems() .set(usedSlot, new
		 * Item(second.id[index])); player.getInventory().init();
		 */
		return true;
	}
	
	public static void resetOverLoadEffect(Player player) {
		if (!player.isDead()) {
			int actualLevel = player.getSkills().getLevel(SkillConstants.ATTACK);
			int realLevel = player.getSkills().getLevelForXp(SkillConstants.ATTACK);
			if (actualLevel > realLevel) {
				player.getSkills().set(SkillConstants.ATTACK, realLevel);
			}
			actualLevel = player.getSkills().getLevel(SkillConstants.STRENGTH);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.STRENGTH);
			if (actualLevel > realLevel) {
				player.getSkills().set(SkillConstants.STRENGTH, realLevel);
			}
			actualLevel = player.getSkills().getLevel(SkillConstants.DEFENCE);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.DEFENCE);
			if (actualLevel > realLevel) {
				player.getSkills().set(SkillConstants.DEFENCE, realLevel);
			}
			actualLevel = player.getSkills().getLevel(SkillConstants.MAGIC);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.MAGIC);
			if (actualLevel > realLevel) {
				player.getSkills().set(SkillConstants.MAGIC, realLevel);
			}
			actualLevel = player.getSkills().getLevel(SkillConstants.RANGE);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.RANGE);
			if (actualLevel > realLevel) {
				player.getSkills().set(SkillConstants.RANGE, realLevel);
			}
			player.heal(500);
		}
		player.setOverloadDelay(0);
		player.getPackets().sendGameMessage("The effects of overload have worn off.");
	}
	
	public static void applyOverLoadEffect(Player player) {
		if (player.getControllerManager().getController() instanceof Wilderness) {
			int actualLevel = player.getSkills().getLevel(SkillConstants.ATTACK);
			int realLevel = player.getSkills().getLevelForXp(SkillConstants.ATTACK);
			int level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.ATTACK, (int) (level + 5 + (realLevel * 0.15)));
			
			actualLevel = player.getSkills().getLevel(SkillConstants.STRENGTH);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.STRENGTH);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.STRENGTH, (int) (level + 5 + (realLevel * 0.15)));
			
			actualLevel = player.getSkills().getLevel(SkillConstants.DEFENCE);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.DEFENCE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.DEFENCE, (int) (level + 5 + (realLevel * 0.15)));
			
			actualLevel = player.getSkills().getLevel(SkillConstants.MAGIC);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.MAGIC);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.MAGIC, level + 5);
			
			actualLevel = player.getSkills().getLevel(SkillConstants.RANGE);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.RANGE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.RANGE, (int) (level + 5 + (realLevel * 0.1)));
		} else {
			int actualLevel = player.getSkills().getLevel(SkillConstants.ATTACK);
			int realLevel = player.getSkills().getLevelForXp(SkillConstants.ATTACK);
			int level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.ATTACK, (int) (level + 5 + (realLevel * 0.22)));
			
			actualLevel = player.getSkills().getLevel(SkillConstants.STRENGTH);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.STRENGTH);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.STRENGTH, (int) (level + 5 + (realLevel * 0.22)));
			
			actualLevel = player.getSkills().getLevel(SkillConstants.DEFENCE);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.DEFENCE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.DEFENCE, (int) (level + 5 + (realLevel * 0.22)));
			
			actualLevel = player.getSkills().getLevel(SkillConstants.MAGIC);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.MAGIC);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.MAGIC, level + 7);
			
			actualLevel = player.getSkills().getLevel(SkillConstants.RANGE);
			realLevel = player.getSkills().getLevelForXp(SkillConstants.RANGE);
			level = actualLevel > realLevel ? realLevel : actualLevel;
			player.getSkills().set(SkillConstants.RANGE, (int) (level + 4 + (Math.floor(realLevel / 5.2))));
		}
	}
	
	public enum Pot {
		
		COMBAT_FLASK(new int[] { 23447, 23449, 23451, 23453, 23455, 23457 }, Effects.COMBAT),
		
		OVERLOAD_FLASK(new int[] { 23531, 23532, 23533, 23534, 23535, 23536 }, Effects.OVERLOAD),
		
		SUPER_RESTORE_FLASK(new int[] { 23399, 23401, 23403, 23405, 23407, 23409 }, Effects.SUPER_RESTORE),
		
		SUPER_STR_FLASK(new int[] { 23279, 23281, 23283, 23285, 23287, 23289 }, Effects.SUPER_STR_POTION),
		
		SUPER_ATT_FLASK(new int[] { 23255, 23257, 23259, 23261, 23263, 23265 }, Effects.SUPER_ATT_POTION),
		
		SARADOMIN_BREW_FLASK(new int[] { 23351, 23353, 23355, 23357, 23359, 23361 }, Effects.SARADOMIN_BREW),
		
		ATTACK_POTION(new int[] { 2428, 121, 123, 125 }, Effects.ATTACK_POTION),
		
		STRENGTH_POTION(new int[] { 113, 115, 117, 119 }, Effects.STRENGTH_POTION),
		
		DEFENCE_POTION(new int[] { 2432, 133, 135, 137 }, Effects.DEFENCE_POTION),
		
		RANGE_POTION(new int[] { 2444, 169, 171, 173 }, Effects.RANGE_POTION),
		
		MAGIC_POTION(new int[] { 3040, 3042, 3044, 3046 }, Effects.MAGIC_POTION),
		
		ANTI_POISION(new int[] { 2446, 175, 177, 179 }, Effects.ANTIPOISON),
		
		PRAYER_POTION(new int[] { 2434, 139, 141, 143 }, Effects.PRAYER_POTION),
		
		SUPER_ATT_POTION(new int[] { 2436, 145, 147, 149 }, Effects.SUPER_ATT_POTION),
		
		SUPER_STR_POTION(new int[] { 2440, 157, 159, 161 }, Effects.SUPER_STR_POTION),
		
		SUPER_DEF_POTION(new int[] { 2442, 163, 165, 167 }, Effects.SUPER_DEF_POTION),
		
		EXTREME_ATT_POTION(new int[] { 15308, 15309, 15310, 15311 }, Effects.EXTREME_ATT_POTION),
		
		EXTREME_STR_POTION(new int[] { 15312, 15313, 15314, 15315 }, Effects.EXTREME_STR_POTION),
		
		EXTREME_DEF_POTION(new int[] { 15316, 15317, 15318, 15319 }, Effects.EXTREME_DEF_POTION),
		
		EXTREME_MAGE_POTION(new int[] { 15320, 15321, 15322, 15323 }, Effects.EXTREME_MAG_POTION),
		
		EXTREME_RANGE_POTION(new int[] { 15324, 15325, 15326, 15327 }, Effects.EXTREME_RAN_POTION),
		
		SUPER_RESTORE_POTION(new int[] { 3024, 3026, 3028, 3030 }, Effects.SUPER_RESTORE),
		
		SARADOMIN_BREW(new int[] { 6685, 6687, 6689, 6691 }, Effects.SARADOMIN_BREW),
		
		RECOVER_SPECIAL(new int[] { 15300, 15301, 15302, 15303 }, Effects.RECOVER_SPECIAL),
		
		SUPER_PRAYER(new int[] { 15328, 15329, 15330, 15331 }, Effects.SUPER_PRAYER),
		
		OVERLOAD(new int[] { 15332, 15333, 15334, 15335 }, Effects.OVERLOAD),
		
		ANTI_FIRE(new int[] { 2452, 2454, 2456, 2458 }, Effects.ANTI_FIRE),
		
		SUMMONING_POTION(new int[] { 12140, 12142, 12144, 12146 }, Effects.SUMMONING_POT);
		
		private int[] id;
		
		private Effects effect;
		
		Pot(int[] id, Effects effect) {
			this.id = id;
			this.effect = effect;
		}
	}
	
	private enum Effects {
		ATTACK_POTION(SkillConstants.ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		SUMMONING_POT() {
			@Override
			public void extra(Player player) {
				/*
				 * player.getPrayer().restorePrayer( (int) ((int)
				 * (Math.floor(player.getSkills().getLevelForXp(
				 * Skills.SUMMONING) * 2.5) + 70)
				 * player.getAuraManager().getPrayerPotsRestoreMultiplier()));
				 */
				// TODO Restores 15% of certain familiars special move [Sonic]
			}
		},
		ANTIPOISON() {
			@Override
			public void extra(Player player) {
				player.addPoisonImmune(180000);
				player.getPackets().sendGameMessage("You are now immune to poison.");
			}
		},
		ANTI_FIRE() {
			@Override
			public void extra(Player player) {
				player.addFireImmune(360000);
				player.getPackets().sendGameMessage("You are now immune to dragonfire.");
			}
		},
		STRENGTH_POTION(SkillConstants.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		DEFENCE_POTION(SkillConstants.DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		RANGE_POTION(SkillConstants.RANGE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.1));
			}
		},
		MAGIC_POTION(SkillConstants.MAGIC) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 5;
			}
		},
		PRAYER_POTION() {
			@Override
			public void extra(Player player) {
				player.getPrayer().restorePrayer((int) ((int) (Math.floor(player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 2.5) + 70) * player.getAuraManager().getPrayerPotsRestoreMultiplier()));
			}
		},
		SUPER_STR_POTION(SkillConstants.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		COMBAT(SkillConstants.DEFENCE, SkillConstants.ATTACK, SkillConstants.STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 4 + (realLevel * 0.15));
			}
		},
		SUPER_DEF_POTION(SkillConstants.DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_ATT_POTION(SkillConstants.ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		EXTREME_STR_POTION(SkillConstants.STRENGTH) {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_DEF_POTION(SkillConstants.DEFENCE) {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_ATT_POTION(SkillConstants.ATTACK) {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_RAN_POTION(SkillConstants.RANGE) {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 4 + (Math.floor(realLevel / 5.2)));
			}
		},
		EXTREME_MAG_POTION(SkillConstants.MAGIC) {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 7;
			}
		},
		RECOVER_SPECIAL() {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				Long time = (Long) player.getTemporaryAttributtes().get("Recover_Special_Pot");
				if (time != null && Misc.currentTimeMillis() - time < 30000) {
					player.getPackets().sendGameMessage("You may only use this pot every 30 seconds.");
					return false;
				}
				return true;
			}
			
			@Override
			public void extra(Player player) {
				player.getTemporaryAttributtes().put("Recover_Special_Pot", Misc.currentTimeMillis());
				player.getCombatDefinitions().restoreSpecialAttack(25);
			}
		},
		SARADOMIN_BREW("You drink some of the foul liquid.", SkillConstants.ATTACK, SkillConstants.DEFENCE, SkillConstants.STRENGTH, SkillConstants.MAGIC, SkillConstants.RANGE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				if (skillId == SkillConstants.DEFENCE) {
					int boost = (int) (realLevel * 0.25);
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return level + boost;
				} else {
					return (int) (actualLevel * 0.90);
				}
			}
			
			@Override
			public void extra(Player player) {
				int hitpointsModification = (int) (player.getMaxHitpoints() * 0.15);
				player.heal(hitpointsModification + 20, hitpointsModification);
			}
		},
		
		OVERLOAD() {
			@Override
			public boolean canDrink(Player player) {
				if (player.getControllerManager().getController() instanceof Wilderness) {
					player.getPackets().sendGameMessage("You cannot drink this potion here.");
					return false;
				}
				if (player.getOverloadDelay() > 0) {
					player.getPackets().sendGameMessage("You may only use this potion every five minutes.");
					return false;
				}
				if (player.getHitpoints() <= 500) {
					player.getPackets().sendGameMessage("You need more than 500 life points to survive the power of overload.");
					return false;
				}
				return true;
			}
			
			@Override
			public void extra(final Player player) {
				player.setOverloadDelay(501);
				WorldTasksManager.schedule(new WorldTask() {
					int count = 4;
					
					@Override
					public void run() {
						if (count == 0) {
							stop();
						}
						player.setNextAnimation(new Animation(3170));
						player.applyHit(new Hit(player, 100, HitSplat.REGULAR_DAMAGE, 0));
						count--;
					}
				}, 0, 2);
			}
		},
		SUPER_PRAYER() {
			@Override
			public void extra(Player player) {
				player.getPrayer().setPrayerpoints((int) ((int) (70 + (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 3.43)) * player.getAuraManager().getPrayerPotsRestoreMultiplier()));
			}
		},
		SUPER_RESTORE(SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.DEFENCE, SkillConstants.MAGIC, SkillConstants.RANGE, SkillConstants.AGILITY, SkillConstants.COOKING, SkillConstants.CRAFTING, SkillConstants.FARMING, SkillConstants.FIREMAKING, SkillConstants.FISHING, SkillConstants.FLETCHING, SkillConstants.HERBLORE, SkillConstants.MINING, SkillConstants.RUNECRAFTING, SkillConstants.SLAYER, SkillConstants.SMITHING, SkillConstants.THIEVING, SkillConstants.WOODCUTTING, SkillConstants.SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel) {
					return actualLevel;
				}
				if (actualLevel + boost > realLevel) {
					return realLevel;
				}
				return actualLevel + boost;
			}
			
			@Override
			public void extra(Player player) {
				player.getPrayer().restorePrayer((int) ((int) (player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 0.33 * 10) * player.getAuraManager().getPrayerPotsRestoreMultiplier()));
			}
			
		};
		
		private int[] affectedSkills;
		
		private String drinkMessage;
		
		Effects(int... affectedSkills) {
			this(null, affectedSkills);
		}
		
		Effects(String drinkMessage, int... affectedSkills) {
			this.drinkMessage = drinkMessage;
			this.affectedSkills = affectedSkills;
		}
		
		public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
			return actualLevel;
		}
		
		public boolean canDrink(Player player) {
			// usualy unused
			return true;
		}
		
		public void extra(Player player) {
			// usualy unused
		}
	}
}