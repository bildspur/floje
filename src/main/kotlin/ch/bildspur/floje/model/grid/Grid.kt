package ch.bildspur.floje.model.grid

import ch.bildspur.floje.model.Mirror

/**
 * Created by cansik on 08.06.17.
 */
class Grid(val width: Int, val height: Int) {
    val columns = Array(width, { i -> Array<GridField>(height, { i -> Empty() }) })

    operator fun get(c: Int, r: Int): GridField {
        return columns[c][r]
    }

    operator fun set(c: Int, r: Int, element: GridField) {
        columns[c][r] = element
    }

    fun forEach(block: (field: GridField, c: Int, r: Int) -> Unit) {
        columns.forEachIndexed { c, fields ->
            fields.forEachIndexed { r, field ->
                block(field, c, r)
            }
        }
    }

    fun forEachMirror(block: (mirror: Mirror, c: Int, r: Int) -> Unit) {
        this.forEach { field, c, r ->
            if (!field.isEmpty())
                block(field as Mirror, c, r)
        }
    }
}