package plugin.rsinterface;

import org.redrune.game.content.entity.actor.combat.function.Magic;
import org.redrune.game.content.entity.actor.player.skills.SkillCapeCustomizer;
import org.redrune.game.content.entity.actor.player.action.impl.PlayerRestAction;
import org.redrune.game.content.entity.actor.player.dialogue.impl.Transportation;
import org.redrune.game.global.WorldTile;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.content.plugin.type.InterfacePlugin;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.constants.PacketConstants;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/31/2017
 */
public class GameframeInterfacePlugin implements InterfacePlugin {
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 548 || interfaceId == 746 || interfaceId == 387) {
			if (componentId == 11) {
				if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20769 || capeId == 20771) {
						SkillCapeCustomizer.startCustomizing(player, capeId);
					}
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767) {
						SkillCapeCustomizer.startCustomizing(player, capeId);
					}
				}
			}
			if (componentId == 14) {
				int amuletId = player.getEquipment().getAmuletId();
				if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3087, 3496, 0))) {
							Item amulet = player.getEquipment().getItem(EquipmentConstants.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(EquipmentConstants.SLOT_AMULET);
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352) {
						player.getPackets().sendMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
					}
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(2918, 3176, 0))) {
							Item amulet = player.getEquipment().getItem(EquipmentConstants.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(EquipmentConstants.SLOT_AMULET);
							}
						}
					}
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3105, 3251, 0))) {
							Item amulet = player.getEquipment().getItem(EquipmentConstants.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(EquipmentConstants.SLOT_AMULET);
							}
						}
					}
				} else if (packetId == PacketConstants.ACTION_BUTTON5_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3293, 3163, 0))) {
							Item amulet = player.getEquipment().getItem(EquipmentConstants.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(EquipmentConstants.SLOT_AMULET);
							}
						}
					}
				}
			}
			if (componentId == 50) {
				if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					EquipmentConstants.sendRemove(player, EquipmentConstants.SLOT_AURA);
					player.getAuraManager().removeAura();
				} else if (packetId == PacketConstants.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(EquipmentConstants.SLOT_AURA);
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getAuraManager().activate();
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getAuraManager().sendAuraRemainingTime();
				}
			}
		}
		if ((interfaceId == 548 && componentId == 180) || (interfaceId == 746 && componentId == 182)) {
			if (player.getInterfaceManager().containsScreenInter() || player.getInterfaceManager().containsInventoryInter()) {
				player.getPackets().sendMessage("Please finish what you're doing before opening the world map.");
				return true;
			}
			// world map open
			player.setNextAnimation(new Animation(840));
			player.getPackets().sendWindowsPane(755, 0);
			int posHash = player.getX() << 14 | player.getY();
			player.getPackets().sendGlobalConfig(622, posHash); // map open
			// center
			// pos
			player.getPackets().sendGlobalConfig(674, posHash); // player
			// position
			
		} else if ((interfaceId == 548 && componentId == 0) || (interfaceId == 746 && componentId == 229)) {
			// xp counter reset
			if (packetId == PacketConstants.ACTION_BUTTON7_PACKET) {
				player.getSkills().resetXpCounter();
			}
		} else if (interfaceId == 750) {
			if (componentId == 1) {
				if (packetId == PacketConstants.ACTION_BUTTON1_PACKET) {
					player.getAttributes().toggleRun(!player.getAttributes().isResting());
					if (player.getAttributes().isResting()) {
						player.stopAll();
					}
				} else if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					if (player.getAttributes().isResting()) {
						player.stopAll();
						return true;
					}
					long currentTime = Misc.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.getPackets().sendMessage("You can't rest while perfoming an emote.");
						return true;
					}
					if (player.getLocks().isLocked("emote")) {
						player.getPackets().sendMessage("You can't rest while perfoming an action.");
						return true;
					}
					player.stopAll();
					player.getActionManager().setAction(new PlayerRestAction());
				}
			}
		} else if (interfaceId == 751) {
			if (componentId == 25) {
				if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getContactManager().setPrivateStatus(0);
				} else if (packetId == PacketConstants.ACTION_BUTTON3_PACKET) {
					player.getContactManager().setPrivateStatus(1);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getContactManager().setPrivateStatus(2);
				}
			} else if (componentId == 31) {
				if (packetId == PacketConstants.ACTION_BUTTON2_PACKET) {
					player.getAttributes().setFilterGame(false);
				} else if (packetId == PacketConstants.ACTION_BUTTON4_PACKET) {
					player.getAttributes().setFilterGame(true);
				}
			}
		}
		return true;
	}
	
	@Override
	public void register() {
		registerInterfacePlugin(548, 746, 387, 750, 751);
	}
}
