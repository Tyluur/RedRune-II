package com.rs.game.content.dialogue;

import com.rs.game.content.dialogue.impl.*;
import com.rs.game.entity.actor.npc.impl.others.Lucien;

import java.util.HashMap;

public final class DialogueHandler {
	
	/**
	 * The map of cached dialogues
	 */
	private static final HashMap<Object, Class<Dialogue>> CACHED_DIALOGUES = new HashMap<>();
	
	private DialogueHandler() {
	
	}
	
	public static void reload() {
		CACHED_DIALOGUES.clear();
		init();
	}
	
	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			CACHED_DIALOGUES.put("LevelUp", (Class<Dialogue>) Class.forName(LevelUp.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Pool", (Class<Dialogue>) Class.forName(Pool.class.getCanonicalName()));
			CACHED_DIALOGUES.put("ZarosAltar", (Class<Dialogue>) Class.forName(ZarosAltar.class.getCanonicalName()));
			CACHED_DIALOGUES.put("ClimbNoEmoteStairs", (Class<Dialogue>) Class.forName(ClimbNoEmoteStairs.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Banker", (Class<Dialogue>) Class.forName(Banker.class.getCanonicalName()));
			CACHED_DIALOGUES.put("DestroyItemOption", (Class<Dialogue>) Class.forName(DestroyItemOption.class.getCanonicalName()));
			CACHED_DIALOGUES.put("FletchingD", (Class<Dialogue>) Class.forName(FletchingD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("SimpleMessage", (Class<Dialogue>) Class.forName(SimpleMessage.class.getCanonicalName()));
			CACHED_DIALOGUES.put("ItemMessage", (Class<Dialogue>) Class.forName(ItemMessage.class.getCanonicalName()));
			CACHED_DIALOGUES.put("ClimbEmoteStairs", (Class<Dialogue>) Class.forName(ClimbEmoteStairs.class.getCanonicalName()));
			CACHED_DIALOGUES.put("GemCuttingD", (Class<Dialogue>) Class.forName(GemCuttingD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("CookingD", (Class<Dialogue>) Class.forName(CookingD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("HerbloreD", (Class<Dialogue>) Class.forName(HerbloreD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("BarrowsD", (Class<Dialogue>) Class.forName(BarrowsD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("SmeltingD", (Class<Dialogue>) Class.forName(SmeltingD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("LeatherCraftingD", (Class<Dialogue>) Class.forName(LeatherCraftingD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("EnchantedGemDialouge", (Class<Dialogue>) Class.forName(EnchantedGemDialouge.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Transportation", (Class<Dialogue>) Class.forName(Transportation.class.getCanonicalName()));
			CACHED_DIALOGUES.put("WildernessDitch", (Class<Dialogue>) Class.forName(WildernessDitch.class.getCanonicalName()));
			CACHED_DIALOGUES.put("SimpleNPCMessage", (Class<Dialogue>) Class.forName(SimpleNPCMessage.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Transportation", (Class<Dialogue>) Class.forName(Transportation.class.getCanonicalName()));
			CACHED_DIALOGUES.put("AncientEffigiesD", (Class<Dialogue>) Class.forName(AncientEffigiesD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("DismissD", (Class<Dialogue>) Class.forName(DismissD.class.getCanonicalName()));
			CACHED_DIALOGUES.put("MakeOverMage", (Class<Dialogue>) Class.forName(MakeOverMage.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Lucien", (Class<Dialogue>) Class.forName(Lucien.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Turael", (Class<Dialogue>) Class.forName(Turael.class.getCanonicalName()));
			CACHED_DIALOGUES.put("Hairdresser", (Class<Dialogue>) Class.forName(Hairdresser.class.getCanonicalName()));
			CACHED_DIALOGUES.put("CompCape", (Class<Dialogue>) Class.forName(CompCape.class.getCanonicalName()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue) {
			return (Dialogue) key;
		}
		Class<Dialogue> classD = CACHED_DIALOGUES.get(key);
		if (classD == null) {
			System.out.println("couldnt find dialogue for key=" + key + "");
			return null;
		}
		try {
			return classD.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
