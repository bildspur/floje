package ch.bildspur.floje.view

import ch.bildspur.floje.draw
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.stackMatrix
import processing.core.PApplet
import processing.core.PGraphics

/**
 * Created by cansik on 08.06.17.
 */
class MirrorVisualiser(val g: PGraphics, val grid: Grid) {

    val radius = 37f

    val elementSize = 50f
    val elementThickness = 2f

    val towerSize = 7f
    val towerHeight = 20f

    val jointSize = 4f

    val emptyHeight = 5f

    val elementSpace = 15

    fun render() {
        g.draw {
            renderFloor()
            grid.columns.forEachIndexed { y, fields ->
                fields.forEachIndexed { x, field ->
                    g.stackMatrix {
                        if (field.isEmpty())
                            renderEmpty(x, y)
                        else
                            renderMirror(field as Mirror, x, y)
                    }
                }
            }
        }
    }

    internal fun renderEmpty(x: Int, y: Int) {
        translateToPosition(x, y)

        g.fill(41f, 128f, 185f, 100f)
        g.noStroke()
        g.translate(0f, 0f, emptyHeight / 2)
        g.box(elementSize, elementSize, emptyHeight)
    }

    internal fun renderMirror(mirror: Mirror, x: Int, y: Int) {
        translateToPosition(x, y)

        // draw tower
        g.fill(127f, 140f, 141f)
        g.translate(0f, 0f, towerHeight / 2)
        g.box(towerSize, towerSize, towerHeight)

        // draw joint
        g.stroke(127f, 140f, 141f)
        g.noFill()
        g.translate(0f, 0f, (towerHeight / 2) + (jointSize / 2))
        g.sphereDetail(4, 4)
        g.sphere(jointSize)

        // draw top plate
        g.fill(236f, 240f, 241f)
        g.translate(0f, 0f, (jointSize / 2) + (elementThickness / 2))

        // rotate mirror
        g.rotateX(PApplet.radians(90f - mirror.xAxis.servo.position.value))
        g.rotateY(PApplet.radians(90f - mirror.yAxis.servo.position.value))

        g.box(elementSize, elementSize, elementThickness)
    }

    internal fun renderFloor() {
        g.pushMatrix()
        g.translate(0f, 0f, 0f - elementSize)

        g.fill(127f, 140f, 141f, 100f)
        g.noStroke()
        g.box(500f, 500f, 2f)
        g.popMatrix()
    }

    internal fun translateToPosition(x: Int, y: Int) {
        val posX = radius
        val posY = 0f
        val posZ = (y * elementSize) + (y * elementSpace)

        // rotate mirrors to direction
        g.rotateZ(PApplet.radians((360f / grid.width) * x))
        g.translate(posX, posY, posZ)

        // rotate whole mirror
        g.rotateY(PApplet.radians(90f))
    }
}