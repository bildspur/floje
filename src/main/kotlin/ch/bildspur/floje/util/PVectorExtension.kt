package ch.bildspur.floje.util

import processing.core.PApplet
import processing.core.PVector


fun PVector.rotationInX(v: PVector): Float {
    val delta = PVector.sub(this, v)
    return PApplet.degrees(PApplet.atan(delta.x / delta.z))
}

fun PVector.rotationInY(v: PVector): Float {
    val delta = PVector.sub(this, v)
    return PApplet.degrees(PApplet.atan(delta.z / delta.y))
}

fun PVector.rotationInZ(v: PVector): Float {
    val delta = PVector.sub(this, v)
    return PApplet.degrees(PApplet.atan(delta.y / delta.x))
}

fun PVector.rotationInAxis(v: PVector): PVector {
    return PVector(this.rotationInX(v), this.rotationInY(v), this.rotationInZ(v))
}

fun PVector.add(s: Float) {
    this.x += s
    this.y += s
    this.z += s
}