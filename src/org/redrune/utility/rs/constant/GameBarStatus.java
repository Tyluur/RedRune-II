package org.redrune.utility.rs.constant;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public enum GameBarStatus {
	
	/**
	 * This status flag means that the selected option is on 'on'
	 */
	ON(0),
	
	/**
	 * This status flag means that the selected option is on 'friends'
	 */
	FRIENDS(1),
	
	/**
	 * This status flag means that the selected option is on 'off'
	 */
	OFF(2),
	
	/**
	 * This status flag means that the selected option is on 'hide'
	 */
	HIDE(3),
	
	/**
	 * This flag is only used for the 'game' option, this is the flag that means we should not filter messages
	 */
	NO_FILTER(0),
	
	/**
	 * This flag is only used for the 'game' option, this is the flag that means we should filter messages
	 */
	FILTER(1);
	
	/**
	 * The value to send for this status
	 */
	@Getter
	private final int value;
	
	GameBarStatus(int value) {
		this.value = value;
	}
	
}
