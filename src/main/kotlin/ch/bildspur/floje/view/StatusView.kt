package ch.bildspur.floje.view

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import processing.core.PGraphics

/**
 * Created by cansik on 12.07.17.
 */
class StatusView(val pg: PGraphics, val grid: Grid) {
    companion object {
        val TEXT_SIZE = 14f
        val SPACE = 4f

        val MARGIN_X = 10f
        val MARGIN_Y = 20f
    }

    fun render() {
        pg.fill(0)
        pg.textSize(TEXT_SIZE)

        grid.columns.forEachIndexed { y, fields ->
            fields.forEachIndexed { x, field ->

                pg.pushMatrix()
                val index = x + (y * grid.height)
                pg.translate(MARGIN_X, MARGIN_Y + (index * (TEXT_SIZE + SPACE)))

                if (!field.isEmpty())
                    showInformation(field as Mirror, x, y)
                else {
                    pg.fill(0)
                    pg.text("C: $y R: $x => EMPTY", 0f, 0f)
                }

                pg.popMatrix()
            }
        }
    }

    private fun showInformation(m: Mirror, x: Int, y: Int) {
        if (m.isOnline)
            pg.fill(0f, 255f, 0f)
        else
            pg.fill(255f, 0f, 0f)

        pg.text("C: $y R: $x => ${m.name}", 0f, 0f)
    }
}