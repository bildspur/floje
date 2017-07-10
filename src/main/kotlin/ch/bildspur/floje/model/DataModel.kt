package ch.bildspur.floje.model

import ch.bildspur.floje.event.Event


/**
 * Created by cansik on 09.06.17.
 */
class DataModel<T>(private var dataValue: T) {
    val onChanged = Event<T>()

    var value: T
        get() = this.dataValue
        set(value) {
            dataValue = value

            // fire event if changed
            if (dataValue != value)
                fire()
        }

    fun fire() {
        onChanged.invoke(dataValue)
    }
}