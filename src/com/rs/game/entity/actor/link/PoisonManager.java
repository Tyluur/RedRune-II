package com.rs.game.entity.actor.link;

import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Hit;
import com.rs.game.entity.actor.mask.Hit.HitLook;
import com.rs.game.entity.actor.player.Player;
import com.rs.utility.Misc;

import java.io.Serializable;

public final class PoisonManager implements Serializable {
	
	private static final long serialVersionUID = -6324477860776313690L;
	
	private transient Actor actor;
	
	private int poisonDamage;
	
	private int poisonCount;
	
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public Actor getActor() {
		return actor;
	}
	
	public void makePoisoned(int startDamage) {
		if (poisonDamage > startDamage) {
			return;
		}
		if (actor instanceof Player) {
			Player player = ((Player) actor);
			if (player.getPoisonImmune() > Misc.currentTimeMillis()) {
				return;
			}
			if (poisonDamage == 0) {
				player.getPackets().sendGameMessage("You are poisoned.");
			}
		}
		poisonDamage = startDamage;
		refresh();
	}
	
	public void processPoison() {
		if (!actor.isDead() && isPoisoned()) {
			if (poisonCount > 0) {
				poisonCount--;
				return;
			}
			boolean heal = false;
			if (actor instanceof Player) {
				Player player = ((Player) actor);
				// inter opened we dont poison while inter opened like at rs
				if (player.getInterfaceManager().containsScreenInter()) {
					return;
				}
				if (player.getAuraManager().hasPoisonPurge()) {
					heal = true;
				}
			}
			actor.applyHit(new Hit(actor, poisonDamage, heal ? HitLook.HEALED_DAMAGE : HitLook.POISON_DAMAGE));
			poisonDamage -= 2;
			if (isPoisoned()) {
				poisonCount = 30;
				return;
			}
			reset();
		}
	}
	
	public void reset() {
		poisonDamage = 0;
		poisonCount = 0;
		refresh();
	}
	
	public void refresh() {
		if (actor instanceof Player) {
			Player player = ((Player) actor);
			player.getPackets().sendConfig(102, isPoisoned() ? 1 : 0);
		}
	}
	
	public boolean isPoisoned() {
		return poisonDamage >= 1;
	}
}
