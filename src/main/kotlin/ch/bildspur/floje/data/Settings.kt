package ch.bildspur.floje.data

import ch.bildspur.floje.model.Mirror

/**
 * Created by cansik on 11.07.17.
 */
class Settings {
    var sweep: Sweep = Sweep()
    var limits: Limits = Limits()
    var interaction: Interaction = Interaction()
    var mirrors: List<Mirror> = emptyList()
}