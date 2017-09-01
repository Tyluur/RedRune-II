package com.rs.game.content.cutscene;

import com.rs.game.content.cutscene.impl.*;

import java.util.HashMap;

public class CutscenesHandler {
	
	private static HashMap<Object, Class<Cutscene>> handledCutscenes = new HashMap<Object, Class<Cutscene>>();
	
	public static void reload() {
		handledCutscenes.clear();
		init();
	}
	
	@SuppressWarnings("unchecked")
	public static void init() {
		try {
			handledCutscenes.put("EdgeWilderness", (Class<Cutscene>) Class.forName(EdgeWilderness.class.getCanonicalName()));
			handledCutscenes.put("DTPreview", (Class<Cutscene>) Class.forName(DTPreview.class.getCanonicalName()));
			handledCutscenes.put("NexCutScene", (Class<Cutscene>) Class.forName(NexCutScene.class.getCanonicalName()));
			handledCutscenes.put("TowersPkCutscene", (Class<Cutscene>) Class.forName(TowersPkCutscene.class.getCanonicalName()));
			handledCutscenes.put("HomeCutScene", (Class<Cutscene>) Class.forName(HomeCutScene.class.getCanonicalName()));
			handledCutscenes.put("NewStartTutorial", (Class<Cutscene>) Class.forName(NewStartTutorial.class.getCanonicalName()));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public static Cutscene getCutscene(Object key) {
		Class<Cutscene> classC = handledCutscenes.get(key);
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
