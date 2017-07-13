package master.network.packet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class IncomingPacket extends Packet {
	
	/**
	 * The data
	 */
	private final byte[] bytes;
	
	/**
	 * The position we're reading at
	 */
	private int position;
	
	/**
	 * Constructs a new incoming packet
	 *
	 * @param id
	 * 		The id of the packet
	 * @param bytes
	 * 		The data in the packet
	 */
	public IncomingPacket(int id, byte[] bytes) {
		super(id, null);
		this.bytes = bytes;
	}
	
	/**
	 * Reads an integer.
	 *
	 * @return An integer.
	 */
	public int readInt() {
		return readShort() << 16 | readShort();
	}
	
	/**
	 * Reads a short
	 *
	 * @return A Short
	 */
	public int readShort() {
		return (readByte() << 8 | readByte());
	}
	
	/**
	 * Reads a byte from the channel buffer.
	 *
	 * @return The byte.
	 */
	public int readByte() {
		return (bytes[position++] & 0xff);
	}
	
	/**
	 * Reads a long.
	 *
	 * @return A long.
	 */
	public long readLong() {
		long value = 0;
		value |= (long) readByte() << 56L;
		value |= (long) readByte() << 48L;
		value |= (long) readByte() << 40L;
		value |= (long) readByte() << 32L;
		value |= (long) readByte() << 24L;
		value |= (long) readByte() << 16L;
		value |= (long) readByte() << 8L;
		value |= readByte();
		return value;
	}
	
	/**
	 * Reads a string
	 *
	 * @return A String
	 */
	public String readString() {
		int index;
		StringBuilder builder = new StringBuilder();
		while ((index = readByte()) != 0) {
			builder.append((char) index);
		}
		return builder.toString();
	}
	
}
