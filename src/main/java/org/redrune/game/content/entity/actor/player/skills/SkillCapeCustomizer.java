package org.redrune.game.content.entity.actor.player.skills;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.entity.actor.player.Player;

import java.util.Arrays;

public final class SkillCapeCustomizer {
	
	private SkillCapeCustomizer() {
	
	}
	
	public static void resetSkillCapes(Player player) {
		player.getAttributes().setMaxedCapeCustomized(Arrays.copyOf(ItemDefinitions.getItemDefinitions(20767).getOriginalModelColors(), 4));
		player.getAttributes().setCompletionistCapeCustomized(Arrays.copyOf(ItemDefinitions.getItemDefinitions(20769).getOriginalModelColors(), 4));
	}
	
	public static void startCustomizing(Player player, int itemId) {
		player.getTemporaryAttributes().put("SkillcapeCustomizeId", itemId);
		int[] skillCape = itemId == 20767 ? player.getAttributes().getMaxedCapeCustomized() : player.getAttributes().getCompletionistCapeCustomized();
		player.getInterfaceManager().sendInterface(20);
		for (int i = 0; i < 4; i++) {
			player.getPackets().sendConfigByFile(9254 + i, skillCape[i]);
		}
		player.getPackets().sendIComponentModel(20, 55, player.getAppearance().isMale() ? ItemDefinitions.getItemDefinitions(itemId).getMaleWornModelId1() : ItemDefinitions.getItemDefinitions(itemId).getFemaleWornModelId1());
	}
	
	public static void handleSkillCapeCustomizerColor(Player player, int colorId) {
		int capeId = getCapeId(player);
		if (capeId == -1) {
			return;
		}
		Integer part = (Integer) player.getTemporaryAttributes().get("SkillcapeCustomize");
		if (part == null) {
			return;
		}
		int[] skillCape = capeId == 20767 ? player.getAttributes().getMaxedCapeCustomized() : player.getAttributes().getCompletionistCapeCustomized();
		skillCape[part] = colorId;
		player.getPackets().sendConfigByFile(9254 + part, colorId);
		player.getInterfaceManager().sendInterface(20);
	}
	
	public static int getCapeId(Player player) {
		Integer id = (Integer) player.getTemporaryAttributes().get("SkillcapeCustomizeId");
		if (id == null) {
			return -1;
		}
		return id;
	}
	
	public static void handleSkillCapeCustomizer(Player player, int buttonId) {
		int capeId = getCapeId(player);
		if (capeId == -1) {
			return;
		}
		int[] skillCape = capeId == 20767 ? player.getAttributes().getMaxedCapeCustomized() : player.getAttributes().getCompletionistCapeCustomized();
		if (buttonId == 58) { // reset
			if (capeId == 20767) {
				player.getAttributes().setMaxedCapeCustomized(Arrays.copyOf(ItemDefinitions.getItemDefinitions(capeId).getOriginalModelColors(), 4));
			} else {
				player.getAttributes().setCompletionistCapeCustomized(Arrays.copyOf(ItemDefinitions.getItemDefinitions(capeId).getOriginalModelColors(), 4));
			}
			for (int i = 0; i < 4; i++) {
				player.getPackets().sendConfigByFile(9254 + i, skillCape[i]);
			}
		} else if (buttonId == 34) { // detail top
			player.getTemporaryAttributes().put("SkillcapeCustomize", 0);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[0]);
		} else if (buttonId == 71) { // background top
			player.getTemporaryAttributes().put("SkillcapeCustomize", 1);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[1]);
		} else if (buttonId == 83) { // detail button
			player.getTemporaryAttributes().put("SkillcapeCustomize", 2);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[2]);
		} else if (buttonId == 95) { // background button
			player.getTemporaryAttributes().put("SkillcapeCustomize", 3);
			player.getInterfaceManager().sendInterface(19);
			player.getPackets().sendConfig(2174, skillCape[3]);
		} else if (buttonId == 114 || buttonId == 142) { // done / close
			player.getAppearance().generateAppearanceData();
			player.closeInterfaces();
		}
	}
}
