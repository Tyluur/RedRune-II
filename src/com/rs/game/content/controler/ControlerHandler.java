package com.rs.game.content.controler;

import com.rs.game.content.controler.impl.*;
import com.rs.game.content.controler.impl.activity.Barrows;
import com.rs.game.content.controler.impl.activity.GodWars;
import com.rs.game.content.controler.impl.activity.JailControler;
import com.rs.game.content.controler.impl.activity.Wilderness;
import com.rs.game.content.controler.impl.minigame.*;

import java.util.HashMap;

public class ControlerHandler {
	
	private static final HashMap<Object, Class<Controler>> handledControlers = new HashMap<Object, Class<Controler>>();
	
	@SuppressWarnings("unchecked")
	public static final void init() {
		try {
			Class<Controler> value1 = (Class<Controler>) Class.forName(Wilderness.class.getCanonicalName());
			handledControlers.put("Wilderness", value1);
			Class<Controler> value4 = (Class<Controler>) Class.forName(GodWars.class.getCanonicalName());
			handledControlers.put("GodWars", value4);
			Class<Controler> value8 = (Class<Controler>) Class.forName(Barrows.class.getCanonicalName());
			handledControlers.put("Barrows", value8);
			Class<Controler> value9 = (Class<Controler>) Class.forName(Duelarena.class.getCanonicalName());
			handledControlers.put("Duelarena", value9);
			Class<Controler> value10 = (Class<Controler>) Class.forName(DuelControler.class.getCanonicalName());
			handledControlers.put("DuelControler", value10);
			Class<Controler> value11 = (Class<Controler>) Class.forName(CorpBeastControler.class.getCanonicalName());
			handledControlers.put("CorpBeastControler", value11);
			Class<Controler> value15 = (Class<Controler>) Class.forName(JailControler.class.getCanonicalName());
			handledControlers.put("JailControler", value15);
			Class<Controler> value16 = (Class<Controler>) Class.forName(ClanReqControler.class.getCanonicalName());
			handledControlers.put("ClanReqControler", value16);
			Class<Controler> value17 = (Class<Controler>) Class.forName(CastleWarsPlaying.class.getCanonicalName());
			handledControlers.put("CastleWarsPlaying", value17);
			Class<Controler> value18 = (Class<Controler>) Class.forName(CastleWarsWaiting.class.getCanonicalName());
			handledControlers.put("CastleWarsWaiting", value18);
			Class<Controler> value23 = (Class<Controler>) Class.forName(ClanWarsControler.class.getCanonicalName());
			handledControlers.put("ClanWarsControler", value23);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static final void reload() {
		handledControlers.clear();
		init();
	}
	
	public static final Controler getControler(Object key) {
		if (key instanceof Controler) {
			return (Controler) key;
		}
		Class<Controler> classC = handledControlers.get(key);
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
