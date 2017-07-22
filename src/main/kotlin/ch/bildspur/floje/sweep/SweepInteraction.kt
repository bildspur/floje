package ch.bildspur.floje.sweep

import ch.bildspur.floje.controller.SweepController
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.tracker.ActiveRegion
import ch.bildspur.floje.util.limit
import ch.bildspur.floje.util.toPolar
import processing.core.PApplet
import processing.core.PVector

class SweepInteraction(val sweepController: SweepController) {

    var regionHeight = 100f

    var mirrorRadius = 37f
    var mirrorSize = 50f
    var mirrorStartHeight = 40f

    var viewAngle = 30f

    var servoLimit = 30f

    lateinit var grid: Grid

    fun interact(regions: List<ActiveRegion>) {

        // loop through mirrors
        grid.forEachMirror { mirror, c, r ->
            rotateMirror(mirror, c, r, regions)
        }
    }

    private fun rotateMirror(mirror: Mirror, c: Int, r: Int, regions: List<ActiveRegion>) {
        val angle = (360f / grid.width) * c
        val mirrorPosition = mirrorPosition(c, r)

        val summedRotation = PVector(0f, 0f)

        var relevantRegions = 0

        regions.forEach {
            // calculate 3d position of regions
            val regionPosition = PVector(it.x.toFloat(), it.y.toFloat(), regionHeight)

            // calculate polar coordinates
            val polar = regionPosition.toPolar()
            val theta = (polar.theta + sweepController.rotation) % 360

            // check if region is relevant
            val angleDiff = (angle - theta + 180 + 360) % 360 - 180
            if (angleDiff in -viewAngle..viewAngle) {

                // calculate angle difference
                summedRotation.x += -angleDiff.toFloat()

                // height difference
                val mirrorHeight = mirrorPosition.z
                val heightDiff = mirrorHeight - regionHeight
                val beta = Math.atan(heightDiff / (polar.r - mirrorRadius.toDouble()))

                summedRotation.y += -PApplet.degrees(beta.toFloat())

                // increment region counter
                relevantRegions++
            }
        }

        val x = 90f + Math.round(summedRotation.x / relevantRegions)
        val y = 90f + Math.round(summedRotation.y / relevantRegions)

        // move mirror
        mirror.xAxis.moveTo(x.limit(90f - servoLimit, 90f + servoLimit).toInt())
        mirror.yAxis.moveTo(y.limit(90f - servoLimit, 90f + servoLimit).toInt())
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