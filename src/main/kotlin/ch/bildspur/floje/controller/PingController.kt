package ch.bildspur.floje.controller

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.util.SimpleNetAddress
import processing.core.PApplet
import java.net.InetAddress
import kotlin.concurrent.thread


/**
 * Created by cansik on 12.07.17.
 */
class PingController(internal var sketch: PApplet) {
    companion object {
        val TIMEOUT = 1000
        val PING_INTERVAL = 25000
    }

    @Volatile private var isPingRunning = false

    fun pingMirrors(grid: Grid) {
        if (isPingRunning) {
            println("ping is already running...")
            return
        }

        isPingRunning = true

        thread {
            grid.forEachMirror { mirror, c, r ->
                ping(mirror)
            }
            isPingRunning = false
        }
    }

    private fun ping(mirror: Mirror) {
        var reachable = false


        try {
            val address = InetAddress.getByName(mirror.address.address()!!)
            reachable = address.isReachable(TIMEOUT)

            if (reachable)
                (mirror.address as SimpleNetAddress).updateAddress(address)
        } catch (e: Exception) {
        }

        mirror.isOnline = reachable
    }
}