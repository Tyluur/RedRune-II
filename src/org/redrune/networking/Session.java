package org.redrune.networking;

import lombok.Getter;
import lombok.Setter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.codec.Decoder;
import org.redrune.networking.codec.Encoder;
import org.redrune.networking.codec.decode.ClientPacketsDecoder;
import org.redrune.networking.codec.decode.GrabPacketsDecoder;
import org.redrune.networking.codec.decode.LoginPacketsDecoder;
import org.redrune.networking.codec.decode.WorldPacketsDecoder;
import org.redrune.networking.codec.encode.GrabPacketsEncoder;
import org.redrune.networking.codec.encode.LoginPacketsEncoder;
import org.redrune.networking.codec.encode.WorldPacketsEncoder;
import org.redrune.networking.stream.InputStream;
import org.redrune.networking.stream.OutputStream;
import org.redrune.utility.functions.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Session {
	
	/**
	 * The channel of this session
	 */
	@Getter
	private final Channel channel;
	
	/**
	 * The queue of incoming packets to process
	 */
	@Getter
	private final List<InputStream> incomingQueue;
	
	/**
	 * The queue of outgoing buffers
	 */
	@Getter
	private final Queue<OutputStream> outgoingQueue;
	
	/**
	 * The current decoder
	 */
	@Getter
	private Decoder decoder;
	
	/**
	 * The current encoder
	 */
	@Getter
	private Encoder encoder;
	
	/**
	 * The player attached to this session
	 */
	@Getter
	private Player player;
	
	/**
	 * The mac address of the session
	 */
	@Getter
	@Setter
	private String macAddress = "";
	
	/**
	 * If this session is in the lobby
	 */
	@Getter
	@Setter
	private boolean inLobby;
	
	public Session(Channel channel) {
		this.channel = channel;
		this.outgoingQueue = new ConcurrentLinkedQueue<>();
		this.incomingQueue = Collections.synchronizedList(new ArrayList<>());
		setDecoder(0);
	}
	
	/**
	 * the Sets the decoder
	 *
	 * @param stage
	 * 		The stage
	 */
	public void setDecoder(int stage) {
		setDecoder(stage, null);
	}
	
	/**
	 * Sets the decoder
	 *
	 * @param stage
	 * 		The stage
	 * @param attachment
	 * 		The attachment for the decoder, only used for world packets
	 */
	public void setDecoder(int stage, Object attachment) {
		switch (stage) {
			case 0:
				decoder = new ClientPacketsDecoder(this);
				break;
			case 1:
				decoder = new GrabPacketsDecoder(this);
				break;
			case 2:
				decoder = new LoginPacketsDecoder(this);
				break;
			case 3:
				decoder = new WorldPacketsDecoder(this, (Player) attachment);
				break;
			case -1:
			default:
				decoder = null;
				break;
		}
	}
	
	/**
	 * Writes an outgoing buffer to the session. If the session has a player the stream is queued and sent at the end of
	 * the current/next world cycle
	 *
	 * @param outStream
	 * 		The buffer
	 */
	public ChannelFuture write(OutputStream outStream) {
		if (player == null) {
			if (channel.isConnected()) {
				return channel.write(ChannelBuffers.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset()));
			}
		} else {
			outgoingQueue.add(outStream);
		}
		return null;
	}
	
	/**
	 * Writes to the channel a buffered stream, and returns a {@code ChannelFuture}. The data is also copied.
	 *
	 * @param outStream
	 * 		The output stream
	 * @return {@code ChannelFuture} {@code Object}
	 */
	public final ChannelFuture writeWithFuture(OutputStream outStream) {
		if (outStream == null || !channel.isOpen()) {
			return null;
		}
		return channel.write(ChannelBuffers.copiedBuffer(outStream.getBuffer(), 0, outStream.getOffset()));
	}
	
	/**
	 * Adds a stream to the queue of incoming streams to process
	 *
	 * @param stream
	 * 		The stream
	 */
	public boolean addStreamToIncomingQueue(InputStream stream) {
		return incomingQueue.add(stream);
	}
	
	/**
	 * Flushes the {@link #outgoingQueue} to the channel
	 */
	public void flushOutgoingQueue() {
		OutputStream stream;
		while ((stream = outgoingQueue.poll()) != null) {
			channel.write(ChannelBuffers.copiedBuffer(stream.getBuffer(), 0, stream.getOffset()));
		}
	}
	
	/**
	 * Writes a buffer to the channel
	 *
	 * @param buffer
	 * 		The buffer
	 */
	public ChannelFuture write(ChannelBuffer buffer) {
		if (channel.isConnected()) {
			return channel.write(buffer);
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the encoder
	 *
	 * @param stage
	 * 		The stage that indicates which encoder to use
	 */
	public void setEncoder(int stage) {
		setEncoder(stage, null);
	}
	
	/**
	 * Sets the encoder
	 *
	 * @param stage
	 * 		The stage that indicates which encoder to use
	 * @param attachment
	 * 		The attachment for the encoder, only used for world packets
	 */
	public void setEncoder(int stage, Object attachment) {
		switch (stage) {
			case 0:
				encoder = new GrabPacketsEncoder(this);
				break;
			case 1:
				encoder = new LoginPacketsEncoder(this);
				break;
			case 2:
				encoder = new WorldPacketsEncoder(this, (Player) attachment);
				break;
			case -1:
			default:
				encoder = null;
				break;
		}
	}
	
	/**
	 * Gets the {@code LoginPacketsEncoder} cast instance of the encoder
	 */
	public LoginPacketsEncoder getLoginPackets() {
		return (LoginPacketsEncoder) encoder;
	}
	
	/**
	 * Gets the {@code GrabPacketsEncoder} cast instance of the encoder
	 */
	public GrabPacketsEncoder getGrabPackets() {
		return (GrabPacketsEncoder) encoder;
	}
	
	/**
	 * Gets the {@code WorldPacketsEncoder} cast instance of the encoder
	 */
	public WorldPacketsEncoder getWorldPackets() {
		return (WorldPacketsEncoder) encoder;
	}
	
	/**
	 * Gets the ip
	 */
	public String getIp() {
		return Misc.getIpAddress(channel);
	}
	
	/**
	 * Syncs the session and player together
	 */
	public void sync(Player player) {
		this.player = player;
		player.setSession(this);
	}
	
}
