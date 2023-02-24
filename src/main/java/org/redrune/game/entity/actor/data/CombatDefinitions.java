package org.redrune.game.entity.actor.data;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.item.Item;
import org.redrune.utility.constants.MagicConstants.MagicBook;
import org.redrune.utility.constants.SkillConstants;
import org.redrune.utility.game.repository.item.ItemCharacteristicRepository;

import java.io.Serializable;

public final class CombatDefinitions implements Serializable {

    public static final int STAB_ATTACK = 0, SLASH_ATTACK = 1, CRUSH_ATTACK = 2, RANGE_ATTACK = 4, MAGIC_ATTACK = 3;

    public static final int STAB_DEF = 5, SLASH_DEF = 6, CRUSH_DEF = 7, RANGE_DEF = 9, MAGIC_DEF = 8, SUMMONING_DEF = 10;

    public static final int STRENGTH_BONUS = 14, RANGED_STR_BONUS = 15, MAGIC_DAMAGE = 17, PRAYER_BONUS = 16;

    public static final int ABSORVE_MELEE_BONUS = 11, ABSORVE_RANGE_BONUS = 13, ABSORVE_MAGE_BONUS = 12;

    public static final int SHARED = -1;

    private static final long serialVersionUID = 2102201264836121104L;

    private byte attackStyle;

    private byte specialAttackPercentage;

    private boolean autoRetaliate;

    // saving stuff

    private byte sortSpellBook;

    private boolean showCombatSpells;

    private boolean showSkillSpells;

    private boolean showMiscallaneousSpells;

    private boolean showTeleportSpells;

    private boolean defensiveCasting;

    private byte spellBook;

    private byte autoCastSpell;

    private transient Player player;

    private transient boolean usingSpecialAttack;

    private transient int[] bonuses;

    private transient boolean dungeonneringSpellBook;

    public CombatDefinitions() {
        specialAttackPercentage = 100;
        autoRetaliate = true;
        showCombatSpells = true;
        showSkillSpells = true;
        showMiscallaneousSpells = true;
        showTeleportSpells = true;
    }

    public static int getMeleeDefenceBonus(int bonusId) {
        if (bonusId == STAB_ATTACK) {
            return STAB_DEF;
        }
        if (bonusId == SLASH_DEF) {
            return SLASH_DEF;
        }
        return CRUSH_DEF;
    }

    public static int getMeleeBonusStyle(int weaponId, int attackStyle) {
        if (weaponId != -1) {
            String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
            if (weaponName.contains("whip")) {
                return SLASH_ATTACK;
            }
            if (weaponName.contains("staff of light")) {
                switch (attackStyle) {
                    case 0:
                        return STAB_ATTACK;
                    case 1:
                        return SLASH_ATTACK;
                    default:
                        return CRUSH_ATTACK;
                }
            }
            if (weaponName.contains("staff") || weaponName.contains("granite mace") || weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em") || weaponName.contains("tzhaar-ket-om") || weaponName.contains("maul")) {
                return CRUSH_ATTACK;
            }
            if (weaponName.contains("scimitar") || weaponName.contains("korasi's sword") || weaponName.contains("hatchet") || weaponName.contains("claws") || weaponName.contains("longsword")) {
                if (attackStyle == 2) {
                    return STAB_ATTACK;
                }
                return SLASH_ATTACK;
            }
            if (weaponName.contains("mace") || weaponName.contains("anchor")) {
                if (attackStyle == 2) {
                    return STAB_ATTACK;
                }
                return CRUSH_ATTACK;
            }
            if (weaponName.contains("halberd")) {
                if (attackStyle == 1) {
                    return SLASH_ATTACK;
                }
                return STAB_ATTACK;
            }
            if (weaponName.contains("spear")) {
                switch (attackStyle) {
                    case 1:
                        return SLASH_ATTACK;
                    case 2:
                        return CRUSH_ATTACK;
                    default:
                        return STAB_ATTACK;
                }
            }
            if (weaponName.contains("pickaxe")) {
                if (attackStyle == 2) {
                    return CRUSH_ATTACK;
                }
                return STAB_ATTACK;
            }

            if (weaponName.contains("dagger") || weaponName.contains("rapier")) {
                if (attackStyle == 2) {
                    return SLASH_ATTACK;
                }
                return STAB_ATTACK;
            }

            if (weaponName.contains("godsword") || weaponName.contains("greataxe") || weaponName.contains("2h sword") || weaponName.equals("saradomin sword")) {
                if (attackStyle == 2) {
                    return CRUSH_ATTACK;
                }
                return SLASH_ATTACK;
            }

        }
        return CRUSH_ATTACK;
    }

    public static int getXpStyle(int weaponId, int attackStyle) {
        // TODO SHARED
        if (weaponId != -1) {
            String weaponName = ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
            if (weaponName.contains("whip")) {
                switch (attackStyle) {
                    case 0:
                        return SkillConstants.ATTACK;
                    case 1:
                        return SHARED;
                    case 2:
                    default:
                        return SkillConstants.DEFENCE;
                }
            }
            if (weaponName.contains("halberd")) {
                switch (attackStyle) {
                    case 0:
                        return SHARED;
                    case 1:
                        return SkillConstants.STRENGTH;
                    case 2:
                    default:
                        return SkillConstants.DEFENCE;
                }
            }
            if (weaponName.contains("staff")) {
                switch (attackStyle) {
                    case 0:
                        return SkillConstants.ATTACK;
                    case 1:
                        return SkillConstants.STRENGTH;
                    case 2:
                    default:
                        return SkillConstants.DEFENCE;
                }
            }
            if (weaponName.contains("godsword") || weaponName.contains("sword") || weaponName.contains("2h")) {
                switch (attackStyle) {
                    case 0:
                        return SkillConstants.ATTACK;
                    case 1:
                        return SkillConstants.STRENGTH;
                    case 2:
                        return SkillConstants.STRENGTH;
                    case 3:
                    default:
                        return SkillConstants.DEFENCE;
                }
            }
        }
        if (weaponId == -1) {
            switch (attackStyle) {
                case 0:
                    return SkillConstants.ATTACK;
                case 1:
                    return SkillConstants.STRENGTH;
                case 2:
                default:
                    return SkillConstants.DEFENCE;
            }
        }
        switch (attackStyle) {
            case 0:
                return SkillConstants.ATTACK;
            case 1:
                return SkillConstants.STRENGTH;
            case 2:
                return SHARED;
            case 3:
            default:
                return SkillConstants.DEFENCE;
        }
    }

    public int getRealSpellId() {
        int tempCastSpell = player.getTemporaryAttribute("tempCastSpell", -1);
        return tempCastSpell != -1 ? tempCastSpell : autoCastSpell;
    }

    public int getSpellId() {
        Integer tempCastSpell = (Integer) player.getTemporaryAttributes().get("tempCastSpell");
        if (tempCastSpell != null) {
            return tempCastSpell + 256;
        }
        return autoCastSpell;
    }

    public int getAutoCastSpell() {
        return autoCastSpell;
    }

    public void setAutoCastSpell(int id) {
        autoCastSpell = (byte) id;
        refreshAutoCastSpell();
    }

    public void resetSpells(boolean removeAutoSpell) {
        player.getTemporaryAttributes().remove("tempCastSpell");
        if (removeAutoSpell) {
            setAutoCastSpell(0);
            refreshAutoCastSpell();
        }
    }

    public int getSpellBook() {
        if (dungeonneringSpellBook) {
            return 950; // dung book
        } else {
            if (spellBook == 0) {
                return 192; // normal
            } else if (spellBook == 1) {
                return 193; // ancients
            } else {
                return 430; // lunar
            }
        }
    }

    public MagicBook getMagicBook() {
        return MagicBook.getMagicBook(getSpellBook()).orElse(MagicBook.REGULAR);
    }

    public void setSpellBook(int id) {
        if (id == 3) {
            dungeonneringSpellBook = true;
        } else {
            spellBook = (byte) id;
        }
        refreshSpellBookScrollBar_DefCast();
        player.getInterfaceManager().sendMagicBook();
    }

    public void refreshSpellBookScrollBar_DefCast() {
        player.getPackets().sendConfig(439, (dungeonneringSpellBook ? 3 : spellBook) + (defensiveCasting ? 0 : 1 << 8));
    }

    public void switchShowCombatSpells() {
        showCombatSpells = !showCombatSpells;
        refreshSpellBook();
    }

    public void refreshSpellBook() {
        if (spellBook == 0) {
            player.getPackets().sendConfig(1376, sortSpellBook | (showCombatSpells ? 0 : 1 << 9) | (showSkillSpells ? 0 : 1 << 10) | (showMiscallaneousSpells ? 0 : 1 << 11) | (showTeleportSpells ? 0 : 1 << 12));
        } else if (spellBook == 1) {
            player.getPackets().sendConfig(1376, sortSpellBook << 3 | (showCombatSpells ? 0 : 1 << 16) | (showTeleportSpells ? 0 : 1 << 17));
        } else if (spellBook == 2) {
            player.getPackets().sendConfig(1376, sortSpellBook << 6 | (showCombatSpells ? 0 : 1 << 13) | (showMiscallaneousSpells ? 0 : 1 << 14) | (showTeleportSpells ? 0 : 1 << 15));
        }
    }

    public void switchShowSkillSpells() {
        showSkillSpells = !showSkillSpells;
        refreshSpellBook();
    }

    public void switchShowMiscallaneousSpells() {
        showMiscallaneousSpells = !showMiscallaneousSpells;
        refreshSpellBook();
    }

    public void switchShowTeleportSkillSpells() {
        showTeleportSpells = !showTeleportSpells;
        refreshSpellBook();
    }

    public void switchDefensiveCasting() {
        defensiveCasting = !defensiveCasting;
        refreshSpellBookScrollBar_DefCast();
    }

    public void setSortSpellBook(int sortId) {
        this.sortSpellBook = (byte) sortId;
        refreshSpellBook();
    }

    public boolean isDefensiveCasting() {
        return defensiveCasting;
    }

    public void setPlayer(Player player) {
        this.player = player;
        bonuses = new int[18];
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public void handleSoaking(Actor source, Actor target, Hit hit) {
        Player p2 = (Player) target;
        int damage = hit.getDamage() > p2.getHitpoints() ? p2.getHitpoints() : hit.getDamage();
        if (hit.isMeleeHit()) {
            int reducedDamage = (damage - 200)
                    * p2.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MELEE_BONUS] / 100;
            if (damage - reducedDamage > 200 && p2.getHitpoints() > 200) {
                if (reducedDamage > 0) {
                    hit.setDamage(damage - reducedDamage);
                    hit.setSoaking(new Hit(player, reducedDamage, HitSplat.ABSORB_DAMAGE));
                }
            }
        }
        if (hit.isRangeHit()) {
            int reducedDamage = (damage - 200)
                    * p2.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_RANGE_BONUS] / 100;
            if (damage - reducedDamage > 200 && p2.getHitpoints() > 200) {
                if (reducedDamage > 0) {
                    hit.setDamage(damage - reducedDamage);
                    hit.setSoaking(new Hit(player, reducedDamage, HitSplat.ABSORB_DAMAGE));
                }
            }
        }
        if (hit.isMagicHit()) {
            int reducedDamage = (damage - 200)
                    * p2.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORVE_MAGE_BONUS] / 100;
            if (damage - reducedDamage > 200 && p2.getHitpoints() > 200) {
                if (reducedDamage > 0) {
                    hit.setDamage(damage - reducedDamage);
                    hit.setSoaking(new Hit(player, reducedDamage, HitSplat.ABSORB_DAMAGE));
                }
            }
        }
    }

    /**
     * Gets the bonus at an index
     *
     * @param index The index
     */
    public int getBonus(int index) {
        if (index < 0 || index >= bonuses.length) {
            System.out.println("Invalid bonus index expected: " + index);
            return 0;
        }
        return bonuses[index];
    }

    public void refreshBonuses() {
        bonuses = new int[18];
        for (Item item : player.getEquipment().getItems().getItems()) {
            if (item == null) {
                continue;
            }
            int[] bonuses = ItemCharacteristicRepository.getBonuses(item.getId());
            if (bonuses == null) {
                continue;
            }
            for (int id = 0; id < bonuses.length; id++) {
                if (id == 15 && this.bonuses[id] != 0) {
                    continue;
                }
                this.bonuses[id] += bonuses[id];
            }
        }
    }

    public void resetSpecialAttack() {
        decreaseSpecialEnergy(0);
        specialAttackPercentage = 100;
        refreshSpecialAttackPercentage();
    }

    public void decreaseSpecialEnergy(int amount) {
        usingSpecialAttack = false;
        refreshUsingSpecialAttack();
        if (amount > 0) {
            specialAttackPercentage -= amount;
            refreshSpecialAttackPercentage();
        }
    }

    public void refreshSpecialAttackPercentage() {
        player.getPackets().sendConfig(300, specialAttackPercentage * 10);
    }

    public void refreshUsingSpecialAttack() {
        player.getPackets().sendConfig(301, usingSpecialAttack ? 1 : 0);
    }

    public void setSpecialAttack(int special) {
        decreaseSpecialEnergy(0);
        specialAttackPercentage = (byte) special;
        refreshSpecialAttackPercentage();
    }

    public void restoreSpecialAttack() {
        restoreSpecialAttack(10);
        if (player.getFamiliar() != null) {
            player.getFamiliar().restoreSpecialAttack(15);
        }
    }

    public void restoreSpecialAttack(int percentage) {
        if (specialAttackPercentage >= 100 || player.getInterfaceManager().containsScreenInter()) {
            return;
        }
        specialAttackPercentage += specialAttackPercentage > (100 - percentage) ? 100 - specialAttackPercentage : percentage;
        refreshSpecialAttackPercentage();
    }

    public void init() {
        refreshUsingSpecialAttack();
        refreshSpecialAttackPercentage();
        refreshAutoRelatie();
        refreshAttackStyle();
        refreshSpellBook();
        refreshAutoCastSpell();
        refreshSpellBookScrollBar_DefCast();
    }

    public void refreshAutoRelatie() {
        player.getPackets().sendConfig(172, autoRetaliate ? 0 : 1);
    }

    public void refreshAttackStyle() {
        player.getPackets().sendConfig(43, autoCastSpell > 0 ? 4 : attackStyle);
    }

    public void refreshAutoCastSpell() {
        refreshAttackStyle();
        player.getPackets().sendConfig(108, getSpellAutoCastConfigValue());
    }

    public int getSpellAutoCastConfigValue() {
        if (dungeonneringSpellBook) {
            return 0;
        }
        if (spellBook == 0) {
            switch (autoCastSpell) {
                case 25:
                    return 3;
                case 28:
                    return 5;
                case 30:
                    return 7;
                case 32:
                    return 9;
                case 34:
                    return 11; // air bolt
                case 39:
                    return 13;// water bolt
                case 42:
                    return 15;// earth bolt
                case 45:
                    return 17; // fire bolt
                case 49:
                    return 19;// air blast
                case 52:
                    return 21;// water blast
                case 58:
                    return 23;// earth blast
                case 63:
                    return 25;// fire blast
                case 66: // Saradomin Strike
                    return 41;
                case 67:// Claws of Guthix
                    return 39;
                case 68:// Flames of Zammorak
                    return 43;
                case 70:
                    return 27;// air wave
                case 73:
                    return 29;// water wave
                case 77:
                    return 31;// earth wave
                case 80:
                    return 33;// fire wave
                case 84:
                    return 47;
                case 87:
                    return 49;
                case 89:
                    return 51;
                case 91:
                    return 53;
                case 99:
                    return 145;
                default:
                    return 0;
            }
        } else if (spellBook == 1) {
            switch (autoCastSpell) {
                case 28:
                    return 63;
                case 32:
                    return 65;
                case 24:
                    return 67;
                case 20:
                    return 69;
                case 30:
                    return 71;
                case 34:
                    return 73;
                case 26:
                    return 75;
                case 22:
                    return 77;
                case 29:
                    return 79;
                case 33:
                    return 81;
                case 25:
                    return 83;
                case 21:
                    return 85;
                case 31:
                    return 87;
                case 35:
                    return 89;
                case 27:
                    return 91;
                case 23:
                    return 93;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public void checkAttackStyle() {
        if (autoCastSpell == 0) {
            setAttackStyle(attackStyle);
        }
    }

    public void sendUnlockAttackStylesButtons() {
        for (int componentId = 11; componentId <= 14; componentId++) {
            player.getPackets().sendUnlockIComponentOptionSlots(884, componentId, -1, 0, 0);
        }
    }

    public void switchUsingSpecialAttack() {
        usingSpecialAttack = !usingSpecialAttack;
        refreshUsingSpecialAttack();
    }

    public void setUsingSpecialAttack(boolean usingSpecialAttack) {
        this.usingSpecialAttack = usingSpecialAttack;
        refreshUsingSpecialAttack();
    }

    public boolean hasRingOfVigour() {
        return player.getEquipment().getRingId() == 19669;
    }

    public int getSpecialAttackPercentage() {
        return specialAttackPercentage;
    }

    public void switchAutoRelatie() {
        autoRetaliate = !autoRetaliate;
        refreshAutoRelatie();
    }

    public boolean isUsingSpecialAttack() {
        return usingSpecialAttack;
    }

    public int getAttackStyle() {
        return attackStyle;
    }

    public void setAttackStyle(int style) {
        int maxSize = 3;
        int weaponId = player.getEquipment().getWeaponId();
        String name = weaponId == -1 ? "" : ItemDefinitions.getItemDefinitions(weaponId).getName().toLowerCase();
        if (weaponId == -1 || CombatAlgorithm.isRanging(player) != 0 || name.contains("whip") || name.contains("halberd")) {
            maxSize = 2;
        }
        if (style > maxSize) {
            style = maxSize;
        }
        if (style != attackStyle) {
            attackStyle = (byte) style;
            if (autoCastSpell > 1) {
                resetSpells(true);
            } else {
                refreshAttackStyle();
            }
        } else if (autoCastSpell > 1) {
            resetSpells(true);
        }
    }

    public boolean isDungeonneringSpellBook() {
        return dungeonneringSpellBook;
    }

    public void removeDungeonneringBook() {
        if (dungeonneringSpellBook) {
            dungeonneringSpellBook = false;
            player.getInterfaceManager().sendMagicBook();
        }
    }

    public boolean isAutocasting() {
        return player.getTemporaryAttribute("tempCastSpell", -1) == -1 && autoCastSpell != 0;
    }

    public boolean isAutoRetaliate() {
        return this.autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }
}
