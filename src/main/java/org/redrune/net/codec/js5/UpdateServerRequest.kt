package org.redrune.net.codec.js5

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 2019-02-02
 */
class UpdateServerRequest(
    /**
     * The id of the index
     */
    val indexId: Int,
    /**
     * The id of the archive
     */
    val archiveId: Int,
    /**
     * If this request should be prioritized
     */
    val isPriority: Boolean
)