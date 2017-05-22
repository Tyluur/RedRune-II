package org.redrune.cache.loaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.redrune.cache.Cache;
import org.redrune.cache.scripts.CacheFixer;
import org.redrune.rs2.node.entity.player.components.PlayerSkills;
import org.redrune.rs2.node.item.Item;
import org.redrune.utility.Misc;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.utils.Constants;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unused")
public final class ItemDefinitions {

	public static final ItemDefinitions[] itemsDefinitions;

	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Misc.getItemDefinitionsSize()];
	}

	public int id;
	private boolean loaded;
	
	@Getter
	@Setter
	private static int[] bodyData;

	public int modelId;
	public String name;

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
	public String[] inventoryOptions;

	private boolean showInGrandExchange;

	// model information
	public int[] originalModelColors;
	public int[] modifiedModelColors;
	public short[] originalTextureColors;
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

	public static ItemDefinitions getItemDefinition(Store cache, int itemId) {
		return new ItemDefinitions(itemId);
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

	public ItemDefinitions(int id) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions();
	}

	public static boolean isInteger(String i) {
		try {
			Integer.parseInt(i);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	public boolean isLoaded() {
		return loaded;
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

	private void toNote() {
		// ItemDefinitions noteItem; //certTemplateId
		ItemDefinitions realItem = getItemDefinitions(certId);
		membersOnly = realItem.membersOnly;
		value = realItem.value;
		name = realItem.name;
		stackable = 1;
		noted = true;
	}

	public static ItemDefinitions forName(String name) {
		for (ItemDefinitions definition : itemsDefinitions) {
			if (definition.name.equalsIgnoreCase(name)) {
				return definition;
			}
		}
		return null;
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

	public int getValue() {
		System.err.println("Values not setup");
//		CustomItemPrices prices = CustomItemPrices.getItemPrice(id);
//		if (prices != null) {
//			return prices.getPrice();
//		}
		return value <= 0 ? 1 : value;
	}

	public void setValue(int value) {
		this.value = value;
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

	public int getArchiveId() {
		return id >>> 8;
	}

	public int getFileId() {
		return 0xff & id;
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
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear")
					|| option.equalsIgnoreCase("equip")) {
				if (equipSlot != -1) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	public boolean isWearItem(boolean male) {
		if (inventoryOptions == null) {
			return false;
		}
		/**
		 * TODO
		 */
		System.err.println("todo isWearItem");
//		if (PlayerEquipment.getItemSlot(id) != PlayerEquipment.SLOT_RING && PlayerEquipment.getItemSlot(id) != PlayerEquipment.SLOT_ARROWS
//				&& PlayerEquipment.getItemSlot(id) != PlayerEquipment.SLOT_AURA
//				&& (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1)) {
//			return false;
//		}
		for (String option : inventoryOptions) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear")
					|| option.equalsIgnoreCase("equip")) {
				if (equipSlot != -1) {
					return true;
				}
				return true;
			}
		}
		return false;
	}

	/*
	 * public boolean isWearItem() { return equipSlot != -1; }
	 * 
	 * public boolean isWearItem(boolean male) { if (equipSlot <
	 * Equipment.SLOT_RING && (male ? getMaleWornModelId1() == -1 :
	 * getFemaleWornModelId1() == -1)) { return false; } return equipSlot != -1;
	 * }
	 */

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

	public int getRenderAnimId() {
		if (clientScriptData == null) {
			return 1426;
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

	public HashMap<Integer, Integer> getWearingSkillRequiriments() {
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

	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
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
		setShowInGrandExchange(false);
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
			setShowInGrandExchange(true);
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
			throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + name);
		}
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

	public String getName() {
		return name;
	}

	public int getFemaleWornModelId1() {
		return femaleEquip1;
	}

	public int getFemaleWornModelId2() {
		return femaleEquip2;
	}

	public int getMaleWornModelId1() {
		return maleEquip1;
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

	public int getId() {
		return id;
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
		if (id >= 24455 && id <= 24457)
			return 6;
		if (clientScriptData == null)
			return 4;
		Object attackSpeed = clientScriptData.get(14);
		if (attackSpeed != null && attackSpeed instanceof Integer)
			return (int) attackSpeed;
		return 4;
	}

	public int getStabAttack() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(0);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getSlashAttack() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(1);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getCrushAttack() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(2);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getMagicAttack() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(3);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getRangeAttack() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(4);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getStabDef() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(5);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getSlashDef() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(6);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getCrushDef() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(7);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getMagicDef() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(8);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getRangeDef() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(9);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getSummoningDef() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(417);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getAbsorveMeleeBonus() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(967);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getAbsorveMageBonus() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(969);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getAbsorveRangeBonus() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(968);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getStrengthBonus() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(641);
		if (value != null && value instanceof Integer)
			return (int) value / 10;
		return 0;
	}

	public int getRangedStrBonus() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(643);
		if (value != null && value instanceof Integer)
			return (int) value / 10;
		return 0;
	}

	public int getMagicDamage() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(685);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public int getPrayerBonus() {
		if (id > 25439 || clientScriptData == null)
			return 0;
		Object value = clientScriptData.get(11);
		if (value != null && value instanceof Integer)
			return (int) value;
		return 0;
	}

	public boolean containsOption(int i, String option) {
		if (inventoryOptions == null || inventoryOptions[i] == null || inventoryOptions.length <= i)
			return false;
		return inventoryOptions[i].equals(option);
	}

	public String[] getInventoryOptions() {
		return inventoryOptions;
	}

	public void setInventoryOptions(String[] inventoryOptions) {
		this.inventoryOptions = inventoryOptions;
	}

	public boolean containsOption(String option) {
		if (inventoryOptions == null)
			return false;
		for (String o : inventoryOptions) {
			if (o == null || !o.equals(option))
				continue;
			return true;
		}
		return false;
	}

	public void write(Store store, boolean rewriteTable) {
		store.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].putFile(getArchiveId(), getFileId(),
				Constants.GZIP_COMPRESSION, encode(), null, rewriteTable, true, -1, -1);
	}

	public byte[] encode() {
		OutputStream stream = new OutputStream();

		stream.writeByte(1);
		stream.writeShort(modelId);

		if (!getName().equals("null") && certTemplateId == -1) {
			stream.writeByte(2);
			stream.writeString(getName());
		}

		if (modelZoom != 2000) {
			stream.writeByte(4);
			stream.writeShort(modelZoom);
		}

		if (modelRotation1 != 0) {
			stream.writeByte(5);
			stream.writeShort(modelRotation1);
		}

		if (modelRotation2 != 0) {
			stream.writeByte(6);
			stream.writeShort(modelRotation2);
		}

		if (modelOffset1 != 0) {
			stream.writeByte(7);
			int value = modelOffset1;
			if (value < 0) {
				value += 65536;
			}
			stream.writeShort(value);
		}

		if (modelOffset2 != 0) {
			stream.writeByte(8);
			int value = modelOffset2;
			if (value < 0) {
				value += 65536;
			}
			stream.writeShort(value);
		}

		if (stackable >= 1 && certTemplateId == -1) {
			stream.writeByte(11);
		}

		if (value != 1 && lendTemplateId == -1) {
			stream.writeByte(12);
			stream.writeInt(value);
		}

		int[] equipInfo = null;
		try {
			equipInfo = CacheFixer.getEquipInfo(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int equipSlot = equipInfo[0];
		if (equipSlot != -1) {
			stream.writeByte(13);
			stream.writeByte(equipSlot);
		}

		int equipType = equipInfo[1];
		if (equipType != -1) {
			stream.writeByte(14);
			stream.writeByte(equipType);
		}

		boolean isTradeable = CacheFixer.isTradeable(id);
		stream.writeByte(15);
		stream.writeByte(isTradeable ? 1 : 0);

		if (membersOnly && certTemplateId == -1) {
			stream.writeByte(16);
		}

		boolean isExchangeable = true;//ItemConstants.shouldExchange(id);
		// System.out.println(name + " is " + (isExchangeable ? "" : "un") +
		// "exchangeable.");
		stream.writeByte(17);
		stream.writeByte(isExchangeable ? 1 : 0);

		if (unknownInt18 != -1) {
			stream.writeByte(18);
			stream.writeShort(unknownInt18);
		}

		if (maleEquip1 != -1) {
			stream.writeByte(23);
			stream.writeShort(maleEquip1);
		}

		if (maleEquip2 != -1) {
			stream.writeByte(24);
			stream.writeShort(maleEquip2);
		}

		if (femaleEquip1 != -1) {
			stream.writeByte(25);
			stream.writeShort(femaleEquip1);
		}

		if (femaleEquip2 != -1) {
			stream.writeByte(26);
			stream.writeShort(femaleEquip2);
		}

		for (int index = 0; index < 4; index++) {
			if (groundOptions[index] == null || (index == 2 && groundOptions[index].equals("take"))) {
				continue;
			}
			stream.writeByte(30 + index);
			stream.writeString(groundOptions[index]);
		}

		for (int index = 0; index < 4; index++) {
			if (getInventoryOptions()[index] == null || (index == 4 && getInventoryOptions()[index].equals("drop"))) {
				continue;
			}
			stream.writeByte(35 + index);
			stream.writeString(getInventoryOptions()[index]);
		}

		if (originalModelColors != null && modifiedModelColors != null) {
			stream.writeByte(40);
			stream.writeByte(originalModelColors.length);
			for (int index = 0; index < originalModelColors.length; index++) {
				stream.writeShort(originalModelColors[index]);
				stream.writeShort(modifiedModelColors[index]);
			}
		}

		if (originalTextureColors != null && modifiedTextureColors != null) {
			stream.writeByte(41);
			stream.writeByte(originalTextureColors.length);
			for (int index = 0; index < originalTextureColors.length; index++) {
				stream.writeShort(originalTextureColors[index]);
				stream.writeShort(modifiedTextureColors[index]);
			}
		}

		if (recolourPallete != null) {
			stream.writeByte(42);
			stream.writeByte(recolourPallete.length);
			for (byte element : recolourPallete) {
				stream.writeByte(element);
			}
		}
		if (isShowInGrandExchange()) {
			stream.writeByte(65);
		}

		if (maleEquipModelId3 != -1) {
			stream.writeByte(78);
			stream.writeShort(maleEquipModelId3);
		}

		if (femaleEquipModelId3 != -1) {
			stream.writeByte(79);
			stream.writeShort(femaleEquipModelId3);
		}

		if (maleDialogueModel != -1) {
			stream.writeByte(90);
			stream.writeShort(maleDialogueModel);
		}

		if (femaleDialogueModel != -1) {
			stream.writeByte(91);
			stream.writeShort(femaleDialogueModel);
		}

		if (maleDialogueHat != -1) {
			stream.writeByte(92);
			stream.writeShort(maleDialogueHat);
		}

		if (femaleDialogueHat != -1) {
			stream.writeByte(93);
			stream.writeShort(femaleDialogueHat);
		}

		if (rotationZoom != 0) {
			stream.writeByte(95);
			stream.writeShort(rotationZoom);
		}

		if (dummyItem != 0) {
			stream.writeByte(96);
			stream.writeByte(dummyItem);
		}

		if (certId != -1) {
			stream.writeByte(97);
			stream.writeShort(certId);
		}

		if (certTemplateId != -1) {
			stream.writeByte(98);
			stream.writeShort(certTemplateId);
		}

		if (stackIds != null && stackAmounts != null) {
			for (int index = 0; index < stackIds.length; index++) {
				if (stackIds[index] == 0 && stackAmounts[index] == 0) {
					continue;
				}
				stream.writeByte(100 + index);
				stream.writeShort(stackIds[index]);
				stream.writeShort(stackAmounts[index]);
			}
		}

		if (modelVerticesX != 128) {
			stream.writeByte(110);
			stream.writeShort(modelVerticesX);
		}

		if (modelVerticesY != 128) {
			stream.writeByte(111);
			stream.writeShort(modelVerticesY);
		}

		if (modelVerticesZ != 128) {
			stream.writeByte(112);
			stream.writeShort(modelVerticesZ);
		}

		if (modelLighting != 0) {
			stream.writeByte(113);
			stream.writeByte(modelLighting);
		}

		if (modelShadowing != 0) {
			stream.writeByte(114);
			stream.writeByte(modelShadowing);
		}

		if (teamId != 0) {
			stream.writeByte(115);
			stream.writeByte(teamId);
		}

		if (lendId != -1) {
			stream.writeByte(121);
			stream.writeShort(lendId);
		}

		if (lendTemplateId != -1) {
			stream.writeByte(122);
			stream.writeShort(lendTemplateId);
		}

		if (unknownInt12 != 0 && unknownInt13 != 0 && unknownInt14 != 0) {
			stream.writeByte(125);
			stream.writeByte(unknownInt12 >> 2);
			stream.writeByte(unknownInt13 >> 2);
			stream.writeByte(unknownInt14 >> 2);
		}

		if (unknownInt15 != 0 && unknownInt16 != 0 && unknownInt17 != 0) {
			stream.writeByte(126);
			stream.writeByte(unknownInt15 >> 2);
			stream.writeByte(unknownInt16 >> 2);
			stream.writeByte(unknownInt17 >> 2);
		}
		if (unknownInt19 != -1 && unknownInt18 != -1) {
			stream.writeByte(127);
			stream.writeByte(unknownInt18);
			stream.writeShort(unknownInt19);
		}

		if (unknownInt20 != -1 && unknownInt21 != -1) {
			stream.writeByte(128);
			stream.writeByte(unknownInt20);
			stream.writeShort(unknownInt21);
		}

		if (unknownInt24 != -1 && unknownInt25 != -1) {
			stream.writeByte(129);
			stream.writeByte(unknownInt24);
			stream.writeShort(unknownInt25);
		}
		if (unknownInt22 != -1 && unknownInt23 != -1) {
			stream.writeByte(130);
			stream.writeByte(unknownInt22);
			stream.writeShort(unknownInt23);
		}

		if (unknownArray2 != null) {
			stream.writeByte(132);
			stream.writeByte(unknownArray2.length);
			for (int element : unknownArray2) {
				stream.writeShort(element);
			}
		}

		if (clientScriptData != null) {
			stream.writeByte(249);
			stream.writeByte(clientScriptData.size());
			for (int key : clientScriptData.keySet()) {
				Object value = clientScriptData.get(key);
				stream.writeByte(value instanceof String ? 1 : 0);
				stream.write24BitInt(key);
				if (value instanceof String) {
					stream.writeString((String) value);
				} else {
					stream.writeInt((Integer) value);
				}
			}
		}

		if (unknownValue3 != 0) {
			stream.writeByte(134);
			stream.writeByte(unknownValue3);
		}

		if (unknownValue2 != -1) {
			stream.writeByte(139);
			stream.writeShort(unknownValue2);
		}

		if (unknownValue1 != -1) {
			stream.writeByte(140);
			stream.writeShort(unknownValue1);
		}

		// end
		stream.writeByte(0);

		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}

	/**
	 * @return the showInGrandExchange
	 */
	public boolean isShowInGrandExchange() {
		return showInGrandExchange;
	}

	/**
	 * @param showInGrandExchange
	 *            the showInGrandExchange to set
	 */
	public void setShowInGrandExchange(boolean showInGrandExchange) {
		this.showInGrandExchange = showInGrandExchange;
	}

	public void setName(String string) {
		this.name = string;
	}

}