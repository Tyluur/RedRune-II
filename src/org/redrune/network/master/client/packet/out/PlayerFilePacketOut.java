package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WriteablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class PlayerFilePacketOut extends WriteablePacket {
	
	/**
	 * The name of the file
	 */
	private final String fileName;
	
	/**
	 * The contents of the file
	 */
	private final String fileContents;
	
	public PlayerFilePacketOut(String fileName, String fileContents) {
		super(PLAYER_FILE_UPDATE_PACKET_ID);
		this.fileName = fileName;
		this.fileContents = fileContents;
	}
	
	@Override
	public WriteablePacket create() {
		writeString(fileName);
		writeString(fileContents);
		return this;
	}
	
}