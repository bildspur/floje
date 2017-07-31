package ch.bildspur.floje.servo.easing

import ch.bildspur.floje.model.Servo
import processing.core.PVector


class EasingServo(val xServo: Servo, val yServo: Servo) {
    var position = PVector(90f, 90f)
    var velocity = PVector()
    var acceleration = PVector()

    var maxForce: Float = 0.toFloat()
    var maxSpeed: Float = 0.toFloat()

    fun applyForce(force: PVector) {
        acceleration.add(force)
    }

    fun update() {
        velocity.add(acceleration)

        velocity.limit(maxSpeed)
        position.add(velocity)

        acceleration.mult(0f)
    }

    fun seek(target: PVector): PVector {
        val desired = PVector.sub(target, position)

        desired.normalize()
        desired.mult(maxSpeed)

        val steer = PVector.sub(desired, velocity)
        steer.limit(maxForce)
        return steer
    }
}