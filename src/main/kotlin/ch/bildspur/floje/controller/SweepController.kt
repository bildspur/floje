package ch.bildspur.floje.controller

import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.sweep.SweepDataProvider
import ch.bildspur.floje.sweep.SweepInteraction
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
class SweepController(internal val sketch: PApplet) {
    var sweepPort = ""

    val sweepDataProvider = SweepDataProvider()
    val sweepInteractor = SweepInteraction()

    var innerCone = 0.0
    var outerCone = Double.MAX_VALUE

    var minLifeTime = 0

    var rotation = 0.0

    var minRegionSize = 0

    lateinit var grid: Grid

    internal var running = false

    internal lateinit var sweepThread: Thread

    fun setup(grid: Grid) {
        this.grid = grid
        sweepInteractor.grid = grid

        sweepDataProvider.port = sweepPort

        val path = Paths.get(sweepPort)

        // if sweep is available
        if (!Files.exists(path)) {
            println("Sweep not found ($sweepPort)")
            return
        }

        running = true

        // start interaction
        sweepThread = thread {
            while (running) {
                try {
                    sweepDataProvider.start()
                } catch (ex: Exception) {
                    println("Sweep Error: ${ex.message}")
                }
            }
        }
    }

    fun close() {
        running = false
        sweepDataProvider.stop()
        sweepThread.join()
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
                        && it.lifeTime > minLifeTime
                        && it.size > minRegionSize
            }
        }


    fun analyseSweep() {
        sweepInteractor.interact(relevantRegions)
    }
}