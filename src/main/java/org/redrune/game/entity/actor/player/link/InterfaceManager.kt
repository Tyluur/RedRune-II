package org.redrune.game.entity.actor.player.link

import org.redrune.cache.loaders.IComponentDefinitions
import org.redrune.game.entity.actor.player.Player
import org.redrune.game.entity.actor.player.data.PlayerInventory
import org.redrune.utility.constants.GameConstants
import java.util.concurrent.ConcurrentHashMap


class InterfaceManager(private val player: Player) {
    private val openedinterfaces = ConcurrentHashMap<Int, IntArray?>()
    private var resizableScreen = false
    var windowsPane = 0

    @Transient
    var displayMode = 0

    @Transient
    var screenHeight = 0

    @Transient
    var screenWidth = 0
    fun sendChatBoxInterface(interfaceId: Int) {
        player.packets.sendInterface(true, 752, CHAT_BOX_TAB, interfaceId)
        player.dialogueManager.updateComponents(interfaceId)
    }

    fun closeChatBoxInterface() {
        player.packets.closeInterface(CHAT_BOX_TAB)
    }

    fun sendOverlay(interfaceId: Int, fullScreen: Boolean) {
        sendTab(if (resizableScreen) if (fullScreen) 1 else 11 else 0, interfaceId)
    }

    fun sendTab(tabId: Int, interfaceId: Int) {
        player.packets.sendInterface(
            true,
            if (resizableScreen) RESIZABLE_WINDOW_ID else FIXED_WINDOW_ID,
            tabId,
            interfaceId
        )
    }

    fun closeOverlay(fullScreen: Boolean) {
        player.packets.closeInterface(if (resizableScreen) if (fullScreen) 1 else 11 else 0)
    }

    fun sendInterface(interfaceId: Int) {
        player.packets.sendInterface(
            false,
            if (resizableScreen) RESIZABLE_WINDOW_ID else FIXED_WINDOW_ID,
            if (resizableScreen) RESIZABLE_SCREEN_TAB_ID else FIXED_SCREEN_TAB_ID,
            interfaceId
        )
    }

    fun sendInterfacec(interfaceId: Int) {
        player.packets.sendInterface(
            true,
            if (resizableScreen) RESIZABLE_WINDOW_ID else FIXED_WINDOW_ID,
            if (resizableScreen) RESIZABLE_SCREEN_TAB_ID else FIXED_SCREEN_TAB_ID,
            interfaceId
        )
    }

    fun sendInventoryInterface(childId: Int) {
        player.packets.sendInterface(
            false,
            if (resizableScreen) RESIZABLE_WINDOW_ID else FIXED_WINDOW_ID,
            if (resizableScreen) RESIZABLE_INV_TAB_ID else FIXED_INV_TAB_ID,
            childId
        )
    }

    fun sendInterfaces() {
        if (player.interfaceManager.displayMode == 2 || player.interfaceManager.displayMode == 3) {
            resizableScreen = true
            sendFullScreenInterfaces()
        } else {
            resizableScreen = false
            sendFixedInterfaces()
        }
        player.combatDefinitions.sendUnlockAttackStylesButtons()
        player.musicManager.unlockMusicPlayer()
        player.inventory.unlockInventoryOptions()
        player.prayer.unlockPrayerBookButtons()
        if (player.familiar != null && player.isRunning) {
            player.familiar.unlock()
        }
        player.controllerManager.sendInterfaces()
    }

    fun replaceRealChatBoxInterface(interfaceId: Int) {
        player.packets.sendInterface(true, 752, 12, interfaceId)
    }

    fun closeReplacedRealChatBoxInterface() {
        player.packets.closeInterface(752, 12)
    }

    fun sendFixedInterfaces() {
        player.packets.sendWindowsPane(548, 0)

        sendTab(15, 745) // tbd

        sendTab(68, 751) // chatbox tabs

        sendTab(192, 752) // chatbox interface
        player.packets.sendInterface(true, 752, 9, 137)

        player.equipment.refresh()
        player.packets.sendGlobalConfig(181, 0)
        player.packets.sendUnlockIComponentOptionSlots(271, 8, 0, 29, 0)

        sendTab(if (resizableScreen) 248 else 264, 1153)

        // Blank Interface
        sendTab(17, 754)

        sendTab(if (resizableScreen) 247 else 263, 1152)

        /* -- the start of all tabs */
        sendTab(204, 884)

        // skills
        sendTab(if (resizableScreen) 91 else 205, 320)

        sendQuestTab()
        sendInventory()
        sendEquipment()
        sendPrayerBook()
        sendMagicBook()
        sendFamiliar()

        // Friends Interface
        sendTab(212, 550)

        // Friends Chat Interface
        sendTab(213, 1109)

        sendLogout()

        sendSettings()

        sendEmotes()

        // Music Interface
        sendTab(217, 187)
    }

    private fun sendLogout() {
        sendTab(if (resizableScreen) 100 else 214, 182)
    }

    private fun sendFullScreenInterfaces() {
        player.packets.sendWindowsPane(746, 0)

        // Blank Interface
        sendTab(15, 745)

        // ChatBox Tabs
        sendTab(19, 751)

        // ChatBox Interface
        sendTab(73, 752)
        player.packets.sendInterface(true, 752, 9, 137)

        // Blank Interface
        sendTab(72, 754)

        // Combat Interface
        sendTab(90, 884)

        // Skills Interface
        sendTab(91, 320)

        // Quests Interface
        sendQuestTab()

        // Inventory Interface
        sendInventory()

        // Equipment Interface
        sendEquipment()

        // Prayer Interface
        sendPrayerBook()

        // Magic Interface
        sendMagicBook()

        sendFamiliar()

        // Friends Interface
        sendTab(98, 550)

        // Friend Chat Interface
        sendTab(99, 1109)

        // Clan Chat Interface (Interface 589 = Lobby Clan Chat)
        sendClanChat()

        sendSettings()

        sendEmotes()

        // Music Interface
        sendTab(104, 187)

        sendNotes()

        // Logout Interface
        sendLogout()
    }

    fun sendCombatStyles() {
        sendTab(if (resizableScreen) 111 else 204, 884)
    }

    fun sendTaskSystem() {
        val interfaceId = 930
        sendTab(if (resizableScreen) 91 else 205, interfaceId)

        player.packets.sendIComponentText(interfaceId, 10, GameConstants.SERVER_NAME)
        player.packets.sendHideIComponent(interfaceId, 12, true) //scroll bar
        for (i in 17..24) {
            player.packets.sendHideIComponent(interfaceId, i, true)
        }
    }

    fun sendQuestTab() {
        val interfaceId = 34
        sendTab(if (resizableScreen) 92 else 206, interfaceId)

        NoteManager.refresh(player, true)
        NoteManager.sendUnlockNotes(player)
    }

    fun sendFamiliar() {
        sendTab(if (resizableScreen) 97 else 97, 662)
    }

    fun sendFriends() {
        sendTab(if (resizableScreen) 100 else 214, 550)
    }

    fun sendEmotes() {
        sendTab(if (resizableScreen) 102 else 216, 464)
    }

    fun sendFriendsChat() {
        sendTab(if (resizableScreen) 100 else 214, 1109)
    }

    fun sendClanChat() {
        val interfaceId = 1019
        sendTab(215, interfaceId)
    }

    fun sendMusic() {
        sendTab(if (resizableScreen) 104 else 218, 187)
    }

    fun sendNotes() {
        sendTab(if (resizableScreen) 104 else 218, 34)
        NoteManager.refresh(player, true)
    }

    fun sendInventory() {
        sendTab(if (resizableScreen) 93 else 207, PlayerInventory.INVENTORY_INTERFACE)
    }

    fun sendEquipment() {
        sendTab(if (resizableScreen) 94 else 208, 387)
    }

    fun closeQuestTab() {
        player.packets.closeInterface(if (resizableScreen) 93 else 207)
    }

    fun closeFriendsChat() {
        player.packets.closeInterface(if (resizableScreen) 100 else 214)
    }

    fun closeClanChat() {
        player.packets.closeInterface(if (resizableScreen) 101 else 215)
    }

    fun closeSettings() {
        player.packets.closeInterface(if (resizableScreen) 102 else 216)
    }

    fun closeMusic() {
        player.packets.closeInterface(if (resizableScreen) 104 else 218)
    }

    fun closeNotes() {
        player.packets.closeInterface(if (resizableScreen) 105 else 219)
    }

    fun closeFriends() {
        player.packets.closeInterface(if (resizableScreen) 99 else 213)
    }

    fun closeEquipment() {
        player.packets.closeInterface(if (resizableScreen) 95 else 209)
    }

    fun closeInventory() {
        player.packets.closeInterface(if (resizableScreen) 94 else 208)
    }

    fun sendSkills() {
        sendTab(if (resizableScreen) 30 else 151, 320)
    }

    @JvmOverloads
    fun sendSettings(interfaceId: Int = 261) {
        sendTab(if (resizableScreen) 101 else 215, interfaceId)
    }

    fun sendPrayerBook() {
        sendTab(if (resizableScreen) 95 else 209, 271)
    }

    fun sendMagicBook() {
        sendTab(if (resizableScreen) 96 else 210, player.combatDefinitions.spellBook)
    }

    fun addInterface(windowId: Int, tabId: Int, childId: Int): Boolean {
        if (openedinterfaces.containsKey(tabId)) {
            player.packets.closeInterface(tabId)
        }
        openedinterfaces[tabId] = intArrayOf(childId, windowId)
        return openedinterfaces[tabId]!![0] == childId
    }

    fun containsInterface(tabId: Int, childId: Int): Boolean {
        if (childId == windowsPane) {
            return true
        }
        return if (!openedinterfaces.containsKey(tabId)) {
            false
        } else openedinterfaces[tabId]!![0] == childId
    }

    fun getTabWindow(tabId: Int): Int {
        return if (!openedinterfaces.containsKey(tabId)) {
            FIXED_WINDOW_ID
        } else openedinterfaces[tabId]!![1]
    }

    fun containsInterface(childId: Int): Boolean {
        if (childId == windowsPane) {
            return true
        }
        for (value in openedinterfaces.values) {
            if (value!![0] == childId) {
                return true
            }
        }
        return false
    }

    fun removeAll() {
        openedinterfaces.clear()
    }

    fun containsScreenInter(): Boolean {
        return containsTab(if (resizableScreen) RESIZABLE_SCREEN_TAB_ID else FIXED_SCREEN_TAB_ID)
    }

    fun containsTab(tabId: Int): Boolean {
        return openedinterfaces.containsKey(tabId)
    }

    fun closeInterface(one: Int, two: Int) {
        player.packets.closeInterface(if (resizableScreen) two else one)
    }

    fun closePrayerBook() {
        player.packets.closeInterface(if (resizableScreen) 117 else 210)
    }

    fun closeMagicBook() {
        player.packets.closeInterface(if (resizableScreen) 118 else 211)
    }

    fun closeEmotes() {
        player.packets.closeInterface(if (resizableScreen) 124 else 217)
    }

    fun closeSkills() {
        player.packets.closeInterface(if (resizableScreen) 113 else 206)
    }

    fun closeCombatStyles() {
        player.packets.closeInterface(if (resizableScreen) 111 else 204)
    }

    fun closeTaskSystem() {
        player.packets.closeInterface(if (resizableScreen) 112 else 205)
    }

    fun containsInventoryInter(): Boolean {
        return containsTab(if (resizableScreen) RESIZABLE_INV_TAB_ID else FIXED_INV_TAB_ID)
    }

    fun closeInventoryInterface() {
        player.packets.closeInterface(if (resizableScreen) RESIZABLE_INV_TAB_ID else FIXED_INV_TAB_ID)
    }

    fun containsChatBoxInter(): Boolean {
        return containsTab(CHAT_BOX_TAB)
    }

    fun removeTab(tabId: Int): Boolean {
        return openedinterfaces.remove(tabId) != null
    }

    fun removeInterface(tabId: Int, childId: Int): Boolean {
        if (!openedinterfaces.containsKey(tabId)) {
            return false
        }
        return if (openedinterfaces[tabId]!![0] != childId) {
            false
        } else openedinterfaces.remove(tabId) != null
    }

    fun sendScreenInterface(backgroundInterface: Int, interfaceId: Int) {
        player.interfaceManager.closeScreenInterface()
        if (hasRezizableScreen()) {
            player.packets.sendInterface(false, RESIZABLE_WINDOW_ID, 9, backgroundInterface)
            player.packets.sendInterface(false, RESIZABLE_WINDOW_ID, 11, interfaceId)
        } else {
            player.packets.sendWindowsPane(interfaceId, 0)
        }
        player.setCloseInterfacesEvent {
            if (hasRezizableScreen()) {
                player.packets.closeInterface(9)
                player.packets.closeInterface(11)
            } else {
                player.packets.sendWindowsPane(FIXED_WINDOW_ID, 0)
            }
        }
    }

    fun closeScreenInterface() {
        player.packets.closeInterface(if (resizableScreen) RESIZABLE_SCREEN_TAB_ID else FIXED_SCREEN_TAB_ID)
    }

    fun hasRezizableScreen(): Boolean {
        return resizableScreen
    }

    fun openGameTab(tabId: Int): Int {
        player.packets.sendGlobalConfig(168, tabId)
        return 4
    }

    val chatboxInterface: Int
        get() {
            val ids = openedinterfaces[CHAT_BOX_TAB] ?: return -1
            for (id in ids) {
                if (id == 752) {
                    continue
                }
                return id
            }
            return -1
        }

    fun getDialogueInterfaceDefinitions(text: String?): IComponentDefinitions? {
        val chatboxInterface = player.interfaceManager.chatboxInterface
        if (chatboxInterface == -1) {
            return null
        }
        val optional = IComponentDefinitions.getComponentByText(chatboxInterface, text)
        return optional.orElse(null)
    }

    companion object {
        const val FIXED_WINDOW_ID = 548
        const val RESIZABLE_WINDOW_ID = 746
        const val CHAT_BOX_TAB = 13
        const val FIXED_SCREEN_TAB_ID = 9
        const val FIXED_SCREEN2_TAB_ID = 11
        const val RESIZABLE_SCREEN_TAB_ID = 12
        const val FIXED_INV_TAB_ID = 199
        const val RESIZABLE_INV_TAB_ID = 87
    }
}