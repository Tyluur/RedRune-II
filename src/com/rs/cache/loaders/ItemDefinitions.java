package com.rs.cache.loaders;

import com.alex.utils.Constants;
import com.rs.cache.Cache;
import com.rs.game.entity.actor.player.data.PlayerSkills;
import com.rs.game.entity.item.Item;
import com.rs.networking.io.InputStream;
import com.rs.utility.Misc;
import com.rs.utility.constants.EquipmentConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class ItemDefinitions {
	
	public static final ItemDefinitions[] itemsDefinitions;
	
	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Misc.getItemDefinitionsSize()];
	}
	
	public int id;
	
	public int modelId;
	
	public String name;
	
	public String[] inventoryOptions;
	
	// model information
	public int[] originalModelColors;
	
	public int[] modifiedModelColors;
	
	public short[] originalTextureColors;
	
	private boolean loaded;
	
	// model size information
	private int modelZoom;
	
	private int modelRotation1;
	
	private int modelRotation2;
	
	private int modelOffset1;
	
	private int modelOffset2;
	
	// extra information
	private int stackable;
	
	private int value;
	
	private boolean membersOnly;
	
	// wearing model information
	private int maleEquip1;
	
	private int femaleEquip1;
	
	private int maleEquip2;
	
	private int femaleEquip2;
	
	// options
	private String[] groundOptions;
	
	private short[] modifiedTextureColors;
	
	private byte[] recolourPallete;
	
	private int[] unknownArray2;
	
	private int maleEquipModelId3;
	
	private int femaleEquipModelId3;
	
	private int certId;
	
	private int certTemplateId;
	
	private int[] stackIds;
	
	private int[] stackAmounts;
	
	private int modelShadowing;
	
	private int teamId;
	
	private int lendId;
	
	private int lendTemplateId;
	
	private int maleDialogueModel;
	
	private int femaleDialogueModel;
	
	private int maleDialogueHat;
	
	private int femaleDialogueHat;
	
	private int rotationZoom;
	
	private int dummyItem;
	
	private int modelVerticesX;
	
	private int modelVerticesY;
	
	private int modelVerticesZ;
	
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
	
	private int equipSlot;
	
	private int equipType;
	
	private int unknownValue1;
	
	private int unknownValue2;
	
	private int unknownValue3;
	
	// extra added
	private boolean noted;
	
	private boolean lended;
	
	private boolean isTradeable;
	
	private boolean isExchangeable;
	
	private HashMap<Integer, Object> clientScriptData;
	
	private HashMap<Integer, Integer> itemRequiriments;
	
	public ItemDefinitions(int id) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions();
	}
	
	private void setDefaultsVariableValues() {
		maleEquip1 = -1;
		unknownInt24 = -1;
		maleEquip2 = -1;
		rotationZoom = 0;
		lendTemplateId = -1;
		unknownInt25 = -1;
		unknownValue2 = -1;
		maleEquipModelId3 = -1;
		modelLighting = 0;
		modelShadowing = 0;
		femaleDialogueModel = -1;
		modelZoom = 2000;
		unknownInt18 = -1;
		teamId = 0;
		membersOnly = false;
		modelVerticesY = 128;
		modelOffset1 = 0;
		name = "null";
		unknownInt23 = -1;
		modelVerticesX = 128;
		maleDialogueHat = -1;
		femaleDialogueHat = -1;
		unknownInt18 = -1;
		unknownInt20 = -1;
		unknownInt21 = -1;
		modelRotation2 = 0;
		unknownInt14 = 0;
		unknownInt19 = -1;
		unknownInt22 = -1;
		unknownInt16 = 0;
		femaleEquip2 = -1;
		modelOffset2 = 0;
		unknownInt15 = 0;
		maleDialogueModel = -1;
		unknownValue3 = 0;
		stackable = 0;
		modelVerticesZ = 128;
		femaleEquipModelId3 = -1;
		certTemplateId = -1;
		certId = -1;
		value = 1;
		dummyItem = 0;
		unknownValue1 = -1;
		modelRotation1 = 0;
		lendId = -1;
		femaleEquip1 = -1;
		unknownInt13 = 0;
		unknownInt17 = 0;
		unknownInt12 = 0;
		equipSlot = -1;
		equipType = -1;
	}
	
	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}
	
	private final void loadItemDefinitions() {
		byte[] data = Cache.STORE.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			// System.out.println("Failed loading Item " + id+".");
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (certTemplateId != -1) {
			toNote();
		}
		if (lendTemplateId != -1) {
			toLend();
		}
		if (unknownValue1 != -1) {
			toLendBind();
		}
		loaded = true;
	}
	
	public int getArchiveId() {
		return id >>> 8;
	}
	
	public int getFileId() {
		return 0xff & id;
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
		ItemDefinitions realItem = getItemDefinitions(certId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
		noted = true;
	}
	
	private void toLend() {
		ItemDefinitions realItem = getItemDefinitions(lendId);
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null) {
			for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
			}
		}
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		clientScriptData = realItem.clientScriptData;
		equipSlot = realItem.equipSlot;
		equipType = realItem.equipType;
		lended = true;
	}
	
	private void toLendBind() {
		// ItemDefinitions lendItem; //lendTemplateId
		ItemDefinitions realItem = getItemDefinitions(unknownValue2);
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null) {
			for (int optionIndex = 0; optionIndex < 4; optionIndex++) {
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
			}
		}
		inventoryOptions[4] = "Discard";
		maleEquip1 = realItem.maleEquip1;
		maleEquip2 = realItem.maleEquip2;
		femaleEquip1 = realItem.femaleEquip1;
		femaleEquip2 = realItem.femaleEquip2;
		clientScriptData = realItem.clientScriptData;
		equipSlot = realItem.equipSlot;
		equipType = realItem.equipType;
		lended = true;
	}
	
	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			modelId = stream.readUnsignedShort();
		} else if (opcode == 2) {
			name = stream.readString();
		} else if (opcode == 4) {
			modelZoom = stream.readUnsignedShort();
		} else if (opcode == 5) {
			modelRotation1 = stream.readUnsignedShort();
		} else if (opcode == 6) {
			modelRotation2 = stream.readUnsignedShort();
		} else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767) {
				modelOffset1 -= 65536;
			}
		} else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767) {
				modelOffset2 -= 65536;
			}
		} else if (opcode == 11) {
			stackable = 1;
		} else if (opcode == 12) {
			value = stream.readInt();
		} else if (opcode == 13) {
			equipSlot = stream.readUnsignedByte();
		} else if (opcode == 14) {
			equipType = stream.readUnsignedByte();
		} else if (opcode == 15) {
			isTradeable = stream.readUnsignedByte() == 1;
		} else if (opcode == 17) {
			isExchangeable = stream.readUnsignedByte() == 1;
		} else if (opcode == 16) {
			membersOnly = true;
		} else if (opcode == 18) {
			unknownInt11 = stream.readUnsignedShort();
		} else if (opcode == 23) {
			maleEquip1 = stream.readUnsignedShort();
		} else if (opcode == 24) {
			maleEquip2 = stream.readUnsignedShort();
		} else if (opcode == 25) {
			femaleEquip1 = stream.readUnsignedShort();
		} else if (opcode == 26) {
			femaleEquip2 = stream.readUnsignedShort();
		} else if (opcode >= 30 && opcode < 35) {
			groundOptions[opcode - 30] = stream.readString();
		} else if (opcode >= 35 && opcode < 40) {
			inventoryOptions[opcode - 35] = stream.readString();
		} else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; length > index; index++) {
				originalModelColors[index] = (short) stream.readUnsignedShort();
				modifiedModelColors[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureColors = new short[length];
			modifiedTextureColors = new short[length];
			for (int index = 0; index < length; index++) {
				originalTextureColors[index] = (short) stream.readUnsignedShort();
				modifiedTextureColors[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			recolourPallete = new byte[length];
			for (int index = 0; index < length; index++) {
				recolourPallete[index] = (byte) stream.readByte();
			}
		} else if (opcode == 65) {
		} else if (opcode == 78) {
			maleEquipModelId3 = stream.readUnsignedShort();
		} else if (opcode == 79) {
			femaleEquipModelId3 = stream.readUnsignedShort();
		} else if (opcode == 90) {
			maleDialogueModel = stream.readUnsignedShort();
		} else if (opcode == 91) {
			femaleDialogueModel = stream.readUnsignedShort();
		} else if (opcode == 92) {
			maleDialogueHat = stream.readUnsignedShort();
		} else if (opcode == 93) {
			femaleDialogueHat = stream.readUnsignedShort();
		} else if (opcode == 95) {
			rotationZoom = stream.readUnsignedShort();
		} else if (opcode == 96) {
			dummyItem = stream.readUnsignedByte();
		} else if (opcode == 97) {
			certId = stream.readUnsignedShort();
		} else if (opcode == 98) {
			certTemplateId = stream.readUnsignedShort();
		} else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110) {
			modelVerticesX = stream.readUnsignedShort();
		} else if (opcode == 111) {
			modelVerticesY = stream.readUnsignedShort();
		} else if (opcode == 112) {
			modelVerticesZ = stream.readUnsignedShort();
		} else if (opcode == 113) {
			modelLighting = stream.readByte();
		} else if (opcode == 114) {
			modelShadowing = stream.readByte() * 5;
		} else if (opcode == 115) {
			teamId = stream.readUnsignedByte();
		} else if (opcode == 121) {
			lendId = stream.readUnsignedShort();
		} else if (opcode == 122) {
			lendTemplateId = stream.readUnsignedShort();
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
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++) {
				unknownArray2[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 134) {
			unknownValue3 = stream.readUnsignedByte();
		} else if (opcode == 139) {
			unknownValue2 = stream.readUnsignedShort();
		} else if (opcode == 140) {
			unknownValue1 = stream.readUnsignedShort();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (clientScriptData == null) {
				clientScriptData = new HashMap<Integer, Object>(length);
			}
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				clientScriptData.put(key, value);
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
			if (definition.name.equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public int getValue(Object... params) {
		return value <= 0 ? 1 : value;
	}

	/*
	 * public boolean isWearItem() { return equipSlot != -1; }
	 *
	 * public boolean isWearItem(boolean male) { if (equipSlot <
	 * Equipment.SLOT_RING && (male ? getMaleWornModelId1() == -1 :
	 * getFemaleWornModelId1() == -1)) { return false; } return equipSlot != -1;
	 * }
	 */
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean isDestroyItem() {
		if (inventoryOptions == null) {
			return false;
		}
		for (String option : inventoryOptions) {
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
		if (inventoryOptions == null) {
			return false;
		}
		for (String option : inventoryOptions) {
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
		if (inventoryOptions == null) {
			return false;
		}
		if (EquipmentConstants.getItemSlot(id) != EquipmentConstants.SLOT_RING && EquipmentConstants.getItemSlot(id) != EquipmentConstants.SLOT_ARROWS && EquipmentConstants.getItemSlot(id) != EquipmentConstants.SLOT_AURA && (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1)) {
			return false;
		}
		for (String option : inventoryOptions) {
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
		return maleEquip1;
	}
	
	public int getFemaleWornModelId1() {
		return femaleEquip1;
	}
	
	public boolean hasSpecialBar() {
		if (clientScriptData == null) {
			return false;
		}
		Object specialBar = clientScriptData.get(686);
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
		if (clientScriptData == null) {
			return 1426;
		}
		if (id == 20821) {
			return 2122;
		}
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer) {
			return (Integer) animId;
		}
		return 1426;
	}
	
	public int getQuestId() {
		if (clientScriptData == null) {
			return -1;
		}
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer) {
			return (Integer) questId;
		}
		return -1;
	}
	
	public List<Item> getCreateItemRequirements(boolean infusingScroll) {
		if (clientScriptData == null) {
			return null;
		}
		List<Item> items = new ArrayList<Item>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
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
	
	public int getId() {
		return id;
	}
	
	public HashMap<Integer, Integer> getCreateItemRequirements() {
		if (clientScriptData == null) {
			return null;
		}
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
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
	
	public HashMap<Integer, Object> getClientScriptData() {
		return clientScriptData;
	}
	
	public HashMap<Integer, Integer> getWearingSkillRequirements() {
		if (clientScriptData == null) {
			return null;
		}
		if (itemRequiriments == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) clientScriptData.get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) clientScriptData.get(750 + (i * 2));
					if (level != null) {
						skills.put(skill, level);
					}
				}
			}
			Integer maxedSkill = (Integer) clientScriptData.get(277);
			if (maxedSkill != null) {
				skills.put(maxedSkill, id == 19709 ? 120 : 99);
			}
			itemRequiriments = skills;
			if (id == 7462) {
				itemRequiriments.put(PlayerSkills.DEFENCE, 40);
			} else if (name.equals("Dragon defender")) {
				itemRequiriments.put(PlayerSkills.ATTACK, 60);
				itemRequiriments.put(PlayerSkills.DEFENCE, 60);
			}
		}
		
		return itemRequiriments;
	}
	
	public String getName() {
		return name;
	}
	
	public int getFemaleWornModelId2() {
		return femaleEquip2;
	}
	
	public int getMaleWornModelId2() {
		return maleEquip2;
	}
	
	public boolean isLended() {
		return lended;
	}
	
	public boolean isStackable() {
		return stackable == 1 || (id == 10943 || id == 10944);
	}
	
	public boolean isNoted() {
		return noted;
	}
	
	public boolean isTradeable() {
		return isTradeable;
	}
	
	public boolean isExchangeable() {
		return isExchangeable;
	}
	
	public int getLendId() {
		return lendId;
	}
	
	public int getCertId() {
		return certId;
	}
	
	public int getEquipSlot() {
		return equipSlot;
	}
	
	public int getEquipType() {
		return equipType;
	}
	
	public int getStageOnDeath() {
		if (clientScriptData == null) {
			return 0;
		}
		Object protectedOnDeath = clientScriptData.get(1397);
		if (protectedOnDeath != null && protectedOnDeath instanceof Integer) {
			return (Integer) protectedOnDeath;
		}
		return 0;
	}
	
	public int getAttackSpeed() {
		if (id >= 24455 && id <= 24457) {
			return 6;
		}
		if (clientScriptData == null) {
			return 4;
		}
		Object attackSpeed = clientScriptData.get(14);
		if (attackSpeed != null && attackSpeed instanceof Integer) {
			return (int) attackSpeed;
		}
		return 4;
	}
	
	public int getStabAttack() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(0);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getSlashAttack() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(1);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getCrushAttack() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(2);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getMagicAttack() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(3);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getRangeAttack() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(4);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getStabDef() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(5);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getSlashDef() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(6);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getCrushDef() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(7);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getMagicDef() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(8);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getRangeDef() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(9);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getSummoningDef() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(417);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getAbsorveMeleeBonus() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(967);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getAbsorveMageBonus() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(969);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getAbsorveRangeBonus() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(968);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getStrengthBonus() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(641);
		if (value != null && value instanceof Integer) {
			return (int) value / 10;
		}
		return 0;
	}
	
	public int getRangedStrBonus() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(643);
		if (value != null && value instanceof Integer) {
			return (int) value / 10;
		}
		return 0;
	}
	
	public int getMagicDamage() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(685);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
	
	public int getPrayerBonus() {
		if (id > 25439 || clientScriptData == null) {
			return 0;
		}
		Object value = clientScriptData.get(11);
		if (value != null && value instanceof Integer) {
			return (int) value;
		}
		return 0;
	}
}