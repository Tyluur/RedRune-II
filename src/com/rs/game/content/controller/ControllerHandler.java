package com.rs.game.content.controller;

import com.rs.game.content.controller.impl.CorpBeastController;
import com.rs.game.content.controller.impl.activity.Wilderness;

import java.util.HashMap;

public final class ControllerHandler {
	
	/**
	 * The map of cached controllers
	 */
	private static final HashMap<Object, Class<Controller>> CACHED_CONTROLLERS = new HashMap<>();
	
	/**
	 * Reloads all controllers
	 */
	public static void reload() {
		CACHED_CONTROLLERS.clear();
		registerAll();
	}
	
	/**
	 * Registers all controllers to the map
	 */
	@SuppressWarnings("unchecked")
	public static void registerAll() {
		try {
			register("Wilderness", Wilderness.class);
			register("CorpBeastController", CorpBeastController.class);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void register(String name, Class clazz) throws ClassNotFoundException {
		CACHED_CONTROLLERS.put(name, (Class<Controller>) Class.forName(clazz.getCanonicalName()));
	}
	
	/**
	 * Finds a controller by a key
	 *
	 * @param key
	 * 		The key
	 */
	public static Controller getController(Object key) {
		if (key instanceof Controller) {
			return (Controller) key;
		}
		Class<Controller> classC = CACHED_CONTROLLERS.get(key);
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
