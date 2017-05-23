package org.redrune.utility.backend.salt;

/**
 * SaltUtilities.java
 * @author Chryonic
 * May 22, 2017 | RedRune
 */
public interface SaltUtilities {

	byte[] getSalt64();

	byte[] getSalt32();

	byte[] getSalt(final int size);

	byte[] hash(final String password, final byte[] salt);

	boolean isExpectedPassword(final String password, final byte[] salt, final byte[] hash);

	String generateRandomPassword(final int length);
}