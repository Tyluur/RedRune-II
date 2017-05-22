package org.redrune.cache.scripts;

import java.io.IOException;

import org.redrune.cache.Cache;
import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.utility.Misc;

import com.alex.store.Store;

public class EquipmentSetOpener {

	public static void main(String... args) throws IOException {
		Cache.init();
		Store ours = new Store("./data/cache/");
		for (int i = 0; i < Misc.getItemDefinitionsSize(); i++) {
			ItemDefinitions definitions = ItemDefinitions.getItemDefinition(ours, i);
			if (definitions.getName().contains(" set")) {
				System.out.println("Set the first option for " + definitions.getName() + "[" + i + "] to 'Open'");
				definitions.getInventoryOptions()[0] = "Open";
				definitions.write(ours, true);
			}
		}
	}

}
