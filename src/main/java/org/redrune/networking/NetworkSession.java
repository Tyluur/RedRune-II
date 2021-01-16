package org.redrune.networking;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.redrune.game.entity.actor.player.Player;
import org.redrune.networking.packet.Packet;
import org.redrune.networking.packet.PacketBuilder;
import org.redrune.networking.packet.context.PacketContext;
import org.redrune.networking.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.functions.Misc;
import org.redrune.utility.game.session.ISAACCipher;

import java.util.concurrent.ConcurrentLinkedQueue;

;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
public class NetworkSession {

    /**
     * The queue of packets that have already been decoded and are awaiting processing
     */
    private final ConcurrentLinkedQueue<PacketContext> contextQueue = new ConcurrentLinkedQueue<>();

    /**
     * The player affiliated with this network session
     */

    private Player player;

    /**
     * The channel instance.
     */

    private Channel channel;

    /**
     * The mac address affiliated with the session
     */

    private String macAddress;

    /**
     * If the session is in the lobby
     */


    private boolean inLobby;

    /**
     * The ISAAC cipher for incoming data.
     */


    private ISAACCipher inCipher;

    /**
     * The ISAAC cipher for outgoing data
     */


    private ISAACCipher outCipher;

    public NetworkSession(Channel channel) {
        this.channel = channel;
    }

    /**
     * This method is invoked when the session is registered
     */
    public void onRegistration() {
        System.out.println("Session registered! [" + toString() + "]");
    }

    @Override
    public String toString() {
        return "NetworkSession{" + "player=" + player + ", inLobby=" + inLobby + '}';
    }

    /**
     * This method is invoked when the session is deregistered
     */
    public void onDeregistration() {
        if (player != null) {
            if (inLobby) {
                player.finishLobby();
            } else {
                player.finish();
            }
        }
        System.out.println("Session deregistered! [" + toString() + "]");
    }

    /**
     * Writes a packet to the channel and flushes it
     *
     * @param bldr The builder of the packet to flush
     */
    public synchronized ChannelFuture write(OutgoingPacketBuilder bldr) {
        Packet build = bldr.build();
        //		System.out.println("Wrote packet " + build);
        return channel.write(build);
    }

    /**
     * Writes a packet to the channel and flushes it
     *
     * @param bldr The builder of the packet to flush
     */
    public synchronized ChannelFuture write(PacketBuilder bldr) {
        Packet msg = bldr.toPacket();
        //		System.out.println("Wrote packet " + msg);
        return channel.write(msg);
    }

    /**
     * Flushes all the outgoing buffers
     *
     * @return
     */
    public synchronized Channel flush() {
        return channel.flush();
    }

    /**
     * Gets the ip
     */
    public String getIPAddress() {
        return Misc.getIpAddress(channel);
    }

    /**
     * Builds the ciphers
     *
     * @param inCipher  The incoming cipher
     * @param outCipher The outgoing cipher
     */
    public void buildCiphers(ISAACCipher inCipher, ISAACCipher outCipher) {
        setInCipher(inCipher);
        setOutCipher(outCipher);
    }

    /**
     * Adds the context of a packet to the queue
     *
     * @param context The context
     */
    public void addContext(PacketContext context) {
        contextQueue.add(context);
    }

    /**
     * Processes the context queue
     */
    public void processContextQueue() {
        PacketContext context;
        while ((context = contextQueue.poll()) != null) {
            context.handle(player);
        }
    }

    public void setInCipher(ISAACCipher inCipher) {
        this.inCipher = inCipher;
    }

    public void setOutCipher(ISAACCipher outCipher) {
        this.outCipher = outCipher;
    }

    public void setInLobby(boolean inLobby) {
        this.inLobby = inLobby;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public boolean isInLobby() {
        return inLobby;
    }
}
