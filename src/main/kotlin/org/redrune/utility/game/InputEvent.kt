package org.redrune.utility.game

/**
 * @author Tyluur<itstyluur@icloud.com>
 * @since May 3, 2015
 */
@Suppress("UNCHECKED_CAST")
abstract class InputEvent(
    /**
     * The text that is shown over the input box
     */
    @JvmField val text: String,
    /**
     * The generic event type
     */
    @JvmField val type: InputEventType,
) {
    /**
     * Handles the input of data over the dialogue box
     */
    abstract fun handleInput()
    fun <K> getInput(): K {
        return input as K
    }

    /**
     * @param input the input to set
     */
    fun setInput(input: Any?) {
        this.input = input
    }
    /**
     * @return the inputText
     */
    /**
     * Gets the type of input event this is
     *
     * @return A `InputEventType` object
     */

    /**
     * The data that has been input back
     */
    private var input: Any? = null

    /**
     * The enum of possible input event types.
     *
     * @author Tyluur
     */
    enum class InputEventType
    /**
     * @return the scriptId
     */(
        /**
         * The script id
         */
        @JvmField val scriptId: Int,
    ) {
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
        LONG_TEXT(110)
    }
}