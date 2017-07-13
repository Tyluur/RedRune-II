package master.server.network.packet.out;

import master.network.packet.writeable.WriteablePacket;
import master.network.packet.writeable.WriteableUuidPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class LoginResponsePacketOut extends WriteableUuidPacket {
	
	/**
	 * The response code
	 */
	private final byte responseCode;
	
	/**
	 * The text in the file
	 */
	private final String fileText;
	
	/**
	 * The username of the login request
	 */
	private final String username;
	
	/**
	 * If the response is to the lobby
	 */
	private final boolean lobby;
	
	public LoginResponsePacketOut(String uuid, byte responseCode, String fileText, String username, boolean lobby) {
		super(LOGIN_RESPONSE_PACKET_ID, uuid);
		this.responseCode = responseCode;
		this.fileText = fileText;
		this.username = username;
		this.lobby = lobby;
	}
	
	@Override
	public WriteablePacket create() {
		writeByte(responseCode);
		writeByte((byte) (lobby ? 1 : 0));
		writeString(fileText);
		writeString(username);
		return this;
	}
}
