package ch.bildspur.floje.tracker

import ch.bildspur.floje.util.distance
import ch.bildspur.floje.util.location
import ch.bildspur.floje.util.sparsePoints
import io.scanse.sweep.SweepSample
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegionTracker {
    val regions = CopyOnWriteArrayList<ActiveRegion>()

    var sparsing = 25.0
    var maxDelta = 20.0

    fun track(components: List<SweepSample>) {
        // sparse and prepare points
        val points = components.map { ActiveRegion(it.location(), it.signalStrength.toDouble(), 1) }
                .toMutableList()
                .sparsePoints(sparsing)
                .map {
                    ActiveRegion(
                            it.map { it.x }.average(),
                            it.map { it.y }.average(),
                            it.map { it.signalStrength }.sum(),
                            it.size
                    )
                }


        // reset all regions
        regions.forEach { it.kill() }

        // create matrix
        matchNearest(points)

        // remove dead regions
        regions.removeAll { it.isDead }

        // create new regions
        regions.addAll(points.filter { !it.used })
    }

    private fun matchNearest(points: List<ActiveRegion>) {
        // create matrix (point to region)
        val distances = Array(points.size, { DoubleArray(regions.size) })

        // fill matrix O((m*n)^2)
        points.forEachIndexed { i, activePoint ->
            regions.forEachIndexed { j, activeRegion ->
                distances[i][j] = activeRegion.distance(activePoint)
            }
        }

        // match best region to point
        points.forEachIndexed { i, activePoint ->
            val minDelta = distances[i].min() ?: Double.MAX_VALUE

            if (minDelta <= maxDelta) {
                val regionIndex = distances[i].indexOf(minDelta)
                regions[regionIndex].setCenter(activePoint)
                regions[regionIndex].isDead = false

                activePoint.used = true
            }
        }
    }
}