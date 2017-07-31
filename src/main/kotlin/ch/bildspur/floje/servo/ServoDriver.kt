package ch.bildspur.floje.servo

import ch.bildspur.floje.model.Servo

abstract class ServoDriver(val servo: Servo) {
    var maxVelocity = 0f
    var maxAcceleration = 0f

    init {
        // init servo to have same position
        servo.write(90)
    }

    @JvmOverloads fun moveTo(targetPosition: Int, velocity: Float = maxVelocity) {
        moveTo(targetPosition, maxVelocity, maxAcceleration)
    }

    abstract fun moveTo(targetPosition: Int, velocity: Float, acceleration: Float)

    abstract fun update()

    abstract fun stop()
}