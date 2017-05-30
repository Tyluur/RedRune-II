package org.redrune.network.rs666.packet.input;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
@FunctionalInterface
public interface InputResponse {
	
	/**
	 * Runs the response
	 *
	 * @param input
	 * 		The input characters
	 */
	void run(String input);
}
