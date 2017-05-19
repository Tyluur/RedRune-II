package org.redrune.utility.backend;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public enum CreationResponse {
	
	/**
	 * The successful response code
	 */
	SUCCESSFUL(2),
	
	/**
	 * The response code that marks a busy server
	 */
	BUSY_SERVER(7),
	
	/**
	 * The response code that says you can't create an account at the moment
	 */
	YOU_CANNOT_CREATE_AT_THE_MOMENT(10),
	
	/***
	 * The response code that says the email is taken
	 */
	TAKEN_EMAIL(20),
	
	/**
	 * The response code that says the email is invalid
	 */
	INVALID_EMAIL(21);
	
	/**
	 * The value.
	 */
	@Getter
	private final byte value;
	
	/**
	 * Constructs a new {@code ReturnCodes} {@code Object}.
	 *
	 * @param value
	 * 		The value.
	 */
	CreationResponse(int value) {
		this.value = (byte) value;
	}
	}
