package org.redrune.cache.loaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.redrune.cache.Cache;
import org.redrune.utility.backend.BufferUtils;

import com.alex.io.InputStream;

public final class ClientScriptMap {

	@SuppressWarnings("unused")
	private char aChar6337;
	@SuppressWarnings("unused")
	private char aChar6345;
	private String defaultStringValue;
	private int defaultIntValue;
	private HashMap<Long, Object> values;

	private static final ConcurrentHashMap<Integer, ClientScriptMap> interfaceScripts = new ConcurrentHashMap<Integer, ClientScriptMap>();

	public static void main(String[] args) throws IOException {
		// Cache.STORE = new Store("C:/.jagex_cache_32/runescape/");
		Cache.init();
//		ClientScriptMap names = ClientScriptMap.getMap(1320);
		ClientScriptMap.getMap(952);
		ClientScriptMap.getMap(1349);
	}

	/*
	 * int musicIndex = (int)
	 * InterfaceScript.getInterfaceScript(1345).getKeyForValue ("Astea Frostweb"
	 * ); int id =
	 * InterfaceScript.getInterfaceScript(1351).getIntValue(musicIndex);
	 * System.out.println(id);
	 */
	//

	public static final ClientScriptMap getMap(int scriptId) {
		ClientScriptMap script = interfaceScripts.get(scriptId);
		if (script != null) {
			return script;
		}
		byte[] data = Cache.store.getIndexes()[17].getFile(scriptId >>> 0xba9ed5a8, scriptId & 0xff);
		script = new ClientScriptMap();
		if (data != null) {
			script.readValueLoop(new InputStream(data));
		}
		interfaceScripts.put(scriptId, script);
		return script;

	}

	public HashMap<Long, Object> getValues() {
		return values;
	}

	public Object getValue(long key) {
		if (values == null) {
			return null;
		}
		return values.get(key);
	}

	public long getKeyForValue(Object value) {
		for (Long key : values.keySet()) {
			if (values.get(key).equals(value)) {
				return key;
			}
		}
		return -1;
	}

	public int getSize() {
		return values.size();
	}

	public int getIntValue(long key) {
		if (values == null) {
			return defaultIntValue;
		}
		Object value = values.get(key);
		if (value == null || !(value instanceof Integer)) {
			return defaultIntValue;
		}
		return (Integer) value;
	}

	public String getStringValue(long key) {
		if (values == null) {
			return defaultStringValue;
		}
		Object value = values.get(key);
		if (value == null || !(value instanceof String)) {
			return defaultStringValue;
		}
		return (String) value;
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			aChar6337 = BufferUtils.method2782((byte) stream.readByte());
		} else if (opcode == 2) {
			aChar6345 = BufferUtils.method2782((byte) stream.readByte());
		} else if (opcode == 3) {
			defaultStringValue = stream.readString();
		} else if (opcode == 4) {
			defaultIntValue = stream.readInt();
		} else if (opcode == 5 || opcode == 6 || opcode == 7 || opcode == 8) {
			int count = stream.readUnsignedShort();
			int loop = opcode == 7 || opcode == 8 ? stream.readUnsignedShort() : count;
			values = new HashMap<Long, Object>(BufferUtils.getHashMapSize(count));
			for (int i = 0; i < loop; i++) {
				int key = opcode == 7 || opcode == 8 ? stream.readUnsignedShort() : stream.readInt();
				Object value = opcode == 5 || opcode == 7 ? stream.readString() : stream.readInt();
				values.put((long) key, value);
			}
		}
	}

	private ClientScriptMap() {
		defaultStringValue = "null";
	}
}
