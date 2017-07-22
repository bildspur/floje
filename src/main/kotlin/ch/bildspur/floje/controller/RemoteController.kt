package ch.bildspur.floje.controller

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.model.Mirror
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
                sketch.grid.columns.forEachIndexed { y, fields ->
                    fields.forEachIndexed { x, field ->
                        if (!field.isEmpty()) {
                            val m = field as Mirror
                            m.xAxis.moveTo(xpos)
                            m.yAxis.moveTo(ypos)
                        }
                    }
                }
            }
            'p' -> {
                sketch.grid.columns.forEachIndexed { y, fields ->
                    fields.forEachIndexed { x, field ->
                        if (!field.isEmpty()) {
                            val m = field as Mirror
                            PApplet.println("${m.name}.local")
                        }
                    }
                }
            }
            's' -> {
                sketch.grid.columns.forEachIndexed { y, fields ->
                    fields.forEachIndexed { x, field ->
                        if (!field.isEmpty()) {
                            val m = field as Mirror
                            m.xAxis.stop()
                            m.yAxis.stop()
                        }
                    }
                }
            }
        }
    }
}