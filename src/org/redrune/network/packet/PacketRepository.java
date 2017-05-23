package org.redrune.network.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.redrune.network.packet.event.PacketContext;
import org.redrune.network.packet.event.impl.InterfacePacket;
import org.redrune.network.packet.event.impl.KeepAlivePacket;
import org.redrune.network.packet.read.PacketReadEvent;
import org.redrune.network.packet.write.PacketWriteEvent;
import org.redrune.network.packet.write.impl.InterfaceWriteEvent;
import org.redrune.network.packet.write.impl.KeepAliveWriteEvent;
import org.redrune.network.stream.IoWriteEvent;

/**
 * PacketRepository.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class PacketRepository {

	public static int[] PACKET_LENGTHS = new int[240];

	private static final Map<int[], PacketReadEvent> DECODING_PACKETS = new HashMap<int[], PacketReadEvent>(112);

	private static final Map<Class<?>, PacketWriteEvent<? extends PacketContext>> ENCODING_PACKETS = new HashMap<Class<?>, PacketWriteEvent<? extends PacketContext>>(
			160);

	@SuppressWarnings("unchecked")
	public static IoWriteEvent writePacket(PacketContext context) {
		PacketWriteEvent<PacketContext> packet = (PacketWriteEvent<PacketContext>) ENCODING_PACKETS.get(context.getClass());
		if (packet != null) {
			return packet.encodePacket(context);
		}
		return null;
	}

	public static PacketReadEvent readPacket(int packetId) {
		for (Entry<int[], PacketReadEvent> i : DECODING_PACKETS.entrySet()) {
			for (int x = 0; x < i.getKey().length; x++) {
				if (i.getKey()[x] == packetId) {
					return i.getValue();
				}
			}
		}
		return null;
	}

	public static void loadPackets() {

//		DECODING_PACKETS.put(new int[] { 9 }, new KeepAliveReadEvent());
//		DECODING_PACKETS.put(new int[] { 50 }, new OpenWebsiteReadEvent());
//		DECODING_PACKETS.put(new int[] { 103 }, new WorldListReadEvent());
////		DECODING_PACKETS.put(new int[] { 32 }, new ChatReadEvent());
//		DECODING_PACKETS.put(new int[] { 34, 62, 16, 83 }, new ButtonReadEvent());
//		DECODING_PACKETS.put(new int[] { 107, 109 }, new MovementReadEvent());
//		DECODING_PACKETS.put(new int[] { 79 }, new ConsoleReadEvent());
//		DECODING_PACKETS.put(new int[] { 84 }, new DisplayReadEvent());
//		DECODING_PACKETS.put(new int[] { 58 }, new ComponentSwapReadEvent());
//		DECODING_PACKETS.put(new int[] { 69, 27 }, new NPCClickReadEvent());
//		DECODING_PACKETS.put(new int[] { 38 }, new ObjectReadEvent());
//		DECODING_PACKETS.put(new int[] { 77, 14 }, new PlayerOptionReadEvent());
//		DECODING_PACKETS.put(new int[] { 7 }, new ItemOnItemReadEvent());
//		DECODING_PACKETS.put(new int[] { 67 }, new ItemOnObjectReadEvent());
//		DECODING_PACKETS.put(new int[] { 39 }, new PerformanceReadEvent());
//		DECODING_PACKETS.put(new int[] { 37 }, new InterfaceOnNPCReadEvent());
//		DECODING_PACKETS.put(new int[] { 101 }, new AddFriendReadEvent());
//		DECODING_PACKETS.put(new int[] { 70 }, new RemoveFriendReadEvent());
//		DECODING_PACKETS.put(new int[] { 104 }, new AddIgnoreReadEvent());
//		DECODING_PACKETS.put(new int[] { 8 }, new RemoveIgnoreReadEvent());
//		DECODING_PACKETS.put(new int[] { 31 }, new PrivateMessageReadEvent());
//		DECODING_PACKETS.put(new int[] { 28 }, new DialogueContinueReadEvent());
//		DECODING_PACKETS.put(new int[] { 35 }, new TestReadEvent());
//		DECODING_PACKETS.put(new int[] { 99 }, new EnterNumberReadEvent());
//		DECODING_PACKETS.put(new int[] { 19 }, new ExchangeItemSearchReadEvent());
//		DECODING_PACKETS.put(new int[] { 12 }, new NPCExamineReadEvent());

//		ENCODING_PACKETS.put(ConfigPacket.class, new ConfigWriteEvent());
		ENCODING_PACKETS.put(KeepAlivePacket.class, new KeepAliveWriteEvent());
//		ENCODING_PACKETS.put(OpenWebsitePacket.class, new OpenWebsiteWriteEvent());
//		ENCODING_PACKETS.put(WorldListPacket.class, new WorldListWriteEvent());
//		ENCODING_PACKETS.put(GamePanePacket.class, new GamePaneWriteEvent());
//		ENCODING_PACKETS.put(RegionPacket.class, new RegionWriteEvent());
		ENCODING_PACKETS.put(InterfacePacket.class, new InterfaceWriteEvent());
//		ENCODING_PACKETS.put(GlobalConfigPacket.class, new GlobalConfigWriteEvent());
//		ENCODING_PACKETS.put(MessagePacket.class, new MessageWriteEvent());
//		ENCODING_PACKETS.put(PlayerUpdatePacket.class, new PlayerUpdateWriteEvent());
//		ENCODING_PACKETS.put(ChatPacket.class, new ChatWriteEvent());
//		ENCODING_PACKETS.put(MinimapFlagPacket.class, new MinimapFlagWriteEvent());
//		ENCODING_PACKETS.put(SkillLevelPacket.class, new SkillLevelWriteEvent());
//		ENCODING_PACKETS.put(NPCUpdatePacket.class, new NPCUpdateWriteEvent());
//		ENCODING_PACKETS.put(RunEnergyPacket.class, new RunEnergyWriteEvent());
//		ENCODING_PACKETS.put(ItemPacket.class, new ItemWriteEvent());
//		ENCODING_PACKETS.put(ItemUpdatePacket.class, new ItemUpdateWriteEvent());
//		ENCODING_PACKETS.put(PlayerOptionPacket.class, new PlayerOptionWriteEvent());
//		ENCODING_PACKETS.put(AccessMaskPacket.class, new AccessMaskWriteEvent());
//		ENCODING_PACKETS.put(LogoutPacket.class, new LogoutWriteEvent());
//		ENCODING_PACKETS.put(SoundPacket.class, new SoundWriteEvent());
//		ENCODING_PACKETS.put(MusicPacket.class, new MusicWriteEvent());
//		ENCODING_PACKETS.put(CS2ScriptPacket.class, new CS2ScriptWriteEvent());
//		ENCODING_PACKETS.put(ObjectPacket.class, new ObjectWriteEvent());
//		ENCODING_PACKETS.put(LocationPacket.class, new LocationWriteEvent());
//		ENCODING_PACKETS.put(StringOnChildPacket.class, new StringOnChildWriteEvent());
//		ENCODING_PACKETS.put(PlayerOnChildPacket.class, new PlayerOnChildWriteEvent());
//		ENCODING_PACKETS.put(AnimationOnChildPacket.class, new AnimationOnChildWriteEvent());
//		ENCODING_PACKETS.put(NPCOnChildPacket.class, new NPCOnChildWriteEvent());
//		ENCODING_PACKETS.put(OnlineStatusPacket.class, new OnlineStatusWriteEvent());
//		ENCODING_PACKETS.put(UnlockFriendsListPacket.class, new UnlockFriendsListWriteEvent());
//		ENCODING_PACKETS.put(FriendsListPacket.class, new FriendsListWriteEvent());
//		ENCODING_PACKETS.put(IgnoresListPacket.class, new IgnoresListWriteEvent());
//		ENCODING_PACKETS.put(SendPrivateMessagePacket.class, new SendPrivateMessageWriteEvent());
//		ENCODING_PACKETS.put(ReceivePrivateMessagePacket.class, new ReceivePrivateMessageWriteEvent());
//		ENCODING_PACKETS.put(CloseInterfacePacket.class, new CloseInterfaceWriteEvent());
//		ENCODING_PACKETS.put(NPCInterfacePacket.class, new NPCInterfaceWriteEvent());
//		ENCODING_PACKETS.put(GlobalStringPacket.class, new ShortGlobalStringWriteEvent());
//		ENCODING_PACKETS.put(GlobalStringPacket.class, new LongGlobalStringWriteEvent());

		loadPacketLengths();
	}

	private static void loadPacketLengths() {
		PACKET_LENGTHS = new int[]{ 0, 7, -1, 8, 3, -1, 15, 8, 6, -1, // 1-10
			3, 8, -1, -1, 3, 4, 7, 8, 1, -1, // 11-20
			4, 2, -1, 7, 7, 8, 16, 3, 7, 3, // 21-30
			-1, -1, 4, 0, 6, -1, 6, 4, 7, 7, // 31-40
			8, 0, 15, 3, 3, 7, -1, 3, 8, 7, // 41-50
			-1, 3, 4, 18, 8, -1, 5, 11, 7, -1, // 51-60
			1, 3, -1, 4, 0, 11, 8, 2, -1, 3, 3, // 61-70
			16, 3, 2, -1, 7, 4, 2, 3, -1, -1, -1, // 71-80
			-1, 3, 8, 8, 7, 0, -1, -1, 3, 3, 4, // 81-90
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 91-100
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 101-110
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 111-120
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 121-130
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 131-140
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 141-150
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 151-160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 161-170
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 171-180
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 181-190
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 191-200
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 201-210
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 211-220
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 221-230
			0, 0, 0, 0, 0, 0, 0, 0, 0, }; // 231-240
	}

}
