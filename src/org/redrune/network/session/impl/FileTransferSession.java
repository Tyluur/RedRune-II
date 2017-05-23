package org.redrune.network.session.impl;

import io.netty.channel.Channel;
import org.redrune.network.protocol.filetransfer.FTEncryptEvent;
import org.redrune.network.protocol.filetransfer.msg.FTEncryptRequestEvent;
import org.redrune.network.protocol.filetransfer.msg.FTRequestEvent;
import org.redrune.network.protocol.filetransfer.msg.FTResponseEvent;
import org.redrune.network.session.Session;

/**
 * FileTransferSession.java
 *
 * @author Chryonic May 22, 2017 | RedRune
 */
public class FileTransferSession extends Session {
	
	public FileTransferSession(Channel channel) {
		super(channel);
	}
	
	@Override
	public void throttleRequest(Object context) {
		if (context instanceof FTRequestEvent) {
			FTRequestEvent request = (FTRequestEvent) context;
			if (request.getArchive() < 0) {
				return;
			}
			channel.writeAndFlush(new FTResponseEvent(request.getContainer(), request.getArchive(), request.isPriority()));
		} else if (context instanceof FTEncryptRequestEvent) {
			FTEncryptRequestEvent encryption = (FTEncryptRequestEvent) context;
			FTEncryptEvent event = channel.pipeline().get(FTEncryptEvent.class);
			event.setEncryptedKey(encryption.getKey());
		}
	}
	
	@Override
	public void disconnect() throws InterruptedException {
	}
	
}
