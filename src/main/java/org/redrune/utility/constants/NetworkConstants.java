package org.redrune.utility.constants;

import com.google.common.collect.ImmutableList;
import io.netty.util.AttributeKey;
import org.redrune.net.NetworkSession;

import java.math.BigInteger;

/**
 * This class contains constant values for networking
 *
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/30/2017
 */
public interface NetworkConstants {

    /**
     * The runescape protocol number of the server
     */
    int PROTOCOL_NUMBER = 667;

    /**
     * The port the server listens on
     */
    int PORT_ID = 43594;

    /**
     * The maximum size of an incoming packet
     */
    int RECEIVE_DATA_LIMIT = 7500;

    /**
     * The maximum size of an outgoing packet
     */
    int PACKET_SIZE_LIMIT = 7500;

    /**
     * The js5-request opcode.
     */
    int JS5_REQUEST = 15;

    /**
     * The login request opcode.
     */
    int LOGIN_REQUEST = 14;

    /**
     * The time that makes a player inactive for logic packets
     */
    long MAX_PACKETS_DECODER_PING_DELAY = 30000;

    /**
     * The keys sent during grab server decoding
     */
    int[] GRAB_SERVER_KEYS = {1362, 77448, 44880, 39771, 24563, 363672, 44375, 0, 1614, 0, 5340, 142976, 741080, 188204, 358294, 416732, 828327, 19517, 22963, 16769, 1244, 11976, 10, 15, 119, 817677, 1624243};

    /**
     * The game server RSA key exponent.
     */
    BigInteger LOGIN_EXPONENT = new BigInteger("118762062543447234074727456808991118872170985751713606465722882159677729022791781634631275342059815254405716406345046978427092008247432011727406028949468214427650611830755367261560706476584680737149281348803965978585646403259797833935425150657845103665015467365527987767725318315713043512372527291907415762273");

    /**
     * The game server RSA key modulus.
     */
    BigInteger LOGIN_MODULUS = new BigInteger("123733137684565391382986985515878973634831964473007354491671126289247096002904505166425503816809330286277302494636833012609314653193945563916110405049937997195310625096132297106334199144922705176219016362504626538048084031862798816953255666088269887586583538984815994152019844370040268440057091407812614894353");

    /**
     * The attribute that contains the key for a session.
     */
    AttributeKey<NetworkSession> SESSION_KEY = AttributeKey.valueOf("session.key");

    /**
     * The list of exceptions that are ignored
     */
    ImmutableList<String> IGNORED_EXCEPTIONS = ImmutableList.of("Connection reset", "An existing connection was forcibly closed by the remote host", "An established connection was aborted by the software in your host machine");

    /**
     * The map sizes
     */
    int[] MAP_SIZES = {104, 120, 136, 168};

    /**
     * The length of a timeout
     */
    Integer TIMEOUT_RATE = 30_000;

}
