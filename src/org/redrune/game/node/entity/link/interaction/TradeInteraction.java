package org.redrune.game.node.entity.link.interaction;

import org.redrune.game.content.event.EventListener;
import org.redrune.game.content.event.EventListener.EventType;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.world.packet.outgoing.impl.ContainerPacketBuilder;

import java.util.Objects;

import static org.redrune.utility.rs.constant.InterfaceConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class TradeInteraction extends Interaction {
	
	/**
	 * The items the source player is offering
	 */
	private final ItemsContainer<Item> sourceItems;
	
	/**
	 * The items the source player is offering
	 */
	private final ItemsContainer<Item> targetItems;
	
	public TradeInteraction(Entity source, Entity target) {
		super(source, target);
		this.sourceItems = new ItemsContainer<>(28, false);
		this.targetItems = new ItemsContainer<>(28, false);
	}
	
	@Override
	public void start() {
		source.turnTo(target);
		target.turnTo(source);
		EventListener.setListener(source, this::end, EventType.MOVE, EventType.DAMAGE, EventType.INTERFACE_CLOSE);
		EventListener.setListener(target, this::end, EventType.MOVE, EventType.DAMAGE, EventType.INTERFACE_CLOSE);
		open(source.toPlayer(), target.toPlayer());
		open(target.toPlayer(), source.toPlayer());
		System.out.println("Started with source=" + source + ", target=" + target);
	}
	
	@Override
	public void request() {
		source.toPlayer().getTransmitter().sendMessage("Sending trade request...");
		target.toPlayer().getTransmitter().requestInteraction(100, "wishes to trade with you", getSource().toPlayer().getDetails().getDisplayName());
	}
	
	@Override
	public void end() {
		source.turnTo(null);
		source.getInteractionManager().end();
		target.turnTo(null);
		target.getInteractionManager().end();
	}
	
	@Override
	public boolean canRequest() {
		return target.toPlayer().getManager().getInterfaces().getScreenInterface() == -1;
	}
	
	@Override
	public void handleInterface(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case FIRST_TRADE_INTERFACE_ID:
				break;
			case SECOND_TRADE_INTERFACE_ID:
				break;
			case TRADE_INVENTORY_INTERFACE_ID:
				break;
		}
		System.out.println("player = [" + player + "], interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		/*
		} else if (context.getInterfaceId() == 334) {
			if (context.getComponentId() == 22) {
				player.closeInterfaces();
			} else if (context.getComponentId() == 21) {
				player.getTrade().accept(false);
			}
		} else if (context.getInterfaceId() == 335) {
			if (context.getComponentId() == 16) {
				player.getTrade().accept(true);
			} else if (context.getComponentId() == 18) {
				player.closeInterfaces();
			} else if (context.getComponentId() == 31) {
				if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getTrade().removeItem(context.getSlotId(), 1);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getTrade().removeItem(context.getSlotId(), 5);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getTrade().removeItem(context.getSlotId(), 10);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getTrade().removeItem(context.getSlotId(), Integer.MAX_VALUE);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getAttributes().put("trade_item_X_Slot", context.getSlotId());
					player.getAttributes().put("trade_isRemove", Boolean.TRUE);
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getTrade().sendValue(context.getSlotId(), false);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getTrade().sendExamine(context.getSlotId(), false);
				}
			} else if (context.getComponentId() == 34) {
				if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getTrade().sendValue(context.getSlotId(), true);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getTrade().sendExamine(context.getSlotId(), true);
				}
			} else if (context.getComponentId() == 57) {
				player.getPackets().requestClientInput(new InputEvent("Enter Amount:", InputEventType.INTEGER) {
					@Override
					public void handleInput() {
						int hours = getInput();
						if (hours > 72) {
							player.getDialogueManager().startDialogue(SimpleMessage.class, "You cannot lend an item for more than 72 hours.");
							return;
						}
						if (hours == 0) {
							player.getTrade().setLentTillLogout(true);
						} else {
							player.getTrade().setLentTillLogout(false);
						}
						player.getTrade().setHoursLentFor(hours);
						player.getTrade().refreshLendHours();
					}
				});
			}
		} else if (context.getInterfaceId() == 336) {
			if (context.getComponentId() == 0) {
				if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
					player.getTrade().addItem(context.getSlotId(), 1);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
					player.getTrade().addItem(context.getSlotId(), 5);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
					player.getTrade().addItem(context.getSlotId(), 10);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
					player.getTrade().addItem(context.getSlotId(), Integer.MAX_VALUE);
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
					player.getAttributes().put("trade_item_X_Slot", context.getSlotId());
					player.getAttributes().remove("trade_isRemove");
					player.getPackets().sendRunScript(108, "Enter Amount:");
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON6_PACKET) {
					player.getTrade().addLendItem(context.getSlotId());
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
					player.getTrade().sendValue(context.getSlotId());
				} else if (context.getPacketId() == WorldPacketsDecoder.ACTION_BUTTON8_PACKET) {
					player.getInventory().sendExamine(context.getSlotId());
				}
			}
		}
		 */
	}
	
	private void open(Player player, Player partner) {
		player.getManager().getInterfaces().sendInterface(FIRST_TRADE_INTERFACE_ID, true).sendInventoryInterface(TRADE_INVENTORY_INTERFACE_ID);
		player.getTransmitter().send(new ContainerPacketBuilder(90, new Item[0]).build(player));
		//		player.getTransmitter().send(new ContainerPacketBuilder());
	}
	
	/**
	 * Handles the requesting of a trade
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The player that was requested to trade
	 */
	public static void handleTradeRequesting(Player player, Player target) {
		player.turnTo(target);
		Entity lastRequested = target.getInteractionManager().getLastRequested(TradeInteraction.class);
		if (Objects.equals(lastRequested, player)) {
			player.getInteractionManager().startInteraction(new TradeInteraction(player, target));
		} else {
			player.getInteractionManager().requestInteraction(new TradeInteraction(player, target));
		}
	}
}
