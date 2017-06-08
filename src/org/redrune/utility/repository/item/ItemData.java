package org.redrune.utility.repository.item;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public final class ItemData {
	
	/**
	 * The bonuses of the item
	 */
	@Getter
	private final int[] bonuses;
	
	public ItemData(int[] bonuses) {
		this.bonuses = bonuses;
	}
}
