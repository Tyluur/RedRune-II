package cache.codec.loaders;

import cache.io.InputStream;
import cache.utils.Constants;
import cache.Cache;
import game.entity.item.Item;
import utility.constants.EquipmentConstants;
import utility.constants.SkillConstants;
import utility.functions.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public final class ItemDefinitions {
	
	private static final ItemDefinitions[] itemsDefinitions;
	
	static { // that's why this is here
		itemsDefinitions = new ItemDefinitions[Misc.getItemDefinitionsSize()];
	}
	
	private int id;
	
	private int modelId;
	
	private String name;
	
	private String[] inventoryOptions;
	
	private int[] originalModelColors;
	
	private int[] modifiedModelColors;
	
	private short[] originalTextureColors;
	
	private boolean loaded;
	
	private int modelZoom;
	
	private int modelRotation1;
	
	private int modelRotation2;
	
	private int modelOffset1;
	
	private int modelOffset2;
	
	private int stackable;
	
	private int value;
	
	private boolean membersOnly;
	
	private int maleEquip1;
	
	private int femaleEquip1;
	
	private int maleEquip2;
	
	private int femaleEquip2;
	
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
	
	private boolean noted;
	
	private boolean lended;
	
	private boolean isTradeable;
	
	private boolean isExchangeable;
	
	private HashMap<Integer, Object> clientScriptData;
	
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
				return true;
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

    public int getId() {
        return this.id;
    }

    public int getModelId() {
        return this.modelId;
    }

    public String getName() {
        return this.name;
    }

    public String[] getInventoryOptions() {
        return this.inventoryOptions;
    }

    public int[] getOriginalModelColors() {
        return this.originalModelColors;
    }

    public int[] getModifiedModelColors() {
        return this.modifiedModelColors;
    }

    public short[] getOriginalTextureColors() {
        return this.originalTextureColors;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public int getModelZoom() {
        return this.modelZoom;
    }

    public int getModelRotation1() {
        return this.modelRotation1;
    }

    public int getModelRotation2() {
        return this.modelRotation2;
    }

    public int getModelOffset1() {
        return this.modelOffset1;
    }

    public int getModelOffset2() {
        return this.modelOffset2;
    }

    public int getStackable() {
        return this.stackable;
    }

    public boolean isMembersOnly() {
        return this.membersOnly;
    }

    public int getMaleEquip1() {
        return this.maleEquip1;
    }

    public int getFemaleEquip1() {
        return this.femaleEquip1;
    }

    public int getMaleEquip2() {
        return this.maleEquip2;
    }

    public int getFemaleEquip2() {
        return this.femaleEquip2;
    }

    public String[] getGroundOptions() {
        return this.groundOptions;
    }

    public short[] getModifiedTextureColors() {
        return this.modifiedTextureColors;
    }

    public byte[] getRecolourPallete() {
        return this.recolourPallete;
    }

    public int[] getUnknownArray2() {
        return this.unknownArray2;
    }

    public int getMaleEquipModelId3() {
        return this.maleEquipModelId3;
    }

    public int getFemaleEquipModelId3() {
        return this.femaleEquipModelId3;
    }

    public int getCertId() {
        return this.certId;
    }

    public int getCertTemplateId() {
        return this.certTemplateId;
    }

    public int[] getStackIds() {
        return this.stackIds;
    }

    public int[] getStackAmounts() {
        return this.stackAmounts;
    }

    public int getModelShadowing() {
        return this.modelShadowing;
    }

    public int getTeamId() {
        return this.teamId;
    }

    public int getLendId() {
        return this.lendId;
    }

    public int getLendTemplateId() {
        return this.lendTemplateId;
    }

    public int getMaleDialogueModel() {
        return this.maleDialogueModel;
    }

    public int getFemaleDialogueModel() {
        return this.femaleDialogueModel;
    }

    public int getMaleDialogueHat() {
        return this.maleDialogueHat;
    }

    public int getFemaleDialogueHat() {
        return this.femaleDialogueHat;
    }

    public int getRotationZoom() {
        return this.rotationZoom;
    }

    public int getDummyItem() {
        return this.dummyItem;
    }

    public int getModelVerticesX() {
        return this.modelVerticesX;
    }

    public int getModelVerticesY() {
        return this.modelVerticesY;
    }

    public int getModelVerticesZ() {
        return this.modelVerticesZ;
    }

    public int getModelLighting() {
        return this.modelLighting;
    }

    public boolean isNoted() {
        return this.noted;
    }

    public boolean isLended() {
        return this.lended;
    }

    public boolean isTradeable() {
        return this.isTradeable;
    }

    public boolean isExchangeable() {
        return this.isExchangeable;
    }

    public HashMap<Integer, Object> getClientScriptData() {
        return this.clientScriptData;
    }

    public HashMap<Integer, Integer> getItemRequirements() {
        return this.itemRequirements;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInventoryOptions(String[] inventoryOptions) {
        this.inventoryOptions = inventoryOptions;
    }

    public void setOriginalModelColors(int[] originalModelColors) {
        this.originalModelColors = originalModelColors;
    }

    public void setModifiedModelColors(int[] modifiedModelColors) {
        this.modifiedModelColors = modifiedModelColors;
    }

    public void setOriginalTextureColors(short[] originalTextureColors) {
        this.originalTextureColors = originalTextureColors;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setModelZoom(int modelZoom) {
        this.modelZoom = modelZoom;
    }

    public void setModelRotation1(int modelRotation1) {
        this.modelRotation1 = modelRotation1;
    }

    public void setModelRotation2(int modelRotation2) {
        this.modelRotation2 = modelRotation2;
    }

    public void setModelOffset1(int modelOffset1) {
        this.modelOffset1 = modelOffset1;
    }

    public void setModelOffset2(int modelOffset2) {
        this.modelOffset2 = modelOffset2;
    }

    public void setStackable(int stackable) {
        this.stackable = stackable;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMembersOnly(boolean membersOnly) {
        this.membersOnly = membersOnly;
    }

    public void setMaleEquip1(int maleEquip1) {
        this.maleEquip1 = maleEquip1;
    }

    public void setFemaleEquip1(int femaleEquip1) {
        this.femaleEquip1 = femaleEquip1;
    }

    public void setMaleEquip2(int maleEquip2) {
        this.maleEquip2 = maleEquip2;
    }

    public void setFemaleEquip2(int femaleEquip2) {
        this.femaleEquip2 = femaleEquip2;
    }

    public void setGroundOptions(String[] groundOptions) {
        this.groundOptions = groundOptions;
    }

    public void setModifiedTextureColors(short[] modifiedTextureColors) {
        this.modifiedTextureColors = modifiedTextureColors;
    }

    public void setRecolourPallete(byte[] recolourPallete) {
        this.recolourPallete = recolourPallete;
    }

    public void setUnknownArray2(int[] unknownArray2) {
        this.unknownArray2 = unknownArray2;
    }

    public void setMaleEquipModelId3(int maleEquipModelId3) {
        this.maleEquipModelId3 = maleEquipModelId3;
    }

    public void setFemaleEquipModelId3(int femaleEquipModelId3) {
        this.femaleEquipModelId3 = femaleEquipModelId3;
    }

    public void setCertId(int certId) {
        this.certId = certId;
    }

    public void setCertTemplateId(int certTemplateId) {
        this.certTemplateId = certTemplateId;
    }

    public void setStackIds(int[] stackIds) {
        this.stackIds = stackIds;
    }

    public void setStackAmounts(int[] stackAmounts) {
        this.stackAmounts = stackAmounts;
    }

    public void setModelShadowing(int modelShadowing) {
        this.modelShadowing = modelShadowing;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setLendId(int lendId) {
        this.lendId = lendId;
    }

    public void setLendTemplateId(int lendTemplateId) {
        this.lendTemplateId = lendTemplateId;
    }

    public void setMaleDialogueModel(int maleDialogueModel) {
        this.maleDialogueModel = maleDialogueModel;
    }

    public void setFemaleDialogueModel(int femaleDialogueModel) {
        this.femaleDialogueModel = femaleDialogueModel;
    }

    public void setMaleDialogueHat(int maleDialogueHat) {
        this.maleDialogueHat = maleDialogueHat;
    }

    public void setFemaleDialogueHat(int femaleDialogueHat) {
        this.femaleDialogueHat = femaleDialogueHat;
    }

    public void setRotationZoom(int rotationZoom) {
        this.rotationZoom = rotationZoom;
    }

    public void setDummyItem(int dummyItem) {
        this.dummyItem = dummyItem;
    }

    public void setModelVerticesX(int modelVerticesX) {
        this.modelVerticesX = modelVerticesX;
    }

    public void setModelVerticesY(int modelVerticesY) {
        this.modelVerticesY = modelVerticesY;
    }

    public void setModelVerticesZ(int modelVerticesZ) {
        this.modelVerticesZ = modelVerticesZ;
    }

    public void setModelLighting(int modelLighting) {
        this.modelLighting = modelLighting;
    }

    public void setNoted(boolean noted) {
        this.noted = noted;
    }

    public void setLended(boolean lended) {
        this.lended = lended;
    }

    public void setTradeable(boolean isTradeable) {
        this.isTradeable = isTradeable;
    }

    public void setExchangeable(boolean isExchangeable) {
        this.isExchangeable = isExchangeable;
    }

    public void setClientScriptData(HashMap<Integer, Object> clientScriptData) {
        this.clientScriptData = clientScriptData;
    }

    public void setItemRequirements(HashMap<Integer, Integer> itemRequirements) {
        this.itemRequirements = itemRequirements;
    }
}