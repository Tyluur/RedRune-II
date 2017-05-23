package org.redrune.network.packet;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.redrune.network.packet.event.impl.*;
import org.redrune.network.packet.write.impl.*;
import org.redrune.rs2.node.entity.player.Player;
import org.redrune.rs2.node.entity.player.components.PlayerRight;
import org.redrune.rs2.node.item.Item;
import org.redrune.rs2.node.item.ItemsContainer;

/**
 * PacketSender.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public class PacketSender {

	private final Player player;

	public PacketSender(Player player) {
		this.player = player;
	}

	public void sendKeepAlive() {
		player.writePacket(KeepAliveWriteEvent.class, new KeepAlivePacket(100));
	}

	public void sendConfig(int id, int value) {
//		player.writePacket(ConfigWriteEvent.class, new ConfigPacket(id, value));
	}

	public void sendGlobalConfig(int id, int value) {
		player.writePacket(GlobalConfigWriteEvent.class, new GlobalConfigPacket(id, value));
	}

	public void sendWebsite(String address) {
//		player.writePacket(OpenWebsiteWriteEvent.class, new OpenWebsitePacket(address));
	}

	public void sendWorldList(boolean update) {
		player.writePacket(WorldListWriteEvent.class, new WorldListPacket(update));
	}

	public void sendGamePane(int paneId, int paneType) {
		player.writePacket(GamePaneWriteEvent.class, new GamePanePacket(paneId, paneType));
	}

	public void sendRegion(boolean login) {
		player.writePacket(RegionWriteEvent.class, new RegionPacket(player, login));
	}

	public void sendInterface(int paneId, int interfaceId, int childId, boolean walkable) {
		player.writePacket(InterfaceWriteEvent.class, new InterfacePacket(paneId, interfaceId, childId, walkable));
	}

	public void sendConsoleMessage(String message) {
		player.writePacket(MessageWriteEvent.class, new MessagePacket(message, 99));
	}

	public void sendGameMessage(String message) {
		player.writePacket(MessageWriteEvent.class, new MessagePacket(message, 0));
	}

	public void sendPlayerUpdate() {
		//player.writePacket(PlayerUpdateWriteEvent.class, new PlayerUpdatePacket(player));
	}

	public void sendNPCUpdate() {
//		player.writePacket(NPCUpdateWriteEvent.class, new NPCUpdatePacket(player));
	}

	public void sendPublicChatMessage(String message, PlayerRight rights, int index, int effect) {
//		player.writePacket(ChatWriteEvent.class, new ChatPacket(message, rights, index, effect));
	}

	public void sendMinimapFlag(int x, int y) {
//		player.writePacket(MinimapFlagWriteEvent.class, new MinimapFlagPacket(x, y));
	}

	public void sendMinimapFlagReset() {
//		player.writePacket(MinimapFlagWriteEvent.class, new MinimapFlagPacket(255, 255));
	}

	public void sendSkillLevel(int skillId) {
		player.writePacket(SkillLevelWriteEvent.class, new SkillLevelPacket(skillId, (int) player.getSkills().getExperience(skillId), player.getSkills().getLevel(skillId)));
	}

	public void sendRunEnergy() {
		//player.writePacket(RunEnergyWriteEvent.class, new RunEnergyPacket(player));
	}
	
	public void sendItems(int opcode, ItemsContainer<Item> items) {
//		player.writePacket(ItemWriteEvent.class, new ItemPacket(opcode, items));
	}

	public void sendItems(int opcode, Item[] items) {
//		player.writePacket(ItemWriteEvent.class, new ItemPacket(opcode, items));
	}

	public void sendItems(int opcode, boolean key, Item[] items) {
//		player.writePacket(ItemWriteEvent.class, new ItemPacket(opcode, key, items));
	}

	public void sendUpdateItems(int opcode, ItemsContainer<Item> items, int... slots) {
//		player.writePacket(ItemUpdateWriteEvent.class, new ItemUpdatePacket(opcode, items, slots));
	}

	public void sendUpdateItems(int opcode, Item[] items, int... slots) {
//		player.writePacket(ItemUpdateWriteEvent.class, new ItemUpdatePacket(opcode, items, slots));
	}

	public void sendUpdateItems(int opcode, boolean key, Item[] items, int... slots) {
//		player.writePacket(ItemUpdateWriteEvent.class, new ItemUpdatePacket(opcode, key, items, slots));
	}

	public void sendPlayerOption(int slot, String option) {
//		player.writePacket(PlayerOptionWriteEvent.class, new PlayerOptionPacket(option, slot));
	}

	public void sendPlayerOption(int slot, String option, boolean isTopOption, int cursor) {
//		player.writePacket(PlayerOptionWriteEvent.class, new PlayerOptionPacket(option, slot, isTopOption, cursor));
	}

	public void sendAccessMask(int interfaceId, int childId, int startingSlot, int finishingSlot, int hash) {
//		player.writePacket(AccessMaskWriteEvent.class,
//				new AccessMaskPacket(interfaceId, childId, startingSlot, finishingSlot, hash));
	}

	public void sendLogout(boolean toLobby) {
//		player.writePacket(LogoutWriteEvent.class, new LogoutPacket(player, toLobby));
	}

	public void sendSound(int soundId, int soundDelay, int effectId) {
//		player.writePacket(SoundWriteEvent.class, new SoundPacket(soundId, soundDelay, effectId));
	}

	public void sendMusic(int songId) {
		sendMusic(songId, 100, 255);
	}

	public void sendMusic(int songId, int volume, int songDelay) {
//		player.writePacket(MusicWriteEvent.class, new MusicPacket(songId, volume, songDelay));
	}

	public void sendCS2Script(int scriptId) {
		sendCS2Script(scriptId, null);
	}

	public void sendCS2Script(int scriptId, Object[] parameters) {
//		player.writePacket(CS2ScriptWriteEvent.class, new CS2ScriptPacket(scriptId, parameters));
	}

//	private void buildLocation(Location location) {
//		player.writePacket(LocationWriteEvent.class, new LocationPacket(player, location));
//	}

//	public void sendObjectSpawn(WorldObject object) {
//		buildLocation(object.copyLocation());
//		player.writePacket(ObjectWriteEvent.class, new ObjectPacket(player, object, true));
//	}

//	public void sendObjectRemoval(WorldObject object) {
//		buildLocation(object.copyLocation());
//		player.writePacket(ObjectWriteEvent.class, new ObjectPacket(player, object, false));
//	}

	public void sendStringOnChild(int interfaceId, int childId, String string) {
//		player.writePacket(StringOnChildWriteEvent.class, new StringOnChildPacket(interfaceId, childId, string));
	}

	public void sendPlayerOnChild(int interfaceId, int childId) {
//		player.writePacket(PlayerOnChildWriteEvent.class, new PlayerOnChildPacket(interfaceId, childId));
	}

	public void sendNPCOnChild(int interfaceId, int childId, int npcId) {
//		player.writePacket(NPCOnChildWriteEvent.class, new NPCOnChildPacket(interfaceId, childId, npcId));
	}

	public void sendAnimationOnChild(int interfaceId, int childId, int animationId) {
//		player.writePacket(AnimationOnChildWriteEvent.class,
//				new AnimationOnChildPacket(interfaceId, childId, animationId));
	}

	public void sendOnlineStatus() {
//		player.writePacket(OnlineStatusWriteEvent.class,
//				new OnlineStatusPacket(player.getSocialManager().getOnlineStatus()));
	}

	public void sendUnlockFriendsList() {
//		player.writePacket(UnlockFriendsListWriteEvent.class, new UnlockFriendsListPacket());
	}

	public void sendEmptyFriendsList() {
//		player.writePacket(FriendsListWriteEvent.class, new FriendsListPacket(null, false, false, true));
	}

//	public void sendFriend(Friend friend, boolean online, boolean inLobby) {
//		player.writePacket(FriendsListWriteEvent.class, new FriendsListPacket(friend, online, inLobby, false));
//	}

	public void sendIgnore(Ignore ignore) {
//		player.writePacket(IgnoresListWriteEvent.class, new IgnoresListPacket(ignore));
	}

	public void sendPrivateMessage(String username, String message) {
//		player.writePacket(SendPrivateMessageWriteEvent.class, new SendPrivateMessagePacket(username, message));
	}

	public void sendReceivePrivateMessage(String username, String message, PlayerRight rights) {
//		player.writePacket(ReceivePrivateMessageWriteEvent.class,
//				new ReceivePrivateMessagePacket(username, message, rights));
	}

	public void sendCloseInterface(int windowId, int childId) {
//		player.writePacket(CloseInterfaceWriteEvent.class, new CloseInterfacePacket(windowId, childId));
	}

	public void sendNPCInterface(int index, int paneId, int interfaceId, int childId, boolean walkable) {
//		player.writePacket(NPCInterfaceWriteEvent.class,
//				new NPCInterfacePacket(index, paneId, interfaceId, childId, walkable));
	}

	public void sendGlobalString(int id, String string) {
		if (string.length() > 253) {
//			player.writePacket(LongGlobalStringWriteEvent.class, new GlobalStringPacket(id, string));
//		} else {
//			player.writePacket(ShortGlobalStringWriteEvent.class, new GlobalStringPacket(id, string));
		}
	}

	public Player getPlayer() {
		return player;
	}

}
