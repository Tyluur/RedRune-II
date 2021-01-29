package org.redrune.game.content.entity.actor.combat.player.style;

import org.redrune.cache.loaders.ItemDefinitions;
import org.redrune.engine.tick.task.WorldTask;
import org.redrune.engine.tick.task.WorldTasksManager;
import org.redrune.game.content.entity.actor.combat.CombatAlgorithm;
import org.redrune.game.content.entity.actor.combat.CombatRoll;
import org.redrune.game.content.entity.actor.combat.CombatSwingDetail;
import org.redrune.game.content.entity.actor.combat.player.AbstractCombatStyle;
import org.redrune.game.content.entity.actor.combat.player.calc.MeleeCombatCalculator;
import org.redrune.game.content.plugin.PluginRepository;
import org.redrune.game.content.plugin.combat.SpecialAttackPlugin;
import org.redrune.game.entity.actor.Actor;
import org.redrune.game.entity.actor.data.CombatDefinitions;
import org.redrune.game.entity.actor.mask.Animation;
import org.redrune.game.entity.actor.mask.Hit;
import org.redrune.game.entity.actor.mask.HitSplat;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.utility.functions.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/4/2017
 */
public class MeleeCombatStyle extends AbstractCombatStyle {

    public MeleeCombatStyle() {
        super(new MeleeCombatCalculator());
    }

    @Override
    public boolean fireSwing(Player source, Actor target) {
        int weaponId = source.getEquipment().getWeaponId();
        int combatStyle = source.getCombatDefinitions().getAttackStyle();

        if (source.getCombatDefinitions().isUsingSpecialAttack()) {
            Optional<SpecialAttackPlugin> optional = PluginRepository.getSpecialPlugin(weaponId);
            int energy = CombatAlgorithm.getSpecialAmount(weaponId);
            if (energy == 0 || !optional.isPresent()) {
                source.getPackets().sendMessage("This weapon has no special attack registered; please report this on forums.");
                return false;
            }
            if (source.getCombatDefinitions().hasRingOfVigour())
                energy *= 0.9;
            source.getCombatDefinitions().switchUsingSpecialAttack();
            if (source.getCombatDefinitions().getSpecialAttackPercentage() < energy) {
                source.getPackets().sendMessage("You don't have enough power left.");
                return fireSwing(source, target);
            }
            SpecialAttackPlugin plugin = optional.get();
            plugin.fire(source, target, this);
            source.getCombatDefinitions().decreaseSpecialEnergy(energy);
        } else {
            String weaponName = weaponId == -1 ? "unarmed" : ItemDefinitions.getItemDefinitions(weaponId).getName();

            // the delay until the hitsplat appears
            final int hitDelay = weaponId == 10887 || (weaponName.toLowerCase().contains("maul") && !weaponName.startsWith("Granite")) ? 1 : 0;

            // the player does the attack animation
            source.setNextAnimation(new Animation(CombatAlgorithm.getWeaponAttackEmote(weaponId, combatStyle)));

            // sends the hit to the target
            sendHit(source, target, calculator.getMaximumHit(source, 1), getRandomDamage(source, target, 1), hitDelay);
        }
        return true;
    }

    @Override
    public void addExperience(Player source, Actor target, Hit hit, int attackStyle, int weaponId) {
        int damage = hit.getDamage();
        double combatXp = damage / 2.5;
        if (combatXp > 0) {
            source.getAuraManager().checkSuccefulHits(hit.getDamage());
            if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
                if (attackStyle == 2) {
                    if (target.isPlayer()) {
                        source.getSkills().addXpNoModifier(RANGE, combatXp / 2);
                        source.getSkills().addXpNoModifier(DEFENCE, combatXp / 2);
                    } else {
                        source.getSkills().addXp(RANGE, combatXp / 2);
                        source.getSkills().addXp(DEFENCE, combatXp / 2);
                    }
                } else {
                    if (target.isPlayer()) {
                        source.getSkills().addXpNoModifier(RANGE, combatXp);
                    } else {
                        source.getSkills().addXp(RANGE, combatXp);
                    }
                }
            } else {
                int xpStyle = CombatDefinitions.getXpStyle(weaponId, attackStyle);
                if (xpStyle != CombatDefinitions.SHARED) {
                    if (target.isPlayer()) {
                        source.getSkills().addXpNoModifier(xpStyle, combatXp);
                    } else {
                        source.getSkills().addXp(xpStyle, combatXp);
                    }
                } else {
                    if (target.isPlayer()) {
                        source.getSkills().addXpNoModifier(ATTACK, combatXp / 3);
                        source.getSkills().addXpNoModifier(STRENGTH, combatXp / 3);
                        source.getSkills().addXpNoModifier(DEFENCE, combatXp / 3);
                    } else {
                        source.getSkills().addXp(ATTACK, combatXp / 3);
                        source.getSkills().addXp(STRENGTH, combatXp / 3);
                        source.getSkills().addXp(DEFENCE, combatXp / 3);
                    }
                }
            }
            double hpXp = damage / 7.5;
            if (hpXp > 0) {
                if (target.isPlayer()) {
                    source.getSkills().addXpNoModifier(HITPOINTS, hpXp);
                } else {
                    source.getSkills().addXp(HITPOINTS, hpXp);
                }
            }
        }
    }

    @Override
    public int getRandomDamage(Actor source, Actor target, double multiplier) {
        int weaponId = source.isPlayer() ? source.toPlayer().getEquipment().getWeaponId() : 0;
        int combatStyle = source.isPlayer() ? source.toPlayer().getCombatDefinitions().getAttackStyle() : source.toNPC().getCombatDefinitions().getAttackStyle();
        return CombatRoll.randomizeHit(calculator.getMaximumHit(source, multiplier), calculator.getAttackBonus(source), calculator.getDefenceBonus(target, weaponId, combatStyle));
    }

    @Override
    public CombatSwingDetail sendHit(Player source, Actor target, int maxHit, int damage, int delay) {
        final Hit hit = new Hit(source, damage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit);
        addExperience(source, target, hit, source.getCombatDefinitions().getAttackStyle(), source.getEquipment().getWeaponId());
        target.setNextAnimationNoPriority(new Animation(CombatAlgorithm.getDefenceEmote(target)));
        handleEffects(source, target, hit);

        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                target.applyHit(hit);
            }
        }, delay);
        return new CombatSwingDetail(source, target, hit);
    }
}