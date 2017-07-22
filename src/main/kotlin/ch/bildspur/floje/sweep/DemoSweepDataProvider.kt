package ch.bildspur.floje.sweep

import io.scanse.sweep.SweepSample

class DemoSweepDataProvider : SweepDataProvider() {

    var demoSamples = mutableListOf<SweepSample>()

    override fun start() {
        running = true

        println("creating sweep demo...")
        createDemoSamples()

        println("sweep running!")

        while (running) {
            interact()
        }
    }

    override fun stop() {
        running = false
    }

    override fun interact() {
        //updateDemoSamples()

        lastScan = demoSamples
        tracker.track(lastScan.filter { it.signalStrength > minimalSignalStrength })
    }

    private fun updateDemoSamples() {
        // rotate first three
        val samples = (0..3).map {
            SweepSample(demoSamples[it].angle + 1,
                    demoSamples[it].distance,
                    demoSamples[it].signalStrength)
        }

        demoSamples = samples.toMutableList()
    }

    private fun createDemoSamples() {
        demoSamples.add(SweepSample(0, 100, 255))
        demoSamples.add(SweepSample(3000, 100, 255))
        demoSamples.add(SweepSample(6000, 100, 255))
    }
}