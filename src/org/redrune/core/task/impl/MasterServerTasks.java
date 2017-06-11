package org.redrune.core.task.impl;

import org.redrune.core.task.ScheduledTask;
import org.redrune.core.task.context.LoginData;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.RS2MasterCommunication;
import org.redrune.network.master.packet.out.client.build.ClientStatisticsBuilder;
import org.redrune.network.master.packet.out.client.context.ClientStatisticsContext;
import org.redrune.network.rs666.NetworkSession;
import org.redrune.network.rs666.packet.outgoing.impl.LobbyResponseBuilder;
import org.redrune.network.rs666.packet.outgoing.impl.LoginResponseCodeBuilder;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.ReturnCode;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterServerTasks extends ScheduledTask {
	
	/**
	 * The queue of data
	 */
	private static final Queue<LoginData> LOGIN_DATA_QUEUE = new ConcurrentLinkedQueue<>();
	
	/**
	 * The last time the world repository statistics were updated
	 */
	private long lastWorldTimeUpdated = -1;
	
	public MasterServerTasks() {
		super(1, true);
	}
	
	@Override
	public Runnable getTask() {
		return () -> {
			this.handleLoginQueue();
			if (Misc.timeHasPassed(lastWorldTimeUpdated, TimeUnit.SECONDS.toMillis(10))) {
				RS2MasterCommunication.writeMasterPacket(new ClientStatisticsBuilder(new ClientStatisticsContext()).build());
				lastWorldTimeUpdated = System.currentTimeMillis();
			}
		};
	}
	
	private void handleLoginQueue() {
		LoginData data;
		while ((data = LOGIN_DATA_QUEUE.poll()) != null) {
			final NetworkSession session = RS2MasterCommunication.getSession(data.getUid());
			if (session == null) {
				System.out.println("Unable to find session by uid: " + data.getUid());
				continue;
			}
			final String username = data.getUsername();
			final String password = data.getPassword();
			final boolean lobby = session.isInLobby();
			final int code = data.getCode();
			session.write(new LoginResponseCodeBuilder(code).build(null));
			
			if (code != ReturnCode.SUCCESSFUL.getValue()) {
				return;
			}
			if (lobby) {
				Player player = new Player(username, password, session);
				player.registerTransients();
				
				session.write(new LobbyResponseBuilder().build(player));
			} else {
				Player player = new Player(username, password, session);
				player.register();
			}
		}
	}
	
	/**
	 * Adds a login to the queue
	 *
	 * @param data
	 * 		The login data
	 */
	public static void addLogin(LoginData data) {
		LOGIN_DATA_QUEUE.add(data);
	}
	
}
