package plugin.command.player;

import org.redrune.game.content.plugin.type.CommandPlugin;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.global.World;
import org.redrune.utility.constants.InterfaceConstants;
import plugin.command.CommandManifest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Shows the players online")
public class PlayersOnlineCommandPlugin extends CommandPlugin {

    @Override
    public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
        List<String> messages = new ArrayList<>();

        World.playerStream().forEach(p -> {
            messages.add("" + p.getDisplayName() + " (lvl. " + p.getSkills().getCombatLevel() + ")");
        });

        player.getPackets().sendMessage("There are currently " + World.getPlayers().size() + " players online.", console);
        InterfaceConstants.sendQuestScroll(player, "Dusk", messages.toArray(new String[messages.size()]));
    }

    @Override
    public String[] identifiers() {
        return arguments("players");
    }
}
