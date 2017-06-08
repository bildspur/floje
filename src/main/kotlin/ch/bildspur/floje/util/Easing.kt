package ch.bildspur.floje.util

/**
 * Created by cansik on 08.06.17.
 */
object Easing {

    /**
     * No easing, no acceleration.
     */
    fun linear(t: Float): Float {
        return t
    }

    /**
     * Accelerating from zero velocity.
     */
    fun easeInQuad(t: Float): Float {
        return t * t
    }

    /**
     * Decelerating to zero velocity.
     */
    fun easeOutQuad(t: Float): Float {
        return t * (2 - t)
    }

    /**
     * Acceleration until halfway, then deceleration.
     */
    fun easeInOutQuad(t: Float): Float {
        return if (t < .5) 2 * t * t else -1 + (4 - 2 * t) * t
    }

    /**
     * Accelerating from zero velocity.
     */
    fun easeInCubic(t: Float): Float {
        return t*t*t
    }

    /**
     * Decelerating to zero velocity.
     */
    fun easeOutCubic(t: Float): Float {
        return (t-1)*(t-1)*(t-1)+1
    }

    /**
     * Acceleration until halfway, then deceleration.
     */
    fun easeInOutCubic(t: Float): Float {
        return if(t<.5) 4*t*t*t else (t-1)*(2*t-2)*(2*t-2)+1
    }

    /**
     * Accelerating from zero velocity.
     */
    fun easeInQuart(t: Float): Float {
        return t*t*t*t
    }

    /**
     * Decelerating to zero velocity.
     */
    fun easeOutQuart(t: Float): Float {
        return 1-(t-1)*(t-1)*(t-1)*(t-1)
    }

    /**
     * Acceleration until halfway, then deceleration.
     */
    fun easeInOutQuart(t: Float): Float {
        return if(t<.5) 8*t*t*t*t else 1-8*(t-1)*(t-1)*(t-1)*(t-1)
    }

    /**
     * Accelerating from zero velocity.
     */
    fun easeInQuint(t: Float): Float {
        return t*t*t*t*t
    }

    /**
     * Aecelerating to zero velocity.
     */
    fun easeOutQuint(t: Float): Float {
        return 1+(t-1)*(t-1)*(t-1)*(t-1)*(t-1)
    }

    /**
     * Acceleration until halfway, then deceleration.
     */
    fun easeInOutQuint(t: Float): Float {
        return if(t<.5) 16*t*t*t*t*t else 1+16*(t-1)*(t-1)*(t-1)*(t-1)*(t-1)
    }
}