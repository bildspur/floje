package ch.bildspur.floje.model.servo

import ch.bildspur.floje.Sketch.Companion.currentMillis
import ch.bildspur.floje.util.Queue


/**
 * Created by cansik on 10.07.17.
 */
class SmoothServo(val servo: Servo, // max speed per seconds
                  var maxVelocity: Float, var maxAcceleration: Float) {
    var tasks = Queue<ServoTask>(30)
    var task: ServoTask? = null

    var servoPosition: Int = 0

    init {
        // init servo to have same position
        servoPosition = 90
        servo.write(servoPosition)
    }

    @JvmOverloads fun moveTo(targetPosition: Int, velocity: Float = maxVelocity) {
        moveTo(targetPosition, maxVelocity, maxAcceleration)
    }

    fun moveTo(targetPosition: Int, velocity: Float, acceleration: Float) {
        tasks.enqueue(ServoTask(targetPosition, velocity * maxVelocity, acceleration * maxAcceleration))
    }

    fun stop() {}

    var start: Int = 0

    fun update() {
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
