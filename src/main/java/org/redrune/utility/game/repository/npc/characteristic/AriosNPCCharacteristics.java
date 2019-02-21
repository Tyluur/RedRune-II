package org.redrune.utility.game.repository.npc.characteristic;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-20
 */
public class AriosNPCCharacteristics {
	
	@Getter
	private final int id;
	
	@Getter
	private final String examine;
	
	@Getter
	private final String name;
	
	@Getter
	private final int lifePoints, attackLevel, strengthLevel, defenceLevel, rangeLevel, magicLevel;
	
	@Getter
	private final int[] bonuses;
	
	@Getter
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
}
