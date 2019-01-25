package org.redrune.game.content.combat.npc.scripts;

import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.content.combat.npc.CombatScript;
import org.redrune.game.entity.actor.npc.data.combat.NPCCombatDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.NPCConstants;

public class MetalDragonCombat extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[] { "Bronze dragon", "Iron dragon", "Steel dragon", "Mithril dragon" };
	}

	@Override
	public int attack(NPC npc, Actor target) {
		NPCCombatDefinitions defs = npc.getCombatDefinitions();
		final Player player = target instanceof Player ? (Player) target : null;
		int damage;
		switch (Misc.getRandom(1)) {
			case 0:
				if (npc.withinDistance(target, 3)) {
					damage = getRandomMaxHit(npc, defs.getMaxHit(), NPCConstants.MELEE, target);
					npc.setNextAnimation(new Animation(defs.getAttackAnim()));
					delayHit(npc, 0, target, getMeleeHit(npc, damage));
				} else {
					damage = Misc.getRandom(650);
					if (CombatAlgorithm.hasAntiDragProtection(target) || (player != null && (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
						damage = (int) (damage * 0.6);
						player.getPackets().sendGameMessage("Your " + (CombatAlgorithm.hasAntiDragProtection(target) ? "shield" : "prayer") + " absorbs most of the dragon's breath!", true);
					} else if ((!CombatAlgorithm.hasAntiDragProtection(target) || !player.getPrayer().usingPrayer(0, 17) || !player.getPrayer().usingPrayer(1, 7)) && player.getFireImmune() > Misc.currentTimeMillis()) {
						damage = Misc.getRandom(164);
						player.getPackets().sendGameMessage("Your potion absorbs most of the dragon's breath!", true);
					}
					npc.setNextAnimation(new Animation(13160));
					RegionManager.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
					delayHit(npc, 1, target, getRegularHit(npc, damage));
				}
				break;
			case 1:
				if (npc.withinDistance(target, 3)) {
					damage = Misc.getRandom(650);
					if (CombatAlgorithm.hasAntiDragProtection(target) || (player != null && (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
						damage = (int) (damage * 0.6);
						player.getPackets().sendGameMessage("Your " + (CombatAlgorithm.hasAntiDragProtection(target) ? "shield" : "prayer") + " absorbs most of the dragon's breath!", true);
					} else if ((!CombatAlgorithm.hasAntiDragProtection(target) || !player.getPrayer().usingPrayer(0, 17) || !player.getPrayer().usingPrayer(1, 7)) && player.getFireImmune() > Misc.currentTimeMillis()) {
						damage = Misc.getRandom(164);
						player.getPackets().sendGameMessage("Your potion fully protects you from the heat of the dragon's breath!", true);
					}
					npc.setNextAnimation(new Animation(13164));
					npc.setNextGraphics(new Graphics(2465));
					delayHit(npc, 1, target, getRegularHit(npc, damage));
				} else {
					damage = Misc.getRandom(650);
					if (CombatAlgorithm.hasAntiDragProtection(target) || (player != null && (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)))) {
						damage = 0;
						player.getPackets().sendGameMessage("Your " + (CombatAlgorithm.hasAntiDragProtection(target) ? "shield" : "prayer") + " absorbs most of the dragon's breath!", true);
					} else if ((!CombatAlgorithm.hasAntiDragProtection(target) || !player.getPrayer().usingPrayer(0, 17) || !player.getPrayer().usingPrayer(1, 7)) && player.getFireImmune() > Misc.currentTimeMillis()) {
						damage = Misc.getRandom(164);
						player.getPackets().sendGameMessage("Your potion fully protects you from the heat of the dragon's breath!", true);
					}
					npc.setNextAnimation(new Animation(13160));
					RegionManager.sendProjectile(npc, target, 393, 28, 16, 35, 35, 16, 0);
					delayHit(npc, 1, target, getRegularHit(npc, damage));
				}
				break;
		}
		return defs.getAttackDelay();
	}

}
