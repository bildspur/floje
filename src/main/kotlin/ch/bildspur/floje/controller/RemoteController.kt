package ch.bildspur.floje.controller

import ch.bildspur.floje.Sketch
import processing.core.PApplet

class RemoteController(internal var sketch: Sketch) {

    fun processCommand(key: Char) {
        when (key) {
            'i' -> sketch.isStatusViewShown = !sketch.isStatusViewShown
            'u' -> sketch.isUIShown = !sketch.isUIShown
            'l' -> sketch.enableLights = !sketch.enableLights
            'm' -> {
                val xpos = sketch.random(45f, 135f).toInt()
                val ypos = sketch.random(45f, 135f).toInt()

                // move all to random position
                sketch.grid.moveMirrors(xpos, ypos)
            }
            't' -> {
                sketch.grid.forEachMirror { mirror, c, r ->
                    PApplet.println("${mirror.name}.local\t" +
                            "TrimX: ${mirror.xAxis.servo.position.value - 90}\t" +
                            "TrimY: ${mirror.yAxis.servo.position.value - 90}\t")
                }
            }
            's' -> {
                sketch.grid.forEachMirror { mirror, c, r ->
                    mirror.xAxis.stop()
                    mirror.yAxis.stop()
                }
            }
            'v' -> {
                // save configuration
                sketch.resyncSettings()
                sketch.config.saveConfiguration()

                println("Configuration saved!")
            }
            'x' -> {
                sketch.isInteractionOn.value = !sketch.isInteractionOn.value
            }
            '1' -> {
                // initial position
                sketch.grid.moveMirrors(90, 90)
            }
            '2' -> {
                // rain position
                sketch.grid.moveMirrors(90, 45)
            }
            '3' -> {
                // transport / access position
                sketch.grid.forEachMirror { mirror, c, r ->
                    if (c == 3 || c == 0) {
                        mirror.xAxis.moveTo(30)
                        mirror.yAxis.moveTo(90)
                    }

                    if (c == 2 || c == 5) {
                        mirror.xAxis.moveTo(150)
                        mirror.yAxis.moveTo(90)
                    }
                }
            }
        }
    }
}