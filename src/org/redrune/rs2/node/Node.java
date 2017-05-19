package org.redrune.rs2.node;

/**
 * This is the parent class of all game nodes. Nodes are anything in the game which undergoes
 * registration/de-registration and is interactible.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Node {
	
	/**
	 * Handles the registration of the node
	 */
	public abstract void register();
	
	/**
	 * Handles the de-registration of the node
	 */
	public abstract void deregister();
	
	/**
	 * Gets the size of the node
	 *
	 * @return The size of the node
	 */
	public abstract int getSize();
	
}
