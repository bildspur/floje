package ch.bildspur.floje.sweep

import ch.bildspur.floje.tracker.ActiveRegionTracker
import io.scanse.sweep.SweepDevice
import io.scanse.sweep.SweepSample


/**
 * Created by cansik on 18.07.17.
 */
class SweepDataProvider() {

    @Volatile lateinit var sweep: SweepDevice

    @Volatile var lastScan: List<SweepSample> = emptyList()

    @Volatile var running = true

    val tracker = ActiveRegionTracker()

    var port: String = ""

    var innerCone = 0.0
    var outerCone = Double.MAX_VALUE

    constructor(port: String) : this() {
        this.port = port
    }

    fun start() {
        running = true

        println("Opening sweep on $port...")
        sweep = SweepDevice(port)

        println("waiting for sweep motors...")
        while (!sweep.isMotorReady) {
            Thread.sleep(100)
        }

        println("start scanning...")
        sweep.startScanning()

        println("sweep running!")
        while (running) {
            interact()
        }

        sweep.stopScanning()
        sweep.close()
    }

    fun stop() {
        running = false
    }

    fun interact() {
        lastScan = sweep.nextScan()
        tracker.track(lastScan)
    }
}