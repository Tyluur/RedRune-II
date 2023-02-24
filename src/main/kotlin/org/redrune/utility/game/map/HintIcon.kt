package org.redrune.utility.game.map

class HintIcon {

    @JvmField
    var coordX = 0

    @JvmField
    var coordY = 0
    var plane = 0

    @JvmField
    var distanceFromFloor = 0

    @JvmField
    var targetType = 0

    @JvmField
    var targetIndex = 0

    @JvmField
    var arrowType = 0

    @JvmField
    var modelId = 0

    @JvmField
    var index = 0

    constructor() {
        index = 7
    }

    constructor(targetType: Int, modelId: Int, index: Int) {
        this.targetType = targetType
        this.modelId = modelId
        this.index = index
    }

    constructor(targetIndex: Int, targetType: Int, arrowType: Int, modelId: Int, index: Int) {
        this.targetType = targetType
        this.targetIndex = targetIndex
        this.arrowType = arrowType
        this.modelId = modelId
        this.index = index
    }

    constructor(
        coordX: Int,
        coordY: Int,
        height: Int,
        distanceFromFloor: Int,
        targetType: Int,
        arrowType: Int,
        modelId: Int,
        index: Int,
    ) {
        this.coordX = coordX
        this.coordY = coordY
        plane = height
        this.distanceFromFloor = distanceFromFloor
        this.targetType = targetType
        this.arrowType = arrowType
        this.modelId = modelId
        this.index = index
    }
}