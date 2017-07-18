package ch.bildspur.floje.util

import io.scanse.sweep.SweepSample
import processing.core.PApplet

/**
 * Created by cansik on 18.07.17.
 */
fun SweepSample.projectedAngle(): Float {
    return this.angle.toFloat() / -1000f
}

fun SweepSample.location(): Point {
    val x = distance * Math.cos(PApplet.radians(projectedAngle()).toDouble())
    val y = distance * Math.sin(PApplet.radians(projectedAngle()).toDouble())

    return Point(x, y)
}
