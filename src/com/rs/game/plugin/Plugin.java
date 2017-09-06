package com.rs.game.plugin;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/30/2017
 */
public abstract class Plugin  {
	
	/**
	 * Handles the registration of a plugin
	 */
	public abstract void register();
	
	/**
	 * Converts a varargs parameter to the String[] array
	 *
	 * @param varArgs
	 * 		The var args
	 */
	public String[] arguments(String... varArgs) {
		return varArgs;
	}
	
	/**
	 * Converts a varargs parameter to the String[] array
	 *
	 * @param varArgs
	 * 		The var args
	 */
	public int[] arguments(int... varArgs) {
		return varArgs;
	}
}
