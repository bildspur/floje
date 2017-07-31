package ch.bildspur.floje.servo.easing

import ch.bildspur.floje.model.Servo
import ch.bildspur.floje.servo.ServoDriver
import processing.core.PVector


class EasingServo(servo: Servo) : ServoDriver(servo) {
    var position = PVector(90f, 90f)
    var velocity = PVector()
    var acceleration = PVector()
    var target = PVector(90f, 0f)

    private fun applyForce(force: PVector) {
        acceleration.add(force)
    }

    private fun seek(): PVector {
        val desired = PVector.sub(target, position)

        desired.normalize()
        desired.mult(maxVelocity)

        val steer = PVector.sub(desired, velocity)
        steer.limit(maxVelocity)

        applyForce(steer)
        return steer
    }

    override fun update() {
        applyForce(seek())

        velocity.add(acceleration)

        velocity.limit(maxVelocity)
        position.add(velocity)

        acceleration.mult(0f)

        servo.write(Math.round(position.x))
    }

    override fun moveTo(targetPosition: Int, speed: Float, acceleration: Float) {
        target = PVector(targetPosition.toFloat(), 0f)
    }

    override fun stop() {
        velocity.x = 0f
    }
}