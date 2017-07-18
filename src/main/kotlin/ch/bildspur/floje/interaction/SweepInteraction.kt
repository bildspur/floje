package ch.bildspur.floje.interaction

import ch.bildspur.floje.model.grid.Grid
import io.scanse.sweep.SweepDevice


/**
 * Created by cansik on 18.07.17.
 */
class SweepInteraction(val port: String, val grid: Grid) {

    lateinit var sweep: SweepDevice

    @Volatile var running = true

    fun start() {
        running = true

        sweep = SweepDevice(port)
        sweep.startScanning()

        while (!sweep.isMotorReady) {
            Thread.sleep(100)
        }

        while (running) {
            interact()
        }

        sweep.close()
    }

    fun stop() {
        running = false
    }

    fun interact() {
        
    }
}