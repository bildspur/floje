package ch.bildspur.floje.simulation

import ch.bildspur.floje.Sketch
import oscP5.OscMessage

class OscSimulation(val sketch: Sketch) {

    fun simulate(msg: OscMessage) {

        val address = msg.addrPattern()
        if (!address.startsWith("/floje/simulation"))
            return

        // split address
        val cmd = SimulationCommand(msg)

        // fix address
        when (msg.addrPattern()) {
            "/floje/simulation/all/x" -> {
                sketch.grid.forEachMirror { mirror, c, r ->
                    mirror.xAxis.servo.write(msg.servoValue())
                }
            }
            "/floje/simulation/all/y" -> {
                sketch.grid.forEachMirror { mirror, c, r ->
                    mirror.yAxis.servo.write(msg.servoValue())
                }
            }
            "/floje/simulation/random/x" -> {
                val limit = msg.floatValue() * 90f

                sketch.grid.forEachMirror { mirror, c, r ->
                    val value = Math.round(sketch.random(-limit, limit))
                    mirror.xAxis.servo.write(90 + value)
                }
            }
            "/floje/simulation/random/y" -> {
                val limit = msg.floatValue() * 90f

                sketch.grid.forEachMirror { mirror, c, r ->
                    val value = Math.round(sketch.random(-limit, limit))
                    mirror.yAxis.servo.write(90 + value)
                }
            }
        }

        // variable address (wildcards)
        if (address.startsWith("/floje/simulation/")) {

        }
    }

    private fun OscMessage.servoValue(): Int {
        return Math.round(this[0].floatValue() * 180)
    }

    private fun OscMessage.floatValue(): Float {
        return this[0].floatValue()
    }
}