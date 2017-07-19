package ch.bildspur.floje.controller

import ch.bildspur.floje.Sketch
import controlP5.ControlP5
import controlP5.Slider

class UIController(internal var sketch: Sketch) {
    lateinit var cp5: ControlP5

    // controls
    lateinit var innerConeSlider: Slider
    lateinit var outerConeSlider: Slider
    lateinit var minLifeTimeSlider: Slider

    lateinit var sparsingSlider: Slider
    lateinit var maxDeltaSlider: Slider

    var hpos = 0f
    var vpos = 20f
    var vspace = 5f

    var controlWidth = 100
    var controlHeight = 20

    fun setup() {
        cp5 = ControlP5(sketch)
        cp5.isAutoDraw = false

        // design cp5
        /*
        cp5.setColorBackground(0x2c3e50)
        cp5.setColorForeground(0x16a085)
        cp5.setColorValueLabel(0x2c3e50)
        */
        cp5.setColorCaptionLabel(0x2c3e50)

        hpos = sketch.width / 7.0f * 5.0f
        setupControls()
    }

    fun render() {
        cp5.begin()
        cp5.draw()
        cp5.end()
    }

    internal fun setupControls() {
        var controlIndex = 0

        innerConeSlider = cp5.addSlider("Inner Cone")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.innerCone.toFloat())
                .setRange(0f, 500f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.innerCone = innerConeSlider.value.toDouble()
                }

        outerConeSlider = cp5.addSlider("Outer Cone")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.outerCone.toFloat())
                .setRange(0f, 500f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.outerCone = outerConeSlider.value.toDouble()
                }
    }
}