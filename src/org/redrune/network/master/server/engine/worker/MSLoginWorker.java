package org.redrune.network.master.server.engine.worker;

import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.MasterSession;
import org.redrune.network.master.server.engine.MSEngineWorker;
import org.redrune.network.master.server.network.packet.out.LoginResponsePacketOut;
import org.redrune.network.master.server.network.packet.out.LobbyRepositoryPacketOut;
import org.redrune.network.master.server.world.MSRepository;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.master.utility.rs.LoginConstants;
import org.redrune.network.master.utility.rs.LoginRequest;
import org.redrune.utility.backend.ReturnCode;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class MSLoginWorker extends MSEngineWorker {
	
	/**
	 * The queue of login requests
	 */
	private final Queue<LoginRequest> loginRequests = new LinkedBlockingQueue<>();
	
	@Override
	public void schedule(ScheduledExecutorService service) {
		service.scheduleWithFixedDelay(this, 0, 100, TimeUnit.MILLISECONDS);
		
		System.out.println("Scheduled the login worker!");
	}
	
	@Override
	public void run() {
		LoginRequest request;
		while ((request = loginRequests.poll()) != null) {
			byte worldId = request.getWorldId();
			boolean lobby = request.isLobby();
			String username = request.getUsername();
			String password = request.getPassword();
			MasterSession session = request.getSession();
			String uid = request.getUuid();
			
			// if there is a user online with that name already
			boolean online;
			
			// the response code
			ReturnCode returnCode = ReturnCode.SUCCESSFUL;
			
			if (lobby) {
				online = MSRepository.isOnline(username, true);
			} else {
				online = MSRepository.isOnline(username, false);
			}
			
			// if the player is online we change the return code
			if (online) {
				returnCode = ReturnCode.ALREADY_ONLINE;
			}
			
			String fileText = "empty";
			
			// the player file existed, so we use the text in it
			if (Utility.playerFileExists(username)) {
				fileText = Utility.getCollapsedText(LoginConstants.getLocation(username));
			}
			
			// the return code was not successful so we
			// dont need to send the file over the network anyway
			if (returnCode != ReturnCode.SUCCESSFUL) {
				fileText = "empty";
			}
			
			// the byte value
			byte responseCode = returnCode.getValue();
			
			// if we had a successful login, we can then add the player to the world
			// as long as they are connecting to a world, not the lobby.
			if (returnCode == ReturnCode.SUCCESSFUL) {
				// add the player and update the lobby if we can
				MSRepository.getWorld(worldId).ifPresent(world -> {
					world.addPlayer(username, uid);
					// sends the repository update as long as the world isn't the lobby world
					if (!world.isLobby()) {
						MSRepository.getWorld(MasterConstants.LOBBY_WORLD_ID).ifPresent(lobbyWorld -> lobbyWorld.getSession().write(new LobbyRepositoryPacketOut(world)));
					}
				});
			}
			
			// writes the response
			session.write(new LoginResponsePacketOut(uid, responseCode, fileText, username, lobby));
			
			System.out.println("handled login request [" + returnCode + "]:\t" + request);
			System.out.println("sent login request back to session: " + session);
		}
	}
	
	/**
	 * Adds a request to the queue
	 *
	 * @param request
	 * 		The request to add
	 */
	public void addRequest(LoginRequest request) {
		loginRequests.add(request);
	}
	
}
