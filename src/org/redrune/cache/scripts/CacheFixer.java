package org.redrune.cache.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.redrune.cache.Cache;
import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.rs2.node.item.Item;
import org.redrune.utility.Misc;

import com.alex.store.Store;

public class CacheFixer {

	private static ArrayList<String> nonTradeables = new ArrayList<String>();

	static {
		try {
			nonTradeables = (ArrayList<String>) Files.readAllLines(new File("./data/items/nontradeables.txt").toPath(),
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loaded: Skeletons, type=0 Loaded: Skins, type=1 Loaded: Config, type=2
	 * Loaded: Intefaces, type=3 Loaded: Sound effects, type=4 Loaded:
	 * Landscapes, type=5 Loaded: Music, type=6 Loaded: Models, type=7 Loaded:
	 * Sprites, type=8 Loaded: Textures, type=9 Loaded: Huffman encoding,
	 * type=10 Loaded: Music2, type=11 Loaded: Interface scripts, type=12
	 * Loaded: Fonts, type=13 Loaded: Sound effects2, type=14 Loaded: Sound
	 * effects3, type=15 Loaded: Objects, type=16 Loaded: Clientscript settings,
	 * type=17 Loaded: Npcs, type=18 Loaded: Items, type=19 Loaded: Animations,
	 * type=20 Loaded: Graphics, type=21 Loaded: Script configs, type=22 Loaded:
	 * World map, type=23 Loaded: Quick chat messages, type=24 Loaded: Quick
	 * chat menus, type=25 Loaded: Native libraries, type=30 Loaded: Graphic
	 * shaders, type=31 Loaded: P11 Fonts/Images, type=32 Loaded: Game tips,
	 * type=33 Loaded: P11 Fonts2/Images, type=34 Loaded: Theora, type=35
	 * Loaded: Vorbis, type=36 Finished loading. Identified 32/36 types.
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Store ours = new Store("./data/cache/");
		Cache.init();
		// int[] ids = { 20137, 20141, 20145, 20149, 20153, 20157, 20161, 20165,
		// 20169 };
		for (int i = 0; i < Misc.getItemDefinitionsSize(); i++) {
			ItemDefinitions definitions = ItemDefinitions.getItemDefinition(ours, i);
			definitions.write(ours, ((i >= Misc.getItemDefinitionsSize() - 1) ? true : false));
			if (i % 1000 == 0)
				System.out.println("Completed item " + i);
		}
	}

	public static int[] getEquipInfo(int id) throws Exception {
		new Item(id);
		BufferedReader reader = new BufferedReader(new FileReader(new File("data/items/equip.txt")));
		String line = "";
		while ((line = reader.readLine()) != null) {
			int lineId = Integer.parseInt(line.substring(0, line.indexOf(":")));
			if (lineId == id) {
				String info = line.substring(line.indexOf(":") + 1, line.length()).trim();
				String[] splitInfo = info.split(",");
				reader.close();
				return new int[] { Integer.parseInt(splitInfo[0].trim()), Integer.parseInt(splitInfo[1].trim()) };
			}
		}
		reader.close();
		return null;
	}

	public static boolean isTradeable(int itemId) {
		ItemDefinitions definitions = ItemDefinitions
				.getItemDefinitions(itemId);
		switch (itemId) {
		case 10943:
			return true;
		}
		if (definitions.isDestroyItem() || definitions.isLended()) {
			return false;
		}
		boolean listContained = false;
		for (String listName : nonTradeables) {
			int id = -1;
			try {
				id = Integer.parseInt(listName);
			} catch (Exception e) {

			}
			if (id != -1) {
				if (itemId == id) {
					listContained = true;
					System.out.println("Found by id!");
					break;
				}
			}
			if (definitions.getName().equalsIgnoreCase(listName)) {
				listContained = true;
				System.out.println("Found by name!");
				break;
			}
		}
		if (listContained) {
			return false;
		}
		if (itemId >= 7454 && itemId <= 7462)
			return false;
		switch (itemId) {
		case 18778:
		case 10548:
		case 10551:
		case 6570:
			return false;
		default:
			String name = definitions.getName().toLowerCase();
			if (name.contains("flameburst") || name.contains("ancient effigy") || name.contains("clue scroll")
					|| name.contains("(i)") || name.contains("chaotic")) {
				return false;
			}
			return true;
		}
	}

}
