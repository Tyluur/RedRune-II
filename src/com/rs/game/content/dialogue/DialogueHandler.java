package com.rs.game.content.dialogue;

import com.rs.game.content.dialogue.draynor.WiseOldMan;
import com.rs.game.content.dialogue.falador.Wizard;
import com.rs.game.content.dialogue.lumbridge.*;
import com.rs.game.content.dialogue.varrock.Aubury;
import com.rs.game.content.dialogue.varrock.GrandExchangeTutor;
import com.rs.game.content.dialogue.varrock.Horvik;
import com.rs.game.content.dialogue.varrock.Lowe;

import java.util.HashMap;

public final class DialogueHandler {
	
	private static final HashMap<Object, Class<Dialogue>> handledDialogues = new HashMap<Object, Class<Dialogue>>();
	
	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			handledDialogues.put("Varnis", (Class<Dialogue>) Class.forName(Varnis.class.getCanonicalName()));
			handledDialogues.put("LevelUp", (Class<Dialogue>) Class.forName(LevelUp.class.getCanonicalName()));
			handledDialogues.put("DungLad", (Class<Dialogue>) Class.forName(Crate.class.getCanonicalName()));
			handledDialogues.put("Crate", (Class<Dialogue>) Class.forName(Crate.class.getCanonicalName()));
			handledDialogues.put("Pool", (Class<Dialogue>) Class.forName(Pool.class.getCanonicalName()));
			handledDialogues.put("ZarosAltar", (Class<Dialogue>) Class.forName(ZarosAltar.class.getCanonicalName()));
			handledDialogues.put("ClimbNoEmoteStairs", (Class<Dialogue>) Class.forName(ClimbNoEmoteStairs.class.getCanonicalName()));
			handledDialogues.put("Banker", (Class<Dialogue>) Class.forName(Banker.class.getCanonicalName()));
			handledDialogues.put("DestroyItemOption", (Class<Dialogue>) Class.forName(DestroyItemOption.class.getCanonicalName()));
			handledDialogues.put("FremennikShipmaster", (Class<Dialogue>) Class.forName(FremennikShipmaster.class.getCanonicalName()));
			handledDialogues.put("NexEntrance", (Class<Dialogue>) Class.forName(NexEntrance.class.getCanonicalName()));
			handledDialogues.put("MagicPortal", (Class<Dialogue>) Class.forName(MagicPortal.class.getCanonicalName()));
			handledDialogues.put("LunarAltar", (Class<Dialogue>) Class.forName(LunarAltar.class.getCanonicalName()));
			handledDialogues.put("AncientAltar", (Class<Dialogue>) Class.forName(AncientAltar.class.getCanonicalName()));
			handledDialogues.put("FletchingD", (Class<Dialogue>) Class.forName(FletchingD.class.getCanonicalName()));
			handledDialogues.put("RuneScapeGuide", (Class<Dialogue>) Class.forName(RuneScapeGuide.class.getCanonicalName()));
			handledDialogues.put("SimpleMessage", (Class<Dialogue>) Class.forName(SimpleMessage.class.getCanonicalName()));
			handledDialogues.put("ItemMessage", (Class<Dialogue>) Class.forName(ItemMessage.class.getCanonicalName()));
			handledDialogues.put("ClimbEmoteStairs", (Class<Dialogue>) Class.forName(ClimbEmoteStairs.class.getCanonicalName()));
			handledDialogues.put("GemCuttingD", (Class<Dialogue>) Class.forName(GemCuttingD.class.getCanonicalName()));
			handledDialogues.put("CookingD", (Class<Dialogue>) Class.forName(CookingD.class.getCanonicalName()));
			handledDialogues.put("HerbloreD", (Class<Dialogue>) Class.forName(HerbloreD.class.getCanonicalName()));
			handledDialogues.put("BarrowsD", (Class<Dialogue>) Class.forName(BarrowsD.class.getCanonicalName()));
			handledDialogues.put("SmeltingD", (Class<Dialogue>) Class.forName(SmeltingD.class.getCanonicalName()));
			handledDialogues.put("LeatherCraftingD", (Class<Dialogue>) Class.forName(LeatherCraftingD.class.getCanonicalName()));
			handledDialogues.put("EnchantedGemDialouge", (Class<Dialogue>) Class.forName(EnchantedGemDialouge.class.getCanonicalName()));
			handledDialogues.put("ForfeitDialouge", (Class<Dialogue>) Class.forName(ForfeitDialouge.class.getCanonicalName()));
			handledDialogues.put("Transportation", (Class<Dialogue>) Class.forName(Transportation.class.getCanonicalName()));
			handledDialogues.put("WildernessDitch", (Class<Dialogue>) Class.forName(WildernessDitch.class.getCanonicalName()));
			handledDialogues.put("SimpleNPCMessage", (Class<Dialogue>) Class.forName(SimpleNPCMessage.class.getCanonicalName()));
			handledDialogues.put("Transportation", (Class<Dialogue>) Class.forName(Transportation.class.getCanonicalName()));
			handledDialogues.put("AncientEffigiesD", (Class<Dialogue>) Class.forName(AncientEffigiesD.class.getCanonicalName()));
			handledDialogues.put("SetSkills", (Class<Dialogue>) Class.forName(SetSkills.class.getCanonicalName()));
			handledDialogues.put("DismissD", (Class<Dialogue>) Class.forName(DismissD.class.getCanonicalName()));
			handledDialogues.put("MrEx", (Class<Dialogue>) Class.forName(MrEx.class.getCanonicalName()));
			handledDialogues.put("MakeOverMage", (Class<Dialogue>) Class.forName(MakeOverMage.class.getCanonicalName()));
			handledDialogues.put("KaramjaTrip", (Class<Dialogue>) Class.forName(KaramjaTrip.class.getCanonicalName()));
			handledDialogues.put("Lucien", (Class<Dialogue>) Class.forName(Lucien.class.getCanonicalName()));
			handledDialogues.put("TeleportMinigame", (Class<Dialogue>) Class.forName(TeleportMinigame.class.getCanonicalName()));
			handledDialogues.put("TeleportBosses", (Class<Dialogue>) Class.forName(TeleportBosses.class.getCanonicalName()));
			handledDialogues.put("TeleportTraining", (Class<Dialogue>) Class.forName(TeleportTraining.class.getCanonicalName()));
			handledDialogues.put("Turael", (Class<Dialogue>) Class.forName(Turael.class.getCanonicalName()));
			handledDialogues.put("JadEnter", (Class<Dialogue>) Class.forName(JadEnter.class.getCanonicalName()));
			handledDialogues.put("BorkEnter", (Class<Dialogue>) Class.forName(BorkEnter.class.getCanonicalName()));
			handledDialogues.put("Veliaf", (Class<Dialogue>) Class.forName(Veliaf.class.getCanonicalName()));
			handledDialogues.put("Max", (Class<Dialogue>) Class.forName(Max.class.getCanonicalName()));
			handledDialogues.put("RoyalGuard", (Class<Dialogue>) Class.forName(RoyalGuard.class.getCanonicalName()));
			handledDialogues.put("BobDialogue", (Class<Dialogue>) Class.forName(BobDialogue.class.getCanonicalName()));
			handledDialogues.put("LumbridgeSage", (Class<Dialogue>) Class.forName(LumbridgeSage.class.getCanonicalName()));
			handledDialogues.put("FatherAereck", (Class<Dialogue>) Class.forName(FatherAereck.class.getCanonicalName()));
			handledDialogues.put("LumbridgeMan", (Class<Dialogue>) Class.forName(LumbridgeMan.class.getCanonicalName()));
			handledDialogues.put("DoomSayer", (Class<Dialogue>) Class.forName(DoomSayer.class.getCanonicalName()));
			handledDialogues.put("WiseOldMan", (Class<Dialogue>) Class.forName(WiseOldMan.class.getCanonicalName()));
			handledDialogues.put("RangedInstructor", (Class<Dialogue>) Class.forName(RangedInstructor.class.getCanonicalName()));
			handledDialogues.put("Musician", (Class<Dialogue>) Class.forName(Musician.class.getCanonicalName()));
			handledDialogues.put("GrandExchangeTutor", (Class<Dialogue>) Class.forName(GrandExchangeTutor.class.getCanonicalName()));
			handledDialogues.put("LumbridgeCook", (Class<Dialogue>) Class.forName(LumbridgeCook.class.getCanonicalName()));
			handledDialogues.put("Hans", (Class<Dialogue>) Class.forName(Hans.class.getCanonicalName()));
			handledDialogues.put("Frog", (Class<Dialogue>) Class.forName(Frog.class.getCanonicalName()));
			handledDialogues.put("SirVant", (Class<Dialogue>) Class.forName(SirVant.class.getCanonicalName()));
			handledDialogues.put("BorderGuard", (Class<Dialogue>) Class.forName(BorderGuard.class.getCanonicalName()));
			handledDialogues.put("Hairdresser", (Class<Dialogue>) Class.forName(Hairdresser.class.getCanonicalName()));
			handledDialogues.put("Wizard", (Class<Dialogue>) Class.forName(Wizard.class.getCanonicalName()));
			handledDialogues.put("CrateTutorial", (Class<Dialogue>) Class.forName(CrateTutorial.class.getCanonicalName()));
			handledDialogues.put("Aubury", (Class<Dialogue>) Class.forName(Aubury.class.getCanonicalName()));
			handledDialogues.put("CompCape", (Class<Dialogue>) Class.forName(CompCape.class.getCanonicalName()));
			handledDialogues.put("Lowe", (Class<Dialogue>) Class.forName(Lowe.class.getCanonicalName()));
			handledDialogues.put("Horvik", (Class<Dialogue>) Class.forName(Horvik.class.getCanonicalName()));
			handledDialogues.put("WaterFillingD", (Class<Dialogue>) Class.forName(WaterFillingD.class.getCanonicalName()));
			handledDialogues.put("PartyRoomLever", (Class<Dialogue>) Class.forName(PartyRoomLever.class.getCanonicalName()));
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static final void reload() {
		handledDialogues.clear();
		init();
	}
	
	public static final Dialogue getDialogue(Object key) {
		if (key instanceof Dialogue) {
			return (Dialogue) key;
		}
		Class<Dialogue> classD = handledDialogues.get(key);
		if (classD == null) {
			return null;
		}
		try {
			return classD.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private DialogueHandler() {
	
	}
}
