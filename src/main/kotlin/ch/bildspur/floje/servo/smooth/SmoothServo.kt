package ch.bildspur.floje.servo.smooth

import ch.bildspur.floje.Sketch.Companion.currentMillis
import ch.bildspur.floje.model.Servo
import ch.bildspur.floje.servo.ServoDriver
import ch.bildspur.floje.util.Queue


/**
 * Created by cansik on 10.07.17.
 */
class SmoothServo(servo: Servo) : ServoDriver(servo) {
    private var tasks = Queue<ServoTask>(30)
    private var task: ServoTask? = null

    private var servoPosition: Int = 0

    var start: Int = 0

    init {
        // init servo to have same position
        servoPosition = 90
        servo.write(servoPosition)
    }

    override fun moveTo(targetPosition: Int, velocity: Float, acceleration: Float) {
        // check if target is already enqueued
        if (!tasks.empty) {
            if (tasks.next().targetPosition == targetPosition)
                return
        }

        tasks.enqueue(ServoTask(targetPosition, velocity * maxVelocity, acceleration * maxAcceleration))
    }

    override fun stop() {
        if (task == null)
            return

        task!!.stop()
    }

    override fun update() {
        //  check if we need a new task from the queue
        if (task == null
                || task!!.status === ServoTaskStatus.FINISHED
                || task!!.status === ServoTaskStatus.CANCELED) {
            if (tasks.empty)
                return

            // get new task from queue
            task = tasks.dequeue()
            task!!.start(servoPosition)

            start = currentMillis()
        }

        // update task
        servoPosition = task!!.nextPosition(servoPosition)
        servo.write(servoPosition)

        // check if task is finished
        if (task!!.status === ServoTaskStatus.FINISHED || task!!.status === ServoTaskStatus.CANCELED) {
            task = null
        }
    }
}
