package ch.bildspur.floje.model.grid

import ch.bildspur.floje.model.grid.Empty
import ch.bildspur.floje.model.grid.GridField

/**
 * Created by cansik on 08.06.17.
 */
class Grid(val width : Int, val height : Int) {
    val row = Array<GridField>(width, { i -> Empty() })
    val column = Array<GridField>(height, { i -> Empty() })
}