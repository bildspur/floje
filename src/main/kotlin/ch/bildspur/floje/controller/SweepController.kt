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
    var sweepPort = ""

    val sweepDataProvider = SweepDataProvider()

    lateinit var grid: Grid

    fun setup(grid: Grid) {
        this.grid = grid

        sweepDataProvider.port = sweepPort

        val path = Paths.get(sweepPort)

        // if sweep is available
        if (!Files.exists(path)) {
            println("Sweep not found ($sweepPort)")
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

    val regions: List<ActiveRegion>
        get() {
            return sweepDataProvider.tracker.regions
        }

    fun analyseSweep() {
        val scan = currentScan
    }
}