package game

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/30/2017
 */
object GameFlags {

    /**
     * If the server was launched in debug mode
     */
	@JvmField
	var debugMode = false

    /**
     * If the server was launched in host mode
     */
    var hostMode = false

    /**
     * If the world is a pvp world
     */
	@JvmField
	var pvpWorld = false
}