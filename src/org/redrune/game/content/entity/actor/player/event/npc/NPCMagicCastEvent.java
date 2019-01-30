package org.redrune.game.content.entity.actor.player.event.npc;

import org.redrune.game.content.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-01-25
 */
public class NPCMagicCastEvent extends Event {
	
	/**
	 * The npc we are casting the spell on
	 */
	private final NPC npc;
	
	/**
	 * The details of the interface the spell is coming from
	 */
	private final int interfaceId, componentId, slot;
	
	public NPCMagicCastEvent(NPC npc, int interfaceId, int componentId, int slot) {
		this.npc = npc;
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.slot = slot;
	}
	
	@Override
	public void run(Player player) {
		if (!npc.getDefinitions().hasAttackOption()) {
			player.getPackets().sendGameMessage("You can't attack this npc.");
			return;
		}
		switch (interfaceId) {
			case PlayerInventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(slot);
				if (item == null) {
					return;
				}
				if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
					return;
				}
				if (!player.getControllerManager().processItemOnNPC(npc, item)) {
					return;
				}
				// InventoryOptionsHandler.handleItemOnNPC(npc, item);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null) {
					return;
				}
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 14) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 17 || interfaceId == 747 && componentId == 23) {
					if ((interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY) {
							return;
						}
					}
					if (npc == player.getFamiliar()) {
						player.getPackets().sendGameMessage("You can't attack your own familiar.");
						return;
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.getPackets().sendGameMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 192:
			case 193:
				switch (componentId) {
					case 25: // air strike
					case 28: // water strike
					case 30: // earth strike
					case 32: // fire strike
					case 34: // air bolt
					case 42: // earth bolt
					case 45: // fire bolt
					case 49: // air blast
					case 52: // water blast
					case 58: // earth blast
					case 63: // fire blast
					case 70: // air wave
					case 73: // water wave
					case 77: // earth wave
					case 80: // fire wave
					case 84: // air surge
					case 87: // water surge
					case 89: // earth surge
					case 66: // Sara Strike
					case 67: // Guthix Claws
					case 68: // Flame of Zammy
					case 93:
					case 91: // fire surge
					case 99: // storm of Armadyl
					case 55: // snare
					case 81: // entangle
					case 24:
					case 20:
					case 26:
					case 22:
					case 29:
					case 33:
					case 21:
					case 31:
					case 35:
					case 27:
					case 23:
					case 75:
					case 78:
					case 82:
					case 86: // teleblock
					case 36: // bind
					case 37:
					case 38:
					case 39: // water bolt
						if (CombatAlgorithm.checkCombatSpell(player, componentId, 1, false)) {
							player.setNextFaceWorldTile(new WorldTile(npc.getCoordFaceX(npc.getSize()), npc.getCoordFaceY(npc.getSize()), npc.getPlane()));
							if (!player.getControllerManager().canAttack(npc)) {
								return;
							}
							player.setNextFaceActor(npc);
							if (npc instanceof Familiar) {
								Familiar familiar = (Familiar) npc;
								if (familiar == player.getFamiliar()) {
									player.getPackets().sendGameMessage("You can't attack your own familiar.");
									return;
								}
								if (!familiar.canAttack(player)) {
									player.getPackets().sendGameMessage("You can't attack this npc.");
									return;
								}
							} else if (!npc.isForceMultiAttacked()) {
								if (!npc.isInMultiArea() || !player.isInMultiArea()) {
									if (player.getAttackedBy() != npc && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
										player.getPackets().sendGameMessage("I'm already under attack.");
										return;
									}
									if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > Misc.currentTimeMillis()) {
										player.getPackets().sendGameMessage("Someone else is already fighting that.");
										return;
									}
								}
							}
							if (!player.getControllerManager().canAttack(npc)) {
								return;
							}
							player.getActionManager().setAction(new PlayerCombatAction(npc));
						}
						break;
				}
				break;
		}
	}
	
	@Override
	public EventPolicy[] policies() {
		return arguments(EventPolicy.CLOSE_INTERFACE, EventPolicy.STOP_WALK);
	}
}
