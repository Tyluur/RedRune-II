package org.redrune.cache.loaders;

import com.alex.io.InputStream;
import org.redrune.cache.Cache;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class ObjectDefinitions {

    private static final ConcurrentHashMap<Integer, ObjectDefinitions> objectDefinitions = new ConcurrentHashMap<Integer, ObjectDefinitions>();

    static int anInt3832;

    static int anInt3836;

    static int anInt3842;

    static int anInt3843;

    static int anInt3846;

    Object loader;

    int anInt3896;

    private int[] anIntArray3833 = null;

    private int anInt3835;

    private int anInt3838 = -1;

    private boolean aBoolean3839;

    private int anInt3844;

    private boolean aBoolean3845;

    private int anInt3850;

    private int anInt3851;

    private int anInt3855;

    private int anInt3857;

    private int[] anIntArray3859;

    private int anInt3860;

    private int anInt3865;

    private boolean aBoolean3866;

    private boolean aBoolean3867;

    private boolean aBoolean3870;

    private boolean aBoolean3872;

    private boolean aBoolean3873;

    private int anInt3892;

    private boolean aBoolean3894;

    private boolean aBoolean3895;

    private int anInt3900;

    private int anInt3904;

    private int anInt3905;

    private boolean aBoolean3906;

    private int[] anIntArray3908;

    private int anInt3913;

    private int anInt3921;

    private boolean aBoolean3923;

    private boolean aBoolean3924;

    private int cflag;

    private boolean secondBool;

    private boolean aBoolean3853;

    private int thirdInt;

    private int anInt3876;

    private boolean aBoolean3891;

    private int secondInt;

    private String name;

    private int id;

    private String[] options;

    private int[][] modelIds;

    private int sizeX;

    private int sizeY;

    private int configFileId;

    private boolean projectileClipped;

    private boolean ignoreClipOnAlternativeRoute;

    private int clipType;

    private int configId;

    private short[] originalColors;

    private int[] toObjectIds;

    private int anInt3834;

    private byte aByte3837;

    private int anInt3840;

    private int anInt3841;

    private byte aByte3847;

    private byte aByte3849;

    private byte[] aByteArray3858;

    private short[] modifiedColors;

    private int[] anIntArray3869;

    private int anInt3875;

    private int anInt3877;

    private int anInt3878;

    private int anInt3882;

    private int anInt3883;

    private int anInt3889;

    private byte[] aByteArray3899;

    private int anInt3902;

    private byte aByte3914;

    private int anInt3915;

    private int anInt3917;

    private short[] aShortArray3919;

    private short[] aShortArray3920;

    private HashMap<Integer, Object> parameters;

    private ObjectDefinitions() {
        anInt3835 = -1;
        anInt3860 = -1;
        setConfigFileId(-1);
        aBoolean3866 = false;
        anInt3851 = -1;
        anInt3865 = 255;
        aBoolean3845 = false;
        aBoolean3867 = false;
        anInt3850 = 0;
        anInt3844 = -1;
        anInt3857 = -1;
        aBoolean3872 = true;
        anInt3882 = -1;
        anInt3834 = 0;
        setOptions(new String[5]);
        anInt3875 = 0;
        aBoolean3839 = false;
        anIntArray3869 = null;
        setSizeY(1);
        thirdInt = -1;
        anInt3883 = 0;
        aBoolean3895 = true;
        anInt3840 = 0;
        aBoolean3870 = false;
        anInt3889 = 0;
        aBoolean3853 = true;
        secondBool = false;
        setClipType(2);
        setProjectileClipped(true);
        setIgnoreClipOnAlternativeRoute(false);
        anInt3855 = -1;
        anInt3878 = 0;
        anInt3904 = 0;
        setSizeX(1);
        anInt3876 = -1;
        aBoolean3891 = false;
        anInt3905 = 0;
        setName("null");
        anInt3913 = -1;
        aBoolean3906 = false;
        aBoolean3873 = false;
        aByte3914 = (byte) 0;
        anInt3915 = 0;
        anInt3900 = 0;
        secondInt = -1;
        aBoolean3894 = false;
        anInt3921 = 0;
        anInt3902 = 128;
        setConfigId(-1);
        anInt3877 = 0;
        cflag = 0;
        anInt3892 = 64;
        aBoolean3923 = false;
        aBoolean3924 = false;
        anInt3841 = 128;
        anInt3917 = 128;
    }

    public static ObjectDefinitions getObjectDefinitions(int id) {
        ObjectDefinitions def = objectDefinitions.get(id);
        if (def == null) {
            def = new ObjectDefinitions();
            def.setId(id);
            byte[] data = Cache.STORE.getIndexes()[16].getFile(getArchiveId(id), id & 0xff);
            if (data == null) {
                System.out.println("Unable to get definitions for object " + id);
                return null;
            } else {
                def.readValueLoop(new InputStream(data));
            }
            def.method3287();
            if ((def.getId() == 30141)) {
                def.setIgnoreClipOnAlternativeRoute(true);
            }
            if ((def.getName() != null && (def.getName().equalsIgnoreCase("bank booth") || def.getName().equalsIgnoreCase("counter")))) {
                def.setIgnoreClipOnAlternativeRoute(false);
                def.setProjectileClipped(true);
                if (def.getClipType() == 0) {
                    def.setClipType(1);
                }
            }
            if (def.isIgnoreClipOnAlternativeRoute()) {
                def.setProjectileClipped(false);
                def.setClipType(0);
            }
            objectDefinitions.put(id, def);
        }
        return def;
    }

    private static int getArchiveId(int id) {
        return id >>> 8;
    }

    private void readValueLoop(InputStream stream) {
        for (; ; ) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0) {
                // System.out.println("Remaining: "+stream.getRemaining());
                break;
            }
            readValues(stream, opcode);
        }
    }

    final void method3287() {
        if (secondInt == -1) {
            secondInt = 0;
            if (aByteArray3899 != null && aByteArray3899.length == 1 && aByteArray3899[0] == 10) {
                secondInt = 1;
            }
            for (int i_13_ = 0; i_13_ < 5; i_13_++) {
                if (getOptions()[i_13_] != null) {
                    secondInt = 1;
                    break;
                }
            }
        }
        if (anInt3855 == -1) {
            anInt3855 = getClipType() != 0 ? 1 : 0;
        }
    }

    private void readValues(InputStream stream, int opcode) {
        // System.out.println(opcode);
        if (opcode == 1 || opcode == 5) {
            boolean aBoolean1162 = false;
            if (opcode == 5 && aBoolean1162) {
                skipReadModelIds(stream);
            }
            int i_73_ = stream.readUnsignedByte();
            setModelIds(new int[i_73_][]);
            aByteArray3899 = new byte[i_73_];
            for (int i_74_ = 0; i_74_ < i_73_; i_74_++) {
                aByteArray3899[i_74_] = (byte) stream.readByte();
                int i_75_ = stream.readUnsignedByte();
                getModelIds()[i_74_] = new int[i_75_];
                for (int i_76_ = 0; i_75_ > i_76_; i_76_++) {
                    getModelIds()[i_74_][i_76_] = stream.readUnsignedShort();
                }
            }
            if (opcode == 5 && !aBoolean1162) {
                skipReadModelIds(stream);
            }
        } else if (opcode == 2) {
            setName(stream.readString());
        } else if (opcode == 14) {
            setSizeX(stream.readUnsignedByte());
        } else if (opcode == 15) {
            // 15
            setSizeY(stream.readUnsignedByte());
        } else if (opcode == 17) { // nocliped
            setProjectileClipped(false);
            setClipType(0);
        } else if (opcode == 18) {
            setProjectileClipped(false);
        } else if (opcode == 19) {
            secondInt = stream.readUnsignedByte();
        } else if (opcode == 21) {
        } else if (opcode == 22) {
            aBoolean3867 = true;
        } else if (opcode == 23) {
            thirdInt = 1;
        } else if (opcode == 24) {
            anInt3876 = stream.readUnsignedShort();
        } else if (opcode == 27) {
            // diff between 2
            // and 1
            setClipType(1);
        } else if (opcode == 28) {
            anInt3892 = (stream.readUnsignedByte() << 2);
        } else if (opcode == 29) {// 29
            anInt3878 = stream.readByte();
        } else if (opcode == 39) {
            // 39
            anInt3840 = (stream.readByte() * 5);
        } else if (opcode >= 30 && opcode < 35) {
            getOptions()[-30 + opcode] = (stream.readString());
        } else if (opcode == 40) {
            int i_53_ = (stream.readUnsignedByte());
            setOriginalColors(new short[i_53_]);
            setModifiedColors(new short[i_53_]);
            for (int i_54_ = 0; i_53_ > i_54_; i_54_++) {
                getOriginalColors()[i_54_] = (short) (stream.readUnsignedShort());
                getModifiedColors()[i_54_] = (short) (stream.readUnsignedShort());
            }
        } else if (opcode == 41) {
            int i_71_ = (stream.readUnsignedByte());
            aShortArray3920 = new short[i_71_];
            aShortArray3919 = new short[i_71_];
            for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
                aShortArray3920[i_72_] = (short) (stream.readUnsignedShort());
                aShortArray3919[i_72_] = (short) (stream.readUnsignedShort());
            }
        } else if (opcode == 42) {
            int i_69_ = (stream.readUnsignedByte());
            aByteArray3858 = (new byte[i_69_]);
            for (int i_70_ = 0; i_70_ < i_69_; i_70_++) {
                aByteArray3858[i_70_] = (byte) (stream.readByte());
            }
        } else if (opcode == 62) {
            aBoolean3839 = true;
        } else if (opcode == 64) {
            aBoolean3872 = false;
        } else if (opcode == 65) {
            anInt3902 = stream.readUnsignedShort();
        } else if (opcode == 66) {
            anInt3841 = stream.readUnsignedShort();
        } else if (opcode == 67) {
            anInt3917 = stream.readUnsignedShort();
        } else if (opcode == 69) {
            cflag = stream.readUnsignedByte();
        } else if (opcode == 70) {
            anInt3883 = stream.readShort() << 2;
        } else if (opcode == 71) {
            anInt3889 = stream.readShort() << 2;
        } else if (opcode == 72) {
            anInt3915 = stream.readShort() << 2;
        } else if (opcode == 73) {
            secondBool = true;
        } else if (opcode == 74) {
            setIgnoreClipOnAlternativeRoute(true);
        } else if (opcode == 75) {
            anInt3855 = stream.readUnsignedByte();
        } else if (opcode == 77 || opcode == 92) {
            setConfigFileId(stream.readUnsignedShort());
            if (getConfigFileId() == 65535) {
                setConfigFileId(-1);
            }
            setConfigId(stream.readUnsignedShort());
            if (getConfigId() == 65535) {
                setConfigId(-1);
            }
            int i_66_ = -1;
            if (opcode == 92) {
                i_66_ = stream.readUnsignedShort();
            }
            int i_67_ = stream.readUnsignedByte();
            setToObjectIds(new int[i_67_ - -2]);
            for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
                getToObjectIds()[i_68_] = stream.readUnsignedShort();
            }
            getToObjectIds()[i_67_ + 1] = i_66_;
        } else if (opcode == 78) {
            anInt3860 = stream.readUnsignedShort();
            anInt3904 = stream.readUnsignedByte();
        } else if (opcode == 79) {
            anInt3900 = stream.readUnsignedShort();
            anInt3905 = stream.readUnsignedShort();
            anInt3904 = stream.readUnsignedByte();
            int i_64_ = stream.readUnsignedByte();
            anIntArray3859 = new int[i_64_];
            for (int i_65_ = 0; i_65_ < i_64_; i_65_++) {
                anIntArray3859[i_65_] = stream.readUnsignedShort();
            }
        } else if (opcode == 81) {
            anInt3882 = 256 * stream.readUnsignedByte();
        } else if (opcode == 82) {
            aBoolean3891 = true;
        } else if (opcode == 88) {
            aBoolean3853 = false;
        } else if (opcode == 89) {
            aBoolean3895 = false;
        } else if (opcode == 90) {
            aBoolean3870 = true;
        } else if (opcode == 91) {
            aBoolean3873 = true;
        } else if (opcode == 93) {
            anInt3882 = stream.readUnsignedShort();
        } else if (opcode == 94) {
        } else if (opcode == 95) {
            anInt3882 = stream.readShort();
        } else if (opcode == 96) {
            aBoolean3924 = true;
        } else if (opcode == 97) {
            aBoolean3866 = true;
        } else if (opcode == 98) {
            aBoolean3923 = true;
        } else if (opcode == 99) {
            anInt3857 = stream.readUnsignedByte();
            anInt3835 = stream.readUnsignedShort();
        } else if (opcode == 100) {
            anInt3844 = stream.readUnsignedByte();
            anInt3913 = stream.readUnsignedShort();
        } else if (opcode == 101) {
            anInt3850 = stream.readUnsignedByte();
        } else if (opcode == 102) {
            anInt3838 = stream.readUnsignedShort();
        } else if (opcode == 103) {
            thirdInt = 0;
        } else if (opcode == 104) {
            anInt3865 = stream.readUnsignedByte();
        } else if (opcode == 105) {
            aBoolean3906 = true;
        } else if (opcode == 106) {
            int i_55_ = stream.readUnsignedByte();
            anIntArray3869 = new int[i_55_];
            anIntArray3833 = new int[i_55_];
            for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
                anIntArray3833[i_56_] = stream.readUnsignedShort();
                int i_57_ = stream.readUnsignedByte();
                anIntArray3869[i_56_] = i_57_;
            }
        } else if (opcode == 107) {
            anInt3851 = stream.readUnsignedShort();
        } else if (opcode >= 150 && opcode < 155) {
            getOptions()[opcode + -150] = stream.readString();
        } else if (opcode == 160) {
            int i_62_ = stream.readUnsignedByte();
            anIntArray3908 = new int[i_62_];
            for (int i_63_ = 0; i_62_ > i_63_; i_63_++) {
                anIntArray3908[i_63_] = stream.readUnsignedShort();
            }
        } else if (opcode == 162) {
            anInt3882 = stream.readInt();
        } else if (opcode == 163) {
            aByte3847 = (byte) stream.readByte();
            aByte3849 = (byte) stream.readByte();
            aByte3837 = (byte) stream.readByte();
            aByte3914 = (byte) stream.readByte();
        } else if (opcode == 164) {
            anInt3834 = stream.readShort();
        } else if (opcode == 165) {
            anInt3875 = stream.readShort();
        } else if (opcode == 166) {
            anInt3877 = stream.readShort();
        } else if (opcode == 167) {
            anInt3921 = stream.readUnsignedShort();
        } else if (opcode == 168) {
            aBoolean3894 = true;
        } else if (opcode == 169) {
            aBoolean3845 = true;
        } else if (opcode == 170) {
            stream.readUnsignedSmart();
        } else if (opcode == 171) {
            stream.readUnsignedSmart();
        } else if (opcode == 173) {
            stream.readUnsignedShort();
            stream.readUnsignedShort();
        } else if (opcode == 177) {
        } else if (opcode == 178) {
            stream.readUnsignedByte();
        } else if (opcode == 189) {
        } else if (opcode == 249) {
            int length = stream.readUnsignedByte();
            if (parameters == null) {
                parameters = new HashMap<>(length);
            }
            for (int i_60_ = 0; i_60_ < length; i_60_++) {
                boolean bool = stream.readUnsignedByte() == 1;
                int i_61_ = stream.read24BitInt();
                if (!bool) {
                    parameters.put(i_61_, stream.readInt());
                } else {
                    parameters.put(i_61_, stream.readString());
                }

            }
        }
    }

    private void skipReadModelIds(InputStream stream) {
        int length = stream.readUnsignedByte();
        for (int index = 0; index < length; index++) {
            stream.skip(1);
            int length2 = stream.readUnsignedByte();
            for (int i = 0; i < length2; i++) {
                stream.readUnsignedShort();
            }
        }
    }

    public static void clearObjectDefinitions() {
        objectDefinitions.clear();
    }

    public String getFirstOption() {
        if (getOptions() == null || getOptions().length < 1) {
            return "";
        }
        return getOptions()[0];
    }

    public String getSecondOption() {
        if (getOptions() == null || getOptions().length < 2) {
            return "";
        }
        return getOptions()[1];
    }

    public String getOption(int option) {
        if (getOptions() == null || getOptions().length < option || option == 0) {
            return "";
        }
        return getOptions()[option - 1];
    }

    public String getThirdOption() {
        if (getOptions() == null || getOptions().length < 3) {
            return "";
        }
        return getOptions()[2];
    }

    public boolean containsOption(int i, String option) {
        if (getOptions() == null || getOptions().length <= i || getOptions()[i] == null) {
            return false;
        }
        return getOptions()[i].equals(option);
    }

    public boolean containsOption(String o) {
        if (getOptions() == null) {
            return false;
        }
        for (String option : getOptions()) {
            if (option == null) {
                continue;
            }
            if (option.equalsIgnoreCase(o)) {
                return true;
            }
        }
        return false;
    }

    public int getAccessBlockFlag() {
        return cflag;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public String[] getOptions() {
        return this.options;
    }

    public int[][] getModelIds() {
        return this.modelIds;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getConfigFileId() {
        return this.configFileId;
    }

    public boolean isProjectileClipped() {
        return this.projectileClipped;
    }

    public boolean isIgnoreClipOnAlternativeRoute() {
        return this.ignoreClipOnAlternativeRoute;
    }

    public int getClipType() {
        return this.clipType;
    }

    public int getConfigId() {
        return this.configId;
    }

    public short[] getOriginalColors() {
        return this.originalColors;
    }

    public int[] getToObjectIds() {
        return this.toObjectIds;
    }

    public short[] getModifiedColors() {
        return this.modifiedColors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setModelIds(int[][] modelIds) {
        this.modelIds = modelIds;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setConfigFileId(int configFileId) {
        this.configFileId = configFileId;
    }

    public void setProjectileClipped(boolean projectileClipped) {
        this.projectileClipped = projectileClipped;
    }

    public void setIgnoreClipOnAlternativeRoute(boolean ignoreClipOnAlternativeRoute) {
        this.ignoreClipOnAlternativeRoute = ignoreClipOnAlternativeRoute;
    }

    public void setClipType(int clipType) {
        this.clipType = clipType;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public void setOriginalColors(short[] originalColors) {
        this.originalColors = originalColors;
    }

    public void setToObjectIds(int[] toObjectIds) {
        this.toObjectIds = toObjectIds;
    }

    public void setModifiedColors(short[] modifiedColors) {
        this.modifiedColors = modifiedColors;
    }
}
