package org.redrune.cache.parse;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.CacheManager;
import org.redrune.cache.parse.definition.BodyData;
import org.redrune.cache.stream.RSInputStream;
import org.redrune.utility.Misc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class BodyDataParser {
	
	/**
	 * The instance of the logger
	 */
	private static final Logger logger = Misc.constructLogger(BodyDataParser.class);
	
	/**
	 * The array of body data
	 */
	@Getter
	@Setter
	private static int[] bodyData;
	
	public static void loadAll() {
		BodyData read = null;
		try {
			read = BodyDataParser.read();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Unable to parse body data!", e);
		}
		if (read == null) {
			return;
		}
		setBodyData(read.getPartsData());
	}
	
	/**
	 * Reads body data from the cache.
	 *
	 * @return The body data object, or null if it failed.
	 */
	public static BodyData read() throws IOException {
		BodyData data = new BodyData();
		byte[] buff = CacheManager.getData(28, 6, 0);
		RSInputStream reader = new RSInputStream(new ByteArrayInputStream(buff));
		data.parse(reader);
		reader.close();
		return data;
	}
}
