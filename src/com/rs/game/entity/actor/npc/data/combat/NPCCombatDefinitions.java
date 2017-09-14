package com.rs.game.entity.actor.npc.data.combat;

import lombok.Getter;
import lombok.Setter;

public class NPCCombatDefinitions {
	
	/**
	 * The hitpoints of the npc
	 */
	@Getter
	@Setter
	private int hitpoints;
	
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
	
}
