package ch.bildspur.floje.data

import com.google.gson.annotations.Expose

class Interaction {
    @Expose var servoLimit: Double = 0.toDouble()
    @Expose var viewAngle: Double = 0.toDouble()
}