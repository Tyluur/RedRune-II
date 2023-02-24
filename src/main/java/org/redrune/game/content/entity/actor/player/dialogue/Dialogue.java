package org.redrune.game.content.entity.actor.player.dialogue;

import org.redrune.cache.loaders.IComponentDefinitions;
import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.cache.loaders.NPCDefinitions;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.constants.ChatAnimations;
import org.redrune.utility.constants.ColorConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Dialogue implements ChatAnimations, ColorConstants {

    public static final int FIRST = 1, SECOND = 2, THIRD = 3, FOURTH = 4, FIFTH = 5, YES = 1, NO = 2;
    public static final int OPTION_1 = 11, OPTION_2 = 13, OPTION_3 = 14, OPTION_4 = 15, OPTION_5 = 16;
    protected static final short SEND_1_TEXT_INFO = 210;
    protected static final short SEND_2_TEXT_INFO = 211;
    protected static final short SEND_3_TEXT_INFO = 212;
    protected static final short SEND_4_TEXT_INFO = 213;
    protected static final String DEFAULT_OPTION = "Select an Option";
    protected static final short SEND_2_OPTIONS = 236;
    protected static final short SEND_3_OPTIONS = 235;
    protected static final short SEND_4_OPTIONS = 237;
    protected static final short SEND_5_OPTIONS = 238;
    protected static final short SEND_2_LARGE_OPTIONS = 229;
    protected static final short SEND_3_LARGE_OPTIONS = 231;
    protected static final short SEND_1_TEXT_CHAT = 241;
    protected static final short SEND_2_TEXT_CHAT = 242;
    protected static final short SEND_3_TEXT_CHAT = 243;
    protected static final short SEND_4_TEXT_CHAT = 244;
    protected static final short SEND_NO_CONTINUE_1_TEXT_CHAT = 245;
    protected static final short SEND_NO_CONTINUE_2_TEXT_CHAT = 246;
    protected static final short SEND_NO_CONTINUE_3_TEXT_CHAT = 247;
    protected static final short SEND_NO_CONTINUE_4_TEXT_CHAT = 248;
    protected static final short SEND_NO_EMOTE = -1;
    protected static final byte IS_NOTHING = -1;
    protected static final byte IS_PLAYER = 0;
    protected static final byte IS_NPC = 1;
    protected static final byte IS_ITEM = 2;
    public Object[] parameters;
    protected int first, second, third, fourth, fifth;
    protected Player player;
    protected byte stage = -1;

    public Dialogue() {

    }

    public static boolean sendNPCDialogueNoContinue(Player player, int npcId, int animationId, String... text) {
        return sendEntityDialogueNoContinue(player, IS_NPC, npcId, animationId, text);
    }

    /*
     *
     * auto selects title, new dialogues
     */
    public static boolean sendEntityDialogueNoContinue(Player player, int type, int entityId, int animationId, String... text) {
        String title = "";
        if (type == IS_PLAYER) {
            title = player.getDisplayName();
        } else if (type == IS_NPC) {
            title = NPCDefinitions.getNPCDefinitions(entityId).getName();
        } else if (type == IS_ITEM) {
            title = ItemDefinitions.getItemDefinitions(entityId).getName();
        }
        return sendEntityDialogueNoContinue(player, type, title, entityId, animationId, text);
    }

    public static boolean sendEntityDialogueNoContinue(Player player, int type, String title, int entityId, int animationId, String... texts) {
        StringBuilder builder = new StringBuilder();
        for (int line = 0; line < texts.length; line++) {
            builder.append(" " + texts[line]);
        }
        String text = builder.toString();
        player.getInterfaceManager().replaceRealChatBoxInterface(1192);
        player.getPackets().sendIComponentText(1192, 16, title);
        player.getPackets().sendIComponentText(1192, 12, text);
        player.getPackets().sendEntityOnIComponent(type == IS_PLAYER, entityId, 1192, 11);
        if (animationId != -1) {
            player.getPackets().sendIComponentAnimation(animationId, 1192, 11);
        }
        return true;
    }

    public static boolean sendPlayerDialogueNoContinue(Player player, int animationId, String... text) {
        return sendEntityDialogueNoContinue(player, IS_PLAYER, -1, animationId, text);
    }

    public static boolean sendEmptyDialogue(Player player) {
        player.getInterfaceManager().replaceRealChatBoxInterface(89);
        return true;
    }

    public static void closeNoContinueDialogue(Player player) {
        player.getInterfaceManager().closeReplacedRealChatBoxInterface();
    }

    private static int[] getIComponentsIds(short interId) {
        int[] childOptions;
        switch (interId) {

            case 458:
                childOptions = new int[4];
                for (int i = 0; i < childOptions.length; i++) {
                    childOptions[i] = i;
                }
                break;
            case 210:
                childOptions = new int[1];
                childOptions[0] = 1;
                break;

            case 211:
                childOptions = new int[2];
                childOptions[0] = 1;
                childOptions[1] = 2;
                break;

            case 212:
                childOptions = new int[3];
                childOptions[0] = 1;
                childOptions[1] = 2;
                childOptions[2] = 3;
                break;

            case 213:
                childOptions = new int[4];
                childOptions[0] = 1;
                childOptions[1] = 2;
                childOptions[2] = 3;
                childOptions[3] = 4;
                break;

            case 229:
                childOptions = new int[3];
                childOptions[0] = 1;
                childOptions[1] = 2;
                childOptions[2] = 3;
                break;

            case 230:
                childOptions = new int[4];
                childOptions[0] = 1;
                childOptions[1] = 2;
                childOptions[2] = 3;
                childOptions[3] = 4;
                break;

            case 231:
                childOptions = new int[4];
                childOptions[0] = 1;
                childOptions[1] = 2;
                childOptions[2] = 3;
                childOptions[3] = 4;
                break;

            case 235:
                childOptions = new int[4];
                childOptions[0] = 1;
                childOptions[1] = 2;
                childOptions[2] = 3;
                childOptions[3] = 4;
                break;

            case 236:
                childOptions = new int[3];
                childOptions[0] = 0;
                childOptions[1] = 1;
                childOptions[2] = 2;
                break;

            case 237:
                childOptions = new int[5];
                childOptions[0] = 0;
                childOptions[1] = 1;
                childOptions[2] = 2;
                childOptions[3] = 3;
                childOptions[4] = 4;
                break;

            case 238:
                childOptions = new int[6];
                childOptions[0] = 0;
                childOptions[1] = 1;
                childOptions[2] = 2;
                childOptions[3] = 3;
                childOptions[4] = 4;
                childOptions[5] = 5;
                break;

            case 64:
                childOptions = new int[2];
                childOptions[0] = 3;
                childOptions[1] = 4;
                break;

            case 65:
                childOptions = new int[3];
                childOptions[0] = 3;
                childOptions[1] = 4;
                childOptions[2] = 5;
                break;

            case 66:
                childOptions = new int[4];
                childOptions[0] = 3;
                childOptions[1] = 4;
                childOptions[2] = 5;
                childOptions[3] = 6;
                break;

            case 67:
                childOptions = new int[5];
                childOptions[0] = 3;
                childOptions[1] = 4;
                childOptions[2] = 5;
                childOptions[3] = 6;
                childOptions[4] = 7;
                break;

            case 241:
            case 245:
                childOptions = new int[2];
                childOptions[0] = 3;
                childOptions[1] = 4;
                break;

            case 242:
            case 246:
                childOptions = new int[3];
                childOptions[0] = 3;
                childOptions[1] = 4;
                childOptions[2] = 5;
                break;

            case 243:
            case 247:
                childOptions = new int[4];
                childOptions[0] = 3;
                childOptions[1] = 4;
                childOptions[2] = 5;
                childOptions[3] = 6;
                break;

            case 244:
            case 248:
                childOptions = new int[5];
                childOptions[0] = 3;
                childOptions[1] = 4;
                childOptions[2] = 5;
                childOptions[3] = 6;
                childOptions[4] = 7;
                break;

            case 214:
            case 215:
            case 216:
            case 217:
            case 218:
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 232:
            case 233:
            case 234:
            case 239:
            case 240:
            default:
                return null;
        }
        return childOptions;
    }

    private static String[] getMessages(String title, String[] message) {
        List<String> textList = new ArrayList<>();
        textList.add(title);
        Collections.addAll(textList, message);
        return textList.toArray(new String[textList.size()]);
    }

    public abstract void start();

    public abstract void run(int interfaceId, int componentId);

    public abstract void finish();

    public void sendNPCDialogue(int npcId, int animation, String... message) {
        sendEntityDialogue(true, npcId, animation, message);
    }

    public void sendPlayerDialogue(int animation, String... message) {
        sendEntityDialogue(false, player.getIndex(), animation, message);
    }

    public void sendItemDialogue(int itemId, int itemAmount, String... messages) {
        int l = messages.length;
        short interfaceId = (l == 1 ? SEND_1_TEXT_CHAT : l == 2 ? SEND_2_TEXT_CHAT : l == 3 ? SEND_3_TEXT_CHAT : SEND_4_TEXT_CHAT);
        List<String> text = new ArrayList<>();
        text.add("");
        Collections.addAll(text, messages);
        String[] message = text.toArray(new String[text.size()]);
        sendEntityDialogue(interfaceId, message, IS_ITEM, itemId, itemAmount);
    }

    public void sendDialogue(String... text) {
        int l = text.length;
        short interfaceId = (l == 4 ? SEND_4_TEXT_INFO : l == 3 ? SEND_3_TEXT_INFO : l == 2 ? SEND_2_TEXT_INFO : SEND_1_TEXT_INFO);
        sendDialogue(interfaceId, text);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    protected final void end() {
        player.getDialogueManager().finishDialogue();
    }

    public boolean sendDialogue(short interId, String... talkDefinitons) {
        int[] componentOptions = getIComponentsIds(interId);
        if (componentOptions == null) {
            return false;
        }
        if (player == null) {
            return false;
        }
        player.getInterfaceManager().sendChatBoxInterface(interId);
        int properLength = (interId > 213 ? talkDefinitons.length - 1 : talkDefinitons.length);
        if (properLength != componentOptions.length) {
            return false;
        }
        for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++) {
            player.getPackets().sendIComponentText(interId, componentOptions[childOptionId], talkDefinitons[childOptionId]);
        }
        return true;
    }

    public boolean sendEntityDialogue(short interId, String[] talkDefinitons, byte type, int entityId, int animationId) {
        int[] componentOptions = getIComponentsIds(interId);
        if (componentOptions == null) {
            return false;
        }
        player.getInterfaceManager().sendChatBoxInterface(interId);
        if (talkDefinitons.length != componentOptions.length) {
            return false;
        }
        for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++) {
            player.getPackets().sendIComponentText(interId, componentOptions[childOptionId], talkDefinitons[childOptionId]);
        }
        if (type == IS_PLAYER || type == IS_NPC) {
            player.getPackets().sendEntityOnIComponent(type == IS_PLAYER, entityId, interId, 2);
            if (animationId != -1) {
                player.getPackets().sendIComponentAnimation(animationId, interId, 2);
            }
        } else if (type == IS_ITEM) {
            player.getPackets().sendItemOnIComponent(interId, 2, entityId, animationId);
        }
        return true;
    }

    private void sendEntityDialogue(boolean npc, int entityId, int animationId, String... message) {
        StringBuilder bldr = new StringBuilder();
        int interfaceId = npc ? 240 : 63;
        for (String element : message) {
            interfaceId++;
        }
        for (String element : message) {
            bldr.append(" ").append(element);
        }
        int[] componentOptions = getIComponentsIds((short) interfaceId);
        String title = npc ? NPCDefinitions.getNPCDefinitions(entityId).getName() : player.getDisplayName();
        String[] messages = getMessages(title, message);
        if (componentOptions == null || (messages.length) != componentOptions.length) {
            return;
        }
        player.getInterfaceManager().sendChatBoxInterface(interfaceId);
        for (int i = 0; i < componentOptions.length; i++) {
            player.getPackets().sendIComponentText(interfaceId, componentOptions[i], messages[i]);
        }
        player.getPackets().sendEntityOnIComponent(!npc, entityId, interfaceId, 2);
        player.getPackets().sendIComponentAnimation(animationId, interfaceId, 2);
    }

    public void player(int animationId, String... message) {
        sendEntityDialogue(false, player.getIndex(), animationId, message);
    }

    public void npc(int npcId, int animationId, String... message) {
        sendEntityDialogue(true, npcId, animationId, message);
    }

    public void sendOptions(String... text) {
        int l = text.length;
        int interfaceId = (l == 6 ? 238 : l == 5 ? 237 : l == 4 ? 230 : 236);
        String[] messages = new String[text.length + 1];
        System.arraycopy(text, 0, messages, 0, text.length);
        sendDialogue((short) interfaceId, messages);
    }

    public void item(int itemId, int itemAmount, String... messages) {
        int length = messages.length;
        short interfaceId = (length == 1 ? SEND_1_TEXT_CHAT : length == 2 ? SEND_2_TEXT_CHAT : length == 3 ? SEND_3_TEXT_CHAT : SEND_4_TEXT_CHAT);
        List<String> text = new ArrayList<>();
        text.add("");
        Collections.addAll(text, messages);
        String[] message = text.toArray(new String[text.size()]);
        sendEntityDialogue(interfaceId, message, IS_ITEM, itemId, itemAmount);
    }

    public void chatbox(String... text) {
        int length = text.length;
        short interfaceId = (length == 4 ? SEND_4_TEXT_INFO : length == 3 ? SEND_3_TEXT_INFO : length == 2 ? SEND_2_TEXT_INFO : SEND_1_TEXT_INFO);
        sendDialogue(interfaceId, text);
    }

    public void options(String... text) {
        int l = text.length;
        int interfaceId = (l == 6 ? 238 : l == 5 ? 237 : l == 4 ? 230 : 236);

        String[] messages = new String[text.length + 1];
        System.arraycopy(text, 0, messages, 0, text.length);
        sendDialogue((short) interfaceId, messages);
    }

    /**
     * Gets a parameter from the {@link #parameters} array and casts the type to it
     *
     * @param indexId The index in the parameters array
     */
    @SuppressWarnings("unchecked")
    protected <K> K getParam(int indexId) {
        return (K) parameters[indexId];
    }

    public void updateComponents(int interfaceId) {
        IComponentDefinitions[] defs = IComponentDefinitions.getInterface(interfaceId);
        if (defs == null) {
            return;
        }
        for (IComponentDefinitions def : defs) {
            if (def == null) {
                continue;
            }
            switch (def.text) {
                case "option1":
                    first = def.getWidgetId();
                    break;
                case "option2":
                    second = def.getWidgetId();
                    break;
                case "option3":
                    third = def.getWidgetId();
                    break;
                case "option4":
                    fourth = def.getWidgetId();
                    break;
                case "option5":
                    fifth = def.getWidgetId();
                    break;
            }
        }
    }

    public byte getStage() {
        return this.stage;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setThird(int third) {
        this.third = third;
    }

    public void setFourth(int fourth) {
        this.fourth = fourth;
    }

    public void setFifth(int fifth) {
        this.fifth = fifth;
    }
}
