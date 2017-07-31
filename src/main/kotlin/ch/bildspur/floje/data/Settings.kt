package ch.bildspur.floje.data

import ch.bildspur.floje.model.Mirror
import com.google.gson.annotations.Expose

/**
 * Created by cansik on 11.07.17.
 */
class Settings {
    @Expose var sweep: Sweep = Sweep()
    @Expose var limits: Limits = Limits()
    @Expose var interaction: Interaction = Interaction()
    @Expose var mirrors: List<Mirror> = emptyList()
}