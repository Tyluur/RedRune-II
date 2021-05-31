package org.redrune.net.packet;



/**
 * @author 'Mystic Flow
 * @author Tyluur
 */
public enum PacketType {
	STANDARD(0),
	VAR_BYTE(1),
	VAR_SHORT(2);
	

	private final int size;
	
	PacketType(int size) {
		this.size = size;
	}
	
}
