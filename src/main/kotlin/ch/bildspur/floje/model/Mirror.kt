package ch.bildspur.floje.model

import ch.bildspur.floje.model.grid.GridField
import ch.bildspur.floje.model.servo.Servo
import ch.bildspur.floje.model.servo.SmoothServo

/**
 * Created by cansik on 08.06.17.
 */
class Mirror(var name: String) : GridField() {
    var xAxis = SmoothServo(Servo(), 60.0f / 180.0f, 0.04f)
    var yAxis = SmoothServo(Servo(), 60.0f / 180.0f, 0.04f)

}