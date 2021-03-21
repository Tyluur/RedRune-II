package org.redrune.game.global.map;

import org.redrune.game.global.map.region.RegionBuilder;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since March 20, 2021
 */
public enum MapMerger {

    DUEL_ARENA_TO_EDGEVILLE {
        @Override
        public void merge() {
            int topFromX = 419, toRegionX = 380;
            int tomFromY = 408, toRegionY = 439;
            int ratio = 4;

            RegionBuilder.copyAllPlanesMap(topFromX, tomFromY, toRegionX, toRegionY, ratio); // top
            // left
            RegionBuilder.copyAllPlanesMap(topFromX - 4, tomFromY, toRegionX - 4, toRegionY, ratio); // top
            // right

            RegionBuilder.copyAllPlanesMap(topFromX - 4, tomFromY - 4, toRegionX - 4, toRegionY - 4, ratio); // bottom
            // left
            RegionBuilder.copyAllPlanesMap(topFromX, tomFromY - 4, toRegionX, toRegionY - 4, ratio); // bottom
            // right
        }
    };

    /**
     * Merges the maps
     */
    public abstract void merge();

    /**
     * Merges all the maps
     */
    public static void start() {
        for (MapMerger map : MapMerger.values()) {
            map.merge();
        }
    }
}