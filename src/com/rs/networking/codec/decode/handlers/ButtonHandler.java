package com.rs.networking.codec.decode.handlers;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.cores.CoresManager;
import com.rs.game.content.*;
import com.rs.game.content.action.impl.PlayerRestAction;
import com.rs.game.content.controler.impl.minigame.DuelControler;
import com.rs.game.content.dialogue.LevelUp;
import com.rs.game.content.dialogue.Transportation;
import com.rs.game.content.skills.crafting.JewelrySmithing;
import com.rs.game.content.skills.runecrafting.Runecrafting;
import com.rs.game.content.skills.smithing.Smithing.ForgingInterface;
import com.rs.game.entity.WorldTile;
import com.rs.game.entity.actor.data.CombatDefinitions;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar;
import com.rs.game.entity.actor.npc.impl.familiar.Familiar.SpecialAttack;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.entity.actor.player.data.Equipment;
import com.rs.game.entity.actor.player.data.Inventory;
import com.rs.game.entity.actor.player.data.Skills;
import com.rs.game.entity.actor.player.link.EmotesManager;
import com.rs.game.entity.item.Item;
import com.rs.game.plugin.PluginRepository;
import com.rs.game.world.task.WorldTask;
import com.rs.game.world.task.WorldTasksManager;
import com.rs.networking.codec.decode.WorldPacketsDecoder;
import com.rs.networking.io.InputStream;
import com.rs.utility.Misc;

import java.util.HashMap;
import java.util.TimerTask;

public class ButtonHandler {
	
	public static void handleButtons(final Player player, InputStream stream, int packetId) {
		int interfaceHash = stream.readIntV2();
		int interfaceId = interfaceHash >> 16;
		if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		}
		if (player.isDead() || !player.getInterfaceManager().containsInterface(interfaceId)) {
			return;
		}
		final int componentId = interfaceHash - (interfaceId << 16);
		if (componentId != 65535 && Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= componentId) {
			// hack, or server error or client error
			// player.getSession().getChannel().close();
			return;
		}
		final int itemId = stream.readUnsignedShortLE128();
		final int slotId = stream.readUnsignedShort();
		if (!player.getControlerManager().processButtonClick(interfaceId, componentId, slotId, packetId)) {
			return;
		}
		if (PluginRepository.handleInterface(player, interfaceId, componentId, itemId, slotId, packetId)) {
			return;
		}
		if (interfaceId == 548 || interfaceId == 746 || interfaceId == 387) {
			if (componentId == 11) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20769 || capeId == 20771) {
						SkillCapeCustomizer.startCustomizing(player, capeId);
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767) {
						SkillCapeCustomizer.startCustomizing(player, capeId);
					}
				}
			}
			if (componentId == 14) {
				int amuletId = player.getEquipment().getAmuletId();
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3087, 3496, 0))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					} else if (amuletId == 1704 || amuletId == 10352) {
						player.getPackets().sendGameMessage("The amulet has ran out of charges. You need to recharge it if you wish it use it once more.");
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(2918, 3176, 0))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3105, 3251, 0))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					if (amuletId <= 1712 && amuletId >= 1706 || amuletId >= 10354 && amuletId <= 10361) {
						if (Magic.sendItemTeleportSpell(player, true, Transportation.EMOTE, Transportation.GFX, 4, new WorldTile(3293, 3163, 0))) {
							Item amulet = player.getEquipment().getItem(Equipment.SLOT_AMULET);
							if (amulet != null) {
								amulet.setId(amulet.getId() - 2);
								player.getEquipment().refresh(Equipment.SLOT_AMULET);
							}
						}
					}
				}
			} // TODO
			if (componentId == 50) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					ButtonHandler.sendRemove(player, Equipment.SLOT_AURA);
					player.getAuraManager().removeAura();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getEquipment().sendExamine(Equipment.SLOT_AURA);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getAuraManager().activate();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getAuraManager().sendAuraRemainingTime();
				}
			}
			
		} else if (interfaceId == 675) {
			JewelrySmithing.handleButtonClick(player, componentId, packetId == 14 ? 1 : packetId == 67 ? 5 : 10);
		}
		if ((interfaceId == 548 && componentId == 180) || (interfaceId == 746 && componentId == 182)) {
			if (player.getInterfaceManager().containsScreenInter() || player.getInterfaceManager().containsInventoryInter()) {
				player.getPackets().sendGameMessage("Please finish what you're doing before opening the world map.");
				return;
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
			if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
				player.getSkills().resetXpCounter();
			}
			
		} else if (interfaceId == 182) {
			if (player.getInterfaceManager().containsInventoryInter()) {
				return;
			}
			if (componentId == 6 || componentId == 13) {
				if (!player.hasFinished()) {
					player.logout();
				}
			}
		} else if (interfaceId == 880) {
			if (componentId >= 7 && componentId <= 19) {
				Familiar.setLeftclickOption(player, (componentId - 7) / 2);
			} else if (componentId == 21) {
				Familiar.confirmLeftOption(player);
			} else if (componentId == 25) {
				Familiar.setLeftclickOption(player, 7);
			}
		} else if (interfaceId == 662) {
			if (player.getFamiliar() == null) {
				return;
			}
			if (componentId == 49) {
				player.getFamiliar().call();
			} else if (componentId == 51) {
				player.getDialogueManager().startDialogue("DismissD");
			} else if (componentId == 67) {
				player.getFamiliar().takeBob();
			} else if (componentId == 69) {
				player.getFamiliar().renewFamiliar();
			} else if (componentId == 74) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
					player.getFamiliar().setSpecial(true);
				}
				if (player.getFamiliar().hasSpecialOn()) {
					player.getFamiliar().submitSpecial(player);
				}
			}
		/*} else if (interfaceId == 583) {
		
			if (componentId == 66) {
				if (DoomSayerButtons.getWildernessWall()) {
					
				}
			}
			*/
		} else if (interfaceId == 747) {
			if (componentId == 7) {
				Familiar.selectLeftOption(player);
			} else if (player.getFamiliar() == null) {
				return;
			}
			if (componentId == 10 || componentId == 19) {
				player.getFamiliar().call();
			} else if (componentId == 11 || componentId == 20) {
				player.getDialogueManager().startDialogue("DismissD");
			} else if (componentId == 12 || componentId == 21) {
				player.getFamiliar().takeBob();
			} else if (componentId == 13 || componentId == 22) {
				player.getFamiliar().renewFamiliar();
			} else if (componentId == 18 || componentId == 18) {
				player.getFamiliar().sendFollowerDetails();
			} else if (componentId == 17) {
				if (player.getFamiliar().getSpecialAttack() == SpecialAttack.CLICK) {
					player.getFamiliar().setSpecial(true);
				}
				if (player.getFamiliar().hasSpecialOn()) {
					player.getFamiliar().submitSpecial(player);
				}
			}
		} else if (interfaceId == 187) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getMusicsManager().playAnotherMusic(slotId / 2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getMusicsManager().addToPlayList(slotId / 2);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getMusicsManager().removeFromPlayList(slotId / 2);
				}
			} else if (componentId == 4) {
				player.getMusicsManager().addPlayingMusicToPlayList();
			} else if (componentId == 10) {
				player.getMusicsManager().switchPlayListOn();
			} else if (componentId == 11) {
				player.getMusicsManager().clearPlayList();
			} else if (componentId == 13) {
				player.getMusicsManager().switchShuffleOn();
			}
		} else if (interfaceId == 464) {
			player.getEmotesManager().useBookEmote(interfaceId == 464 ? componentId : EmotesManager.getId(slotId, packetId));
		} else if (interfaceId == 192) {
			if (componentId == 2) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 9) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId == 11) {
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			} else if (componentId == 13) {
				player.getCombatDefinitions().switchShowSkillSpells();
			} else if (componentId >= 15 & componentId <= 17) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 15);
			} else {
				Magic.processNormalSpell(player, componentId, packetId);
			}
		} else if (interfaceId == 336) {
			if (componentId == 0) {
				if (packetId == 81) {
					player.getTemporaryAttributtes().put("offerX", Integer.valueOf(slotId));
					player.getPackets().sendRunScript(108, "Enter Amount:");
				}
			}
			if (packetId == 61) {
				player.getTrade().addItem(player, slotId, 1);
			} else if (packetId == 64) {
				player.getTrade().addItem(player, slotId, 5);
			} else if (packetId == 4) {
				player.getTrade().addItem(player, slotId, 10);
			} else if (packetId == 52) {
				player.getTrade().addItem(player, slotId, 0x7fffffff);
			}
			// else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET)
			// player.getTrade().addLendItem(slotId);
			else if (packetId == 91) {
				player.getPackets().sendGameMessage((new StringBuilder("A ")).append((new Item(itemId, 1)).getDefinitions().getName().toLowerCase()).append("'s value is ").append(ItemDefinitions.getItemDefinitions(itemId).getValue(itemId)).append(" gold.").toString());
			}
		} else if (interfaceId == 335) {
			if (componentId == 34) {
				if (packetId == 25) {
					player.getBank().sendExamine(itemId);
				} else if (packetId == 61) {
					player.getPackets().sendGameMessage((new StringBuilder("A ")).append((new Item(itemId, 1)).getDefinitions().getName().toLowerCase()).append("'s value is ").append(ItemDefinitions.getItemDefinitions(itemId).getValue(itemId)).append(" gold.").toString());
				}
			}
			if (componentId == 31) {
				if (packetId == 61) {
					player.getTrade().removeItem(player, slotId, 1);
				} else if (packetId == 64) {
					player.getTrade().removeItem(player, slotId, 5);
				} else if (packetId == 4) {
					player.getTrade().removeItem(player, slotId, 10);
				} else if (packetId == 52) {
					player.getTrade().removeItem(player, slotId, 0x7fffffff);
				} else if (packetId == 25)
				// layer.getBank().sendExamine(slotId);
				{
					return;
				} else if (packetId == 91) {
					player.getPackets().sendGameMessage((new StringBuilder("A ")).append((new Item(itemId, 1)).getDefinitions().getName().toLowerCase()).append("'s value is ").append(ItemDefinitions.getItemDefinitions(itemId).getValue(itemId)).append(" gold.").toString());
				} else if (packetId == 81) {
					player.getTemporaryAttributtes().put("removeX", Integer.valueOf(slotId));
					player.getPackets().sendRunScript(108, "Enter Amount:");
				}
			}
			if (componentId == 18 || componentId == 12) {
				player.getTrade().tradeFailed();
			} else if (componentId == 16) {
				player.getTrade().acceptPressed(player);
			}
		} else if (interfaceId == 334) {
			if (componentId == 22) {
				player.getTrade().tradeFailed();
			} else if (componentId == 21) {
				player.getTrade().acceptPressed(player);
			}
		} else if (interfaceId == 300) {
			ForgingInterface.handleIComponents(player, componentId);
		} else if (interfaceId == 206) {
			if (componentId == 15) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getPriceCheckManager().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("pc_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, "Enter Amount:");
				}
			}
			/*
			 * } else if (interfaceId == 672) { if (componentId == 16) { if
			 * (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 1); else if (packetId
			 * == WorldPacketsDecoder.ACTION_BUTTON2_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 5); else if (packetId
			 * == WorldPacketsDecoder.ACTION_BUTTON3_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 10); else if (packetId
			 * == WorldPacketsDecoder.ACTION_BUTTON4_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, Integer.MAX_VALUE);
			 * else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET)
			 * Summoning.sendCreatePouch(player, slotId2, 28);// x else if
			 * (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
			 * player.getPackets().sendGameMessage( "You currently need " +
			 * ItemDefinitions.getItemDefinitions( slotId2)
			 * .getCreateItemRequirements()); } }
			 */
		} else if (interfaceId == 207) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getPriceCheckManager().addItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getPriceCheckManager().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("pc_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("pc_isRemove");
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 665) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
				return;
			}
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFamiliar().getBob().addItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bob_isRemove");
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
		} else if (interfaceId == 506) {
			switch (componentId) {
				case 4: // commands
					player.getInterfaceManager().sendInterface(275);
					for (int i = 0; i < 316; i++) {
						player.getPackets().sendIComponentText(275, i, " ");
					}
					player.getPackets().sendIComponentText(275, 2, "Echo Commands");
					player.getPackets().sendIComponentText(275, 16, "Missing or broken command? Report it to a staff member!");
					player.getPackets().sendIComponentText(275, 18, "::kq - Teleport to the Kalphite Queen!");
					player.getPackets().sendIComponentText(275, 19, "::bork - Teleport to Bork!");
					player.getPackets().sendIComponentText(275, 20, "::polypore - Teleport to the Polypore Dungeon!");
					player.getPackets().sendIComponentText(275, 21, "::jadinko - Teleport to Jadinkos!");
					player.getPackets().sendIComponentText(275, 22, "::jad - Teleport to the Tzhaar Caves!");
					player.getPackets().sendIComponentText(275, 23, "::corp - Teleport to Corporeal Beasts Entrance!");
					player.getPackets().sendIComponentText(275, 24, "::nex - Teleport to Nex!");
					player.getPackets().sendIComponentText(275, 25, "::brimhaven - Teleport to Brimhaven Dungeon!");
					player.getPackets().sendIComponentText(275, 26, "::train - Teleport to the Training Area of Echo!");
					player.getPackets().sendIComponentText(275, 27, "::train2 - Teleport to a secondary Training Area!");
					player.getPackets().sendIComponentText(275, 28, "");
					player.getPackets().sendIComponentText(275, 29, "");
					player.getPackets().sendIComponentText(275, 30, "::taverly - Teleport to Taverly Dungeon!");
					player.getPackets().sendIComponentText(275, 31, "::icefiends - Teleport to Icefiends!");
					player.getPackets().sendIComponentText(275, 32, "::slayertower - Teleport to Slayer Tower!");
					player.getPackets().sendIComponentText(275, 33, "::nomad - Teleport to Nomads Lair!");
					player.getPackets().sendIComponentText(275, 34, "::glacors - Teleport to Glacors!");
					player.getPackets().sendIComponentText(275, 35, "::yell");
					player.getPackets().sendIComponentText(275, 36, "::changepass");
					player.getPackets().sendIComponentText(275, 37, "::vote");
					player.getPackets().sendIComponentText(275, 38, "::claim (claim vote reward)");
					player.getPackets().sendIComponentText(275, 39, "::commands/cmd");
					player.getPackets().sendIComponentText(275, 40, "::players");
					player.getPackets().sendIComponentText(275, 41, "::highscores");
					player.getPackets().sendIComponentText(275, 42, "::xpoff/xpon");
					player.getPackets().sendIComponentText(275, 43, "");
					break;
				case 8: // updates
					player.getDialogueManager().startDialogue("SimpleMessage", "This option isn't available yet!");
					break;
				case 10: // vote
					player.getDialogueManager().startDialogue("SimpleMessage", "This option isn't available yet!");
					break;
				case 12: // help
					player.getInterfaceManager().sendInterface(275);
					player.getPackets().sendIComponentText(275, 2, "Echo Information & Help");
					player.getPackets().sendIComponentText(275, 16, "Hello, and welcome to Echo 666.");
					player.getPackets().sendIComponentText(275, 17, "If you require more advanced help, please contact a staff member.");
					player.getPackets().sendIComponentText(275, 18, "");
					player.getPackets().sendIComponentText(275, 19, "Commands can be found at: ::commands or ::cmd");
					player.getPackets().sendIComponentText(275, 20, "Found a bug? Report it for extra benefits!");
					player.getPackets().sendIComponentText(275, 21, "Need some extra cash? Voting is a great way to get an extra 5M!");
					player.getPackets().sendIComponentText(275, 22, "Don't know where to buy something? Use the Grand Exchange!");
					player.getPackets().sendIComponentText(275, 23, "Lost? Don't have anything to do? View some guides on the forums!");
					player.getPackets().sendIComponentText(275, 24, "Stuck or need immediate help? Contact a staff member!");
					player.getPackets().sendIComponentText(275, 25, "Is someone harrassing you? Please report them!");
					player.getPackets().sendIComponentText(275, 26, "");
					
					break;
			}
		} else if (interfaceId == 671) {
			if (player.getFamiliar() == null || player.getFamiliar().getBob() == null) {
				return;
			}
			if (componentId == 27) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFamiliar().getBob().removeItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bob_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("bob_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, "Enter Amount:");
				}
			} else if (componentId == 29) {
				player.getFamiliar().takeBob();
			}
		} else if (interfaceId == 916) {
			SkillsDialogue.handleSetQuantityButtons(player, componentId);
		} else if (interfaceId == 193) {
			if (componentId == 5) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId >= 9 && componentId <= 11) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 9);
			} else if (componentId == 18) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else {
				Magic.processAncientSpell(player, componentId, packetId);
			}
		} else if (interfaceId == 430) {
			if (componentId == 5) {
				player.getCombatDefinitions().switchShowCombatSpells();
			} else if (componentId == 7) {
				player.getCombatDefinitions().switchShowTeleportSkillSpells();
			} else if (componentId == 9) {
				player.getCombatDefinitions().switchShowMiscallaneousSpells();
			} else if (componentId >= 11 & componentId <= 13) {
				player.getCombatDefinitions().setSortSpellBook(componentId - 11);
			} else if (componentId == 20) {
				player.getCombatDefinitions().switchDefensiveCasting();
			} else {
				Magic.processLunarSpell(player, componentId, packetId);
			}
		} else if (interfaceId == 261) {
			if (player.getInterfaceManager().containsInventoryInter()) {
				return;
			}
			if (componentId == 14) {
				if (player.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage("Please close the interface you have open before setting your graphic options.");
					return;
				}
				player.stopAll();
				player.getInterfaceManager().sendInterface(742);
			} else if (componentId == 4) {
				player.switchAllowChatEffects();
			} else if (componentId == 5) {
				player.getInterfaceManager().sendSettings(982);
			} else if (componentId == 6) {
				player.switchMouseButtons();
			} else if (componentId == 16) {
				if (player.getInterfaceManager().containsScreenInter()) {
					player.getPackets().sendGameMessage("Please close the interface you have open before setting your audio options.");
					return;
				}
				player.stopAll();
				player.getInterfaceManager().sendInterface(743);
			}
		} else if (interfaceId == 982) {
			if (componentId == 5) {
				player.getInterfaceManager().sendSettings();
			} else if (componentId == 42) {
				player.setPrivateChatSetup(player.getPrivateChatSetup() == 0 ? 1 : 0);
			} else if (componentId >= 49 && componentId <= 61) {
				player.setPrivateChatSetup(componentId - 48);
			}
		} else if (interfaceId == 271) {
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					if (componentId == 8 || componentId == 42) {
						player.getPrayer().switchPrayer(slotId);
					} else if (componentId == 43 && player.getPrayer().isUsingQuickPrayer()) {
						player.getPrayer().switchSettingQuickPrayer();
					}
				}
			});
		} else if (interfaceId == 320) {
			player.stopAll();
			int lvlupSkill = -1;
			int skillMenu = -1;
			switch (componentId) {
				case 200: // Attack
					skillMenu = 1;
					if (player.getTemporaryAttributtes().remove("leveledUp[0]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 1);
					} else {
						lvlupSkill = 0;
						player.getPackets().sendConfig(1230, 10);
					}
					break;
				case 11: // Strength
					skillMenu = 2;
					if (player.getTemporaryAttributtes().remove("leveledUp[2]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 2);
					} else {
						lvlupSkill = 2;
						player.getPackets().sendConfig(1230, 20);
					}
					break;
				case 28: // Defence
					skillMenu = 5;
					if (player.getTemporaryAttributtes().remove("leveledUp[1]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 5);
					} else {
						lvlupSkill = 1;
						player.getPackets().sendConfig(1230, 40);
					}
					break;
				case 52: // Ranged
					skillMenu = 3;
					if (player.getTemporaryAttributtes().remove("leveledUp[4]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 3);
					} else {
						lvlupSkill = 4;
						player.getPackets().sendConfig(1230, 30);
					}
					break;
				case 76: // Prayer
					if (player.getTemporaryAttributtes().remove("leveledUp[5]") != Boolean.TRUE) {
						skillMenu = 7;
						player.getPackets().sendConfig(965, 7);
					} else {
						lvlupSkill = 5;
						player.getPackets().sendConfig(1230, 60);
					}
					break;
				case 93: // Magic
					if (player.getTemporaryAttributtes().remove("leveledUp[6]") != Boolean.TRUE) {
						skillMenu = 4;
						player.getPackets().sendConfig(965, 4);
					} else {
						lvlupSkill = 6;
						player.getPackets().sendConfig(1230, 33);
					}
					break;
				case 110: // Runecrafting
					if (player.getTemporaryAttributtes().remove("leveledUp[20]") != Boolean.TRUE) {
						skillMenu = 12;
						player.getPackets().sendConfig(965, 12);
					} else {
						lvlupSkill = 20;
						player.getPackets().sendConfig(1230, 100);
					}
					break;
				case 134: // Construction
					skillMenu = 22;
					if (player.getTemporaryAttributtes().remove("leveledUp[21]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 22);
					} else {
						lvlupSkill = 21;
						player.getPackets().sendConfig(1230, 698);
					}
					break;
				case 193: // Hitpoints
					skillMenu = 6;
					if (player.getTemporaryAttributtes().remove("leveledUp[3]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 6);
					} else {
						lvlupSkill = 3;
						player.getPackets().sendConfig(1230, 50);
					}
					break;
				case 19: // Agility
					skillMenu = 8;
					if (player.getTemporaryAttributtes().remove("leveledUp[16]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 8);
					} else {
						lvlupSkill = 16;
						player.getPackets().sendConfig(1230, 65);
					}
					break;
				case 36: // Herblore
					skillMenu = 9;
					if (player.getTemporaryAttributtes().remove("leveledUp[15]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 9);
					} else {
						lvlupSkill = 15;
						player.getPackets().sendConfig(1230, 75);
					}
					break;
				case 60: // Thieving
					skillMenu = 10;
					if (player.getTemporaryAttributtes().remove("leveledUp[17]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 10);
					} else {
						lvlupSkill = 17;
						player.getPackets().sendConfig(1230, 80);
					}
					break;
				case 84: // Crafting
					skillMenu = 11;
					if (player.getTemporaryAttributtes().remove("leveledUp[12]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 11);
					} else {
						lvlupSkill = 12;
						player.getPackets().sendConfig(1230, 90);
					}
					break;
				case 101: // Fletching
					skillMenu = 19;
					if (player.getTemporaryAttributtes().remove("leveledUp[9]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 19);
					} else {
						lvlupSkill = 9;
						player.getPackets().sendConfig(1230, 665);
					}
					break;
				case 118: // Slayer
					skillMenu = 20;
					if (player.getTemporaryAttributtes().remove("leveledUp[18]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 20);
					} else {
						lvlupSkill = 18;
						player.getPackets().sendConfig(1230, 673);
					}
					break;
				case 142: // Hunter
					skillMenu = 23;
					if (player.getTemporaryAttributtes().remove("leveledUp[22]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 23);
					} else {
						lvlupSkill = 22;
						player.getPackets().sendConfig(1230, 689);
					}
					break;
				case 186: // Mining
					skillMenu = 13;
					if (player.getTemporaryAttributtes().remove("leveledUp[14]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 13);
					} else {
						lvlupSkill = 14;
						player.getPackets().sendConfig(1230, 110);
					}
					break;
				case 179: // Smithing
					skillMenu = 14;
					if (player.getTemporaryAttributtes().remove("leveledUp[13]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 14);
					} else {
						lvlupSkill = 13;
						player.getPackets().sendConfig(1230, 115);
					}
					break;
				case 44: // Fishing
					skillMenu = 15;
					if (player.getTemporaryAttributtes().remove("leveledUp[10]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 15);
					} else {
						lvlupSkill = 10;
						player.getPackets().sendConfig(1230, 120);
					}
					break;
				case 68: // Cooking
					skillMenu = 16;
					if (player.getTemporaryAttributtes().remove("leveledUp[7]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 16);
					} else {
						lvlupSkill = 7;
						player.getPackets().sendConfig(1230, 641);
					}
					break;
				case 172: // Firemaking
					skillMenu = 17;
					if (player.getTemporaryAttributtes().remove("leveledUp[11]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 17);
					} else {
						lvlupSkill = 11;
						player.getPackets().sendConfig(1230, 649);
					}
					break;
				case 165: // Woodcutting
					skillMenu = 18;
					if (player.getTemporaryAttributtes().remove("leveledUp[8]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 18);
					} else {
						lvlupSkill = 8;
						player.getPackets().sendConfig(1230, 660);
					}
					break;
				case 126: // Farming
					skillMenu = 21;
					if (player.getTemporaryAttributtes().remove("leveledUp[19]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 21);
					} else {
						lvlupSkill = 19;
						player.getPackets().sendConfig(1230, 681);
					}
					break;
				case 150: // Summoning
					skillMenu = 24;
					if (player.getTemporaryAttributtes().remove("leveledUp[23]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 24);
					} else {
						lvlupSkill = 23;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
				case 158: // Dung
					skillMenu = 25;
					if (player.getTemporaryAttributtes().remove("leveledUp[24]") != Boolean.TRUE) {
						player.getPackets().sendConfig(965, 25);
					} else {
						lvlupSkill = 24;
						player.getPackets().sendConfig(1230, 705);
					}
					break;
			}
			
			player.getInterfaceManager().sendInterface(lvlupSkill != -1 ? 741 : 499);
			if (lvlupSkill != -1) {
				LevelUp.switchFlash(player, lvlupSkill, false);
			}
			if (skillMenu != -1) {
				player.getTemporaryAttributtes().put("skillMenu", skillMenu);
			}
		} else if (interfaceId == 499) {
			int skillMenu = -1;
			if (player.getTemporaryAttributtes().get("skillMenu") != null) {
				skillMenu = (Integer) player.getTemporaryAttributtes().get("skillMenu");
			}
			switch (componentId) {
				case 10:
					player.getPackets().sendConfig(965, skillMenu);
					break;
				case 11:
					player.getPackets().sendConfig(965, 1024 + skillMenu);
					break;
				case 12:
					player.getPackets().sendConfig(965, 2048 + skillMenu);
					break;
				case 13:
					player.getPackets().sendConfig(965, 3072 + skillMenu);
					break;
				case 14:
					player.getPackets().sendConfig(965, 4096 + skillMenu);
					break;
				case 15:
					player.getPackets().sendConfig(965, 5120 + skillMenu);
					break;
				case 16:
					player.getPackets().sendConfig(965, 6144 + skillMenu);
					break;
				case 17:
					player.getPackets().sendConfig(965, 7168 + skillMenu);
					break;
				case 18:
					player.getPackets().sendConfig(965, 8192 + skillMenu);
					break;
				case 19:
					player.getPackets().sendConfig(965, 9216 + skillMenu);
					break;
				case 20:
					player.getPackets().sendConfig(965, 10240 + skillMenu);
					break;
				case 21:
					player.getPackets().sendConfig(965, 11264 + skillMenu);
					break;
				case 22:
					player.getPackets().sendConfig(965, 12288 + skillMenu);
					break;
				case 23:
					player.getPackets().sendConfig(965, 13312 + skillMenu);
					break;
				case 29: // close inter
					player.stopAll();
					break;
			}
			
		} else if (interfaceId == 449) {
			if (componentId == 1) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null) {
					return;
				}
				shop.sendInventory(player);
			} else if (componentId == 21) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null) {
					return;
				}
				Integer slot = (Integer) player.getTemporaryAttributtes().get("ShopSelectedSlot");
				if (slot == null) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					shop.buy(player, slot, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					shop.buy(player, slot, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					shop.buy(player, slot, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					shop.buy(player, slot, 50);
				}
				
			}
		} else if (interfaceId == 620) {
			if (componentId == 25) {
				Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
				if (shop == null) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					shop.sendInfo(player, slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					shop.buy(player, slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					shop.buy(player, slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					shop.buy(player, slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					shop.buy(player, slotId, 50);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					shop.buy(player, slotId, 500);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					shop.sendExamine(player, slotId);
				}
			}
		} else if (interfaceId == 621) {
			if (componentId == 0) {
				
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				} else {
					Shop shop = (Shop) player.getTemporaryAttributtes().get("Shop");
					if (shop == null) {
						return;
					}
					if (shop.id == 76 || shop.id == 89) {
						player.getPackets().sendGameMessage("You cannot sell to this shop.");
						return;
					}
					if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
						shop.sendValue(player, slotId);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
						shop.sell(player, slotId, 1);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
						shop.sell(player, slotId, 5);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
						shop.sell(player, slotId, 10);
					} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
						shop.sell(player, slotId, 50);
					}
				}
			}
		} else if (interfaceId == 640) {
			if (componentId == 18 || componentId == 22) {
				player.getTemporaryAttributtes().put("WillDuelFriendly", true);
				player.getPackets().sendConfig(283, 67108864);
			} else if (componentId == 19 || componentId == 21) {
				player.getTemporaryAttributtes().put("WillDuelFriendly", false);
				player.getPackets().sendConfig(283, 134217728);
			} else if (componentId == 20) {
				DuelControler.challenge(player);
			}
		} else if (interfaceId == 650) {
			if (componentId == 17) {
				player.stopAll();
				player.setNextWorldTile(new WorldTile(2974, 4384, 0));
				player.getControlerManager().startControler("CorpBeastControler");
			} else if (componentId == 18) {
				player.closeInterfaces();
			}
		} else if (interfaceId == Inventory.INVENTORY_INTERFACE) { // inventory
			if (componentId == 0) {
				if (slotId > 27 || player.getInterfaceManager().containsInventoryInter()) {
					return;
				}
				Item item = player.getInventory().getItem(slotId);
				if (item == null || item.getId() != itemId) {
					return;
				}
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					InventoryOptionsHandler.handleItemOption1(player, slotId, itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					InventoryOptionsHandler.handleItemOption2(player, slotId, itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					InventoryOptionsHandler.handleItemOption3(player, slotId, itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					InventoryOptionsHandler.handleItemOption4(player, slotId, itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					InventoryOptionsHandler.handleItemOption5(player, slotId, itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					InventoryOptionsHandler.handleItemOption6(player, slotId, itemId, item);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON7_PACKET) {
					InventoryOptionsHandler.handleItemOption7(player, slotId, itemId, item);
				}
			}
		} else if (interfaceId == 742) {
			if (componentId == 46) // close
			{
				player.stopAll();
			}
		} else if (interfaceId == 743) {
			if (componentId == 20) // close
			{
				player.stopAll();
			}
		} else if (interfaceId == 741) {
			if (componentId == 9) // close
			{
				player.stopAll();
			}
		} else if (interfaceId == 749) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) // activate
				{
					player.getPrayer().switchQuickPrayers();
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) // switch
				{
					player.getPrayer().switchSettingQuickPrayer();
				}
			}
		} else if (interfaceId == 750) {
			if (componentId == 1) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.toogleRun(!player.isResting());
					if (player.isResting()) {
						player.stopAll();
					}
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					if (player.isResting()) {
						player.stopAll();
						return;
					}
					long currentTime = Misc.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an emote.");
						return;
					}
					if (player.getLockDelay() >= currentTime) {
						player.getPackets().sendGameMessage("You can't rest while perfoming an action.");
						return;
					}
					player.stopAll();
					player.getActionManager().setAction(new PlayerRestAction());
				}
			}
		} else if (interfaceId == 11) {
			if (componentId == 17) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().depositItem(slotId, 1, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().depositItem(slotId, 5, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getBank().depositItem(slotId, 10, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			} else if (componentId == 18) {
				player.getBank().depositAllInventory(false);
			} else if (componentId == 20) {
				player.getBank().depositAllEquipment(false);
			}
		} else if (interfaceId == 762) {
			if (componentId == 117) {
				return;
			}
			if (componentId == 15) {
				player.getBank().switchInsertItems();
			} else if (componentId == 19) {
				player.getBank().switchWithdrawNotes();
			} else if (componentId == 33) {
				player.getBank().depositAllInventory(true);
			} else if (componentId == 35) {
				player.getBank().depositAllEquipment(true);
			} else if (componentId == 44) {
				player.closeInterfaces();
				player.getInterfaceManager().sendInterface(767);
				player.setCloseInterfacesEvent(new Runnable() {
					@Override
					public void run() {
						player.getBank().openBank();
					}
				});
			} else if (componentId >= 44 && componentId <= 62) {
				int tabId = 9 - ((componentId - 44) / 2);
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().setCurrentTab(tabId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().collapse(tabId);
				}
			} else if (componentId == 93) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().withdrawItem(slotId, 1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().withdrawItem(slotId, 5);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getBank().withdrawItem(slotId, 10);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getBank().withdrawLastAmount(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().put("bank_isWithdraw", Boolean.TRUE);
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					player.getBank().withdrawItemButOne(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getBank().sendExamine(slotId);
				}
				
			}
		} else if (interfaceId == 763) {
			if (componentId == 0) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getBank().depositItem(slotId, 1, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getBank().depositItem(slotId, 5, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getBank().depositItem(slotId, 10, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getBank().depositLastAmount(slotId);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getTemporaryAttributtes().put("bank_item_X_Slot", slotId);
					player.getTemporaryAttributtes().remove("bank_isWithdraw");
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getInventory().sendExamine(slotId);
				}
			}
			
			// Clan Stuff
		} else if (interfaceId == 767) {
			if (componentId == 10) {
				player.getBank().openBank();
			}
		} else if (interfaceId == 884) {
			if (componentId == 4) {
				if (player.hasInstantSpecial(player.getEquipment().getWeaponId())) {
					return;
				}
				CoresManager.fastExecutor.schedule(new TimerTask() {
					@Override
					public void run() {
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								player.getCombatDefinitions().switchUsingSpecialAttack();
							}
						}, 0);
						
					}
				}, 200);
			} else if (componentId >= 11 && componentId <= 14) {
				player.getCombatDefinitions().setAttackStyle(componentId - 11);
			} else if (componentId == 15) {
				player.getCombatDefinitions().switchAutoRelatie();
			}
		} else if (interfaceId == 755) {
			if (componentId == 44) {
				player.setNextAnimation(new Animation(-1));
			}
			player.getPackets().sendWindowsPane(player.getInterfaceManager().hasRezizableScreen() ? 746 : 548, 2);
		} else if (interfaceId == 20) {
			SkillCapeCustomizer.handleSkillCapeCustomizer(player, componentId);
		} else if (interfaceId == 1056) {
			if (componentId == 102) {
				player.getInterfaceManager().sendInterface(917);
			}
		} else if (interfaceId == 751) {
			if (componentId == 25) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getFriendsIgnores().setPrivateStatus(0);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getFriendsIgnores().setPrivateStatus(1);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getFriendsIgnores().setPrivateStatus(2);
				}
			} else if (componentId == 25) {
				if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.setFilterGame(false);
				} else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.setFilterGame(true);
				}
			}
		} else if (interfaceId == 631 || interfaceId == 628 || interfaceId == 626 || interfaceId == 637 || interfaceId == 639) {
			player.getDuelConfigurations().processButtonClick(player, interfaceId, componentId, slotId, packetId);
		} else if (interfaceId == 1028) {
			PlayerDesign.handle(player, componentId, slotId);
		} else if (interfaceId == 900) {
			PlayerLook.handleMageMakeOverButtons(player, componentId);
		} else if (interfaceId == 309) {
			PlayerLook.handleHairdresserSalonButtons(player, componentId, slotId);
		} else if (interfaceId == 729) {
			PlayerLook.handleThessaliasMakeOverButtons(player, componentId, slotId);
		} else if (interfaceId == 1108 || interfaceId == 1109 || interfaceId == 1110) {
			player.getFriendsIgnores().handleFriendChatButtons(interfaceId, componentId, packetId);
		} else if (interfaceId == 1079) {
			player.closeInterfaces();
		}
		System.out.println("InterfaceId " + interfaceId + ", componentId " + componentId + ", slotId " + slotId + ", slotId2 " + itemId + ", PacketId: " + packetId);
	}
	
	public static void sendRemove(Player player, int slotId) {
		if (slotId >= 15) {
			return;
		}
		Item item = player.getEquipment().getItem(slotId);
		if (item == null || !player.getInventory().addItem(item.getId(), item.getAmount())) {
			return;
		}
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getAppearence().generateAppearenceData();
		if (Runecrafting.isTiara(item.getId())) {
			player.getPackets().sendConfig(491, 0);
		}
		if (slotId == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
	}
	
	public static boolean sendWear(Player player, int slotId, int itemId) {
		if (player.hasFinished() || player.isDead()) {
			return false;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearence().isMale()) && item.getDefinitions().id != 4084) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return true;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return true;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0) {
					continue;
				}
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120) {
					continue;
				}
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequiriments) {
						player.getPackets().sendGameMessage("You are not high enough level to use this item.");
					}
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
				}
				
			}
		}
		if (!hasRequiriments) {
			return true;
		}
		if (!player.getControlerManager().canEquip(targetSlot, itemId)) {
			return false;
		}
		player.getInventory().deleteItem(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(5).getId(), player.getEquipment().getItem(5).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null && Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().addItem(player.getEquipment().getItem(3).getId(), player.getEquipment().getItem(3).getAmount())) {
					player.getInventory().getItems().set(slotId, item);
					player.getInventory().refresh(slotId);
					return true;
				}
				player.getEquipment().getItems().set(3, null);
			}
			
		}
		if (player.getEquipment().getItem(targetSlot) != null && (itemId != player.getEquipment().getItem(targetSlot).getId() || !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
				player.getInventory().refresh(slotId);
			} else {
				player.getInventory().addItem(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendSound(2240, 0, 1);
		if (targetSlot == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
		player.getCharges().wear(targetSlot);
		return true;
	}
	
	//	public DoomSayerButtons DoomSayer;
	public static boolean sendWear2(Player player, int slotId, int itemId) {
		if (player.hasFinished() || player.isDead()) {
			return false;
		}
		Item item = player.getInventory().getItem(slotId);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getAppearence().isMale())) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		int targetSlot = Equipment.getItemSlot(itemId);
		if (targetSlot == -1) {
			player.getPackets().sendGameMessage("You can't wear that.");
			return false;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && Equipment.isTwoHandedWeapon(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getPackets().sendGameMessage("Not enough free space in your inventory.");
			return false;
		}
		HashMap<Integer, Integer> requiriments = item.getDefinitions().getWearingSkillRequiriments();
		boolean hasRequiriments = true;
		if (requiriments != null) {
			for (int skillId : requiriments.keySet()) {
				if (skillId > 24 || skillId < 0) {
					continue;
				}
				int level = requiriments.get(skillId);
				if (level < 0 || level > 120) {
					continue;
				}
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequiriments) {
						player.getPackets().sendGameMessage("You are not high enough level to use this item.");
					}
					hasRequiriments = false;
					String name = Skills.SKILL_NAME[skillId].toLowerCase();
					player.getPackets().sendGameMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".");
				}
				
			}
		}
		if (!hasRequiriments) {
			return false;
		}
		if (!player.getControlerManager().canEquip(targetSlot, itemId)) {
			return false;
		}
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null && Equipment.isTwoHandedWeapon(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return false;
				}
				player.getEquipment().getItems().set(3, null);
			}
			
		}
		if (player.getEquipment().getItem(targetSlot) != null && (itemId != player.getEquipment().getItem(targetSlot).getId() || !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			} else {
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(itemId, oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : targetSlot == 3 ? 0 : 3);
		if (targetSlot == 3) {
			player.getCombatDefinitions().desecreaseSpecialAttack(0);
		}
		player.getCharges().wear(targetSlot);
		return true;
	}
	
	public static void sendWear(Player player, int[] slotIds) {
		if (player.hasFinished() || player.isDead()) {
			return;
		}
		
		boolean worn = false;
		Item[] copy = player.getInventory().getItems().getItemsCopy();
		for (int slotId : slotIds) {
			Item item = player.getInventory().getItem(slotId);
			if (item == null) {
				continue;
			}
			if (sendWear2(player, slotId, item.getId())) {
				worn = true;
			}
		}
		player.getInventory().refreshItems(copy);
		if (worn) {
			player.getAppearence().generateAppearenceData();
			player.getPackets().sendSound(2240, 0, 1);
		}
	}
	
	public static void refreshEquipBonuses(Player player) {
		player.getPackets().sendIComponentText(667, 31, "Stab: +" + player.getCombatDefinitions().getBonuses()[0]);
		player.getPackets().sendIComponentText(667, 32, "Slash: +" + player.getCombatDefinitions().getBonuses()[1]);
		player.getPackets().sendIComponentText(667, 33, "Crush: +" + player.getCombatDefinitions().getBonuses()[2]);
		player.getPackets().sendIComponentText(667, 34, "Magic: +" + player.getCombatDefinitions().getBonuses()[3]);
		player.getPackets().sendIComponentText(667, 35, "Range: +" + player.getCombatDefinitions().getBonuses()[4]);
		player.getPackets().sendIComponentText(667, 36, "Stab: +" + player.getCombatDefinitions().getBonuses()[5]);
		player.getPackets().sendIComponentText(667, 37, "Slash: +" + player.getCombatDefinitions().getBonuses()[6]);
		player.getPackets().sendIComponentText(667, 38, "Crush: +" + player.getCombatDefinitions().getBonuses()[7]);
		player.getPackets().sendIComponentText(667, 39, "Magic: +" + player.getCombatDefinitions().getBonuses()[8]);
		player.getPackets().sendIComponentText(667, 40, "Range: +" + player.getCombatDefinitions().getBonuses()[9]);
		player.getPackets().sendIComponentText(667, 41, "Summoning: +" + player.getCombatDefinitions().getBonuses()[10]);
		player.getPackets().sendIComponentText(667, 42, "Absorve Melee: " + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 43, "Absorve Magic: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 44, "Absorve Ranged: +" + player.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] + "%");
		player.getPackets().sendIComponentText(667, 45, "Strength: " + player.getCombatDefinitions().getBonuses()[14]);
		player.getPackets().sendIComponentText(667, 46, "Ranged Str: " + player.getCombatDefinitions().getBonuses()[15]);
		player.getPackets().sendIComponentText(667, 47, "Prayer: +" + player.getCombatDefinitions().getBonuses()[16]);
		player.getPackets().sendIComponentText(667, 48, "Magic Damage: +" + player.getCombatDefinitions().getBonuses()[17] + "%");
	}
}
