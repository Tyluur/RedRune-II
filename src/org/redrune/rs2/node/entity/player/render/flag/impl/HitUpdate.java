package org.redrune.rs2.node.entity.player.render.flag.impl;

import org.redrune.network.stream.IoWriteEvent;
import org.redrune.rs2.node.entity.Entity;
import org.redrune.rs2.node.entity.data.Hit;
import org.redrune.rs2.node.entity.player.render.flag.UpdateFlag;

/**
 * Represents the hit update mask.
 *
 * @author Emperor
 */
public class HitUpdate extends UpdateFlag {
	
	/**
	 * The entity.
	 */
	public final Entity entity;
	
	/**
	 * Constructs a new {@code HitUpdate} {@code Object}.
	 *
	 * @param entity
	 * 		The entity.
	 */
	public HitUpdate(Entity entity) {
		this.entity = entity;
	}
	
	// TODO: implement interactingWith (colored if we are fighting the guy hitting us or smomeshit)
	
	@Override
	public void write(IoWriteEvent bldr) {
		final int size = entity.getHitMap().getHitList().size();
		bldr.writeByteA(size); //Amount of hits
		if (size == 0) {
			return;
		}
		int hitpoints = entity.getHitpoints();
		int maxHitpoints = entity.getMaxHitpoints();
		if (hitpoints > maxHitpoints) {
			hitpoints = maxHitpoints;
		}
		int hpBarPercentage = (hitpoints == 0 || maxHitpoints == 0) ? 0 : (hitpoints * 255 / maxHitpoints);
		for (Hit hit : entity.getHitMap().getHitList()) {
			if (hit.getSoaked() > 0) {
				bldr.writeSmart(32767);
			}
			int type = hit.getSplat().ordinal();
			if (type != 9) {
				if (hit.getDamage() < 1) {
					type = 8;
				} else if (hit.isCritical()) {
					type += 10;
				}
				if (false/*hit.getSource() == bldr.getPlayer() || entity == bldr.getPlayer()*/) {
					bldr.writeSmart(type);
				} else {
					bldr.writeSmart(type + 14);
				}
			} else {
				bldr.writeSmart(type);
			}
			bldr.writeSmart(hit.getDamage());
			if (hit.getSoaked() > 0) {
				if (false/*hit.getSource() == entity || entity == bldr.getPlayer()*/) {
					bldr.writeSmart(5);
				} else {
					bldr.writeSmart(19);
				}
				bldr.writeSmart(hit.getSoaked());
			}
			bldr.writeSmart(hit.getDelay());
			if (entity.isNPC()) {
				bldr.writeByteA(hpBarPercentage);
			} else {
				bldr.write(hpBarPercentage);
			}
		}
	}
	
	@Override
	public int getMaskData() {
		return entity.isNPC() ? 0x10 : 0x8;
	}
	
	@Override
	public int getOrdinal() {
		return entity.isNPC() ? 7 : 2;
	}
	
}