package ch.bildspur.floje.model

import ch.bildspur.floje.controller.OscController
import ch.bildspur.floje.data.Position
import ch.bildspur.floje.data.Trim
import ch.bildspur.floje.model.grid.GridField
import ch.bildspur.floje.model.servo.Servo
import ch.bildspur.floje.model.servo.SmoothServo
import ch.bildspur.floje.util.SimpleNetAddress
import netP5.NetAddress

/**
 * Created by cansik on 08.06.17.
 */
class Mirror() : GridField() {
    var xAxis = SmoothServo(Servo(), 60.0f / 180.0f, 0.04f)
    var yAxis = SmoothServo(Servo(), 60.0f / 180.0f, 0.04f)

    var position: Position = Position()
    var name: String = ""
    var trim: Trim = Trim()

    lateinit var address: NetAddress

    @Volatile var isOnline = false

    fun setup() {
        val addr = SimpleNetAddress("$name.local", OscController.OUTGOING_PORT)
        //addr.updatePort(OscController.OUTGOING_PORT)

        address = addr
    }

    override fun equals(other: Any?): Boolean {
        if (other is Mirror)
            return other.name == name

        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}