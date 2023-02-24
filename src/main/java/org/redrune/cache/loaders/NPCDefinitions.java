package org.redrune.cache.loaders;

import com.alex.io.InputStream;
import org.redrune.cache.Cache;
import org.redrune.utility.game.repository.npc.NPCWalkingFlag;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public final class NPCDefinitions {

    private static final ConcurrentHashMap<Integer, NPCDefinitions> npcDefinitions = new ConcurrentHashMap<>();

    public int anInt842;

    public int anInt853;

    public int anInt875;

    public int anInt876;

    public int anInt884;

    private boolean aBoolean3190;

    private HashMap<Integer, Object> aClass180_832;

    private int anInt833;

    private int anInt836;

    private int anInt837;

    private byte respawnDirection;

    private int[][] anIntArrayArray840;

    private boolean aBoolean841;

    private int anInt844;

    private int[] anIntArray845;

    private int anInt846;

    private boolean aBoolean849 = false;

    private int anInt850;

    private byte aByte851;

    private boolean aBoolean852;

    private byte aByte854;

    private boolean aBoolean856;

    private boolean aBoolean857;

    private short[] aShortArray859;

    private byte[] aByteArray861;

    private short aShort862;

    private boolean aBoolean863;

    private int anInt864;

    private String name;

    private short[] aShortArray866;

    private int anInt869;

    private int anInt870;

    private int anInt871;

    private int anInt872;

    private int anInt874;

    private int anInt879;

    private short[] aShortArray880;

    private int[] anIntArray885;

    private int anInt888;

    private int anInt889;

    private int[] anIntArray892;

    private short aShort894;

    private short[] aShortArray896;

    private int anInt897;

    private int anInt899;

    private int anInt901;

    private int id;

    private int size = 1;

    private int renderEmote;

    private int combatLevel;

    private byte walkMask;

    private int[] modelIds;

    private int headIcons;

    private boolean isVisibleOnMap;

    private String[] options;

    public NPCDefinitions(int id) {
        this.setId(id);
        anInt842 = -1;
        anInt844 = -1;
        anInt837 = -1;
        anInt846 = -1;
        anInt853 = 32;
        setCombatLevel(-1);
        anInt836 = -1;
        setName("null");
        anInt869 = 0;
        setWalkMask((byte) 0);
        anInt850 = 255;
        anInt871 = -1;
        aBoolean852 = true;
        aShort862 = (short) 0;
        anInt876 = -1;
        aByte851 = (byte) -96;
        anInt875 = 0;
        anInt872 = -1;
        setRenderEmote(-1);
        setRespawnDirection((byte) 7);
        aBoolean857 = true;
        anInt870 = -1;
        anInt874 = -1;
        anInt833 = -1;
        anInt864 = 128;
        setHeadIcons(-1);
        aBoolean856 = false;
        anInt888 = -1;
        aByte854 = (byte) -16;
        aBoolean863 = false;
        setVisibleOnMap(true);
        anInt889 = -1;
        anInt884 = -1;
        aBoolean841 = true;
        anInt879 = -1;
        anInt899 = 128;
        aShort894 = (short) 0;
        setOptions(new String[5]);
        anInt897 = 0;
        anInt901 = -1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static NPCDefinitions getNPCDefinitions(int id) {
        try {
            NPCDefinitions def = npcDefinitions.get(id);
            if (def == null) {
                def = new NPCDefinitions(id);
                def.method694();
                byte[] data = Cache.STORE.getIndexes()[18].getFile(id >>> 7, id & 0x7f);
                if (data == null) {
                    System.out.println("Failed loading NPC " + id + ".");
                } else {
                    def.readValueLoop(new InputStream(data));
                }
                npcDefinitions.put(id, def);
            }
            return def;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void method694() {
        if (getModelIds() == null) {
            setModelIds(new int[0]);
        }
    }

    private void readValueLoop(InputStream stream) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0) {
                break;
            }
            readValues(stream, opcode);
        }
    }

    private void readValues(InputStream stream, int opcode) {
        if (opcode == 1) {
            int i = stream.readUnsignedByte();
            modelIds = new int[i];
            for (int i_66_ = 0; i_66_ < i; i_66_++) {
                modelIds[i_66_] = stream.readUnsignedShort();
                if (~modelIds[i_66_] == -65536) {
                    modelIds[i_66_] = -1;
                }
            }
        } else if (opcode == 2) {
            setName(stream.readString());
        } else if (~opcode == -13) {
            size = stream.readUnsignedByte();
        } else if (opcode >= 30 && ~opcode > -36) {
            options[opcode - 30] = stream.readString();
            if (options[-30 + opcode].equalsIgnoreCase("Hidden")) {
                options[-30 + opcode] = null;
            }
        } else if (~opcode == -41) {
            int i = stream.readUnsignedByte();
            aShortArray859 = new short[i];
            aShortArray896 = new short[i];
            for (int i_65_ = 0; ~i < ~i_65_; i_65_++) {
                aShortArray896[i_65_] = (short) stream.readUnsignedShort();
                aShortArray859[i_65_] = (short) stream.readUnsignedShort();
            }
        } else if (opcode == 41) {
            int i = stream.readUnsignedByte();
            aShortArray880 = new short[i];
            aShortArray866 = new short[i];
            for (int i_54_ = 0; ~i_54_ > ~i; i_54_++) {
                aShortArray880[i_54_] = (short) stream.readUnsignedShort();
                aShortArray866[i_54_] = (short) stream.readUnsignedShort();
            }
        } else if (~opcode == -43) {
            int i = stream.readUnsignedByte();
            aByteArray861 = new byte[i];
            for (int i_55_ = 0; i > i_55_; i_55_++) {
                aByteArray861[i_55_] = (byte) stream.readByte();
            }
        } else if (~opcode == -61) {
            int i = stream.readUnsignedByte();
            anIntArray892 = new int[i];
            for (int i_64_ = 0; ~i_64_ > ~i; i_64_++) {
                anIntArray892[i_64_] = stream.readUnsignedShort();
            }
        } else if (opcode == 93) {
            isVisibleOnMap = false;
        } else if (~opcode == -96) {
            combatLevel = stream.readUnsignedShort();
        } else if (opcode == 97) {
            anInt864 = stream.readUnsignedShort();
        } else if (~opcode == -99) {
            anInt899 = stream.readUnsignedShort();
        } else if (~opcode == -100) {
            aBoolean863 = true;
        } else if (opcode == 100) {
            anInt869 = stream.readByte();
        } else if (~opcode == -102) {
            anInt897 = stream.readByte() * 5;
        } else if (~opcode == -103) {
            headIcons = stream.readUnsignedShort();
        } else if (opcode == 103) {
            anInt853 = stream.readUnsignedShort();
        } else if (opcode == 106 || opcode == 118) {
            anInt844 = stream.readUnsignedShort();
            if (anInt844 == 65535) {
                anInt844 = -1;
            }
            anInt888 = stream.readUnsignedShort();
            if (anInt888 == 65535) {
                anInt888 = -1;
            }
            int i = -1;
            if (~opcode == -119) {
                i = stream.readUnsignedShort();
                if (~i == -65536) {
                    i = -1;
                }
            }
            int i_56_ = stream.readUnsignedByte();
            anIntArray845 = new int[2 + i_56_];
            for (int i_57_ = 0; i_56_ >= i_57_; i_57_++) {
                anIntArray845[i_57_] = stream.readUnsignedShort();
                if (anIntArray845[i_57_] == 65535) {
                    anIntArray845[i_57_] = -1;
                }
            }
            anIntArray845[i_56_ - -1] = i;
        } else if (~opcode == -108) {
            aBoolean841 = false;
        } else if (~opcode == -110) {
            aBoolean852 = false;
        } else if (~opcode == -112) {
            aBoolean857 = false;
        } else if (opcode == 113) {
            aShort862 = (short) stream.readUnsignedShort();
            aShort894 = (short) stream.readUnsignedShort();
        } else if (opcode == 114) {
            aByte851 = (byte) stream.readByte();
            aByte854 = (byte) stream.readByte();
        } else if (opcode == 115) {
            stream.readUnsignedByte();
            stream.readUnsignedByte();
        } else if (~opcode == -120) {
            walkMask = (byte) stream.readByte();
            if (NPCWalkingFlag.containsKey(id)) {
                walkMask = (byte) NPCWalkingFlag.getWalkingFlag(id);
            }
        } else if (opcode == 121) {
            anIntArrayArray840 = new int[modelIds.length][];
            int i = stream.readUnsignedByte();
            for (int i_62_ = 0; ~i_62_ > ~i; i_62_++) {
                int i_63_ = stream.readUnsignedByte();
                int[] is = anIntArrayArray840[i_63_] = new int[3];
                is[0] = stream.readByte();
                is[1] = stream.readByte();
                is[2] = stream.readByte();
            }
        } else if (~opcode == -123) {
            anInt836 = stream.readUnsignedShort();
        } else if (opcode == 123) {
            anInt846 = stream.readUnsignedShort();
        } else if (opcode == 125) {
            respawnDirection = (byte) stream.readByte();
        } else if (opcode == 127) {
            renderEmote = stream.readUnsignedShort();
        } else if (~opcode == -129) {
            stream.readUnsignedByte();
        } else if (opcode == 134) {
            anInt876 = stream.readUnsignedShort();
            if (anInt876 == 65535) {
                anInt876 = -1;
            }
            anInt842 = stream.readUnsignedShort();
            if (anInt842 == 65535) {
                anInt842 = -1;
            }
            anInt884 = stream.readUnsignedShort();
            if (~anInt884 == -65536) {
                anInt884 = -1;
            }
            anInt871 = stream.readUnsignedShort();
            if (~anInt871 == -65536) {
                anInt871 = -1;
            }
            anInt875 = stream.readUnsignedByte();
        } else if (opcode == 135) {
            anInt833 = stream.readUnsignedByte();
            anInt874 = stream.readUnsignedShort();
        } else if (opcode == 136) {
            anInt837 = stream.readUnsignedByte();
            anInt889 = stream.readUnsignedShort();
        } else if (opcode == 137) {
            anInt872 = stream.readUnsignedShort();
        } else if (opcode == 138) {
            anInt901 = stream.readUnsignedShort();
        } else if (~opcode == -140) {
            anInt879 = stream.readUnsignedShort();
        } else if (opcode == 140) {
            anInt850 = stream.readUnsignedByte();
        } else if (opcode == 141) {
            aBoolean849 = true;
        } else if (~opcode == -143) {
            anInt870 = stream.readUnsignedShort();
        } else if (opcode == 143) {
            aBoolean856 = true;
        } else if (~opcode <= -151 && opcode < 155) {
            options[opcode - 150] = stream.readString();
            if (options[opcode - 150].equalsIgnoreCase("Hidden")) {
                options[opcode + -150] = null;
            }
        } else if (~opcode == -161) {
            int i = stream.readUnsignedByte();
            anIntArray885 = new int[i];
            for (int i_58_ = 0; i > i_58_; i_58_++) {
                anIntArray885[i_58_] = stream.readUnsignedShort();
            }
        } else if (opcode == 155) {
            stream.readByte();
            stream.readByte();
            stream.readByte();
            stream.readByte();
        } else if (opcode == 158) {
        } else if (opcode == 159) {
        } else if (opcode == 162) { // added
            // opcode
            aBoolean3190 = true;
        } else if (opcode == 163) { // added
            stream.readUnsignedByte();
        } else if (opcode == 164) {
            stream.readUnsignedShort();
            stream.readUnsignedShort();
        } else if (opcode == 165) {
            stream.readUnsignedByte();
        } else if (opcode == 168) {
            stream.readUnsignedByte();
        } else if (opcode == 249) {
            int i = stream.readUnsignedByte();
            if (aClass180_832 == null) {
                aClass180_832 = new HashMap<>(i);
            }
            for (int i_60_ = 0; i > i_60_; i_60_++) {
                boolean bool = stream.readUnsignedByte() == 1;
                int key = stream.read24BitInt();
                Object value;
                if (bool) {
                    value = stream.readString();
                } else {
                    value = stream.readInt();
                }
                aClass180_832.put(key, value);
            }
        }
    }

    public static void clearNPCDefinitions() {
        npcDefinitions.clear();
    }

    public boolean hasPickupOption() {
        String[] as;
        int j = (as = getOptions()).length;
        for (int i = 0; i < j; i++) {
            String option = as[i];
            if (option != null && option.equalsIgnoreCase("pick-up")) {
                return true;
            }
        }

        return false;
    }

    public boolean hasTakeOption() {
        String[] as;
        int j = (as = getOptions()).length;
        for (int i = 0; i < j; i++) {
            String option = as[i];
            if (option != null && option.equalsIgnoreCase("take")) {
                return true;
            }
        }

        return false;
    }

    public boolean hasMarkOption() {
        for (String option : getOptions()) {
            if (option != null && option.equalsIgnoreCase("mark")) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOption(String op) {
        for (String option : getOptions()) {
            if (option != null && option.equalsIgnoreCase(op)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAttackOption() {
        for (String option : getOptions()) {
            if (option != null && option.equalsIgnoreCase("attack")) {
                return true;
            }
        }
        return false;
    }

    public String getOption(int option) {
        if (getOptions() == null || getOptions().length < option || option == 0) {
            return "";
        }
        return getOptions()[option - 1];
    }

    public boolean isABoolean3190() {
        return this.aBoolean3190;
    }

    public byte getRespawnDirection() {
        return this.respawnDirection;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getSize() {
        return this.size;
    }

    public int getRenderEmote() {
        return this.renderEmote;
    }

    public int getCombatLevel() {
        return this.combatLevel;
    }

    public byte getWalkMask() {
        return this.walkMask;
    }

    public int[] getModelIds() {
        return this.modelIds;
    }

    public int getHeadIcons() {
        return this.headIcons;
    }

    public boolean isVisibleOnMap() {
        return this.isVisibleOnMap;
    }

    public String[] getOptions() {
        return this.options;
    }

    public void setABoolean3190(boolean aBoolean3190) {
        this.aBoolean3190 = aBoolean3190;
    }

    public void setRespawnDirection(byte respawnDirection) {
        this.respawnDirection = respawnDirection;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setRenderEmote(int renderEmote) {
        this.renderEmote = renderEmote;
    }

    public void setCombatLevel(int combatLevel) {
        this.combatLevel = combatLevel;
    }

    public void setWalkMask(byte walkMask) {
        this.walkMask = walkMask;
    }

    public void setModelIds(int[] modelIds) {
        this.modelIds = modelIds;
    }

    public void setHeadIcons(int headIcons) {
        this.headIcons = headIcons;
    }

    public void setVisibleOnMap(boolean isVisibleOnMap) {
        this.isVisibleOnMap = isVisibleOnMap;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}