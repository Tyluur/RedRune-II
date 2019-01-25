package plugin.command.owner;

import org.redrune.game.entity.actor.player.Player;
import org.redrune.game.entity.object.WorldObject;
import org.redrune.game.plugin.type.CommandPlugin;
import org.redrune.game.global.map.region.RegionManager;
import org.redrune.utility.game.entity.object.ObjectRemoval;
import plugin.command.CommandManifest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 9/1/2017
 */
@CommandManifest(description = "Stops an object from spawning on our tile")
public class StopObjectSpawnCommandPlugin extends CommandPlugin {
	
	public static final List<WorldObject> STOPPED_OBJECTS = new ArrayList<>();
	
	@Override
	public void handle(Player player, String[] args, boolean console, boolean clientCommand) {
		List<WorldObject> stream = RegionManager.getRegion(player.getRegionId()).getObjects().stream().filter(object -> !object.isSpawned() && !STOPPED_OBJECTS.contains(object) && object.getWorldTile().matches(player.getWorldTile())).collect(Collectors.toList());
		System.out.println(stream);
		Optional<WorldObject> optional = stream.stream().findFirst();
		if (!optional.isPresent()) {
			player.getPackets().sendGameMessage("Did not find any object on this tile...");
			return;
		}
		WorldObject object = optional.get();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ObjectRemoval.NONSPAWNING_OBJECTS_FILE, true))) {
			String pattern = "{0} {1} {2} {3} {4} {5}";
			Object[] arguments = new Object[] { String.valueOf(object.getId()), object.getType(), object.getRotation(), String.valueOf(player.getX()), String.valueOf(player.getY()), String.valueOf(player.getPlane()) };
			String output = MessageFormat.format(pattern, arguments);
			bw.append(output);
			bw.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		STOPPED_OBJECTS.add(object);
		RegionManager.removeObject(object);
		player.getPackets().sendGameMessage("Found and stopped this object from spawning!<br>" + object + "");
	}
	
	@Override
	public String[] identifiers() {
		return arguments("stos");
	}
}
