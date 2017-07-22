package ch.bildspur.floje.controller

import ch.bildspur.floje.Sketch
import oscP5.OscMessage
import oscP5.OscP5
import processing.core.PApplet

/**
 * Created by cansik on 09.07.17.
 */
class OscController(internal var sketch: Sketch) {
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

    fun processMessage(msg: OscMessage) {
        if (msg.addrPattern().contains("/floje/status")) {
            PApplet.print("Startup: ")
            PApplet.print(msg.addrPattern())

            var i = 0
            val version = msg.arguments()[i++] as String
            val reason = msg.arguments()[i++] as Int
            val excause = msg.arguments()[i++] as Int
            val epc1 = msg.arguments()[i++] as Int
            val epc2 = msg.arguments()[i++] as Int
            val epc3 = msg.arguments()[i++] as Int
            val excvaddr = msg.arguments()[i++] as Int
            val depc = msg.arguments()[i++] as Int

            val restartReason = if (reason == 0) "DEFAULT"
            else if (reason == 1) "WDT"
            else if (reason == 2) "EXCEPTION"
            else if (reason == 3) "SOFT_WDT"
            else if (reason == 4) "SOFT_RESTART"
            else if (reason == 5) "DEEP_SLEEP_AWAKE"
            else if (reason == 6) "EXT_SYS_RST" else "???"

            PApplet.print("\tVersion: $version")
            PApplet.print("\tReason: ")
            PApplet.println(restartReason)

            if (reason == 2) {
                PApplet.print("EXCEPTION:")
                PApplet.print("\tExcuse: " + excause)
                PApplet.print("\tEPC1: " + epc1)
                PApplet.print("\tEPC2: " + epc2)
                PApplet.print("\tEPC3: " + epc3)
                PApplet.print("\tEXCVADDR: " + excvaddr)
                PApplet.println("\tDEPC: " + depc)
            }
        }
    }
}