package ch.bildspur.floje.tracker

import ch.bildspur.floje.util.distance
import ch.bildspur.floje.util.location
import ch.bildspur.floje.util.sparsePoints
import io.scanse.sweep.SweepSample

/**
 * Created by cansik on 12.02.17.
 */
class ActiveRegionTracker {
    val regions = mutableListOf<ActiveRegion>()

    var sparsing = 0.0
    var maxDelta = 100.0

    fun track(components: List<SweepSample>) {
        // sparse and prepare points
        val points = components.map { ActiveRegion(it.location(), it.signalStrength.toDouble()) }
                .toMutableList()
                .sparsePoints(sparsing)
                .map {
                    ActiveRegion(
                            it.map { it.x }.average(),
                            it.map { it.y }.average(),
                            it.map { it.signalStrength }.sum())
                }

        // reset all regions
        regions.forEach { it.isDead = true }

        // create matrix
        matchNearest(points)

        // remove dead regions
        regions.removeAll { it.isDead }

        // update regions
        regions.forEach { it.lifeTime++ }

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