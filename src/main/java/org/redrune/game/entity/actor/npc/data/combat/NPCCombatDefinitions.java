package org.redrune.game.entity.actor.npc.data.combat;

import lombok.Getter;
import lombok.Setter;
import org.redrune.utility.constants.NPCConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCCombatDefinitions {
	
	/**
	 * The hitpoints of the npc
	 */
	@Getter
	@Setter
	private int hitpoints;
	
	/**
	 * The attack level of the npc
	 */
	@Getter
	@Setter
	private int attackLevel;
	
	/**
	 * The strength level of the npc
	 */
	@Getter
	@Setter
	private int strengthLevel;
	
	/**
	 * The defence level of the npc
	 */
	@Getter
	@Setter
	private int defenceLevel;
	
	/**
	 * The range level of the npc
	 */
	@Getter
	@Setter
	private int rangeLevel;
	
	/**
	 * The magic level of the npc
	 */
	@Getter
	@Setter
	private int magicLevel;
	
	/**
	 * The bonuses of the npc
	 */
	@Getter
	@Setter
	private int[] bonuses;
	
	/**
	 * The attack animation of the npc
	 */
	@Getter
	@Setter
	private int attackAnim;
	
	/**
	 * The defend animation of the npc
	 */
	@Getter
	@Setter
	private int defenceAnim;
	
	/**
	 * The dying animation of the npc
	 */
	@Getter
	@Setter
	private int deathAnim;
	
	/**
	 * The delay between combat attacks of the npc
	 */
	@Getter
	@Setter
	private int attackDelay;
	
	/**
	 * The delay until death of the npc
	 */
	@Getter
	@Setter
	private int deathDelay;
	
	/**
	 * The respawn delay of the npc
	 */
	@Getter
	@Setter
	private int respawnDelay;
	
	/**
	 * The max hit of the npc
	 */
	@Getter
	@Setter
	private int maxHit;
	
	/**
	 * The attack style of the npc
	 */
	@Getter
	@Setter
	private int attackStyle;
	
	/**
	 * The graphics sent on a combat swing of the npc
	 */
	@Getter
	@Setter
	private int attackGfx;
	
	/**
	 * The projectile id sent on a combat swing of the npc
	 */
	@Getter
	@Setter
	private int attackProjectile;
	
	/**
	 * The type of aggressiveness the npc has
	 */
	@Getter
	@Setter
	private int aggressivenessType;
	
	/**
	 * The required slayer level to attack this npc
	 */
	@Getter
	@Setter
	private int requiredSlayerLevel;
	
	/**
	 * The slayer category to which the npc belongs
	 */
	@Getter
	@Setter
	private String slayerCategory = "null";
	
	/**
	 * The slayer experience received from killing the npc
	 */
	@Getter
	@Setter
	private double experienceReceived = 0;
	
	/**
	 * The areas from which the npc may be found
	 */
	@Getter
	private final List<String> areas = new ArrayList<>();
	
	/**
	 * If the npc is a poisonous npc
	 */
	@Getter
	@Setter
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
		this(100, 0, 0, 0, 0, 0, new int[18], -1, -1, -1, 3, 6, 50, 1, NPCConstants.MELEE, -1, -1, 0);
	}
	
	@Override
	public String toString() {
		return "NPCCombatDefinitions{" + "hitpoints=" + hitpoints + ", attackLevel=" + attackLevel + ", strengthLevel=" + strengthLevel + ", defenceLevel=" + defenceLevel + ", rangeLevel=" + rangeLevel + ", magicLevel=" + magicLevel + ", bonuses=" + Arrays.toString(bonuses) + ", attackAnim=" + attackAnim + ", defenceAnim=" + defenceAnim + ", deathAnim=" + deathAnim + ", attackDelay=" + attackDelay + ", deathDelay=" + deathDelay + ", respawnDelay=" + respawnDelay + ", maxHit=" + maxHit + ", attackStyle=" + attackStyle + ", attackGfx=" + attackGfx + ", attackProjectile=" + attackProjectile + ", aggressivenessType=" + aggressivenessType + '}';
	}
	
}