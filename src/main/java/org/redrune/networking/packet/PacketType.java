package org.redrune.networking.packet;

import lombok.Getter;

/**
 * @author 'Mystic Flow
 * @author Tyluur
 */
public enum PacketType {
	STANDARD(0),
	VAR_BYTE(1),
	VAR_SHORT(2);
	
	@Getter
	private final int size;
	
	PacketType(int size) {
		this.size = size;
	}
	
}
