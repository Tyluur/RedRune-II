package org.redrune.network;

import org.redrune.rs2.GameConstants;

/**
 * All network constants are stored here.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface NetworkConstants extends GameConstants {
	
	/**
	 * The revision of the game
	 */
	int REVISION = 666;
	
	/**
	 * Client sub revision
	 */
	int SUB_REVISION = 1;
	
	/**
	 * The opcode that symbolizes a js5 request
	 */
	int JS5_REQUEST = 15;
	
	/**
	 * The opcode that symbolizes a login request
	 */
	int LOGIN_REQUEST = 14;
	
	/**
	 * The opcode to check the email received is good
	 */
	int EMAIL_VERIFICATION = 28;
	
	/**
	 * The opcode that is sent for account creation
	 */
	int CREATE_ACCOUNT = 22;
	
	/**
	 * The update server keys
	 */
	int[] DATA = { 56, 79325, 55568, 46770, 24563, 299978, 44375, 0, 4176, 3589, 109125, 604031, 176138, 292288, 350498, 686783, 18008, 20836, 16339, 1244, 8142, 743, 119, 699632, 932831, 3931, 2974, };
	
	/**
	 * The packet sizes.
	 */
	byte[] PACKET_SIZES = { 0, 7, -1, 8, 3, -1, 15, 8, 6, -1, // 1-10
			3, 8, -1, -1, 3, 4, 7, 8, 1, -1, // 11-20
			4, 2, -1, 7, 7, 8, 16, 3, 7, 3, // 21-30
			-1, -1, 4, 0, 6, -1, 6, 4, 7, 7, // 31-40
			8, 0, 15, 3, 3, 7, -1, 3, 8, 7, // 41-50
			-1, 3, 4, 18, 8, -1, 5, 11, 7, -1, // 51-60
			1, 3, -1, 4, 0, 11, 8, 2, -1, 3, 3, // 61-70
			16, 3, 2, -1, 7, 4, 2, 3, -1, -1, -1, // 71-80
			-1, 3, 8, 8, 7, 0, -1, -1, 3, 3, 4, // 81-90
			-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 91-100
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 101-110
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 111-120
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 121-130
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 131-140
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 141-150
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 151-160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 161-170
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 171-180
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 181-190
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 191-200
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 201-210
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 211-220
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 221-230
			0, 0, 0, 0, 0, 0, 0, 0, 0, }; // 231-240
	
	/**
	 * The login token string
	 */
	String LOGIN_TOKEN = "wwGlrZHF5gKN6D3mDdihco3oPeYN2KFybL9hUUFqOvk";
}
