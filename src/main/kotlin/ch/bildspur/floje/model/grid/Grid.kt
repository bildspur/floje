package ch.bildspur.floje.model.grid

/**
 * Created by cansik on 08.06.17.
 */
class Grid(val width: Int, val height: Int) {
    val columns = Array(height, { i -> Array<GridField>(width, { i -> Empty() }) })

    operator fun get(c: Int, r: Int): GridField {
        return columns[c][r]
    }

    operator fun set(c: Int, r: Int, element: GridField) {
        columns[c][r] = element
    }
}