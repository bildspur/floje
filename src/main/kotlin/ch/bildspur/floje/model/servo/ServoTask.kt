package ch.bildspur.floje.model.servo

import ch.bildspur.floje.Sketch.Companion.currentMillis
import ch.bildspur.floje.util.Easing.easeInQuad
import ch.bildspur.floje.util.Easing.easeOutQuad
import ch.bildspur.floje.util.Easing.linearTween
import processing.core.PApplet
import processing.core.PApplet.abs


/**
 * Created by cansik on 10.07.17.
 */
class ServoTask(var targetPosition: Int, var velocity: Float, var acceleration: Float) {
    var state: ServoState
    var status = ServoTaskStatus.CREATED

    var startPosition: Int = 0

    var accelerationTarget: Float = 0.toFloat()
    var linearMotionTarget: Float = 0.toFloat()
    var decelerationTarget: Float = 0.toFloat()

    var duration: Float = 0.toFloat()
    var startTime: Int = 0

    var accelerationTime: Float = 0.toFloat()
    var linearMotionTime: Float = 0.toFloat()
    var decelerationTime: Float = 0.toFloat()

    var accelerationPath: Float = 0.toFloat()
    var linearMotionPath: Float = 0.toFloat()
    var decelerationPath: Float = 0.toFloat()

    var direction: Int = 0

    init {

        this.state = ServoState.ACCELERATION
    }

    fun start(startPosition: Int) {
        status = ServoTaskStatus.RUNNING

        this.startPosition = startPosition
        this.startTime = currentMillis()

        // caluclate time and path
        accelerationTime = calculateMotionTime(0f, velocity, acceleration)
        decelerationTime = calculateMotionTime(velocity, 0f, acceleration)

        accelerationPath = calculateMotionPath(0f, accelerationTime, acceleration)
        decelerationPath = calculateMotionPath(velocity, decelerationTime, acceleration * -1)

        duration = 0f

        // calculate positions
        val pathLength = abs(targetPosition - startPosition)
        direction = getSign(targetPosition - startPosition)

        linearMotionPath = PApplet.max(0f, pathLength - (accelerationPath + decelerationPath))
        linearMotionTime = linearMotionPath / velocity

        // recalculate accelerationTime if no linear motion possible
        if (linearMotionPath == 0f) {
            accelerationPath = (pathLength / 2).toFloat()
            decelerationPath = (pathLength / 2).toFloat()

            accelerationTime = calculateMotionTime(acceleration, accelerationPath)
            decelerationTime = calculateMotionTime(acceleration, decelerationPath)
        }

        duration = accelerationTime + linearMotionTime + decelerationTime

        accelerationTarget = startPosition + direction * accelerationPath
        linearMotionTarget = accelerationTarget + direction * linearMotionPath
        decelerationTarget = linearMotionTarget + direction * decelerationPath
    }

    fun stop() {
        state = ServoState.DECELERATION
    }

    fun nextPosition(currentPosition: Int): Int {
        // fix for 0 or 1 motion
        if (abs(targetPosition - startPosition) < 2) {
            status = ServoTaskStatus.FINISHED
            return targetPosition
        }

        when (state) {
            ServoState.ACCELERATION -> return acceleration()

            ServoState.LINEARMOTION -> return linearMotion()

            ServoState.DECELERATION -> return deceleration()
        }
    }

    // t = current time, b = start value, c = change in value, d = duration

    fun acceleration(): Int {
        val t = (currentMillis() - startTime).toFloat()
        val b = startPosition.toFloat()
        val c = accelerationTarget - startPosition
        val d = accelerationTime

        // check state switch
        if (t >= d) {
            state = ServoState.LINEARMOTION
        }

        return Math.round(easeInQuad(t, b, c, d))
    }

    fun linearMotion(): Int {
        val t = currentMillis() - (startTime + accelerationTime)
        val b = accelerationTarget
        val c = linearMotionTarget - accelerationTarget
        val d = linearMotionTime

        // check state switch
        if (t >= d) {
            state = ServoState.DECELERATION
        }

        if (d <= 0) {
            // skip linear motion
            return deceleration()
        }

        return Math.round(linearTween(t, b, c, d))
    }

    fun deceleration(): Int {
        val t = currentMillis() - (startTime.toFloat() + accelerationTime + linearMotionTime)
        val b = linearMotionTarget
        val c = decelerationTarget - linearMotionTarget
        val d = decelerationTime

        // check state switch
        if (t >= d) {
            status = ServoTaskStatus.FINISHED
        }

        return Math.round(easeOutQuad(t, b, c, d))
    }

    private fun getSign(n: Int): Int {
        return if (n >= 0) 1 else -1
    }

    private fun calculateMotionTime(vI: Float, vF: Float, a: Float): Float {
        return Math.abs((vF - vI) / a)
    }

    private fun calculateMotionPath(vI: Float, t: Float, a: Float): Float {
        return Math.round(vI * t + 0.5 * a.toDouble() * Math.pow(t.toDouble(), 2.0)).toFloat()
    }

    private fun calculateMotionTime(a: Float, d: Float): Float {
        return Math.round(Math.sqrt((2 * d / a).toDouble())).toFloat()
    }
}