package org.redrune.game.content.entity.actor.player.design;

import java.io.Serializable;

/**
 * @author Tyluur <itstyluur@icloud.com>
 * @since 9/13/2017
 */
public class DesignState implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5470858813509958422L;

    InterfaceState state = InterfaceState.MAIN;

    CustomizeCategory customIndex = CustomizeCategory.SKIN;

    int designIndex = -1;

    int secondaryDesignIndex = -1;

    public enum InterfaceState {
        MAIN,
        CUSTOMIZATION
    }

    public enum CustomizeCategory {
        SKIN,
        HAIR,
        TORSO,
        LEGS,
        SHOES,
        FACIAL_HAIR;

        public static CustomizeCategory getCustomIndex(int index) {
            switch (index) {
                case 0:
                    return CustomizeCategory.SKIN;
                case 1:
                    return CustomizeCategory.HAIR;
                case 2:
                    return CustomizeCategory.TORSO;
                case 3:
                    return CustomizeCategory.LEGS;
                case 4:
                    return CustomizeCategory.SHOES;
                case 5:
                    return CustomizeCategory.FACIAL_HAIR;
            }
            return null;
        }
    }
}
