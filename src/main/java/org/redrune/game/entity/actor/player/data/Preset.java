package org.redrune.game.entity.actor.player.data;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

public final class Preset implements Serializable {

	private static final long serialVersionUID = 1385575955598546603L;

	private final Item[] inventory, equipment;
	private final boolean isAncientCurses;
	private final byte spellBook;
	private final String name;
	private final double[] xp;

	public Preset(String id, final Item[] inventory, final Item[] equipment, boolean isAncientCurses, byte spellBook,
			double[] xp) {
		this.name = id;
		this.inventory = inventory;
		this.equipment = equipment;
		this.isAncientCurses = isAncientCurses;
		this.spellBook = spellBook;
		this.xp = xp;
		/*int i = 0;
		for (Entry<Integer, Item[]> charges : runicStaff.entrySet()) {
			if (charges == null)
				continue;
			if (i != 0)
				continue;
			this.runicStaff.put(charges.getKey(), charges.getValue());
			i++;
		}
		Iterator<Map.Entry<Integer, Item[]>> iterator = this.runicStaff.entrySet().iterator();
		System.out.println("save this.runicStaff = runicStaff");
		if (!iterator.hasNext())
			System.out.println("failed to save this.runicStaff = runicStaff!");
		while (iterator.hasNext()) {
			Map.Entry<Integer, Item[]> pair = iterator.next();
			for (Item item : pair.getValue()) {
				if (item == null)
					continue;
				System.out
						.println("Preset name: " + name + "saved, rune: " + item.getName() + " x " + item.getAmount());
			}
			System.out.println("Preset name: " + name + "saved, spellId: " + pair.getKey());
		}*/
	}

	public final Item[] getInventory() {
		return inventory;
	}

	public final Item[] getEquipment() {
		return equipment;
	}

	public final boolean isAncientCurses() {
		return isAncientCurses;
	}

	public final byte getSpellBook() {
		return spellBook;
	}

	public final double[] getLevels() {
		return xp;
	}

//    public final int getId() {
//        return name;
//    }

	public final int getId(final Player player) {
		int i = 0;
		for (Entry<String, Preset> gear : player.getPresetManager().PRESET_SETUPS.entrySet()) {
			if (gear.getKey().toLowerCase().equals(name)) {
				return i;
			}
			i++;
		}
		throw new RuntimeException("failed to locate preset");
	}

}