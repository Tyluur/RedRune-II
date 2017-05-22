package org.redrune.network.stream;

import io.netty.buffer.ByteBuf;

/**
 * IoHeap.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public interface IoHeap {

	public boolean isRaw();

	public ByteBuf getBuffer();

}
