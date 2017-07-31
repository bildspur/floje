package ch.bildspur.floje.data

import com.google.gson.annotations.Expose

/**
 * Created by cansik on 11.07.17.
 */
class Position {
    @Expose var column: Int = 0
    @Expose var row: Int = 0
}