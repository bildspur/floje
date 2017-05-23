package ch.bildspur.floje.animation

import ch.bildspur.floje.draw
import processing.core.PGraphics

/**
 * Created by cansik on 13.02.17.
 */
object Animator {
    val animations = mutableListOf<Animation>()

    fun update(g: PGraphics) {
        g.draw {
            it.background(0f)
            animations.filter { !it.finished && it.running }.forEach {
                // update animations
                it.lifeTime++
                it.render(it, g)
            }
        }

        // remove unused animations
        animations.removeAll { it.finished }
    }
}