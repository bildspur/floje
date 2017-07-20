package ch.bildspur.floje.tracker

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.util.Point

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegion(x: Double, y: Double, val signalStrength: Double, val size: Int) : Point(x, y) {
    internal var used = false

    val creationTime = Sketch.currentMillis()
    var deathTime = 0

    var isDead = true

    var lifeTime: Int = 0
        get() {
            return Sketch.currentMillis() - creationTime
        }

    internal fun setCenter(point: Point) {
        this.x = point.x
        this.y = point.y
    }

    internal fun kill() {
        isDead = true
        deathTime = Sketch.currentMillis()
    }

    constructor(point: Point, signalStrength: Double, size: Int) : this(point.x, point.y, signalStrength, size)
}