package ch.bildspur.floje.sweep

import ch.bildspur.floje.tracker.ActiveRegionTracker
import io.scanse.sweep.SweepDevice
import io.scanse.sweep.SweepSample


/**
 * Created by cansik on 18.07.17.
 */
open class SweepDataProvider() {

    @Volatile lateinit var sweep: SweepDevice

    @Volatile var lastScan: List<SweepSample> = emptyList()

    @Volatile var running = true

    val tracker = ActiveRegionTracker()

    var minimalSignalStrength = 0

    var port: String = ""

    constructor(port: String) : this() {
        this.port = port
    }

    open fun start() {
        running = true

        println("Opening sweep on $port...")
        sweep = SweepDevice(port)

        println("setting configuration")
        sweep.sampleRate = 500
        sweep.motorSpeed = 5

        println("waiting for sweep motors...")
        while (!sweep.isMotorReady) {
            Thread.sleep(100)
        }

        println("start scanning with ${sweep.sampleRate} Hz sample rate and ${sweep.motorSpeed} Hz motor speed...")
        sweep.startScanning()

        println("sweep running!")
        while (running) {
            try {
                interact()
            } catch (ex: Exception) {
                println("Sweep is not connected anymore!")
                running = false
                return
            }
        }

        sweep.stopScanning()
        sweep.close()
    }

    open fun stop() {
        running = false
    }

    open fun interact() {
        lastScan = sweep.nextScan()
        tracker.track(lastScan.filter { it.signalStrength > minimalSignalStrength })
    }
}