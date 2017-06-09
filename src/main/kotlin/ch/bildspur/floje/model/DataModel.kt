package ch.bildspur.floje.model

import ch.bildspur.floje.event.Event


/**
 * Created by cansik on 09.06.17.
 */
class DataModel<T>(internal var dataValue: T) {
    val onChanged = Event<T>()

    var value: T
        get() = this.dataValue
        set(value) {
            dataValue = value
            onChanged.invoke(value)
        }
}