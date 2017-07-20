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

    lateinit var rotationSlider: Slider

    var hpos = 0f
    var vpos = 20f
    var vspace = 5f

    var controlWidth = 100
    var controlHeight = 20

    fun setup() {
        cp5 = ControlP5(sketch)
        cp5.isAutoDraw = false

        // design cp5
        cp5.setColorBackground(sketch.color(44f, 62f, 80f))
        cp5.setColorForeground(sketch.color(22f, 160f, 133f))
        cp5.setColorActive(sketch.color(26f, 188f, 156f))

        cp5.setColorValueLabel(sketch.color(236, 240, 241))
        cp5.setColorCaptionLabel(sketch.color(44f, 62f, 80f))

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

        minLifeTimeSlider = cp5.addSlider("Min Lifetime")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.minLifeTime.toFloat())
                .setRange(0f, 1000f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.minLifeTime = minLifeTimeSlider.value.toInt()
                }

        sparsingSlider = cp5.addSlider("Sparse Distance")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.sweepDataProvider.tracker.sparsing.toFloat())
                .setRange(0f, 500f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.sweepDataProvider.tracker.sparsing = sparsingSlider.value.toDouble()
                }

        maxDeltaSlider = cp5.addSlider("Max Delta")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.sweepDataProvider.tracker.maxDelta.toFloat())
                .setRange(0f, 500f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.sweepDataProvider.tracker.maxDelta = maxDeltaSlider.value.toDouble()
                }

        rotationSlider = cp5.addSlider("Rotation")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.rotation.toFloat())
                .setRange(0f, 360f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.rotation = rotationSlider.value.toDouble()
                }
    }
}