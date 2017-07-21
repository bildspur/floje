package ch.bildspur.floje.sweep

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.tracker.ActiveRegion
import ch.bildspur.floje.util.toPolar
import processing.core.PApplet
import processing.core.PVector

class SweepInteraction {

    var regionHeight = 100f

    var mirrorRadius = 37f
    var mirrorSize = 50f
    var mirrorStartHeight = 40f

    var viewAngle = 30f

    lateinit var grid: Grid

    fun interact(regions: List<ActiveRegion>) {

        // loop through mirrors
        grid.columns.forEachIndexed { c, fields ->
            fields.forEachIndexed { r, field ->
                if (!field.isEmpty()) {
                    rotateMirror(field as Mirror, c, r, regions)
                }
            }
        }
    }

    private fun rotateMirror(mirror: Mirror, c: Int, r: Int, regions: List<ActiveRegion>) {
        val angle = (360f / grid.width) * c
        val summedRotation = PVector(0f, 0f)

        var relevantRegions = 0

        regions.forEach {
            // calculate 3d position of regions
            val regionPosition = PVector(it.x.toFloat(), it.y.toFloat(), regionHeight)

            // calculate polar coordinates
            val polar = regionPosition.toPolar()

            // check if region is relevant
            val angleDiff = (angle - polar.theta + 180 + 360) % 360 - 180
            if (angleDiff !in -viewAngle..viewAngle) {
                return
            }

            // calculate angle difference
            relevantRegions++
            summedRotation.x += -angleDiff
        }

        val x = 90f + Math.round(summedRotation.x / relevantRegions)
        //val y = Math.abs(Math.round(summedRotation.y / regions.size.toFloat()) + 90f)

        // move mirror
        mirror.xAxis.moveTo(x.toInt())//limit(x, 60f, 120f).toInt())
        //mirror.yAxis.moveTo(limit(y, 60f, 120f).toInt())
    }

    private fun mirrorPosition(c: Int, r: Int): PVector {
        val angle = (360f / grid.width) * c
        val distance = mirrorRadius
        val height = mirrorSize * r + mirrorStartHeight

        val x = distance * Math.cos(PApplet.radians(angle).toDouble())
        val y = distance * Math.sin(PApplet.radians(angle).toDouble())

        return PVector(x.toFloat(), y.toFloat(), height)
    }
}