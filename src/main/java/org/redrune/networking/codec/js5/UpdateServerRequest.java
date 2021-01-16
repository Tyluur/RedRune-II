package org.redrune.networking.codec.js5;



/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-02
 */
public class UpdateServerRequest {
	
	/**
	 * The id of the index
	 */

	private final int indexId;
	
	/**
	 * The id of the archive
	 */

	private final int archiveId;
	
	/**
	 * If this request should be prioritized
	 */

	private final boolean priority;
	
	public UpdateServerRequest(int indexId, int archiveId, boolean priority) {
		this.indexId = indexId;
		this.archiveId = archiveId;
		this.priority = priority;
	}

	public int getIndexId() {
		return indexId;
	}

	public int getArchiveId() {
		return archiveId;
	}

	public boolean isPriority() {
		return priority;
	}
}
