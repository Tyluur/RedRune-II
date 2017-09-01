package com.rs.game.entity.actor.mask;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.player.Player;

public final class Hit {
	
	private Actor source;
	
	private HitLook look;
	
	private int damage;
	
	private boolean critical;
	
	private Hit soaking;
	
	private int delay;
	
	public Hit(Actor source, int damage, HitLook look) {
		this(source, damage, look, 0);
	}
	
	public Hit(Actor source, int damage, HitLook look, int delay) {
		this.source = source;
		this.damage = damage;
		this.look = look;
		this.delay = delay;
	}
	
	public void setCriticalMark() {
		critical = true;
	}
	
	public void setHealHit() {
		look = HitLook.HEALED_DAMAGE;
		critical = false;
	}
	
	public boolean missed() {
		return damage == 0;
	}
	
	public int getMark(Player player, Actor victm) {
		if (HitLook.HEALED_DAMAGE == look) {
			return look.getMark();
		}
		if (damage == 0) {
			return HitLook.MISSED.getMark();
		}
		int mark = look.getMark();
		if (critical) {
			mark += 10;
		}
		if (!interactingWith(player, victm)) {
			mark += 14;
		}
		return mark;
	}
	
	public boolean interactingWith(Player player, Actor victm) {
		return player == victm || player == source;
	}
	
	public HitLook getLook() {
		return look;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public Actor getSource() {
		return source;
	}
	
	public void setSource(Actor source) {
		this.source = source;
	}
	
	public boolean isCriticalHit() {
		return critical;
	}
	
	public Hit getSoaking() {
		return soaking;
	}
	
	public void setSoaking(Hit soaking) {
		this.soaking = soaking;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public enum HitLook {
		
		MISSED(8),
		REGULAR_DAMAGE(3),
		MELEE_DAMAGE(0),
		RANGE_DAMAGE(1),
		MAGIC_DAMAGE(2),
		REFLECTED_DAMAGE(4),
		ABSORB_DAMAGE(5),
		POISON_DAMAGE(6),
		DESEASE_DAMAGE(7),
		HEALED_DAMAGE(9),
		CANNON_DAMAGE(13);
		
		private int mark;
		
		HitLook(int mark) {
			this.mark = mark;
		}
		
		public int getMark() {
			return mark;
		}
	}
	
}
