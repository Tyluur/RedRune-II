package game.entity.actor.npc.data.combat;

import utility.constants.BonusConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCCombatDefinitions {
	
	/**
	 * The hitpoints of the npc
	 */
	private int hitpoints;
	
	/**
	 * The attack level of the npc
	 */
	private int attackLevel;
	
	/**
	 * The strength level of the npc
	 */
	private int strengthLevel;
	
	/**
	 * The defence level of the npc
	 */
	private int defenceLevel;
	
	/**
	 * The range level of the npc
	 */
	private int rangeLevel;
	
	/**
	 * The magic level of the npc
	 */
	private int magicLevel;
	
	/**
	 * The bonuses of the npc
	 */
	private int[] bonuses;
	
	/**
	 * The attack animation of the npc
	 */
	private int attackAnim;
	
	/**
	 * The defend animation of the npc
	 */
	private int defenceAnim;
	
	/**
	 * The dying animation of the npc
	 */
	private int deathAnim;
	
	/**
	 * The delay between combat attacks of the npc
	 */
	private int attackDelay;
	
	/**
	 * The delay until death of the npc
	 */
	private int deathDelay;
	
	/**
	 * The respawn delay of the npc
	 */
	private int respawnDelay;
	
	/**
	 * The max hit of the npc
	 */
	private int maxHit;
	
	/**
	 * The attack style of the npc
	 */
	private int attackStyle;
	
	/**
	 * The graphics sent on a combat swing of the npc
	 */
	private int attackGfx;
	
	/**
	 * The projectile id sent on a combat swing of the npc
	 */
	private int attackProjectile;
	
	/**
	 * The type of aggressiveness the npc has
	 */
	private int aggressivenessType;
	
	/**
	 * The required slayer level to attack this npc
	 */
	private int requiredSlayerLevel;
	
	/**
	 * The slayer category to which the npc belongs
	 */
	private String slayerCategory = "null";
	
	/**
	 * The slayer experience received from killing the npc
	 */
	private double experienceReceived = 0;
	
	/**
	 * The areas from which the npc may be found
	 */
	private final List<String> areas = new ArrayList<>();
	
	/**
	 * If the npc is a poisonous npc
	 */
	private boolean poisonous;
	
	public NPCCombatDefinitions(int hitpoints, int attackLevel, int strengthLevel, int defenceLevel, int rangeLevel, int magicLevel, int[] bonuses, int attackAnim, int defenceAnim, int deathAnim, int attackDelay, int deathDelay, int respawnDelay, int maxHit, int attackStyle, int attackGfx, int attackProjectile, int aggressivenessType) {
		this.hitpoints = hitpoints;
		this.attackLevel = attackLevel;
		this.strengthLevel = strengthLevel;
		this.defenceLevel = defenceLevel;
		this.rangeLevel = rangeLevel;
		this.magicLevel = magicLevel;
		this.bonuses = bonuses;
		this.attackAnim = attackAnim;
		this.defenceAnim = defenceAnim;
		this.deathAnim = deathAnim;
		this.attackDelay = attackDelay;
		this.deathDelay = deathDelay;
		this.respawnDelay = respawnDelay;
		this.maxHit = maxHit;
		this.attackStyle = attackStyle;
		this.attackGfx = attackGfx;
		this.attackProjectile = attackProjectile;
		this.aggressivenessType = aggressivenessType;
	}
	
	public NPCCombatDefinitions(int hitpoints, int attackAnim, int defenceAnim, int deathAnim, int attackDelay, int deathDelay, int respawnDelay, int maxHit, int attackStyle, int attackGfx, int attackProjectile, int aggressivenessType) {
		this.hitpoints = hitpoints;
		this.attackAnim = attackAnim;
		this.defenceAnim = defenceAnim;
		this.deathAnim = deathAnim;
		this.attackDelay = attackDelay;
		this.deathDelay = deathDelay;
		this.respawnDelay = respawnDelay;
		this.maxHit = maxHit;
		this.attackStyle = attackStyle;
		this.attackGfx = attackGfx;
		this.attackProjectile = attackProjectile;
		this.aggressivenessType = aggressivenessType;
	}
	
	public NPCCombatDefinitions() {
		this(100, 0, 0, 0, 0, 0, new int[18], -1, -1, -1, 3, 6, 50, 1, BonusConstants.SLASH_ATTACK, -1, -1, 0);
	}
	
	@Override
	public String toString() {
		return "NPCCombatDefinitions{" + "hitpoints=" + hitpoints + ", attackLevel=" + attackLevel + ", strengthLevel=" + strengthLevel + ", defenceLevel=" + defenceLevel + ", rangeLevel=" + rangeLevel + ", magicLevel=" + magicLevel + ", bonuses=" + Arrays.toString(bonuses) + ", attackAnim=" + attackAnim + ", defenceAnim=" + defenceAnim + ", deathAnim=" + deathAnim + ", attackDelay=" + attackDelay + ", deathDelay=" + deathDelay + ", respawnDelay=" + respawnDelay + ", maxHit=" + maxHit + ", attackStyle=" + attackStyle + ", attackGfx=" + attackGfx + ", attackProjectile=" + attackProjectile + ", aggressivenessType=" + aggressivenessType + '}';
	}

    public int getHitpoints() {
        return this.hitpoints;
    }

    public int getAttackLevel() {
        return this.attackLevel;
    }

    public int getStrengthLevel() {
        return this.strengthLevel;
    }

    public int getDefenceLevel() {
        return this.defenceLevel;
    }

    public int getRangeLevel() {
        return this.rangeLevel;
    }

    public int getMagicLevel() {
        return this.magicLevel;
    }

    public int[] getBonuses() {
        return this.bonuses;
    }

    public int getAttackAnim() {
        return this.attackAnim;
    }

    public int getDefenceAnim() {
        return this.defenceAnim;
    }

    public int getDeathAnim() {
        return this.deathAnim;
    }

    public int getAttackDelay() {
        return this.attackDelay;
    }

    public int getDeathDelay() {
        return this.deathDelay;
    }

    public int getRespawnDelay() {
        return this.respawnDelay;
    }

    public int getMaxHit() {
        return this.maxHit;
    }

    public int getAttackStyle() {
        return this.attackStyle;
    }

    public int getAttackGfx() {
        return this.attackGfx;
    }

    public int getAttackProjectile() {
        return this.attackProjectile;
    }

    public int getAggressivenessType() {
        return this.aggressivenessType;
    }

    public int getRequiredSlayerLevel() {
        return this.requiredSlayerLevel;
    }

    public String getSlayerCategory() {
        return this.slayerCategory;
    }

    public double getExperienceReceived() {
        return this.experienceReceived;
    }

    public List<String> getAreas() {
        return this.areas;
    }

    public boolean isPoisonous() {
        return this.poisonous;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public void setAttackLevel(int attackLevel) {
        this.attackLevel = attackLevel;
    }

    public void setStrengthLevel(int strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public void setDefenceLevel(int defenceLevel) {
        this.defenceLevel = defenceLevel;
    }

    public void setRangeLevel(int rangeLevel) {
        this.rangeLevel = rangeLevel;
    }

    public void setMagicLevel(int magicLevel) {
        this.magicLevel = magicLevel;
    }

    public void setBonuses(int[] bonuses) {
        this.bonuses = bonuses;
    }

    public void setAttackAnim(int attackAnim) {
        this.attackAnim = attackAnim;
    }

    public void setDefenceAnim(int defenceAnim) {
        this.defenceAnim = defenceAnim;
    }

    public void setDeathAnim(int deathAnim) {
        this.deathAnim = deathAnim;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public void setDeathDelay(int deathDelay) {
        this.deathDelay = deathDelay;
    }

    public void setRespawnDelay(int respawnDelay) {
        this.respawnDelay = respawnDelay;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public void setAttackStyle(int attackStyle) {
        this.attackStyle = attackStyle;
    }

    public void setAttackGfx(int attackGfx) {
        this.attackGfx = attackGfx;
    }

    public void setAttackProjectile(int attackProjectile) {
        this.attackProjectile = attackProjectile;
    }

    public void setAggressivenessType(int aggressivenessType) {
        this.aggressivenessType = aggressivenessType;
    }

    public void setRequiredSlayerLevel(int requiredSlayerLevel) {
        this.requiredSlayerLevel = requiredSlayerLevel;
    }

    public void setSlayerCategory(String slayerCategory) {
        this.slayerCategory = slayerCategory;
    }

    public void setExperienceReceived(double experienceReceived) {
        this.experienceReceived = experienceReceived;
    }

    public void setPoisonous(boolean poisonous) {
        this.poisonous = poisonous;
    }
}