package org.redrune.game.content.entity.actor.player.skills.thieving;

import org.redrune.game.content.entity.actor.player.action.Action;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.ForceTalk;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.constants.SkillConstants;

/**
 * Handels the pick pocketing.
 *
 * @author Raghav/Own4g3
 */
public class PickPocketAction extends Action {
	
	/**
	 * The npc stun animation.
	 */
	private static final Animation STUN_ANIMATION = new Animation(422),
	
	/**
	 * The pick pocketing animation.
	 */
	PICKPOCKETING_ANIMATION = new Animation(881),
	
	/**
	 * The double loot animation.
	 */
	DOUBLE_LOOT_ANIMATION = new Animation(5074),
	
	/**
	 * The triple loot animation.
	 */
	TRIPLE_LOOT_ANIMATION = new Animation(5075),
	
	/**
	 * The quadruple loot animation.
	 */
	QUADRUPLE_LOOT_ANIMATION = new Animation(5078);
	
	/**
	 * The double loot gfx.
	 */
	private static final Graphics DOUBLE_LOOT_GFX = new Graphics(873),
	
	/**
	 * The triple loot gfx.
	 */
	TRIPLE_LOOT_GFX = new Graphics(874),
	
	/**
	 * The quadruple loot gfx.
	 */
	QUADRUPLE_LOOT_GFX = new Graphics(875);
	
	/**
	 * Pick pocketing npc.
	 */
	private NPC npc;
	
	/**
	 * Data of an npc.
	 */
	private PickPocketableNPC npcData;
	
	/**
	 * The index to use in the levels required arrays.
	 */
	private int index;
	
	/**
	 * Constructs a new {@code PickpocketAction} {@code Object}.
	 *
	 * @param npc
	 * 		The npc to whom the player is pickpocketing.
	 * @param npcData
	 * 		Data of an npc.
	 */
	public PickPocketAction(NPC npc, PickPocketableNPC npcData) {
		this.npc = npc;
		this.npcData = npcData;
	}
	
	@Override
	public boolean start(Player player) {
		if (checkAll(player)) {
			int thievingLevel = player.getSkills().getLevel(SkillConstants.THIEVING);
			int agilityLevel = player.getSkills().getLevel(SkillConstants.AGILITY);
			if (Misc.getRandom(50) < 5) { // Possibility of multiple
				// pickpocket.
				for (int i = 0; i < 4; i++) {
					if (npcData.getThievingLevels()[i] <= thievingLevel && npcData.getAgilityLevels()[i] <= agilityLevel) {
						index = i;
					}
				}
			}
			player.faceActor(npc);
			player.setNextAnimation(getAnimation());
			player.setNextGraphics(getGraphics());
			player.getPackets().sendGameMessage("You attempt to pick the " + npc.getDefinitions().getName().toLowerCase() + "'s pocket...");
			setActionDelay(player, 3);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}
	
	@Override
	public int processWithDelay(Player player) {
		if (!isSuccesfull(player)) {
			player.getPackets().sendGameMessage("You fail to pick the " + npc.getDefinitions().getName().toLowerCase() + "'s pocket.");
			npc.setNextAnimation(STUN_ANIMATION);
			npc.faceActor(player);
			player.setNextAnimation(new Animation(424));
			player.setNextGraphics(new Graphics(80, 5, 60));
			player.getPackets().sendGameMessage("You've been stuned.");
			player.applyHit(new Hit(player, npcData.getStunDamage(), HitSplat.REGULAR_DAMAGE));
			if (npcData.equals(PickPocketableNPC.MASTER_FARMER) || npcData.equals(PickPocketableNPC.FARMER)) {
				npc.setNextForceTalk(new ForceTalk("Cor blimey mate, what are ye doing in me pockets?"));
			} else {
				npc.setNextForceTalk(new ForceTalk("What do you think you're doing?"));
			}
			player.getLocks().lock((int) (long) npcData.getStunTime());
			stop(player);
		} else {
			player.getPackets().sendGameMessage("" + getMessage(player));
			player.getSkills().addXp(SkillConstants.THIEVING, npcData.getExperience());
			for (int i = 0; i <= index; i++) {
				Item item = npcData.getLoot()[Misc.random(npcData.getLoot().length)];
				player.getInventory().addItem(item.getId(), item.getAmount());
			}
		}
		return -1;
	}
	
	@Override
	public void stop(Player player) {
		npc.setNextFaceActor(null);
	}
	
	/**
	 * Checks if the player is succesfull to thiev or not.
	 *
	 * @param player
	 * 		The player.
	 * @return {@code True} if succesfull, {@code false} if not.
	 */
	private boolean isSuccesfull(Player player) {
		int thievingLevel = player.getSkills().getLevel(SkillConstants.THIEVING);
		int increasedChance = getIncreasedChance(player);
		int level = Misc.random(thievingLevel + increasedChance) + 1;
		double ratio = level / (Misc.random(npcData.getThievingLevels()[0] + 5) + 1);
		return !(Math.round(ratio * thievingLevel) < npcData.getThievingLevels()[0] / player.getAuraManager().getThievingAccurayMultiplier());
	}
	
	/**
	 * Gets the message to send when finishing.
	 *
	 * @param player
	 * 		The player.
	 * @return The message.
	 */
	private String getMessage(Player player) {
		switch (index) {
			case 0:
				return "You succesfully pick the " + npc.getDefinitions().getName().toLowerCase() + "'s pocket.";
			case 1:
				return "Your lightning-fast reactions allow you to steal double.";
			case 2:
				return "Your lightning-fast reactions allow you to steal triple.";
			case 3:
				return "Your lightning-fast reactions allow you to steal quadruple.";
		}
		return null;
	}
	
	/**
	 * Gets the increased chance for succesfully pickpocketing.
	 *
	 * @param player
	 * 		The player.
	 * @return The amount of increased chance.
	 */
	private int getIncreasedChance(Player player) {
		int chance = 0;
		if (EquipmentConstants.getItemSlot(EquipmentConstants.SLOT_HANDS) == 10075) {
			chance += 12;
		}
		player.getEquipment();
		if (EquipmentConstants.getItemSlot(EquipmentConstants.SLOT_CAPE) == 15349) {
			chance += 15;
		}
		if (npc.getDefinitions().getName().contains("H.A.M")) {
			for (Item item : player.getEquipment().getItems().getItems()) {
				if (item != null && item.getDefinitions().getName().contains("H.A.M")) {
					chance += 3;
				}
			}
		}
		return chance;
	}
	
	/**
	 * Checks everything before starting.
	 *
	 * @param player
	 * 		The player.
	 */
	private boolean checkAll(Player player) {
		if (player.getSkills().getLevel(SkillConstants.THIEVING) < npcData.getThievingLevels()[0]) {
			player.getDialogueManager().startDialogue("SimpleMessage", "You need a thieving level of " + npcData.getThievingLevels()[0] + "to steal from this npc.");
			return false;
		}
		if (player.getInventory().getFreeSlots() < 1) {
			player.getPackets().sendGameMessage("You don't have enough space in your inventory.");
			return false;
		}
		if (player.getAttackedBy() != null && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You can't do this while you're under combat.");
			return false;
		}
		if (npc.getAttackedBy() != null && npc.getAttackedByDelay() > Misc.currentTimeMillis()) {
			player.getPackets().sendGameMessage("The npc is under combat.");
			return false;
		}
		if (npc.isDead()) {
			player.getPackets().sendGameMessage("Too late, the npc is dead.");
			return false;
		}
		return true;
		
	}
	
	/**
	 * Gets the animation to perform.
	 *
	 * @param player
	 * 		The player.
	 * @return The animation.
	 */
	private Animation getAnimation() {
		switch (index) {
			case 0:
				return PICKPOCKETING_ANIMATION;
			case 1:
				return DOUBLE_LOOT_ANIMATION;
			case 2:
				return TRIPLE_LOOT_ANIMATION;
			case 3:
				return QUADRUPLE_LOOT_ANIMATION;
		}
		return null;
	}
	
	/**
	 * Gets the graphic to perform.
	 *
	 * @param player
	 * 		The player.
	 * @return The graphic.
	 */
	private Graphics getGraphics() {
		switch (index) {
			case 0:
				return null;
			case 1:
				return DOUBLE_LOOT_GFX;
			case 2:
				return TRIPLE_LOOT_GFX;
			case 3:
				return QUADRUPLE_LOOT_GFX;
		}
		return null;
	}
	
}
