package plugin.combat.magic.lunar;

import com.rs.game.content.combat.CombatAlgorithm;
import com.rs.game.entity.actor.Actor;
import com.rs.game.entity.actor.mask.Animation;
import com.rs.game.entity.actor.mask.Graphics;
import com.rs.game.entity.actor.player.Player;
import com.rs.game.plugin.combat.spell.type.RegularSpellPlugin;
import com.rs.game.world.World;
import com.rs.game.world.region.Region;
import com.rs.game.world.region.RegionManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class VengeanceGroupSpell extends RegularSpellPlugin {
	
	private static final Animation ANIMATION = new Animation(4411);
	
	private static final Graphics GRAPHICS = new Graphics(725, 0, 100);
	
	@Override
	public int spellId() {
		return 74;
	}
	
	@Override
	public double exp() {
		return 120;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNAR;
	}
	
	@Override
	public void cast(Player player, Actor target) {
		
		Long lastTimeCast = player.getAttribute("LAST_VENG", -1L);
		if (player.getSkills().getLevel(MAGIC) < 94) {
			player.getPackets().sendGameMessage("Your Magic level is not high enough for this spell.");
			return;
		} else if (player.getSkills().getLevel(DEFENCE) < 40) {
			player.getPackets().sendGameMessage("You need a Defence level of 40 for this spell");
			return;
		} else if (lastTimeCast != null && lastTimeCast + 30_000 > System.currentTimeMillis()) {
			player.getPackets().sendGameMessage("You must wait " + (TimeUnit.MILLISECONDS.toSeconds((lastTimeCast + 30_000) - System.currentTimeMillis())) + " more seconds to cast vengeance.");
			return;
		} else if (!CombatAlgorithm.checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 3, EARTH_RUNE, 11)) {
			return;
		}
		// the amount of people the spell affected
		int affectedPeopleCount = 0;
		
		for (int regionId : player.getMapRegionsIds()) {
			Region region = RegionManager.getRegion(regionId);
			List<Integer> playerIndexes = region.getPlayerIndexes();
			
			if (playerIndexes == null) {
				continue;
			}
			for (int playerIndex : playerIndexes) {
				Player other = World.getPlayers().get(playerIndex);
				// skip the unavailable players first
				if (other == null || other == player || other.isDead() || !other.withinDistance(player, 4)) {
					continue;
				}
				/*
				// lets the caster know the other person needs to have accept aid on
				if (!other.getVariables().isAcceptingAid()) {
					player.getPackets().sendGameMessage(other.getDetails().getDisplayName() + " is not accepting aid.");
					continue;
				}
				 */
				// visual
				other.setNextGraphics(GRAPHICS);
				// attributes
				other.putAttribute("cast_veng", true);
				other.putAttribute("LAST_VENG", System.currentTimeMillis());
				// increment total amount
				affectedPeopleCount++;
			}
		}
		
		// visuals
		player.setNextAnimation(ANIMATION);
		player.getSkills().addXp(MAGIC, exp());
		player.getPackets().sendGameMessage("The spell affected " + affectedPeopleCount + " nearby people.");
		
		// store attribute information
		player.putAttribute("LAST_VENG", System.currentTimeMillis());
	}
	
}
