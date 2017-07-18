package ch.bildspur.floje.tracker

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.util.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegion(x: Double, y: Double, val signalStrength: Double) : Point(x, y) {
    internal var used = false

    var creationTime = Sketch.currentMillis()
    var deathTime = 0

    var isDead = true

    var lifeTime: Int = 0
        get() {
            if (isDead)
                return deathTime - creationTime
            else
                return Sketch.currentMillis() - creationTime
        }

    fun setCenter(point: Point) {
        this.x = point.x
        this.y = point.y
    }

    constructor(point: Point, signalStrength: Double) : this(point.x, point.y, signalStrength)
}