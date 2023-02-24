package org.redrune.utility.game.repository.npc.characteristic;


/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-20
 */
public class AriosNPCCharacteristics {


    private final int id;


    private final String examine;


    private final String name;


    private final int lifePoints, attackLevel, strengthLevel, defenceLevel, rangeLevel, magicLevel;


    private final int[] bonuses;


    private final int respawnDelay, attackSpeed, meleeAnimation, defenceAnimation, deathAnimation, spawnAnimation, magicAnimation, rangeAnimation, attackGfx, attackProjectile, endGfx, combatStyle, aggressive;

    public AriosNPCCharacteristics(int id, String examine, String name, int lifePoints, int attackLevel, int strengthLevel, int defenceLevel, int rangeLevel, int magicLevel, int[] bonuses, int respawnDelay, int attackSpeed, int meleeAnimation, int defenceAnimation, int deathAnimation, int spawnAnimation, int magicAnimation, int rangeAnimation, int attackGfx, int attackProjectile, int endGfx, int combatStyle, int aggressive) {
        this.id = id;
        this.examine = examine;
        this.name = name;
        this.lifePoints = lifePoints;
        this.attackLevel = attackLevel;
        this.strengthLevel = strengthLevel;
        this.defenceLevel = defenceLevel;
        this.rangeLevel = rangeLevel;
        this.magicLevel = magicLevel;
        this.bonuses = bonuses;
        this.respawnDelay = respawnDelay;
        this.attackSpeed = attackSpeed;
        this.meleeAnimation = meleeAnimation;
        this.defenceAnimation = defenceAnimation;
        this.deathAnimation = deathAnimation;
        this.spawnAnimation = spawnAnimation;
        this.magicAnimation = magicAnimation;
        this.rangeAnimation = rangeAnimation;
        this.attackGfx = attackGfx;
        this.attackProjectile = attackProjectile;
        this.endGfx = endGfx;
        this.combatStyle = combatStyle;
        this.aggressive = aggressive;
    }

    public int getAttackLevel() {
        return attackLevel;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public int getRangeLevel() {
        return rangeLevel;
    }

    public int getMagicLevel() {
        return magicLevel;
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public int getAttackGfx() {
        return attackGfx;
    }
}
