package org.redrune.game.entity.actor.player.data

import org.redrune.game.entity.actor.player.Player
import org.redrune.utility.functions.Misc
import java.util.*

/**
 * The rights the player can have
 *
 * @author Tyluur <itstyluur></itstyluur>@icloud.com>
 * @since 5/18/2017
 */
enum class PlayerRight(clientRight: Int, memberGroupId: Int) {
    OWNER(2, 19) {
        override val messageIcon = 2
    },
    ADMINISTRATOR(2, 14) {
        override fun create() {
            addOtherRights(OWNER, ADMINISTRATOR)
        }

        override val messageIcon = 2
    },
    SERVER_MODERATOR(1, 7) {
        override fun create() {
            addOtherRights(OWNER, ADMINISTRATOR)
        }

        override val messageIcon = 1
    },
    DONATOR(9), YOUTUBER(18), VETERAN(13), RESPECTED_MEMBER(15), BETA_TESTER(21), PLAYER(3);

    /**
     * The rights the player has in the client
     */
    val clientRight: Byte

    /**
     * The member group id of the right
     */
    val memberGroupId: Byte

    /**
     * The rights that can also access this right
     */
    private val rightsWithAccess: MutableSet<PlayerRight>

    /**
     * Constructs a right with a client right of 0
     *
     * @param memberGroupId The id of the member group for the forum
     */
    constructor(memberGroupId: Int) : this(0, memberGroupId)

    /**
     * Called on the creation of a right, due to enums not being able to call other values below them while
     * constructing
     */
    open fun create() {}

    /**
     * Gets the icon that is displayed on messages
     */
    open val messageIcon: Int
        get() = 0

    /**
     * Gets the formatted name of the right
     */
    val formattedName: String
        get() = Misc.formatPlayerNameForDisplay(name)

    /**
     * Adds other rights that can access this right
     */
    fun addOtherRights(vararg rights: PlayerRight) {
        rightsWithAccess.addAll(listOf(*rights))
    }

    /**
     * Checking that the player has access to this right
     *
     * @param player The player
     */
    fun playerHasRights(player: Player): Boolean {
        for (right in rightsWithAccess) {
            if (player.rightsContains(right)) {
                return true
            }
        }
        return false
    }

    companion object {
        /**
         * Finds the right optional by the [PlayerRight.name].
         *
         * @param name The name to look for.
         */
        @JvmStatic
        fun getRightByName(name: String?): Optional<PlayerRight> {
            return Arrays.stream(values()).filter { right: PlayerRight -> right.name.equals(name, ignoreCase = true) }
                .findFirst()
        }

        /**
         * Finds the right by the group id
         *
         * @param memberGroupId The group id to look for
         */
        fun getRightByGroupId(memberGroupId: Int): Optional<PlayerRight> {
            return Arrays.stream(values()).filter { right: PlayerRight -> right.memberGroupId.toInt() == memberGroupId }
                .findFirst()
        }
    }

    /**
     * Constructs a right
     *
     * @param clientRight   The client right
     * @param memberGroupId The id of the member group for the forum
     */
    init {
        this.clientRight = clientRight.toByte()
        this.memberGroupId = memberGroupId.toByte()
        rightsWithAccess = LinkedHashSet()
        rightsWithAccess.add(this)
        create()
    }
}