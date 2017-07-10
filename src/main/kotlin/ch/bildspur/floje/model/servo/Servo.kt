package ch.bildspur.floje.model.servo


/**
 * Created by cansik on 10.07.17.
 */
class Servo() {
    var position = 90

    fun write(position: Int) {
        this.position = position
    }
}