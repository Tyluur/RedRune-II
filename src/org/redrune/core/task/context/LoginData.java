package org.redrune.core.task.context;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class LoginData {
	
	/**
	 * The uid of the session
	 */
	@Getter
	private final long uid;
	
	/**
	 * The username of the session
	 */
	@Getter
	private final String username;
	
	/**
	 * The password of the session
	 */
	@Getter
	private final String password;
	
	/**
	 * The response code of the login attempt
	 */
	@Getter
	private final int code;
	
	/**
	 * If the session is going to the lobby
	 */
	@Getter
	private final boolean lobby;
	
	/**
	 * The file text received back
	 */
	@Getter
	private final String fileText;
	
	public LoginData(long uid, String username, String password, int code, boolean lobby, String fileText) {
		this.username = username;
		this.password = password;
		this.uid = uid;
		this.code = code;
		this.lobby = lobby;
		this.fileText = fileText;
	}
	
	@Override
	public String toString() {
		return "LoginData{" + "uid=" + uid + ", username='" + username + '\'' + ", password='" + password + '\'' + ", code=" + code + ", lobby=" + lobby + '}';
	}
}
