package ch.bildspur.floje.controller

import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.sweep.SweepDataProvider
import ch.bildspur.floje.tracker.ActiveRegion
import ch.bildspur.floje.util.Point
import ch.bildspur.floje.util.distance
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

    var innerCone = 0.0
    var outerCone = Double.MAX_VALUE

    var minLifeTime = 0

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

    val relevantRegions: List<ActiveRegion>
        get() {
            return regions.filter {
                it.distance(Point(0.0, 0.0)) in innerCone..outerCone
                        && it.lifeTime > 1000
            }
        }


    fun analyseSweep() {
        val scan = currentScan
    }
}