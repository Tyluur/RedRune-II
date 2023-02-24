package org.redrune.game.entity.actor.player.data;

import com.alex.io.OutputStream;
import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.cache.loaders.ItemEquipIds;
import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.game.global.World;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.functions.Misc;

import java.io.Serializable;
import java.util.Arrays;

public class PlayerAppearance implements Serializable {

    private static final long serialVersionUID = 7655608569741626586L;

    private int title;

    private int[] looks;

    private byte[] colors;

    private boolean male;

    private transient int renderEmote;

    private transient boolean glowRed;

    private transient byte[] appearanceData;

    private transient byte[] md5Hash;

    private transient short transformedNpcId;

    private transient boolean hidePlayer;

    private transient Player player;

    public PlayerAppearance() {
        male = true;
        renderEmote = -1;
        title = -1;
        resetAppearance();
    }

    public void resetAppearance() {
        looks = new int[7];
        colors = new byte[10];
        male();
    }

    public void male() {
        looks[0] = 3; // Hair
        looks[1] = 14; // Beard
        looks[2] = 18; // Torso
        looks[3] = 26; // Arms
        looks[4] = 34; // Bracelets
        looks[5] = 38; // Legs
        looks[6] = 42; // Shoes~

        colors[2] = 2;
        colors[1] = 7;
        colors[0] = 3;
        male = true;
    }

    public void setGlowRed(boolean glowRed) {
        this.glowRed = glowRed;
        generateAppearanceData();
    }

    public void generateAppearanceData() {
        OutputStream stream = new OutputStream();
        int flag = 0;
        if (!male) {
            flag |= 0x1;
        }
        if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).isABoolean3190()) {
            flag |= 0x2;
        }
        stream.writeByte(flag);
        stream.writeByte(title); // mobi arms titles
        stream.writeByte(player.getAttributes().hasSkull() ? player.getAttributes().getSkullId() : -1); // pk
        // icon
        stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
        stream.writeByte(hidePlayer ? 1 : 0);
        // npc
        if (transformedNpcId >= 0) {
            stream.writeShort(-1); // 65535 tells it a npc
            stream.writeShort(transformedNpcId);
            stream.writeByte(0);
        } else {
            for (int index = 0; index < 4; index++) {
                Item item = player.getEquipment().getItems().get(index);
                if (glowRed) {
                    if (index == 0) {
                        stream.writeShort(32768 + ItemEquipIds.getEquipId(2910));
                        continue;
                    }
                    if (index == 1) {
                        stream.writeShort(32768 + ItemEquipIds.getEquipId(14641));
                        continue;
                    }
                }
                if (item == null) {
                    stream.writeByte(0);
                } else {
                    stream.writeShort(32768 + ItemEquipIds.getEquipId(item.getId()));
                }
            }
            Item item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_CHEST);
            stream.writeShort(item == null ? 0x100 + looks[2] : 32768 + ItemEquipIds.getEquipId(item.getId()));
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_SHIELD);
            if (item == null) {
                stream.writeByte(0);
            } else {
                stream.writeShort(32768 + ItemEquipIds.getEquipId(item.getId()));
            }
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_CHEST);
            if (item == null || !EquipmentConstants.isFullBody(item)) {
                stream.writeShort(0x100 + looks[3]);
            } else {
                stream.writeByte(0);
            }
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_LEGS);
            stream.writeShort(glowRed ? 32768 + ItemEquipIds.getEquipId(2908) : item == null ? 0x100 + looks[5] : 32768 + ItemEquipIds.getEquipId(item.getId()));
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_HAT);
            if (!glowRed && (item == null || (!EquipmentConstants.isFullMask(item) && !EquipmentConstants.isFullHat(item)))) {
                stream.writeShort(0x100 + looks[0]);
            } else {
                stream.writeByte(0);
            }
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_HANDS);
            stream.writeShort(glowRed ? 32768 + ItemEquipIds.getEquipId(2912) : item == null ? 0x100 + looks[4] : 32768 + ItemEquipIds.getEquipId(item.getId()));
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_FEET);
            stream.writeShort(glowRed ? 32768 + ItemEquipIds.getEquipId(2904) : item == null ? 0x100 + looks[6] : 32768 + ItemEquipIds.getEquipId(item.getId()));
            // tits for female, bear for male
            item = player.getEquipment().getItems().get(male ? EquipmentConstants.SLOT_HAT : EquipmentConstants.SLOT_CHEST);
            if (item == null || !EquipmentConstants.isFullMask(item)) {
                stream.writeShort(0x100 + looks[1]);
            } else {
                stream.writeByte(0);
            }
            item = player.getEquipment().getItems().get(EquipmentConstants.SLOT_AURA);
            if (item == null) {
                stream.writeByte(0);
            } else {
                stream.writeShort(32768 + ItemEquipIds.getEquipId(item.getId()));
            }
            int pos = stream.getOffset();
            stream.writeShort(0);
            int hash = 0;
            int slotFlag = -1;
            for (int slotId = 0; slotId < player.getEquipment().getItems().getSize(); slotId++) {
                if (EquipmentConstants.DISABLED_SLOTS[slotId] != 0) {
                    continue;
                }
                slotFlag++;
                if (slotId == EquipmentConstants.SLOT_HAT) {
                    int hatId = player.getEquipment().getHatId();
                    if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
                        ItemDefinitions defs = ItemDefinitions.getItemDefinitions(hatId - 1);
                        if ((hatId == 20768 && Arrays.equals(player.getAttributes().getMaxedCapeCustomized(), defs.getOriginalModelColors()) || ((hatId == 20770 || hatId == 20772) && Arrays.equals(player.getAttributes().getCompletionistCapeCustomized(), defs.getOriginalModelColors())))) {
                            continue;
                        }
                        hash |= 1 << slotFlag;
                        stream.writeByte(0x4); // modify 4 model colors
                        int[] hat = hatId == 20768 ? player.getAttributes().getMaxedCapeCustomized() : player.getAttributes().getCompletionistCapeCustomized();
                        int slots = 1 << 4 | 2 << 8 | 3 << 12;
                        stream.writeShort(slots);
                        for (int i = 0; i < 4; i++) {
                            stream.writeShort(hat[i]);
                        }
                    }
                } else if (slotId == EquipmentConstants.SLOT_CAPE) {
                    int capeId = player.getEquipment().getCapeId();
                    if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
                        ItemDefinitions defs = ItemDefinitions.getItemDefinitions(capeId);
                        if ((capeId == 20767 && Arrays.equals(player.getAttributes().getMaxedCapeCustomized(), defs.getOriginalModelColors()) || ((capeId == 20769 || capeId == 20771) && Arrays.equals(player.getAttributes().getCompletionistCapeCustomized(), defs.getOriginalModelColors())))) {
                            continue;
                        }
                        hash |= 1 << slotFlag;
                        stream.writeByte(0x4); // modify 4 model colors
                        int[] cape = capeId == 20767 ? player.getAttributes().getMaxedCapeCustomized() : player.getAttributes().getCompletionistCapeCustomized();
                        int slots = 1 << 4 | 2 << 8 | 3 << 12;
                        stream.writeShort(slots);
                        for (int i = 0; i < 4; i++) {
                            stream.writeShort(cape[i]);
                        }
                    }
                } else if (slotId == EquipmentConstants.SLOT_AURA) {
                    int auraId = player.getEquipment().getAuraId();
                    if (auraId == -1 || !player.getAuraManager().isActivated()) {
                        continue;
                    }
                    ItemDefinitions auraDefs = ItemDefinitions.getItemDefinitions(auraId);
                    if (auraDefs.getMaleWornModelId1() == -1 || auraDefs.getFemaleWornModelId1() == -1) {
                        continue;
                    }
                    hash |= 1 << slotFlag;
                    stream.writeByte(0x1); // modify model ids
                    int modelId = player.getAuraManager().getAuraModelId();
                    stream.writeBigSmart(modelId); // male modelid1
                    stream.writeBigSmart(modelId); // female modelid1
                    if (auraDefs.getMaleWornModelId2() != -1 || auraDefs.getFemaleWornModelId2() != -1) {
                        int modelId2 = player.getAuraManager().getAuraModelId();
                        stream.writeBigSmart(modelId2);
                        stream.writeBigSmart(modelId2);
                    }
                }
            }
            int pos2 = stream.getOffset();
            stream.setOffset(pos);
            stream.writeShort(hash);
            stream.setOffset(pos2);
        }

        for (int index = 0; index < colors.length; index++) {
            stream.writeByte(colors[index]);
        }

        stream.writeShort(getRenderEmote());
        stream.writeString(player.getDisplayName());
        boolean pvpArea = World.isPvpArea(player);
        stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
        stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
        stream.writeByte(-1); // higher level acc name appears in front :P
        stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
        // need to send more
        // data
        if (transformedNpcId >= 0) {
            NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
            stream.writeShort(defs.anInt876);
            stream.writeShort(defs.anInt842);
            stream.writeShort(defs.anInt884);
            stream.writeShort(defs.anInt875);
            stream.writeByte(defs.anInt875);
        }

        // done separated for safe because of synchronization
        byte[] appearanceData = new byte[stream.getOffset()];
        System.arraycopy(stream.getBuffer(), 0, appearanceData, 0, appearanceData.length);
        byte[] md5Hash = Misc.encryptUsingMD5(appearanceData);
        this.appearanceData = appearanceData;
        this.md5Hash = md5Hash;
    }

    public int getRenderEmote() {
        if (renderEmote >= 0) {
            return renderEmote;
        }
        if (transformedNpcId >= 0) {
            return NPCDefinitions.getNPCDefinitions(transformedNpcId).getRenderEmote();
        }
        return player.getEquipment().getWeaponRenderEmote();
    }

    public void setRenderEmote(int id) {
        this.renderEmote = id;
        generateAppearanceData();
    }

    public void setPlayer(Player player) {
        this.player = player;
        transformedNpcId = -1;
        renderEmote = -1;
        if (looks == null) {
            resetAppearance();
        }
    }

    public void transformIntoNPC(int id) {
        transformedNpcId = (short) id;
        generateAppearanceData();
    }

    public void switchHidden() {
        hidePlayer = !hidePlayer;
        generateAppearanceData();
    }

    public boolean isHidden() {
        return hidePlayer;
    }

    public int getSize() {
        if (transformedNpcId >= 0) {
            return NPCDefinitions.getNPCDefinitions(transformedNpcId).getSize();
        }
        return 1;
    }

    public void female() {
        looks[0] = 48; // Hair
        looks[1] = 57; // Beard
        looks[2] = 57; // Torso
        looks[3] = 65; // Arms
        looks[4] = 68; // Bracelets
        looks[5] = 77; // Legs
        looks[6] = 80; // Shoes

        colors[2] = 16;
        colors[1] = 16;
        colors[0] = 3;
        male = false;
    }

    public void setLook(int index, int value) {
        looks[index] = value;
    }

    public void setColor(int index, int value) {
        colors[index] = (byte) value;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setHairStyle(int value) {
        looks[0] = value;
    }

    public int getTopStyle() {
        return looks[2];
    }

    public void setTopStyle(int value) {
        looks[2] = value;
    }

    public void setArmsStyle(int value) {
        looks[3] = value;
    }

    public void setWristsStyle(int value) {
        looks[4] = value;
    }

    public void setLegsStyle(int value) {
        looks[5] = value;
    }

    public void setBeardStyle(int value) {
        looks[1] = value;
    }

    public int getSkinColor() {
        return colors[4];
    }

    public void setSkinColor(int color) {
        colors[4] = (byte) color;
    }

    public void setHairColor(int color) {
        colors[0] = (byte) color;
    }

    public void setTopColor(int color) {
        colors[1] = (byte) color;
    }

    public void setLegsColor(int color) {
        colors[2] = (byte) color;
    }

    public void setTitle(int title) {
        this.title = title;
        generateAppearanceData();
    }

    public void setLooks(short[] look) {
        for (byte i = 0; i < this.looks.length; i = (byte) (i + 1)) {
            if (look[i] != -1) {
                this.looks[i] = look[i];
            }
        }
    }

    public void copyColors(short[] colors) {
        for (byte i = 0; i < this.colors.length; i = (byte) (i + 1)) {
            if (colors[i] != -1) {
                this.colors[i] = (byte) colors[i];
            }
        }
    }

    public void setBodyStyle(int index, int value) {
        this.looks[index] = value;
    }

    public void setBodyColor(int index, short value) {
        this.looks[index] = value;
    }

    public void resetAppearence() {
        looks = new int[7];
        colors = new byte[10];
        male();
    }

    public boolean isMale() {
        return this.male;
    }

    public byte[] getAppearanceData() {
        return this.appearanceData;
    }

    public byte[] getMd5Hash() {
        return this.md5Hash;
    }
}