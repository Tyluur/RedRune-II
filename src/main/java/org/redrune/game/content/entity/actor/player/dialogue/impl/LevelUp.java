package org.redrune.game.content.entity.actor.player.dialogue.impl;

import org.redrune.game.content.entity.actor.player.dialogue.Dialogue;
import org.redrune.game.entity.actor.mask.Graphics;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.utility.constants.SkillConstants;

public final class LevelUp extends Dialogue {

    private static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = {37, 37, 37, 37, 37, -1, 37, -1, 39, -1, -1, -1, -1, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, 300, 417};

    private int skill;

    @Override
    public void start() {
        skill = (Integer) parameters[0];
        int level = player.getSkills().getLevel(skill);
        String name = SkillConstants.SKILL_NAME[skill];
        player.putTemporaryAttribute("leveledUp", skill);
        player.putTemporaryAttribute("leveledUp[" + skill + "]", Boolean.TRUE);

        player.setNextGraphics(new Graphics(199));
        sendChatbox(level, name);
        playMusic();
        sendAnnouncement(level, name);
        switchFlash(player, skill, true);
    }

    private void sendChatbox(int level, String name) {
        player.getInterfaceManager().sendChatBoxInterface(740);
        player.getPackets().sendIComponentText(740, 0, "Congratulations, you have just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level!");
        player.getPackets().sendIComponentText(740, 1, "You have now reached level " + level + ".");
        player.getPackets().sendMessage("You've just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level! You have reached level " + level + ".");
        player.getPackets().sendConfigByFile(4757, getIconValue(skill));
    }

    private void playMusic() {
        int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
        if (musicEffect != -1) {
            player.getPackets().sendMusicEffect(musicEffect);
        }
    }

    private void sendAnnouncement(int level, String name) {
        if (level == 99 || level == 120) {
            World.sendWorldMessage("<col=F20505>News: " + player.getDisplayName() + " has achieved " + level + " " + SkillConstants.SKILL_NAME[skill] + ".", false);
        }
        if (player.getSkills().getXp(skill) == 200_000_000) {
            for (Player p : World.getPlayers()) {
                p.getPackets().sendMessage("<col=F20505>News: " + player.getDisplayName() + " has just achieved 200 million experience in " + name + ".");
            }
        }
    }

    public static void switchFlash(Player player, int skill, boolean on) {
        int id;
        switch (skill) {
            case SkillConstants.ATTACK:
                id = 4732;
                break;
            case SkillConstants.STRENGTH:
                id = 4733;
                break;
            case SkillConstants.DEFENCE:
                id = 4734;
                break;
            case SkillConstants.RANGE:
                id = 4735;
                break;
            case SkillConstants.PRAYER:
                id = 4736;
                break;
            case SkillConstants.MAGIC:
                id = 4737;
                break;
            case SkillConstants.HITPOINTS:
                id = 4738;
                break;
            case SkillConstants.AGILITY:
                id = 4739;
                break;
            case SkillConstants.HERBLORE:
                id = 4740;
                break;
            case SkillConstants.THIEVING:
                id = 4741;
                break;
            case SkillConstants.CRAFTING:
                id = 4742;
                break;
            case SkillConstants.FLETCHING:
                id = 4743;
                break;
            case SkillConstants.MINING:
                id = 4744;
                break;
            case SkillConstants.SMITHING:
                id = 4745;
                break;
            case SkillConstants.FISHING:
                id = 4746;
                break;
            case SkillConstants.COOKING:
                id = 4747;
                break;
            case SkillConstants.FIREMAKING:
                id = 4748;
                break;
            case SkillConstants.WOODCUTTING:
                id = 4749;
                break;
            case SkillConstants.RUNECRAFTING:
                id = 4750;
                break;
            case SkillConstants.SLAYER:
                id = 4751;
                break;
            case SkillConstants.FARMING:
                id = 4752;
                break;
            case SkillConstants.CONSTRUCTION:
                id = 4753;
                break;
            case SkillConstants.HUNTER:
                id = 4754;
                break;
            case SkillConstants.SUMMONING:
                id = 4755;
                break;
            default:
                id = 7756;
                break;
        }
        player.getPackets().sendConfigByFile(id, on ? 1 : 0);
    }

    private static int getIconValue(int skill) {
        switch (skill) {
            case SkillConstants.ATTACK:
                return 1;
            case SkillConstants.STRENGTH:
                return 2;
            case SkillConstants.RANGE:
                return 3;
            case SkillConstants.MAGIC:
                return 4;
            case SkillConstants.DEFENCE:
                return 5;
            case SkillConstants.HITPOINTS:
                return 6;
            case SkillConstants.PRAYER:
                return 7;
            case SkillConstants.AGILITY:
                return 8;
            case SkillConstants.HERBLORE:
                return 9;
            case SkillConstants.THIEVING:
                return 10;
            case SkillConstants.CRAFTING:
                return 11;
            case SkillConstants.RUNECRAFTING:
                return 12;
            case SkillConstants.MINING:
                return 13;
            case SkillConstants.SMITHING:
                return 14;
            case SkillConstants.FISHING:
                return 15;
            case SkillConstants.COOKING:
                return 16;
            case SkillConstants.FIREMAKING:
                return 17;
            case SkillConstants.WOODCUTTING:
                return 18;
            case SkillConstants.FLETCHING:
                return 19;
            case SkillConstants.SLAYER:
                return 20;
            case SkillConstants.FARMING:
                return 21;
            case SkillConstants.CONSTRUCTION:
                return 22;
            case SkillConstants.SUMMONING:
                return 24;
        }
        return 25;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        end();
    }

    @Override
    public void finish() {
        // player.getPackets().sendConfig(1179, SKILL_ICON[skill]); //removes
        // random flash
    }
}
