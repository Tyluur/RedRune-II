package org.redrune.utility.game;

/**
 * @author Tyluur<itstyluur@icloud.com>
 * @since May 3, 2015
 */
public abstract class InputEvent {
	
	public InputEvent(String text, InputEventType type) {
		this.text = text;
		this.type = type;
	}
	
	/**
	 * Handles the input of data over the dialogue box
	 */
	public abstract void handleInput();
	
	/**
	 * Gets the type of input event this is
	 *
	 * @return A {@code InputEventType} object
	 */
	public InputEventType getType() {
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public <K> K getInput() {
		return (K) input;
	}
	
	/**
	 * @param input
	 * 		the input to set
	 */
	public void setInput(Object input) {
		this.input = input;
	}
	
	/**
	 * @return the inputText
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * The text that is shown over the input box
	 */
	private final String text;
	
	/**
	 * The generic event type
	 */
	private final InputEventType type;
	
	/**
	 * The data that has been input back
	 */
	private Object input;
	
	/**
	 * The enum of possible input event types.
	 *
	 * @author Tyluur
	 */
	public enum InputEventType {
		
		/**
		 * The integer input event type. Only numbers are allowed
		 */
		INTEGER(108),
		/**
		 * The name input event type. 12 characters max
		 */
		NAME(109),
		/**
		 * The long text input event type. This can be entered for a long time
		 */
		LONG_TEXT(110);
		
		InputEventType(int scriptId) {
			this.scriptId = scriptId;
		}
		
		/**
		 * @return the scriptId
		 */
		public int getScriptId() {
			return scriptId;
		}
		
		/**
		 * The script id
		 */
		private final int scriptId;
	}
	
}