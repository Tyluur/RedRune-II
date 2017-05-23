package org.redrune.network.session.impl;

import io.netty.channel.Channel;
import org.redrune.network.ChannelListener;
import org.redrune.network.NetworkConstants;
import org.redrune.network.protocol.ProtocolResponse;
import org.redrune.network.protocol.ProtocolThrottle;
import org.redrune.network.protocol.game.GameReadEvent;
import org.redrune.network.protocol.game.GameWriteEvent;
import org.redrune.network.protocol.login.msg.LoginRequestEvent;
import org.redrune.network.protocol.login.msg.LoginResponseEvent;
import org.redrune.network.session.Session;
import org.redrune.rs2.node.entity.player.Player;

/**
 * LoginSession.java
 *
 * @author Chryonic May 22, 2017 | RedRune
 */
public class LoginSession extends Session {
	
	private Player player;
	
	public LoginSession(Channel channel) {
		super(channel);
	}
	
	@Override
	public void throttleRequest(Object context) {
		if (context instanceof LoginRequestEvent) {
			LoginRequestEvent request = (LoginRequestEvent) context;
			
			if (request.getMajor() != NetworkConstants.REVISION) {
				channel.writeAndFlush(new LoginResponseEvent(request.getLoginRequest(), ProtocolResponse.OUT_OF_DATE, null));
			} else {
				player = new Player(request.getUsername(), request.getPassword());
				player.addPlayerToWorld(request.getLoginRequest());
				channel.writeAndFlush(new LoginResponseEvent(request.getLoginRequest(), ProtocolResponse.SUCCESSFUL_LOGIN, player));
				channel.pipeline().addAfter("login.read", "game.write", new GameWriteEvent());
				channel.pipeline().replace("login.read", "game.read", ProtocolThrottle.setSession(channel, new GameReadEvent(request.getIsaacPair())));
				channel.attr(ChannelListener.CURRENT_SESSION).set(new GameSession(player, channel, request.getIsaacPair()));
				player.getInterfaceManager().setDisplayMode(request.getDisplayMode());
				player.sendLogin(channel.attr(ChannelListener.CURRENT_SESSION).get(), request.getLoginRequest());
			}
		}
	}
	
	@Override
	public void disconnect() throws InterruptedException {
		// player.logout
	}
	
}
