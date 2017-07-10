package ch.bildspur.floje.model.servo

import ch.bildspur.floje.model.DataModel


/**
 * Created by cansik on 10.07.17.
 */
class Servo() {
    var position = DataModel(90)

    fun write(position: Int) {
        this.position.value = position
    }
}