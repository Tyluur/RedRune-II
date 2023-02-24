package org.redrune.game.content.plugin;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 8/30/2017
 */
public interface Plugin {

    /**
     * Handles the registration of a plugin
     */
    void register();

    /**
     * Converts a varargs parameter to the String[] array
     *
     * @param varArgs The var args
     */
    default String[] arguments(String... varArgs) {
        return varArgs;
    }

    /**
     * Converts a varargs parameter to the String[] array
     *
     * @param varArgs The var args
     */
    default int[] arguments(int... varArgs) {
        return varArgs;
    }
}
