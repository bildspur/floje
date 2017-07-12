package ch.bildspur.floje.controller

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import processing.core.PApplet
import java.net.InetAddress
import kotlin.concurrent.thread


/**
 * Created by cansik on 12.07.17.
 */
class PingController(internal var sketch: PApplet) {
    companion object {
        val TIMEOUT = 500
        val PING_INTERVAL = 15000
    }

    fun pingMirrors(grid: Grid) {
        thread {
            grid.columns.forEachIndexed { y, fields ->
                fields.forEachIndexed { x, field ->
                    if (!field.isEmpty())
                        ping(field as Mirror)
                }
            }
        }
    }

    private fun ping(mirror: Mirror) {
        var reachable = false
        try {
            reachable = InetAddress.getByName(mirror.address.address()!!).isReachable(TIMEOUT)
        } catch (e: Exception) {

        }

        mirror.isOnline = reachable
    }
}