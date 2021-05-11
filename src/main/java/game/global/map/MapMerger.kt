package game.global.map

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since March 20, 2021
 */
enum class MapMerger {
    DUEL_ARENA_TO_EDGEVILLE {
        override fun merge() {
            val fromX = 420
            val fromY = 408

            val toX = 383
            val toY = 436

            val ratio = 4

           // RegionBuilder.copyAllPlanesMap(fromX, fromY, toX, toY, ratio);
//            RegionBuilder.copyAllPlanesMap(fromX, fromY, toX, toY, ratio);

//            RegionBuilder.copyAllPlanesMap(fromX, fromY, toX, toY, ratio) // top-left
        }
    };

    /**
     * Merges the maps
     */
    abstract fun merge()

    companion object {
        /**
         * Merges all the maps
         */
        fun start() {
            for (map in values()) {
                map.merge()
            }
        }
    }
}