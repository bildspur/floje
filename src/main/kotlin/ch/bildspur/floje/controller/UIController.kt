package ch.bildspur.floje.controller

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.model.Mirror
import controlP5.ControlP5
import controlP5.DropdownList
import controlP5.Slider
import controlP5.Toggle

class UIController(internal var sketch: Sketch) {
    lateinit var cp5: ControlP5

    // controls
    lateinit var innerConeSlider: Slider
    lateinit var outerConeSlider: Slider
    lateinit var minLifeTimeSlider: Slider

    lateinit var sparsingSlider: Slider
    lateinit var maxDeltaSlider: Slider

    lateinit var rotationSlider: Slider

    lateinit var minSignalStrengthSlider: Slider
    lateinit var minRegionSizeSlider: Slider

    lateinit var mirrorSelector: DropdownList
    lateinit var yAxisSlider: Slider
    lateinit var xAxisSlider: Slider

    lateinit var interactionToggle: Toggle

    var selectedMirror: Mirror? = null

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

        rotationSlider = cp5.addSlider("Rotation")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.rotation.toFloat())
                .setColorForeground(sketch.color(22, 160, 133))
                .setColorActive(sketch.color(26, 188, 156))
                .setRange(0f, 360f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.rotation = rotationSlider.value.toDouble()
                }

        innerConeSlider = cp5.addSlider("Inner Cone")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.innerCone.toFloat())
                .setColorForeground(sketch.color(46, 204, 113))
                .setColorActive(sketch.color(39, 174, 96))
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
                .setColorForeground(sketch.color(46, 204, 113))
                .setColorActive(sketch.color(39, 174, 96))
                .setRange(0f, 500f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.outerCone = outerConeSlider.value.toDouble()
                }

        sparsingSlider = cp5.addSlider("Sparse Distance")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.sweepDataProvider.tracker.sparsing.toFloat())
                .setColorForeground(sketch.color(41, 128, 185))
                .setColorActive(sketch.color(52, 152, 219))
                .setRange(0f, 500f)
                .onEnter {
                    sketch.peasy.disable()
                    sketch.visualiser.showSparsing = true
                }
                .onLeave {
                    sketch.peasy.enable()
                    sketch.visualiser.showSparsing = false
                }
                .onChange { e ->
                    sketch.sweep.sweepDataProvider.tracker.sparsing = sparsingSlider.value.toDouble()
                }

        maxDeltaSlider = cp5.addSlider("Max Delta")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.sweepDataProvider.tracker.maxDelta.toFloat())
                .setColorForeground(sketch.color(41, 128, 185))
                .setColorActive(sketch.color(52, 152, 219))
                .setRange(0f, 500f)
                .onEnter {
                    sketch.peasy.disable()
                    sketch.visualiser.showMaxDelta = true
                }
                .onLeave {
                    sketch.peasy.enable()
                    sketch.visualiser.showMaxDelta = false
                }
                .onChange { e ->
                    sketch.sweep.sweepDataProvider.tracker.maxDelta = maxDeltaSlider.value.toDouble()
                }


        minLifeTimeSlider = cp5.addSlider("Min Lifetime")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.minLifeTime.toFloat())
                .setColorForeground(sketch.color(142, 68, 173))
                .setColorActive(sketch.color(155, 89, 182))
                .setRange(0f, 1000f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.minLifeTime = minLifeTimeSlider.value.toInt()
                }

        minRegionSizeSlider = cp5.addSlider("Min Region Size")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.minRegionSize.toFloat())
                .setColorForeground(sketch.color(142, 68, 173))
                .setColorActive(sketch.color(155, 89, 182))
                .setRange(0f, 50f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.minRegionSize = minRegionSizeSlider.value.toInt()
                }

        minSignalStrengthSlider = cp5.addSlider("Min Signal Strength")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.sweep.sweepDataProvider.minimalSignalStrength.toFloat())
                .setColorForeground(sketch.color(142, 68, 173))
                .setColorActive(sketch.color(155, 89, 182))
                .setRange(0f, 255f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    sketch.sweep.sweepDataProvider.minimalSignalStrength = minSignalStrengthSlider.value.toInt()
                }

        mirrorSelector = cp5.addDropdownList("Mirror Trim")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(90f)
                .setColorForeground(sketch.color(243, 156, 18))
                .setColorActive(sketch.color(241, 196, 15))
                .setColorValueLabel(sketch.color(255))
                .setColorCaptionLabel(sketch.color(255))
                .onEnter {
                    sketch.peasy.disable()
                    mirrorSelector.bringToFront()
                    mirrorSelector.height = 150
                    mirrorSelector.width = 150
                }
                .onLeave {
                    sketch.peasy.enable()
                    mirrorSelector.height = controlHeight
                    mirrorSelector.width = mirrorSelector.width
                }
                .addItems(sketch.grid.mirror.map { "(${it.position.column}, ${it.position.row}) ${it.name}" })
                .onChange { e ->
                    val mirror = sketch.grid.mirror[mirrorSelector.value.toInt()]
                    selectedMirror = mirror

                    xAxisSlider.value = selectedMirror!!.trim.x.toFloat()
                    yAxisSlider.value = selectedMirror!!.trim.y.toFloat()

                    println("mirror ${mirror.name} selected!")
                }

        xAxisSlider = cp5.addSlider("X-Axis")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(0f)
                .setColorForeground(sketch.color(243, 156, 18))
                .setColorActive(sketch.color(241, 196, 15))
                .setRange(-30f, 30f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    if (selectedMirror != null) {
                        selectedMirror!!.trim.x = xAxisSlider.value.toInt()

                        // force write trim position
                        selectedMirror!!.xAxis.servo.write(90)
                        selectedMirror!!.xAxis.servo.position.fire()
                    }
                }

        yAxisSlider = cp5.addSlider("Y-Axis")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(0f)
                .setColorForeground(sketch.color(243, 156, 18))
                .setColorActive(sketch.color(241, 196, 15))
                .setRange(-30f, 30f)
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e ->
                    if (selectedMirror != null) {
                        selectedMirror!!.trim.y = yAxisSlider.value.toInt()

                        // force write trim position
                        selectedMirror!!.yAxis.servo.write(90)
                        selectedMirror!!.yAxis.servo.position.fire()
                    }
                }

        interactionToggle = cp5.addToggle("Interaction")
                .setPosition(hpos, vpos + (vspace + controlHeight) * controlIndex++)
                .setSize(controlWidth, controlHeight)
                .setValue(sketch.isInteractionOn.value)
                .setColorForeground(sketch.color(192, 57, 43))
                .setColorActive(sketch.color(231, 76, 60))
                .onEnter { sketch.peasy.disable() }
                .onLeave { sketch.peasy.enable() }
                .onChange { e -> sketch.isInteractionOn.value = interactionToggle.booleanValue }
        sketch.isInteractionOn.onChanged += { interactionToggle.setValue(sketch.isInteractionOn.value) }
    }
}