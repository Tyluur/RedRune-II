package game.entity.actor.player.link

import cache.codec.loaders.IComponentDefinitions
import game.content.entity.actor.player.skills.PresetHandler
import game.entity.actor.player.Player
import game.entity.actor.player.data.PlayerInventory
import utility.constants.ColorConstants
import utility.constants.GameConstants
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
        // Blank Interface
        sendTab(15, 745)

        // ChatBox Tabs
        sendTab(68, 751)

        // ChatBox Interface
        sendTab(192, 752)
        player.packets.sendInterface(true, 752, 9, 137)

        // Blank Interface
        sendTab(17, 754)

        // HP Orb
        sendTab(183, 748)

        // Prayer Orb
        sendTab(185, 749)

        // Energy Orb
        sendTab(186, 750)

        // Summoning Orb
        sendTab(188, 747)

        // Combat Interface
        sendTab(204, 884)

        sendTaskSystem()

        // Skills Interface
        sendTab(206, 320)

        // quests Interface
        sendQuestTab()

        // Inventory Interface
        sendInventory()

        // Equipment Interface
        sendEquipment()

        // Prayer Interface
        sendPrayerBook()

        // Magic Interface
        sendMagicBook()

        // Friends Interface
        sendTab(213, 550)

        // Friends Chat Interface
        sendTab(214, 1109)

        // Clan Chat Interface
        sendClanChat()

        // Settings Interface
        sendSettings()

        // Emotes Interface
        sendTab(217, 464)

        // Music Interface
        sendTab(218, 187)

        //// notes Interface
        sendNotes()

        // Logout Interface
        sendTab(222, 182) // Logout tab
    }

    fun sendFullScreenInterfaces() {
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

        // HP Orb
        sendTab(177, 748)

        // Prayer Orb
        sendTab(178, 749)

        // Energy Orb
        sendTab(179, 750)

        // Summoning Orb
        sendTab(180, 747)

        // Combat Interface
        sendTab(90, 884)
        sendTaskSystem()

        // Skills Interface
        sendTab(92, 320)

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

        // Friends Interface
        sendTab(99, 550)

        // Friend Chat Interface
        sendTab(100, 1109)

        // Clan Chat Interface (Interface 589 = Lobby Clan Chat)
        sendClanChat()

        // Settings Interface
        sendSettings()

        // Emotes Interface
        sendTab(103, 464)

        // Music Interface
        sendTab(104, 187)

        // Notes Interface
        sendNotes()
        // Logout Interface
        sendTab(108, 182)
    }

    fun sendCombatStyles() {
        sendTab(if (resizableScreen) 111 else 204, 884)
    }

    fun sendTaskSystem() {

    }

    fun sendQuestTab() {
        val interfaceId = 34
        sendTab(if (resizableScreen) 91 else 205, interfaceId)

        PresetHandler.sendLoginConfiguration(player)
        PresetHandler.unlock(player)
        player.packets.sendHideIComponent(930, 12, true) //scroll bar
        for (i in 17..24) {
            player.packets.sendHideIComponent(930, i, true)
        }
        PresetHandler.refresh(player)
    }

    fun sendFriends() {
        sendTab(if (resizableScreen) 100 else 214, 550)
    }

    fun sendEmotes() {
        sendTab(if (resizableScreen) 124 else 217, 464)
    }

    fun sendFriendsChat() {
        sendTab(if (resizableScreen) 100 else 214, 1109)
    }

    fun sendClanChat() {
        val interfaceId = 1019
        sendTab(215, interfaceId)
        player.packets.sendIComponentText(interfaceId, 10, "<col=" + ColorConstants.RED + ">Information")
        player.packets.sendIComponentText(interfaceId, 16, "")
        player.packets.sendIComponentText(interfaceId, 3, "Player support")
        player.packets.sendIComponentText(interfaceId, 16, "Report Bug")
        player.packets.sendIComponentText(interfaceId, 18, "Submit Ticket")
        player.packets.sendIComponentText(interfaceId, 11, " ")
        player.packets.sendIComponentText(
            interfaceId,
            0,
            "Report any game/website bug you have found on " + GameConstants.SERVER_NAME + ""
        )
        player.packets.sendIComponentText(interfaceId, 8, "Submit help-request ticket to online staff member ")
    }

    fun sendMusic() {
        sendTab(if (resizableScreen) 104 else 218, 187)
    }

    fun sendNotes() {
        /*	sendTab(resizableScreen ? 105 : 219, 34);
		NoteManager.refresh(player, true);*/
    }

    fun sendEquipment() {
        sendTab(if (resizableScreen) 95 else 209, 387)
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

    fun sendInventory() {
        sendTab(if (resizableScreen) 94 else 208, PlayerInventory.INVENTORY_INTERFACE)
    }

    fun closeInventory() {
        player.packets.closeInterface(if (resizableScreen) 94 else 208)
    }

    fun sendSkills() {
        sendTab(if (resizableScreen) 30 else 151, 320)
    }

    @JvmOverloads
    fun sendSettings(interfaceId: Int = 261) {
        sendTab(if (resizableScreen) 102 else 216, interfaceId)
    }

    fun sendPrayerBook() {
        sendTab(if (resizableScreen) 96 else 210, 271)
    }

    fun sendMagicBook() {
        sendTab(if (resizableScreen) 97 else 211, player.combatDefinitions.spellBook)
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