package ch.bildspur.floje.controller

import peasy.PeasyCam
import processing.core.PApplet
import processing.core.PGraphics


/**
 * Created by cansik on 08.06.17.
 */
class PeasyController(internal var sketch: PApplet) {
    lateinit var cam: PeasyCam

    fun setup() {
        cam = PeasyCam(sketch, 0.0, 0.0, 0.0, 380.0)
        //cam.rotateX(radians(-20));
        cam.setMinimumDistance(50.0)
        cam.setMaximumDistance(500.0)
    }

    fun applyTo(canvas: PGraphics) {
        cam.state.apply(canvas)
    }

    fun hud(block: () -> Unit) {
        cam.beginHUD()
        block()
        cam.endHUD()
    }
}