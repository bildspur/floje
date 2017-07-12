package ch.bildspur.floje.view

import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import oscP5.OscP5

/**
 * Created by cansik on 10.07.17.
 */
class OscOutput(val osc: OscP5, val grid: Grid) {

    fun registerMirror(mirror: Mirror) {

    }

    fun updateMirrors() {
        grid.columns.forEachIndexed { y, fields ->
            fields.forEachIndexed { x, field ->
                if (!field.isEmpty())
                    updateMirror(field as Mirror)
            }
        }
    }

    private fun updateMirror(mirror: Mirror) {
        // send the output

    }
}