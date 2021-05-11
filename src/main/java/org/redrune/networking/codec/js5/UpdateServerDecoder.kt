package org.redrune.networking.codec.js5

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.redrune.cache.Cache
import org.redrune.engine.factory.JS5ThreadFactory
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.experimental.and

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class UpdateServerDecoder : ByteToMessageDecoder() {

    /**
     * The list of requests
     */
    private val requests = LinkedList<UpdateServerRequest>()

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: List<Any>) {
        while (`in`.readableBytes() >= 4) {
            val priority: Int = (`in`.readByte() and 0xFF.toByte()).toInt()
            serveRequest(ctx, `in`, priority)
        }
    }

    /**
     * Serves the request
     *
     * @param ctx      The channel context
     * @param `in`       The buffer
     * @param priority The priority of the request
     */
    private fun serveRequest(ctx: ChannelHandlerContext, buf: ByteBuf, priority: Int) {
        val indexId = (buf.readUnsignedByte() and 0xFF).toInt()
        val archiveId = (buf.readUnsignedShort() and 0xFFFF).toInt()

        if (indexId != 255) {
            if (Cache.STORE.indexes.size <= indexId || Cache.STORE.indexes[indexId] == null || !Cache.STORE.indexes[indexId].archiveExists(
                    archiveId
                )
            ) {
                return
            }
        } else if (archiveId != 255) {
            if (Cache.STORE.indexes.size <= archiveId || Cache.STORE.indexes[archiveId] == null) {
                return
            }
        }
        when (priority) {
            0 -> requests.add(UpdateServerRequest(indexId, archiveId, false))
            1 -> EXECUTOR_SERVICE.submit(Callable {
                ctx.writeAndFlush(
                    Cache.getCacheArchive(
                        indexId,
                        archiveId,
                        true
                    )
                )
            })
            2, 3 -> requests.clear()
        }
        while (requests.size > 0) {
            val request = requests.removeFirst()
            ctx.writeAndFlush(Cache.getCacheArchive(request.indexId, request.archiveId, request.isPriority))
        }
    }

    companion object {
        /**
         * The service used explicitly for update server transmission
         */
        private val EXECUTOR_SERVICE =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), JS5ThreadFactory("JS5-Worker"))
    }
}