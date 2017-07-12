package ch.bildspur.floje.controller

import oscP5.OscP5
import processing.core.PApplet

/**
 * Created by cansik on 09.07.17.
 */
class OscController(internal var sketch: PApplet) {
    companion object {
        @JvmStatic val INCOMING_PORT = 9000
        @JvmStatic val OUTGOING_PORT = 8000
    }

    @Volatile
    var isSetup = false

    lateinit var osc: OscP5

    fun setup() {
        osc = OscP5(sketch, INCOMING_PORT)
        isSetup = true
    }
}