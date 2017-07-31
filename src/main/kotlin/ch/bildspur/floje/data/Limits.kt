package ch.bildspur.floje.data

import com.google.gson.annotations.Expose

/**
 * Created by cansik on 11.07.17.
 */
class Limits {
    @Expose var maxAcceleration: Double = 0.toDouble()
    @Expose var maxVelocity: Double = 0.toDouble()
}