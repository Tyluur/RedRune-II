package org.redrune.network.packet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public abstract @interface PacketHeader {

	public abstract PacketType packet();

	public enum PacketType {

		STANDARD(0),

		VAR_BYTE(1),

		VAR_SHORT(2);

		private final int size;

		private PacketType(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}
	}

}
