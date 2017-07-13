package master.server.engine.worker;

import master.network.MasterSession;
import master.server.engine.MSEngineWorker;
import master.server.network.packet.out.LoginResponsePacketOut;
import master.server.world.MSRepository;
import master.utility.Utility;
import master.utility.rs.LoginConstants;
import master.utility.rs.LoginRequest;
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
			String uuid = request.getUuid();
			
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
			if (returnCode == ReturnCode.SUCCESSFUL) {
				MSRepository.getWorld(worldId).ifPresent(world -> world.addPlayer(username, lobby));
			}
			
			// writes the response
			session.write(new LoginResponsePacketOut(uuid, responseCode, fileText, username, lobby));
			
			System.out.println("handled login request [" + returnCode + "]:\t" + request);
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
