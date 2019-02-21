package org.redrune.game.content.entity.actor.player.cutscene;

import org.redrune.game.content.entity.actor.player.cutscene.impl.*;

import java.util.HashMap;

public class CutscenesHandler {
	
	private static final HashMap<Object, Class<Cutscene>> HANDLED_CUTSCENES = new HashMap<>();
	
	public static void reload() {
		HANDLED_CUTSCENES.clear();
		init();
	}
	
	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			HANDLED_CUTSCENES.put("EdgeWilderness", (Class<Cutscene>) Class.forName(EdgeWilderness.class.getCanonicalName()));
			HANDLED_CUTSCENES.put("DTPreview", (Class<Cutscene>) Class.forName(DTPreview.class.getCanonicalName()));
			HANDLED_CUTSCENES.put("NexCutScene", (Class<Cutscene>) Class.forName(NexCutScene.class.getCanonicalName()));
			HANDLED_CUTSCENES.put("TowersPkCutscene", (Class<Cutscene>) Class.forName(TowersPkCutscene.class.getCanonicalName()));
			HANDLED_CUTSCENES.put("HomeCutScene", (Class<Cutscene>) Class.forName(HomeCutScene.class.getCanonicalName()));
			HANDLED_CUTSCENES.put("NewStartTutorial", (Class<Cutscene>) Class.forName(NewStartTutorial.class.getCanonicalName()));
			System.out.println("Loaded " + HANDLED_CUTSCENES.size() + " game cutscenes");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static Cutscene getCutscene(Object key) {
		Class<Cutscene> classC = HANDLED_CUTSCENES.get(key);
		if (classC == null) {
			return null;
		}
		try {
			return classC.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
