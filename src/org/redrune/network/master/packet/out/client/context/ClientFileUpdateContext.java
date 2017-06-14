package org.redrune.network.master.packet.out.client.context;

import lombok.Getter;
import org.redrune.network.master.packet.out.MasterPacketContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class ClientFileUpdateContext implements MasterPacketContext {
	
	/**
	 * The name of the file
	 */
	@Getter
	private final String username;
	
	/**
	 * The text of the file
	 */
	@Getter
	private final String jsonText;
	
	public ClientFileUpdateContext(String username, String jsonText) {
		this.username = username;
		this.jsonText = jsonText;
	}
}
