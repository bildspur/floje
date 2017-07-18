package ch.bildspur.floje.controller

import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.sweep.SweepDataProvider
import ch.bildspur.floje.tracker.ActiveRegion
import io.scanse.sweep.SweepSample
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

    lateinit var sweepDataProvider: SweepDataProvider

    lateinit var grid: Grid

    fun setup(grid: Grid) {
        this.grid = grid

        sweepDataProvider = SweepDataProvider(SWEEP_PORT)

        val path = Paths.get(SWEEP_PORT)

        // if sweep is available
        if (!Files.exists(path)) {
            println("Sweep not found ($SWEEP_PORT)")
            return
        }

        // start interaction
        thread {
            sweepDataProvider.start()
        }
    }

    fun close() {
        sweepDataProvider.stop()
    }

    val currentScan: List<SweepSample>
        get() {
            if (sweepDataProvider.running)
                return sweepDataProvider.lastScan

            return emptyList()
        }

    val regions: Array<ActiveRegion>
        get() {
            return sweepDataProvider.tracker.regions.toTypedArray()
        }

    fun analyseSweep() {
        val scan = currentScan
    }
}