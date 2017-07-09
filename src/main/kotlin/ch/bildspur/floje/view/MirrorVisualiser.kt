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

    val elementSize = 80f
    val elementThickness = 2f

    val towerSize = 7f
    val towerHeight = 40f

    val jointSize = 4f

    val emptyHeight = 5f

    val elementSpace = 10

    fun render() {
        g.draw {
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
        g.rotateX(PApplet.radians(90f - mirror.xAxis.value))
        g.rotateY(PApplet.radians(90f - mirror.yAxis.value))

        g.box(elementSize, elementSize, elementThickness)
    }

    internal fun translateToPosition(x: Int, y: Int) {
        val totalWidth = ((grid.width - 1) * elementSize) + (grid.width - 1) * elementSpace
        val totalHeight = ((grid.height - 1) * elementSize) + (grid.height - 1) * elementSpace

        val posX = (x * elementSize) + (x * elementSpace) - totalWidth / 2.0f
        val posY = (y * elementSize) + (y * elementSpace) - totalHeight / 2.0f

        g.translate(posX, posY)
    }
}