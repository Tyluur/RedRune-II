package org.redrune.game.node.entity.player.event.impl;

import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.event.Event;
import org.redrune.game.node.entity.player.event.EventPolicy.ActionPolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.InterfacePolicy;
import org.redrune.game.node.entity.player.event.EventPolicy.WalkablePolicy;
import org.redrune.game.node.entity.player.event.context.CommandEventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class CommandEvent extends Event<CommandEventContext> {
	
	/**
	 * Constructs a new event
	 *
	 * @param context
	 * 		The context wrapper of the event
	 */
	public CommandEvent(CommandEventContext context) {
		super(context);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player) {
		CommandRepository.processEntry(player, getContext().getArguments(), getContext().isConsole());
/*		final String[] args = getContext().getArguments();
		String name = args[0];
		switch (name) {
			case "invinter":
				player.getManager().getInterfaces().sendInventoryInterface(Integer.parseInt(args[1]));
				break;
			case "chatboxinter":
				player.getManager().getInterfaces().sendChatboxInterface(Integer.parseInt(args[1]));
				break;
			case "regiondebug":
				System.out.println(player.getRegion());
				break;
			case "loopconfigs":
				int start = Integer.parseInt(args[1]);
				int end = Integer.parseInt(args[2]);
				boolean on = Boolean.parseBoolean(args[3]);
				for (int i = start; i <= end; i++) {
					player.getTransmitter().send(new ConfigPacketBuilder(i, on ? 1 : 0).build(player));
				}
				System.out.println("Finished config loop! stopped at " + end);
				break;
			case "pnpc":
				player.getDetails().getAppearance().setNpcId(Integer.parseInt(args[1]));
				player.getUpdateMasks().register(new AppearanceUpdate(player));
				break;
			case "npc":
				World.get().addNPC(Integer.parseInt(args[1]), player.getLocation(), Direction.NORTH);
				break;
			case "item":
				player.getInventory().addItem(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				break;
			case "closeinter":
				player.getTransmitter().send(new CloseInterfaceBuilder(Integer.parseInt(args[1]), Integer.parseInt(args[2])).build(player));
				break;
			case "idbg":
				player.getTransmitter().send(new InterfaceDisplayBuilder(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), true).build(player));
				break;
			case "inter":
				player.getManager().getInterfaces().sendInterface(Integer.parseInt(args[1]), true);
				break;
			case "dbi":
				int interId = Integer.parseInt(args[1]);
				int length = Cache.getAmountOfComponents(interId);
				for (int i = 0; i < length; i++) {
					player.getManager().getInterfaces().sendInterfaceText(interId, i, "" + i);
				}
				player.getManager().getInterfaces().sendInterface(interId, true);
				break;
			case "dbg":
				player.getTransmitter().send(new CS2ConfigBuilder(Integer.parseInt(args[1]), Integer.parseInt(args[2])).build(player));
				break;
			case "interstring":
				player.getManager().getInterfaces().sendInterfaceText(Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
				player.getManager().getInterfaces().sendInterface(Integer.parseInt(args[1]), true);
				System.out.println(Cache.getAmountOfComponents(Integer.parseInt(args[1])));
				break;
			case "config":
				int id = Integer.parseInt(args[1]);
				int value = Integer.parseInt(args[2]);
				player.getTransmitter().send(new ConfigPacketBuilder(id, value).build(player));
				break;
			case "hits":
				player.getHitMap().getHitList().add(new Hit(player, 500, HitSplat.ABSORB_DAMAGE));
				player.getUpdateMasks().register(new HitUpdate(player));
				break;
			case "pos":
				player.getTransmitter().send(new MessageBuilder(player.getLocation().toString()).build(player));
				break;
			case "teleport":
				Animation MODERN_ANIM = new Animation(8939, 0, false, Priority.HIGHEST);
				Graphic MODERN_GRAPHIC = new Graphic(1576, 0, 0, false);
				
				player.getUpdateMasks().register(MODERN_ANIM);
				player.getUpdateMasks().register(MODERN_GRAPHIC);
				
				SystemManager.getScheduler().schedule(new ScheduledTask(3, 1, false, () -> {
					player.getUpdateMasks().register(new Animation(-1));
					player.getUpdateMasks().register(new Graphic(-1));
					player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(Integer.parseInt(args[1]), Integer.parseInt(args[2]), 0));
				}));
				break;
			case "xtele":
				try {
					Integer x = Integer.parseInt(args[1]);
					Integer y = Integer.parseInt(args[2]);
					Integer z = Integer.parseInt(args[3]);
					
					player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(x, y, z));
				} catch (NumberFormatException e) {
					player.getTransmitter().send(new MessageBuilder("Invalid parameters...").build(player));
				}
				break;
			case "tele":
				int x, y, z;
				if (args[1].contains(",")) {
					String[] args2 = args[1].split(",");
					x = Integer.parseInt(args2[1]) << 6 | Integer.parseInt(args2[3]);
					y = Integer.parseInt(args2[2]) << 6 | Integer.parseInt(args2[4]);
					z = Integer.parseInt(args2[0]);
				} else {
					x = Integer.parseInt(args[1]);
					y = Integer.parseInt(args[2]);
					z = player.getLocation().getPlane();
					if (args.length > 3) {
						z = Integer.parseInt(args[3]);
					}
				}
				player.putAttribute(AttributeKey.TELEPORT_LOCATION, Location.create(x, y, z));
				break;
			default:
				System.out.println("Unhandled command: " + Arrays.toString(args));
				break;
		}*/
	}
}
