package org.redrune.core.master.server;

import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.channel.Channel;
import org.redrune.core.EngineWorkingSet;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.packet.out.server.build.ServerLoginResponseBuilder;
import org.redrune.network.master.packet.out.server.build.ServerPlayerLoginBuilder;
import org.redrune.network.master.packet.out.server.context.ServerLoginResponseContext;
import org.redrune.network.master.packet.out.server.context.ServerPlayerLoginContext;
import org.redrune.network.master.server.MasterServerHandler;
import org.redrune.network.master.server.login.MasterServerLogin;
import org.redrune.utility.Misc;
import org.redrune.utility.backend.ReturnCode;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * The worker for all master server operations
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class MasterUpdateWorker implements Runnable {
	
	/**
	 * The logger
	 */
	private static final Logger LOGGER = Misc.constructLogger(MasterUpdateWorker.class);
	
	/**
	 * The instance of the repository
	 */
	private final MasterServerRepository repository;
	
	/**
	 * The queue of login attempts
	 */
	private final Queue<MasterServerLogin> loginQueue;
	
	/**
	 * If the worker is running
	 */
	@Getter
	@Setter
	private boolean running;
	
	/**
	 * The last time the world statuses have been checked
	 */
	private long lastStatusCheck = -1;
	
	MasterUpdateWorker(MasterServerRepository repository) {
		this.repository = repository;
		this.loginQueue = new LinkedBlockingQueue<>();
	}
	
	@Override
	public void run() {
		while (running) {
			process();
		}
		LOGGER.info("The master update worker thread stopped.");
	}
	
	/**
	 * Handles a single master server tick
	 */
	private void process() {
		try {
			if (Misc.timeHasPassed(lastStatusCheck, TimeUnit.SECONDS.toMillis(10))) {
				for (int worldId = 1; worldId <= repository.getWorldCount(); worldId++) {
					repository.setWorldOnline(worldId, isWorldOnline(worldId));
				}
				lastStatusCheck = System.currentTimeMillis();
			}
			processLogins();
			Thread.sleep(600);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Processes the login queue sequencially.
	 */
	private void processLogins() {
		if (loginQueue.isEmpty()) {
			return;
		}
		MasterServerLogin login;
		while ((login = loginQueue.poll()) != null) {
			try {
				Channel channel = login.getChannel();
				long uid = login.getUid();
				String username = login.getUsername();
				String password = login.getPassword();
				boolean lobbyConnection = login.isLobbyConnection();
				int worldId = login.getWorldId();
				
				ReturnCode code;
				if (Misc.invalidAccountName(username) || username.length() < 3 || password.length() >= 30) {
					code = ReturnCode.INVALID_CREDENTIALS;
				} else if (MasterServerHandler.getRepository().userLoggedIn(username, lobbyConnection)) {
					code = ReturnCode.ALREADY_ONLINE;
				} else {
					code = ReturnCode.SUCCESSFUL;
				}
				
				final ServerLoginResponseContext context = new ServerLoginResponseContext(uid, username, password, lobbyConnection, code.getValue());
				
				if (code == ReturnCode.SUCCESSFUL) {
					if (lobbyConnection) {
						// just incase we weren't able to add the player.
						if (!MasterServerHandler.getRepository().addLobbyPlayer(uid, username, worldId)) {
							context.setResponseCode(ReturnCode.ALREADY_ONLINE.getValue());
							channel.write(new ServerLoginResponseBuilder(context).build());
							continue;
						}
					} else {
						// failsafe
						if (!MasterServerHandler.getRepository().addWorldPlayer(uid, username, worldId)) {
							context.setResponseCode(ReturnCode.ALREADY_ONLINE.getValue());
							channel.write(new ServerLoginResponseBuilder(context).build());
							continue;
						}
					}
					
					if (lobbyConnection) {
						MasterServerHandler.getRepository().removeWorldPlayer(username);
					} else {
						MasterServerHandler.getRepository().removeLobbyPlayer(username);
					}
					
					boolean exists = MasterServerLogin.accountExists(username);
					Player player;
					if (!exists) {
						player = new Player(username);
					} else {
						player = Misc.constructPlayer(MasterServerLogin.getAccountFileText(username));
					}
					if (player == null) {
						System.out.println("Unexpected error while parsing player file!");
						System.out.println("Username: " + username);
						context.setResponseCode(ReturnCode.MALFORMED_LOGIN_PACKET.getValue());
						continue;
					}
					
					String text = MasterServerLogin.generateJsonFileText(player, false);
					if (text == null) {
						System.out.println("Unexpected error while parsing player file!");
						System.out.println("Username: " + username);
						context.setResponseCode(ReturnCode.MALFORMED_LOGIN_PACKET.getValue());
						continue;
					}
					
					context.setFileJsonText(text);
					EngineWorkingSet.getScheduledExecutorService().schedule(() -> MasterServerHandler.getRepository().writeToAllWorlds(new ServerPlayerLoginBuilder(new ServerPlayerLoginContext(username, lobbyConnection ? 0 : worldId)).build()), 3, TimeUnit.SECONDS);
				}
				System.out.println("Writing " + context.getResponseCode() + " [" + context.getUsername() + "/" + context.getPassword() + "] to " + Misc.getIpAddress(channel));
				channel.write(new ServerLoginResponseBuilder(context).build());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Pings the socket to verify the world is online
	 *
	 * @param worldId
	 * 		The id of the world, used to find what port to ping
	 */
	private boolean isWorldOnline(int worldId) {
		return Misc.portIsOpen("localhost", NetworkConstants.BASE_PORT_ID + worldId);
	}
	
	/**
	 * Starts the thread
	 */
	public void start() {
		if (running) {
			return;
		}
		setRunning(true);
		EngineWorkingSet.submitEngineWork(this);
		LOGGER.info("Started the master update worker thread.");
	}
	
	/**
	 * Adds a login to the queue
	 *
	 * @param login
	 * 		The login to add
	 */
	public boolean addLogin(MasterServerLogin login) {
		return loginQueue.add(login);
	}
}
