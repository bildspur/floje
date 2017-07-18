package ch.bildspur.floje.controller

import ch.bildspur.floje.interaction.SweepInteraction
import ch.bildspur.floje.model.grid.Grid
import processing.core.PApplet
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.concurrent.thread


/**
 * Created by cansik on 18.07.17.
 */
class SweepController(internal var sketch: PApplet) {
    companion object {
        @JvmStatic val SWEEP_PORT = "/dev/tty.usbserial-DO004HM4"
    }

    lateinit var sweepInteraction: SweepInteraction

    fun setup(grid: Grid) {
        sweepInteraction = SweepInteraction(SWEEP_PORT, grid)

        val path = Paths.get(SWEEP_PORT)

        // if sweep is available
        if (!Files.exists(path)) {
            println("Sweep not found ($SWEEP_PORT)")
            return
        }

        // start interaction
        thread {
            sweepInteraction.start()
        }
    }

    fun close() {
        sweepInteraction.stop()
    }
}