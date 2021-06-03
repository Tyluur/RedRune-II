package org.redrune.game.content.entity.actor.player.event.player;

import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.player.action.impl.PlayerCombatAction;
import org.redrune.game.content.entity.actor.player.event.Event;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.entity.actor.npc.NPC;
import org.redrune.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.actor.player.data.PlayerInventory;
import org.redrune.game.entity.actor.player.data.RouteEvent;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.WorldTile;
import org.redrune.utility.functions.Misc;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-01-25
 */
public class PlayerInterfaceInteractionEvent extends Event {
	
	/**
	 * The target player we wish to cast to
	 */
	private final Player target;
	
	/**
	 * The details of the interface we're casting from
	 */
	private final int interfaceId, componentId, slotId;
	
	public PlayerInterfaceInteractionEvent(Player target, int interfaceId, int componentId, int slotId) {
		this.target = target;
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.slotId = slotId;
	}
	
	@Override
	public void run(Player player) {
		switch (interfaceId) {
			case PlayerInventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(slotId);
				if (item == null) {
					return;
				}
				if (!player.getInventory().containsItem(item.getId(), item.getAmount())) {
					return;
				}
				player.setNextFaceActor(target);
				player.setRouteEvent(new RouteEvent(target, () -> {
					if (target.getInterfaceManager().containsScreenInter()) {
						player.getPackets().sendMessage("That player is busy at the moment.");
						return;
					}
					if (!player.getControllerManager().processItemOnPlayer(target, item)) {
						return;
					}
					if (PluginRepository.handleItemOnPlayer(player, item, target)) {
						return;
					}
					player.getPackets().sendMessage("Nothing interesting happens.");
				}, true));
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null) {
					return;
				}
				player.resetWalkSteps();
				if ((interfaceId == 747 && componentId == 14) || (interfaceId == 662 && componentId == 65) || (interfaceId == 662 && componentId == 74) || interfaceId == 747 && componentId == 17) {
					if (interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY) {
							return;
						}
					}
					if (!player.getAttributes().isCanPvp() || !target.getAttributes().isCanPvp()) {
						
						player.getPackets().sendMessage("You can't attack players when you're not in the Wilderness..");
						return;
					}
					if (!player.getFamiliar().canAttack(target)) {
						player.getPackets().sendMessage("You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(interfaceId == 662 && componentId == 74 || interfaceId == 747 && componentId == 17);
						player.getFamiliar().setTarget(target);
					}
				}
				break;
			case 193:
			case 192:
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
							player.setNextFaceWorldTile(new WorldTile(target.getCoordFaceX(target.getSize()), target.getCoordFaceY(target.getSize()), target.getPlane()));
							if (!player.getControllerManager().canAttack(target)) {
								return;
							}
							if (!player.getAttributes().isCanPvp() || !target.getAttributes().isCanPvp()) {
								player.getPackets().sendMessage("You can't attack players who aren't in the Wilderness.");
								return;
							}
							player.setNextFaceActor(target);
							if (!target.isInMultiArea() || !player.isInMultiArea()) {
								if (player.getAttackedBy() != target && player.getAttackedByDelay() > Misc.currentTimeMillis()) {
									player.getPackets().sendMessage("That " + (player.getAttackedBy() instanceof Player ? "player" : "npc") + " is already in combat.");
									return;
								}
								if (target.getAttackedBy() != player && target.getAttackedByDelay() > Misc.currentTimeMillis()) {
									if (target.getAttackedBy() instanceof NPC) {
										target.setAttackedBy(player);
									} else {
										player.getPackets().sendMessage("Someone else is already fighting " + (target.isNPC() ? "that." : "your opponent."));
										return;
									}
								}
							}
							if (!player.getControllerManager().canAttack(target)) {
								return;
							}
							player.getActionManager().setAction(new PlayerCombatAction(target));
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
