package ch.bildspur.floje.view

import ch.bildspur.floje.draw
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.stackMatrix
import processing.core.PGraphics

/**
 * Created by cansik on 08.06.17.
 */
class MirrorVisualiser(val g: PGraphics, val grid: Grid) {

    val elementSize = 80f
    val elementHeight = 40f
    val towerSize = 7f
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

        g.fill(41f, 128f, 185f)
        g.box(elementSize, elementSize, emptyHeight)
    }

    internal fun renderMirror(mirror: Mirror, x: Int, y: Int) {
        translateToPosition(x, y)

        g.fill(231f, 76f, 60f)
        g.box(elementSize, elementSize, elementHeight)
    }

    internal fun translateToPosition(x: Int, y: Int) {
        val totalWidth = ((grid.width - 1) * elementSize) + (grid.width - 1) * elementSpace
        val totalHeight = ((grid.height - 1) * elementSize) + (grid.height - 1) * elementSpace

        val posX = (x * elementSize) + (x * elementSpace) - totalWidth / 2.0f
        val posY = (y * elementSize) + (y * elementSpace) - totalHeight / 2.0f

        g.translate(posX, posY)
    }
}