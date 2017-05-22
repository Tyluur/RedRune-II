package org.redrune.cache.loaders;

import org.redrune.cache.Cache;

import com.alex.io.InputStream;

/**
 * Represents body data.
 * @author Jolt Environment V2 Development team.
 * @author Emperor (Converting to Java)
 *
 */
public class BodyDefinitions {
	
	/**
	 * The parts data.
	 */
    public int[] partsData;
    
    /**
     * Something to do with weapon display.
     */
    public int somethingWithWeaponDisplay;
   
    /**
     * Contains weapon data.
     */
    public int[] weaponData;
    
    /**
     * Contains shield data.
     */
    public int[] shieldData;

    /**
     * Something to do with shield display.
     */
    public int somethingWithShieldDisplay;
    
    /**
     * Constructs a new {@code BodyData} {@code Object}.
     */
    public BodyDefinitions() {
        this.partsData = new int[0];
        this.somethingWithWeaponDisplay = -1;
        this.somethingWithShieldDisplay = -1;
    }
    
    /**
     * Parses body data from the given buffer.
     * @param buffer The buffer.
     */
    private void parse(InputStream buffer) {
        for (;;) {
            byte opcode = (byte) buffer.readUnsignedByte();
            if (opcode == 0) {
                break;
            }
            this.parse(opcode, buffer);
        }
    }

    /**
     * Parses the current opcode.
     * @param opcode The opcode.
     * @param buffer The buffer to parse from.
     */
    private void parse(byte opcode, InputStream buffer) {
    	try {
        if (opcode == 1) {
            int length = buffer.readUnsignedByte();
            this.partsData = new int[length];
            for (int i = 0; i < this.partsData.length; i++)
                this.partsData[i] = buffer.readUnsignedByte();
        } else if (opcode == 3) {
            this.somethingWithShieldDisplay = buffer.readUnsignedByte();
        } else if (opcode == 4) {
            this.somethingWithWeaponDisplay = buffer.readUnsignedByte();
        } else if (opcode == 5) {
            int length = buffer.readUnsignedByte();
            this.shieldData = new int[length];
            for (int i = 0; i < this.shieldData.length; i++)
                this.shieldData[i] = buffer.readUnsignedByte();
        } else if (opcode == 6) {
            int length = buffer.readUnsignedByte();
            this.weaponData = new int[length];
            for (int i = 0; i < this.weaponData.length; i++)
                this.weaponData[i] = buffer.readUnsignedByte();
        } else
        	throw new Exception("Unknown opcode:" + opcode);
    	} catch (Throwable t) {
    		t.printStackTrace();
    	}
    }

    /**
     * Reads body data from the cache.
     * @return The body data object, or null if it failed.
     */
    public static BodyDefinitions read() {
        BodyDefinitions data = new BodyDefinitions();
        try {
            byte[] buff = Cache.STORE.getIndexes()[28].getArchive(6).getData();
            InputStream reader = new InputStream(buff);
            data.parse(reader);
            return data;
        } catch (Exception exception) {
            /**
             * There shouldn't be any fails.
             */
            exception.printStackTrace();
            return null;
        }
    }
}