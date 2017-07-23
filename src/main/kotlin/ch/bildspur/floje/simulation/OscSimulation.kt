package ch.bildspur.floje.simulation

import ch.bildspur.floje.Sketch
import oscP5.OscMessage

class OscSimulation(val sketch: Sketch) {

    fun simulate(msg: OscMessage) {

        when (msg.addrPattern()) {
            "/floje/simulation/x" -> {
                val value = Math.round(msg[0].floatValue() * 180)

                sketch.grid.forEachMirror { mirror, c, r ->
                    mirror.xAxis.servo.write(value)
                }
            }
        }
    }
}