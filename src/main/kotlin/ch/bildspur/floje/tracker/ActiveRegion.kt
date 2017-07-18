package ch.bildspur.floje.tracker

import ch.bildspur.floje.util.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegion(x: Double, y: Double, val signalStrength: Double) : Point(x, y) {
    internal var used = false

    var lifeTime = 0
    var isDead = true

    fun setCenter(point: Point) {
        this.x = point.x
        this.y = point.y
    }

    constructor(point: Point, signalStrength: Double) : this(point.x, point.y, signalStrength)
}