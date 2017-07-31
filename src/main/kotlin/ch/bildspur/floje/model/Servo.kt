package ch.bildspur.floje.model


/**
 * Created by cansik on 10.07.17.
 */
class Servo() {
    var position = DataModel(90)

    fun write(position: Int) {
        this.position.value = position
    }
}