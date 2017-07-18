package ch.bildspur.floje.controller

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import processing.core.PApplet
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.concurrent.thread


/**
 * Created by cansik on 12.07.17.
 */
class PingController(internal var sketch: PApplet) {
    companion object {
        val TIMEOUT = 500
        val PING_INTERVAL = 15000
        val TCP_PING_PORT = 80
    }

    @Volatile private var isPingRunning = false

    private val bytes = ByteArray(128)

    fun pingMirrors(grid: Grid) {
        if (isPingRunning) {
            println("ping is already running...")
            return
        }

        isPingRunning = true

        thread {
            println("new ping started!")
            grid.columns.forEachIndexed { y, fields ->
                fields.forEachIndexed { x, field ->
                    if (!field.isEmpty())
                        ping(field as Mirror)
                }
            }
            isPingRunning = false
        }
    }

    private fun ping(mirror: Mirror) {
        print("trying to ping ${mirror.name}...")

        var reachable = false

        /*
        try {
            reachable = InetAddress.getByName(mirror.address.address()!!).isReachable(TIMEOUT)
        } catch (e: Exception) {

        }
        */

        try {
            val socket = Socket()
            socket.connect(InetSocketAddress(mirror.address.address(), TCP_PING_PORT), 500)
            reachable = socket.isConnected
            socket.close()
        } catch (e: Exception) {
        }

        mirror.isOnline = reachable
    }
}