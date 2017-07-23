package ch.bildspur.floje.simulation

import oscP5.OscMessage

class SimulationCommand(val msg: OscMessage) {
    val value: Float

    val column: Int
    val row: Int
    val command: String
    val axis: String

    init {
        val tokens = msg.addrPattern().split("/")
        value = msg[0].floatValue()

        var i = 3

        column = tokens[i++].toInt()
        row = tokens[i++].toInt()
        command = tokens[i++]
        axis = tokens[i]
    }
}