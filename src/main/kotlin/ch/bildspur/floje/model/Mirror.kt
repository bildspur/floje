package ch.bildspur.floje.model

import ch.bildspur.floje.controller.OscController
import ch.bildspur.floje.data.Position
import ch.bildspur.floje.data.Trim
import ch.bildspur.floje.model.grid.GridField
import ch.bildspur.floje.servo.ServoDriver
import ch.bildspur.floje.servo.smooth.SmoothServo
import ch.bildspur.floje.util.SimpleNetAddress
import com.google.gson.annotations.Expose
import netP5.NetAddress

/**
 * Created by cansik on 08.06.17.
 */
class Mirror() : GridField() {
    var xAxis: ServoDriver = SmoothServo(Servo())
    var yAxis: ServoDriver = SmoothServo(Servo())

    @Expose var position: Position = Position()
    @Expose var name: String = ""
    @Expose var trim: Trim = Trim()

    lateinit var address: NetAddress

    @Volatile var isOnline = false

    fun setup() {
        val addr = SimpleNetAddress("$name.local", 0)
        addr.updatePort(OscController.OUTGOING_PORT)

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