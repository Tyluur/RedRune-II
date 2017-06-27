package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.prayer.PrayerBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/26/2017
 */
@CommandManifest(description = "Sets the prayer book", types = String.class)
public class SetPrayerBookCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("setprayerbook");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		PrayerBook book = PrayerBook.valueOf(args[1].toUpperCase());
		player.getManager().getPrayers().setBook(book);
	}
}
