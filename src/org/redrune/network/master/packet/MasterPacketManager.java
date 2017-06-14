package org.redrune.network.master.packet;

import org.jboss.netty.channel.Channel;
import org.redrune.network.master.MasterPacket;
import org.redrune.network.master.MasterPacketReader;
import org.redrune.utility.Misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public abstract class MasterPacketManager {
	
	/**
	 * The logger
	 */
	private final Logger logger = Misc.constructLogger(getClass());
	
	/**
	 * The map for packets
	 */
	private final Map<Integer, MasterPacketReader> bindings = new HashMap<>();
	
	/**
	 * The directory of the packets
	 */
	private final String directory;
	
	/**
	 * Constructs a new packet manager
	 *
	 * @param directory
	 * 		The directory of the packets
	 */
	protected MasterPacketManager(String directory) {
		this.directory = directory;
		load();
	}
	
	/**
	 * Reads an incoming packet
	 *
	 * @param channel
	 * 		The channel that received the packet
	 * @param packet
	 * 		The packet
	 */
	public boolean read(Channel channel, MasterPacket packet) {
		try {
			int opcode = packet.readByte();
			packet.setOpcode(opcode);
			MasterPacketReader reader = bindings.get(opcode);
			if (reader == null) {
				logger.info("Unable to find reader by opcode #" + opcode);
				return false;
			}
			reader.read(channel, packet);
			if (opcode != 4 && opcode != 5) {
				System.out.println("Reading packet #" + opcode + " [" + reader + "]");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Loads the packets from a directory
	 */
	public void load() {
		int count = 0;
		final List<Object> classesInDirectory = Misc.getClassesInDirectory(directory);
		List<Object> packetReaderList = classesInDirectory.stream().filter(MasterPacketReader.class::isInstance).collect(Collectors.toList());
		for (Object o : packetReaderList) {
			MasterPacketReader reader = (MasterPacketReader) o;
			if (bindings.put(reader.getId(), reader) != null) {
				logger.info("Already had a reader for opcode " + reader.getId());
				continue;
			}
			count++;
		}
		logger.info("Loaded " + count + " packets from " + directory);
	}
	
}
