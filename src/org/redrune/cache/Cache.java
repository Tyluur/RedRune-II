package org.redrune.cache;

import org.redrune.rs2.GameConstants;

public class Cache {

    public static void init() {
        try {
            CacheManager.load(GameConstants.CACHE_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static int getAmountOfItems() {
        return CacheManager.cacheCFCount(CacheConstants.ITEMDEF_IDX_ID);
    }

    public static int getAmountOfObjects() {
        return CacheManager.cacheCFCount(CacheConstants.OBJECTDEF_IDX_ID);
    }

    public static int getAmountOfInterfaces() {
        return CacheManager.containerCount(CacheConstants.INTERFACEDEF_IDX_ID);
    }

    public static int getAmountOfNpcs() {
        return CacheManager.cacheCFCount2(CacheConstants.NPCDEF_IDX_ID);
    }

    public static int getAmountOfGraphics() {
        return CacheManager.cacheCFCount(CacheConstants.GFX_IDX_ID);
    }

    public static int getAmountOfAnims() {
        return CacheManager.cacheCFCount2(CacheConstants.ANIM_IDX_ID);
    }

}
