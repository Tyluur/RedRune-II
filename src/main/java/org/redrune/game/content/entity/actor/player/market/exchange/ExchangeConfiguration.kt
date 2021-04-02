package org.redrune.game.content.entity.actor.player.market.exchange

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since April 02, 2021
 */
object ExchangeConfiguration {

    /**
     * The interface id of the main interface
     */
    const val MAIN_INTERFACE = 105

    /**
     * The interface id of the sell interface
     */
    const val SELL_INTERFACE = 107

    /**
     * The interface id of the collection interface
     */
    const val COLLECTION_INTERFACE = 109

    /**
     * The array of the buttons used to send the buying interfaces
     */
    val BUY_BUTTON_IDS = intArrayOf(31, 82, 101, 47, 63, 120)

    /**
     * The array of the buttons used to initaliaze the selling process
     */
    val SELL_BUTTON_IDS = intArrayOf(83, 32, 48, 102, 121, 64)

    /**
     * The array of the component ids that are used to display offers in the
     * collection interface
     */
    val COLLECTION_COMPONENTS = intArrayOf(19, 23, 27, 32, 37, 42)

    enum class Progress(val value: Int) {
        BUY_ABORTED(5),
        SELL_ABORTED(-3),
        RESET(0),
        BUY_PROGRESSING(4),
        FINISHED_BUYING(5),
        SELL_PROGRESSING(11),
        FINISHED_SELLING(13);
    }

}