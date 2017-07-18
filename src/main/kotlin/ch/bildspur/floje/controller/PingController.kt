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
        val TIMEOUT = 5000
        val PING_INTERVAL = 30000
    }

    @Volatile private var isPingRunning = false

    fun pingMirrors(grid: Grid) {
        if (isPingRunning) {
            println("ping is already running...")
            return
        }

        isPingRunning = true

        thread {
            grid.columns.forEachIndexed { y, fields ->
                fields.forEachIndexed { x, field ->
                    thread {
                        if (!field.isEmpty())
                            ping(field as Mirror)
                    }
                }
            }
            isPingRunning = false
        }
    }

    private fun ping(mirror: Mirror) {
        var reachable = false


        try {
            val address = InetAddress.getByName(mirror.address.address()!!)
            reachable = address.isReachable(TIMEOUT)
        } catch (e: Exception) {
        }

        mirror.isOnline = reachable
    }
}