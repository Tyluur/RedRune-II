package org.redrune.networking.codec.js5;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 2019-02-02
 */
public class UpdateServerRequest {
	
	/**
	 * The id of the index
	 */
	@Getter
	private final int indexId;
	
	/**
	 * The id of the archive
	 */
	@Getter
	private final int archiveId;
	
	/**
	 * If this request should be prioritized
	 */
	@Getter
	private final boolean priority;
	
	public UpdateServerRequest(int indexId, int archiveId, boolean priority) {
		this.indexId = indexId;
		this.archiveId = archiveId;
		this.priority = priority;
	}
}
