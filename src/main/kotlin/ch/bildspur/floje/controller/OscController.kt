package ch.bildspur.floje.controller

import oscP5.OscP5
import oscP5.OscPacket
import processing.core.PApplet

/**
 * Created by cansik on 09.07.17.
 */
class OscController(internal var sketch: PApplet) {
    companion object {
        val OUTGOING_PORT = 9000
    }

    @Volatile
    var isSetup = false

    lateinit var osc: OscP5

    fun setup() {
        osc = OscP5(sketch, OUTGOING_PORT)
        isSetup = true
    }

    fun send(packet: OscPacket) {
        osc.send(packet)
    }
}