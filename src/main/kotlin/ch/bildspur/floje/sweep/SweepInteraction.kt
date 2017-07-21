package ch.bildspur.floje.sweep

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.tracker.ActiveRegion
import ch.bildspur.floje.util.rotationInAxis
import processing.core.PApplet
import processing.core.PVector

class SweepInteraction {

    var regionHeight = 100f

    var mirrorRadius = 37f
    var mirrorSize = 50f
    var mirrorStartHeight = 40f

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

        println("-------")
    }

    private fun rotateMirror(mirror: Mirror, c: Int, r: Int, regions: List<ActiveRegion>) {
        val angle = (360f / grid.width) * c
        val mirrorPosition = mirrorPosition(c, r)
        val summedRotation = PVector(0f, 0f)

        regions.forEach {
            // calculate 3d position of regions
            val regionPosition = PVector(it.x.toFloat(), it.y.toFloat(), regionHeight)

            // calculate angle to mirror
            val rotations = mirrorPosition.rotationInAxis(regionPosition)
            //rotations.add(angle)

            // check if mirror direction is relevant
            println("Mirror c: $c r: $r -> x: ${rotations.x} y: ${rotations.y} z: ${rotations.z}")

            summedRotation.x += rotations.z
            summedRotation.y += rotations.y
        }

        val x = Math.abs(Math.round(summedRotation.x / regions.size.toFloat()) + 90f)
        val y = Math.abs(Math.round(summedRotation.y / regions.size.toFloat()) + 90f)

        // move mirror
        mirror.xAxis.moveTo(limit(x, 60f, 120f).toInt())
        mirror.yAxis.moveTo(limit(y, 60f, 120f).toInt())
    }

    private fun limit(value: Float, min: Float, max: Float): Float {
        return Math.max(Math.min(max, value), min)
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