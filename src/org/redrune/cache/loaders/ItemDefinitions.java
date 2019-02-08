package org.redrune.cache.loaders;

import com.alex.io.InputStream;
import com.alex.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.Cache;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.constants.EquipmentConstants;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.functions.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class ItemDefinitions {
	
	private static final ItemDefinitions[] itemsDefinitions;
	
	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Misc.getItemDefinitionsSize()];
	}
	
	@Getter
	@Setter
	private int id;
	
	@Getter
	@Setter
	private int modelId;
	
	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private String[] inventoryOptions;
	
	@Getter
	@Setter
	private int[] originalModelColors;
	
	@Getter
	@Setter
	private int[] modifiedModelColors;
	
	@Getter
	@Setter
	private short[] originalTextureColors;
	
	@Getter
	@Setter
	private boolean loaded;
	
	@Getter
	@Setter
	private int modelZoom;
	
	@Getter
	@Setter
	private int modelRotation1;
	
	@Getter
	@Setter
	private int modelRotation2;
	
	@Getter
	@Setter
	private int modelOffset1;
	
	@Getter
	@Setter
	private int modelOffset2;
	
	@Getter
	@Setter
	private int stackable;
	
	@Setter
	private int value;
	
	@Getter
	@Setter
	private boolean membersOnly;
	
	@Getter
	@Setter
	private int maleEquip1;
	
	@Getter
	@Setter
	private int femaleEquip1;
	
	@Getter
	@Setter
	private int maleEquip2;
	
	@Getter
	@Setter
	private int femaleEquip2;
	
	@Getter
	@Setter
	private String[] groundOptions;
	
	@Getter
	@Setter
	private short[] modifiedTextureColors;
	
	@Getter
	@Setter
	private byte[] recolourPallete;
	
	@Getter
	@Setter
	private int[] unknownArray2;
	
	@Getter
	@Setter
	private int maleEquipModelId3;
	
	@Getter
	@Setter
	private int femaleEquipModelId3;
	
	@Getter
	@Setter
	private int certId;
	
	@Getter
	@Setter
	private int certTemplateId;
	
	@Getter
	@Setter
	private int[] stackIds;
	
	@Getter
	@Setter
	private int[] stackAmounts;
	
	@Getter
	@Setter
	private int modelShadowing;
	
	@Getter
	@Setter
	private int teamId;
	
	@Getter
	@Setter
	private int lendId;
	
	@Getter
	@Setter
	private int lendTemplateId;
	
	@Getter
	@Setter
	private int maleDialogueModel;
	
	@Getter
	@Setter
	private int femaleDialogueModel;
	
	@Getter
	@Setter
	private int maleDialogueHat;
	
	@Getter
	@Setter
	private int femaleDialogueHat;
	
	@Getter
	@Setter
	private int rotationZoom;
	
	@Getter
	@Setter
	private int dummyItem;
	
	@Getter
	@Setter
	private int modelVerticesX;
	
	@Getter
	@Setter
	private int modelVerticesY;
	
	@Getter
	@Setter
	private int modelVerticesZ;
	
	@Getter
	@Setter
	private int modelLighting;
	
	private int unknownInt11;
	
	private int unknownInt12;
	
	private int unknownInt13;
	
	private int unknownInt14;
	
	private int unknownInt15;
	
	private int unknownInt16;
	
	private int unknownInt17;
	
	private int unknownInt18;
	
	private int unknownInt19;
	
	private int unknownInt20;
	
	private int unknownInt21;
	
	private int unknownInt22;
	
	private int unknownInt23;
	
	private int unknownInt24;
	
	private int unknownInt25;
	
	@Getter
	private int equipSlot;
	
	@Getter
	private int equipType;
	
	private int unknownValue1;
	
	private int unknownValue2;
	
	private int unknownValue3;
	
	@Getter
	@Setter
	private boolean noted;
	
	@Getter
	@Setter
	private boolean lended;
	
	@Getter
	@Setter
	private boolean isTradeable;
	
	@Getter
	@Setter
	private boolean isExchangeable;
	
	@Getter
	@Setter
	private HashMap<Integer, Object> clientScriptData;
	
	@Getter
	@Setter
	private HashMap<Integer, Integer> itemRequirements;
	
	public ItemDefinitions(int id) {
		this.setId(id);
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions();
	}
	
	private void setDefaultsVariableValues() {
		setMaleEquip1(-1);
		unknownInt24 = -1;
		setMaleEquip2(-1);
		setRotationZoom(0);
		setLendTemplateId(-1);
		unknownInt25 = -1;
		unknownValue2 = -1;
		setMaleEquipModelId3(-1);
		setModelLighting(0);
		setModelShadowing(0);
		setFemaleDialogueModel(-1);
		setModelZoom(2000);
		unknownInt18 = -1;
		setTeamId(0);
		setMembersOnly(false);
		setModelVerticesY(128);
		setModelOffset1(0);
		setName("null");
		unknownInt23 = -1;
		setModelVerticesX(128);
		setMaleDialogueHat(-1);
		setFemaleDialogueHat(-1);
		unknownInt18 = -1;
		unknownInt20 = -1;
		unknownInt21 = -1;
		setModelRotation2(0);
		unknownInt14 = 0;
		unknownInt19 = -1;
		unknownInt22 = -1;
		unknownInt16 = 0;
		setFemaleEquip2(-1);
		setModelOffset2(0);
		unknownInt15 = 0;
		setMaleDialogueModel(-1);
		unknownValue3 = 0;
		setStackable(0);
		setModelVerticesZ(128);
		setFemaleEquipModelId3(-1);
		setCertTemplateId(-1);
		setCertId(-1);
		setValue(1);
		setDummyItem(0);
		unknownValue1 = -1;
		setModelRotation1(0);
		setLendId(-1);
		setFemaleEquip1(-1);
		unknownInt13 = 0;
		unknownInt17 = 0;
		unknownInt12 = 0;
		equipSlot = -1;
		equipType = -1;
	}
	
	private void setDefaultOptions() {
		setGroundOptions(new String[] { null, null, "take", null, null });
		setInventoryOptions(new String[] { null, null, null, null, "drop" });
	}
	
	private void loadItemDefinitions() {
		byte[] data = Cache.STORE.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			// System.out.println("Failed loading Item " + id+".");
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (getCertTemplateId() != -1) {
			toNote();
		}
		if (getLendTemplateId() != -1) {
			toLend();
		}
		if (unknownValue1 != -1) {
			toLendBind();
		}
		setLoaded(true);
	}
	
	public int getArchiveId() {
		return getId() >>> 8;
	}
	
	public int getFileId() {
		return 0xff & getId();
	}
	
	private final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}
	
	private void toNote() {
		// ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getItemDefinitions(getCertId());
		setMembersOnly(realItem.isMembersOnly());
		setValue(realItem.getValue());
		setName(realItem.getName());
		setStackable(1);
		setNoted(true);
	}
	
	private void toLend() {
		ItemDefinitions realItem = getItemDefinitions(getLendId());
		setOriginalModelColors(realItem.getOriginalModelColors());
		setMaleEquipModelId3(realItem.getMaleEquipModelId3());
		setFemaleEquipModelId3(realItem.getFemaleEquipModelId3());
		setTeamId(realItem.getTeamId());
		setValue(0);
		setMembersOnly(realItem.isMembersOnly());
		setName(realItem.getName());
		setInventoryOptions(new String[5]);
		setGroundOptions(realItem.getGroundOptions());
		if (realItem.getInventoryOptions() != null) {
			for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
				getInventoryOptions()[optionIndex] = realItem.getInventoryOptions()[optionIndex];
			}
		}
		getInventoryOptions()[4] = "Discard";
		setMaleEquip1(realItem.getMaleEquip1());
		setMaleEquip2(realItem.getMaleEquip2());
		setFemaleEquip1(realItem.getFemaleEquip1());
		setFemaleEquip2(realItem.getFemaleEquip2());
		setClientScriptData(realItem.getClientScriptData());
		equipSlot = realItem.equipSlot;
		equipType = realItem.equipType;
		setLended(true);
	}
	
	private void toLendBind() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(unknownValue2);
		setOriginalModelColors(realItem.getOriginalModelColors());
		setMaleEquipModelId3(realItem.getMaleEquipModelId3());
		setFemaleEquipModelId3(realItem.getFemaleEquipModelId3());
		setTeamId(realItem.getTeamId());
		setValue(0);
		setMembersOnly(realItem.isMembersOnly());
		setName(realItem.getName());
		setInventoryOptions(new String[5]);
		setGroundOptions(realItem.getGroundOptions());
		if (realItem.getInventoryOptions() != null) {
			for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
				getInventoryOptions()[optionIndex] = realItem.getInventoryOptions()[optionIndex];
			}
		}
		getInventoryOptions()[4] = "Discard";
		setMaleEquip1(realItem.getMaleEquip1());
		setMaleEquip2(realItem.getMaleEquip2());
		setFemaleEquip1(realItem.getFemaleEquip1());
		setFemaleEquip2(realItem.getFemaleEquip2());
		setClientScriptData(realItem.getClientScriptData());
		equipSlot = realItem.equipSlot;
		equipType = realItem.equipType;
		setLended(true);
	}
	
	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			setModelId(stream.readUnsignedShort());
		} else if (opcode == 2) {
			setName(stream.readString());
		} else if (opcode == 4) {
			setModelZoom(stream.readUnsignedShort());
		} else if (opcode == 5) {
			setModelRotation1(stream.readUnsignedShort());
		} else if (opcode == 6) {
			setModelRotation2(stream.readUnsignedShort());
		} else if (opcode == 7) {
			setModelOffset1(stream.readUnsignedShort());
			if (getModelOffset1() > 32767) {
				setModelOffset1(getModelOffset1() - 65536);
			}
		} else if (opcode == 8) {
			setModelOffset2(stream.readUnsignedShort());
			if (getModelOffset2() > 32767) {
				setModelOffset2(getModelOffset2() - 65536);
			}
		} else if (opcode == 11) {
			setStackable(1);
		} else if (opcode == 12) {
			setValue(stream.readInt());
		} else if (opcode == 13) {
			equipSlot = stream.readUnsignedByte();
		} else if (opcode == 14) {
			equipType = stream.readUnsignedByte();
		} else if (opcode == 15) {
			setTradeable(stream.readUnsignedByte() == 1);
		} else if (opcode == 17) {
			setExchangeable(stream.readUnsignedByte() == 1);
		} else if (opcode == 16) {
			setMembersOnly(true);
		} else if (opcode == 18) {
			unknownInt11 = stream.readUnsignedShort();
		} else if (opcode == 23) {
			setMaleEquip1(stream.readUnsignedShort());
		} else if (opcode == 24) {
			setMaleEquip2(stream.readUnsignedShort());
		} else if (opcode == 25) {
			setFemaleEquip1(stream.readUnsignedShort());
		} else if (opcode == 26) {
			setFemaleEquip2(stream.readUnsignedShort());
		} else if (opcode >= 30 && opcode < 35) {
			getGroundOptions()[opcode - 30] = stream.readString();
		} else if (opcode >= 35 && opcode < 40) {
			getInventoryOptions()[opcode - 35] = stream.readString();
		} else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			setOriginalModelColors(new int[length]);
			setModifiedModelColors(new int[length]);
			for (int index = 0; length > index; index++) {
				getOriginalModelColors()[index] = (short) stream.readUnsignedShort();
				getModifiedModelColors()[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			setOriginalTextureColors(new short[length]);
			setModifiedTextureColors(new short[length]);
			for (int index = 0; index < length; index++) {
				getOriginalTextureColors()[index] = (short) stream.readUnsignedShort();
				getModifiedTextureColors()[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			setRecolourPallete(new byte[length]);
			for (int index = 0; index < length; index++) {
				getRecolourPallete()[index] = (byte) stream.readByte();
			}
		} else if (opcode == 65) {
		} else if (opcode == 78) {
			setMaleEquipModelId3(stream.readUnsignedShort());
		} else if (opcode == 79) {
			setFemaleEquipModelId3(stream.readUnsignedShort());
		} else if (opcode == 90) {
			setMaleDialogueModel(stream.readUnsignedShort());
		} else if (opcode == 91) {
			setFemaleDialogueModel(stream.readUnsignedShort());
		} else if (opcode == 92) {
			setMaleDialogueHat(stream.readUnsignedShort());
		} else if (opcode == 93) {
			setFemaleDialogueHat(stream.readUnsignedShort());
		} else if (opcode == 95) {
			setRotationZoom(stream.readUnsignedShort());
		} else if (opcode == 96) {
			setDummyItem(stream.readUnsignedByte());
		} else if (opcode == 97) {
			setCertId(stream.readUnsignedShort());
		} else if (opcode == 98) {
			setCertTemplateId(stream.readUnsignedShort());
		} else if (opcode >= 100 && opcode < 110) {
			if (getStackIds() == null) {
				setStackIds(new int[10]);
				setStackAmounts(new int[10]);
			}
			getStackIds()[opcode - 100] = stream.readUnsignedShort();
			getStackAmounts()[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110) {
			setModelVerticesX(stream.readUnsignedShort());
		} else if (opcode == 111) {
			setModelVerticesY(stream.readUnsignedShort());
		} else if (opcode == 112) {
			setModelVerticesZ(stream.readUnsignedShort());
		} else if (opcode == 113) {
			setModelLighting(stream.readByte());
		} else if (opcode == 114) {
			setModelShadowing(stream.readByte() * 5);
		} else if (opcode == 115) {
			setTeamId(stream.readUnsignedByte());
		} else if (opcode == 121) {
			setLendId(stream.readUnsignedShort());
		} else if (opcode == 122) {
			setLendTemplateId(stream.readUnsignedShort());
		} else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 2;
			unknownInt13 = stream.readByte() << 2;
			unknownInt14 = stream.readByte() << 2;
		} else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 2;
			unknownInt16 = stream.readByte() << 2;
			unknownInt17 = stream.readByte() << 2;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			unknownInt24 = stream.readUnsignedByte();
			unknownInt25 = stream.readUnsignedShort();
		} else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			setUnknownArray2(new int[length]);
			for (int index = 0; index < length; index++) {
				getUnknownArray2()[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 134) {
			unknownValue3 = stream.readUnsignedByte();
		} else if (opcode == 139) {
			unknownValue2 = stream.readUnsignedShort();
		} else if (opcode == 140) {
			unknownValue1 = stream.readUnsignedShort();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (getClientScriptData() == null) {
				setClientScriptData(new HashMap<Integer, Object>(length));
			}
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				getClientScriptData().put(key, value);
			}
		} else {
			//throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + name);
		}
	}
	
	public static final ItemDefinitions getItemDefinitions(int itemId) {
		if (itemId < 0 || itemId >= itemsDefinitions.length) {
			itemId = 0;
		}
		ItemDefinitions def = itemsDefinitions[itemId];
		if (def == null) {
			itemsDefinitions[itemId] = def = new ItemDefinitions(itemId);
		}
		return def;
		
	}
	
	public static final void clearItemsDefinitions() {
		for (int i = 0; i < itemsDefinitions.length; i++) {
			itemsDefinitions[i] = null;
		}
	}
	
	public static boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static ItemDefinitions forName(String name) {
		for (ItemDefinitions definition : itemsDefinitions) {
			if (definition.getName().equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
	}
	
	public int getValue() {
		return value <= 0 ? 1 : value;
	}
	
	public boolean isDestroyItem() {
		if (getInventoryOptions() == null) {
			return false;
		}
		for (String option : getInventoryOptions()) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("destroy")) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isWearItem() {
		if (getInventoryOptions() == null) {
			return false;
		}
		for (String option : getInventoryOptions()) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear") || option.equalsIgnoreCase("equip")) {
				return equipSlot != -1;
			}
		}
		return false;
	}
	
	public boolean isWearItem(boolean male) {
		if (getInventoryOptions() == null) {
			return false;
		}
		if (EquipmentConstants.getItemSlot(getId()) != EquipmentConstants.SLOT_RING && EquipmentConstants.getItemSlot(getId()) != EquipmentConstants.SLOT_ARROWS && EquipmentConstants.getItemSlot(getId()) != EquipmentConstants.SLOT_AURA && (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1)) {
			return false;
		}
		for (String option : getInventoryOptions()) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear") || option.equalsIgnoreCase("equip")) {
				if (equipSlot != -1) {
					return true;
				}
				return true;
			}
		}
		return false;
	}
	
	public int getMaleWornModelId1() {
		return getMaleEquip1();
	}
	
	public int getFemaleWornModelId1() {
		return getFemaleEquip1();
	}
	
	public boolean hasSpecialBar() {
		if (getClientScriptData() == null) {
			return false;
		}
		Object specialBar = getClientScriptData().get(686);
		if (specialBar != null && specialBar instanceof Integer) {
			return (Integer) specialBar == 1;
		}
		return false;
	}

	/*
	 * public HashMap<Integer, Integer> getWearingSkillRequiriments() { if
	 * (clientScriptData == null) return null; HashMap<Integer, Integer> skills
	 * = new HashMap<Integer, Integer>(); int nextLevel = -1; int nextSkill =
	 * -1; for (int key : clientScriptData.keySet()) { Object value =
	 * clientScriptData.get(key); if (value instanceof String) continue; if(key
	 * == 277) { skills.put((Integer) value, id == 19709 ? 120 : 99); }else if
	 * (key == 23 && id == 15241) { skills.put(4, (Integer) value);
	 * skills.put(11, 61); } else if (key >= 749 && key < 797) { if (key % 2 ==
	 * 0) nextLevel = (Integer) value; else nextSkill = (Integer) value; if
	 * (nextLevel != -1 && nextSkill != -1) { skills.put(nextSkill, nextLevel);
	 * nextLevel = -1; nextSkill = -1; } }
	 *
	 * } return skills; }
	 */
	
	public int getRenderAnimId() {
		if (getClientScriptData() == null) {
			return 1426;
		}
		if (getId() == 20821) {
			return 2122;
		}
		Object animId = getClientScriptData().get(644);
		if (animId != null && animId instanceof Integer) {
			return (Integer) animId;
		}
		return 1426;
	}
	
	public int getQuestId() {
		if (getClientScriptData() == null) {
			return -1;
		}
		Object questId = getClientScriptData().get(861);
		if (questId != null && questId instanceof Integer) {
			return (Integer) questId;
		}
		return -1;
	}
	
	public List<Item> getCreateItemRequirements(boolean infusingScroll) {
		if (getClientScriptData() == null) {
			return null;
		}
		List<Item> items = new ArrayList<Item>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : getClientScriptData().keySet()) {
			Object value = getClientScriptData().get(key);
			if (value instanceof String) {
				continue;
			}
			if (key >= 536 && key <= 770) {
				if (key % 2 == 0) {
					requiredId = (Integer) value;
				} else {
					requiredAmount = (Integer) value;
				}
				if (requiredId != -1 && requiredAmount != -1) {
					if (infusingScroll) {
						requiredId = getId();
						requiredAmount = 1;
					}
					if (items.size() == 0 && !infusingScroll) {
						items.add(new Item(requiredAmount, 1));
					} else {
						items.add(new Item(requiredId, requiredAmount));
					}
					requiredId = -1;
					requiredAmount = -1;
					if (infusingScroll) {
						break;
					}
				}
			}
		}
		return items;
	}
	
	public HashMap<Integer, Integer> getWearingRequirements() {
		HashMap<Integer, Integer> skills = new HashMap<>();
		if (clientScriptData == null) {
			return skills;
		}
		int nextLevel = -1;
		int nextSkill = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String) {
				continue;
			}
			if (key >= 749 && key < 797) {
				if (key % 2 == 0) {
					nextLevel = (Integer) value;
				} else {
					nextSkill = (Integer) value;
				}
				if (nextLevel != -1 && nextSkill != -1) {
					if (nextSkill >= SkillConstants.SKILL_NAME.length) {
						skills.put(nextLevel, nextSkill);
					} else {
						skills.put(nextSkill, nextLevel);
					}
					nextLevel = -1;
					nextSkill = -1;
				}
			}
		}
		return skills;
	}
	
	public HashMap<Integer, Integer> getCreateItemRequirements() {
		if (getClientScriptData() == null) {
			return null;
		}
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : getClientScriptData().keySet()) {
			Object value = getClientScriptData().get(key);
			if (value instanceof String) {
				continue;
			}
			if (key >= 538 && key <= 770) {
				if (key % 2 == 0) {
					requiredId = (Integer) value;
				} else {
					requiredAmount = (Integer) value;
				}
				if (requiredId != -1 && requiredAmount != -1) {
					items.put(requiredAmount, requiredId);
					requiredId = -1;
					requiredAmount = -1;
				}
			}
		}
		return items;
	}
	
	public HashMap<Integer, Integer> getWearingSkillRequirements() {
		if (getClientScriptData() == null) {
			return null;
		}
		if (getItemRequirements() == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) getClientScriptData().get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) getClientScriptData().get(750 + (i * 2));
					if (level != null) {
						skills.put(skill, level);
					}
				}
			}
			Integer maxedSkill = (Integer) getClientScriptData().get(277);
			if (maxedSkill != null) {
				skills.put(maxedSkill, getId() == 19709 ? 120 : 99);
			}
			setItemRequirements(skills);
			if (getId() == 7462) {
				getItemRequirements().put(SkillConstants.DEFENCE, 40);
			} else if (getName().equals("Dragon defender")) {
				getItemRequirements().put(SkillConstants.ATTACK, 60);
				getItemRequirements().put(SkillConstants.DEFENCE, 60);
			}
		}
		
		return getItemRequirements();
	}
	
	public int getFemaleWornModelId2() {
		return getFemaleEquip2();
	}
	
	public int getMaleWornModelId2() {
		return getMaleEquip2();
	}
	
	public boolean isStackable() {
		return getStackable() == 1;
	}
	
	public int getStageOnDeath() {
		if (getClientScriptData() == null) {
			return 0;
		}
		Object protectedOnDeath = getClientScriptData().get(1397);
		if (protectedOnDeath != null && protectedOnDeath instanceof Integer) {
			return (Integer) protectedOnDeath;
		}
		return 0;
	}
	
	public int getAttackSpeed() {
		if (getId() >= 24455 && getId() <= 24457) {
			return 6;
		}
		if (getClientScriptData() == null) {
			return 4;
		}
		Object attackSpeed = getClientScriptData().get(14);
		if (attackSpeed != null && attackSpeed instanceof Integer) {
			return (int) attackSpeed;
		}
		return 4;
	}
	
	public int getStabAttack() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(0);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getSlashAttack() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(1);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getCrushAttack() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(2);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getMagicAttack() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(3);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getRangeAttack() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(4);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getStabDef() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(5);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getSlashDef() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(6);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getCrushDef() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(7);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getMagicDef() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(8);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getRangeDef() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(9);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getSummoningDef() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(417);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getAbsorveMeleeBonus() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(967);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getAbsorveMageBonus() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(969);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getAbsorveRangeBonus() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(968);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getStrengthBonus() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(641);
		if (value != null && value instanceof Integer) {
			return (int) value / 10;
		}
		return 0;
	}
	
	public int getRangedStrBonus() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(643);
		if (value != null && value instanceof Integer) {
			return (int) value / 10;
		}
		return 0;
	}
	
	public int getMagicDamage() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(685);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getPrayerBonus() {
		if (getId() > 25439 || getClientScriptData() == null) {
			return 0;
		}
		Object value = getClientScriptData().get(11);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	/**
	 * Checks if the item has the selected inventory option
	 *
	 * @param option
	 * 		The option to look for
	 */
	public boolean hasOption(String option) {
		for (String inventoryOption : getInventoryOptions()) {
			if (inventoryOption != null && inventoryOption.equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}
	
	public String getInventoryOption(int option) {
		if (getInventoryOptions() == null || getInventoryOptions().length < option || option == 0) {
			return "";
		}
		return getInventoryOptions()[option - 1];
	}
	
}