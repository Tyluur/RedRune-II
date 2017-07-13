package master.client.packet.out;

import master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class LoginRequestPacketOut extends WriteablePacket {
	
	/**
	 * If the player is logging into the world's lobby or actual world
	 */
	private final boolean lobby;
	
	/**
	 * The of the world the player is in
	 */
	private final byte worldId;
	
	/**
	 * The name of the player logging in
	 */
	private final String username;
	
	/**
	 * The password entered
	 */
	private final String password;
	
	/**
	 * The uuid of the session
	 */
	private final String uuid;
	
	public LoginRequestPacketOut(byte worldId, boolean lobby, String username, String password, String uuid) {
		super(LOGIN_REQUEST_PACKET_ID);
		this.username = username;
		this.lobby = lobby;
		this.worldId = worldId;
		this.password = password;
		this.uuid = uuid;
	}
	
	@Override
	public WriteablePacket create() {
		writeByte(worldId);
		writeByte((byte) (lobby ? 1 : 0));
		writeString(username);
		writeString(password);
		writeString(uuid);
		return this;
	}
}
