package ch.bildspur.floje.view

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.util.limit
import oscP5.OscMessage
import oscP5.OscP5

/**
 * Created by cansik on 10.07.17.
 */
class OscOutput(val osc: OscP5, val grid: Grid) {
    companion object {
        @JvmStatic val SERVO_ADDRESS = "/floje/servo/xy"
        @JvmStatic val OUTPUT_INTERVAL = 20
    }

    val modifiedMirrors = hashMapOf<Mirror, Boolean>()

    fun registerMirror(mirror: Mirror) {
        // register events
        mirror.xAxis.servo.position.onChanged += {
            modifiedMirrors[mirror] = true
        }

        mirror.yAxis.servo.position.onChanged += {
            modifiedMirrors[mirror] = true
        }

        modifiedMirrors.put(mirror, true)
    }

    fun updateMirrors() {
        modifiedMirrors.forEach { mirror, modified ->
            // updated if modified
            if (modified)
                updateMirror(mirror)

            // reset modified flag
            modifiedMirrors[mirror] = false
        }
    }

    private fun updateMirror(mirror: Mirror) {
        if (!mirror.isOnline)
            return

        val x = mirror.xAxis.servo.position.value + mirror.trim.x
        val y = mirror.yAxis.servo.position.value + mirror.trim.y

        // send the output
        val msg = OscMessage(SERVO_ADDRESS)
        msg.add(x.toFloat().limit(0f, 180f))
        msg.add(y.toFloat().limit(0f, 180f))

        osc.send(msg, mirror.address)
    }
}