package ch.bildspur.floje.model.grid

/**
 * Created by cansik on 08.06.17.
 */
abstract class GridField {
    fun isEmpty(): Boolean {
        return this is Empty
    }
}