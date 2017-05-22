package org.redrune.network.session.impl;

import org.redrune.cache.Cache;
import org.redrune.network.protocol.filetransfer.FTEncryptEvent;
import org.redrune.network.protocol.filetransfer.msg.FTEncryptRequestEvent;
import org.redrune.network.protocol.filetransfer.msg.FTRequestEvent;
import org.redrune.network.protocol.filetransfer.msg.FTResponseEvent;
import org.redrune.network.session.Session;

import io.netty.channel.Channel;

/**
 * FileTransferSession.java
 * @author Chryonic
 * May 22, 2017 | RedRune
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
			if (request.getContainer() != 0xff) {
				if (Cache.getSTORE().getIndexes().length <= request.getContainer()
						|| Cache.getSTORE().getIndexes()[request.getContainer()] == null
						|| !Cache.getSTORE().getIndexes()[request.getContainer()].archiveExists(request.getArchive())) {
					System.out.println(request.getContainer() + " : " + request.getArchive());
					return;
				}
			} else if (request.getArchive() != 0xff) {
				if (Cache.getSTORE().getIndexes().length <= request.getArchive()
						|| Cache.getSTORE().getIndexes()[request.getArchive()] == null) {
					System.out.println(request.getContainer() + " : " + request.getArchive());
					return;
				}
			}
			channel.writeAndFlush(
					new FTResponseEvent(request.getContainer(), request.getArchive(), request.isPriority()));
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
