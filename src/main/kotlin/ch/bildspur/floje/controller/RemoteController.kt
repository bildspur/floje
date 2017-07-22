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
            'p' -> {
                sketch.grid.forEachMirror { mirror, c, r ->
                    PApplet.println("${mirror.name}.local")
                }
            }
            's' -> {
                sketch.grid.forEachMirror { mirror, c, r ->
                    mirror.xAxis.stop()
                    mirror.yAxis.stop()
                }
            }
            'x' -> {
                sketch.isInteractionOn = !sketch.isInteractionOn
            }
            '1' -> {
                // initial position
                sketch.grid.moveMirrors(90, 90)
            }
            '2' -> {
                // rain position
                sketch.grid.moveMirrors(90, 45)
            }
        }
    }
}