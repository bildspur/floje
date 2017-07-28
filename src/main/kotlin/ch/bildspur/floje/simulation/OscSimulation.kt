package ch.bildspur.floje.simulation

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.model.Mirror
import oscP5.OscMessage
import processing.core.PApplet

class OscSimulation(val sketch: Sketch) {

    fun simulate(msg: OscMessage) {

        val address = msg.addrPattern()
        if (!address.startsWith("/floje/show"))
            return

        // split address
        val cmd = SimulationCommand(msg)

        // select mirrors or special case(direct select)
        val mirrors = if (cmd.column > 0 && cmd.row > 0)
            listOf(sketch.grid[cmd.column, cmd.row] as Mirror)
        else sketch.config.settings.mirrors.filter {
            (cmd.column == -1 || it.position.column == cmd.column)
                    && (cmd.row == -1 || it.position.row == cmd.row)
        }

        // iterate through every mirror
        mirrors.forEach {
            // select command
            val value = when (cmd.command) {
                "move" -> cmd.value
                "random" -> 0.5f + sketch.random(-cmd.value, cmd.value)
                "sin" -> {
                    val input = 0.25f
                    val speed = cmd.value
                    val sinusSpeed = 1f / (500f * (1f - speed))
                    val pos = 0.5f + PApplet.sin(sketch.millis() * sinusSpeed) * input

                    println(pos)
                    pos
                }
                else -> 0.5f
            }

            // send to servo
            when (cmd.axis) {
                "x" -> it.xAxis.servo.write(value.toServoValue())
                "y" -> it.yAxis.servo.write(value.toServoValue())
            }
        }
    }

    private fun Float.toServoValue(): Int {
        return Math.round(this * 180)
    }
}