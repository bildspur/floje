package ch.bildspur.floje.view

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.controller.SweepController
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.util.cylinder
import ch.bildspur.floje.util.draw
import ch.bildspur.floje.util.projectedAngle
import ch.bildspur.floje.util.stackMatrix
import processing.core.PApplet
import processing.core.PGraphics


/**
 * Created by cansik on 08.06.17.
 */
class MirrorVisualiser(val sketch: Sketch, val g: PGraphics, val grid: Grid, val sweep: SweepController) {
    val elementThickness = 2f

    val towerSize = 7f
    val towerHeight = 20f

    val jointSize = 4f

    val emptyHeight = 5f

    val elementSpace = 15

    var showPosition = true

    var showMaxDelta = false

    var showSparsing = false

    fun render() {
        g.draw {
            if (sketch.enableLights)
                setupLights()

            visualiseSweep()
            renderFloor()

            grid.forEach { field, c, r ->
                g.stackMatrix {
                    if (field.isEmpty())
                        renderEmpty(r, c)
                    else
                        renderMirror(field as Mirror, r, c)
                }
            }
        }
    }

    internal fun setupLights() {
        g.lights()
    }

    internal fun renderEmpty(x: Int, y: Int) {
        translateToPosition(x, y)

        g.fill(41f, 128f, 185f, 100f)
        g.noStroke()
        g.translate(0f, 0f, emptyHeight / 2)
        g.box(sketch.sweep.sweepInteraction.mirrorSize, sketch.sweep.sweepInteraction.mirrorSize, emptyHeight)

        // add text name
        g.translate(0f, 0f, (emptyHeight / 2) + 1)
        g.rotateZ(PApplet.radians(-90f))
        showInformation(x, y)
    }

    internal fun renderMirror(mirror: Mirror, x: Int, y: Int) {
        translateToPosition(x, y)

        // draw tower
        g.fill(127f, 140f, 141f)
        g.translate(0f, 0f, towerHeight / 2)
        g.box(towerSize, towerSize, towerHeight)

        // draw joint
        g.stroke(127f, 140f, 141f)
        g.noFill()
        g.translate(0f, 0f, (towerHeight / 2) + (jointSize / 2))
        g.sphereDetail(4, 4)
        g.sphere(jointSize)

        // draw top plate
        if (mirror.isOnline)
            g.fill(236f, 240f, 241f)
        else
            g.fill(236f, 100f, 75f)

        g.translate(0f, 0f, (jointSize / 2) + (elementThickness / 2))

        // rotate mirror
        g.rotateX(PApplet.radians(90f - (180 - mirror.xAxis.servo.position.value)))
        g.rotateY(PApplet.radians(90f - (180 - mirror.yAxis.servo.position.value)))

        // add mirror
        g.box(sketch.sweep.sweepInteraction.mirrorSize,
                sketch.sweep.sweepInteraction.mirrorSize, elementThickness)

        // add text name
        g.translate(0f, 0f, (elementThickness / 2) + 1)
        g.rotateZ(PApplet.radians(-90f))
        showInformation(x, y)
    }

    internal fun showInformation(x: Int, y: Int) {
        if (!showPosition)
            return

        g.fill(55f)
        g.noStroke()
        g.textSize(14f)
        g.textAlign(PApplet.CENTER, PApplet.CENTER)
        g.text("$y,$x", 0f, 0f, 0f)
    }

    internal fun renderFloor() {
        g.pushMatrix()
        g.translate(0f, 0f, 0f - sketch.sweep.sweepInteraction.mirrorSize)

        g.fill(127f, 140f, 141f, 100f)
        g.noStroke()
        g.box(500f, 500f, 2f)
        g.popMatrix()
    }

    internal fun translateToPosition(x: Int, y: Int) {
        val posX = sketch.sweep.sweepInteraction.mirrorRadius
        val posY = 0f
        val posZ = (x * sketch.sweep.sweepInteraction.mirrorSize) + (x * elementSpace) + sketch.sweep.sweepInteraction.mirrorStartHeight

        // rotate mirrors to direction
        g.rotateZ(PApplet.radians((360f / grid.width) * y))
        g.translate(posX, posY, posZ)

        // rotate whole mirror
        g.rotateY(PApplet.radians(90f))
    }

    internal fun visualiseSweep() {
        val scan = sweep.currentScan

        // visualise dots
        scan.filter { it.signalStrength >= sweep.sweepDataProvider.minimalSignalStrength }.forEach { s ->
            g.stackMatrix {
                g.rotateZ(PApplet.radians(s.projectedAngle() + sweep.rotation.toFloat()))
                g.translate(s.distance.toFloat(), 0f)
                g.noFill()
                g.stroke(142f, 68f, 173f, s.signalStrength.toFloat())
                g.box(5f)
            }
        }

        // visualise active regions
        sweep.relevantRegions.forEachIndexed { i, s ->
            g.stackMatrix {
                g.rotateZ(PApplet.radians(sweep.rotation.toFloat()))
                g.translate(s.x.toFloat(), s.y.toFloat(), 0f)
                g.noFill()
                g.strokeWeight(2.0f)
                g.stroke(46f, 204f, 113f, 100f)
                g.box(20f)

                // visualise parameter

                if (showMaxDelta) {
                    // max delta
                    g.strokeWeight(0.5f)
                    g.noFill()
                    g.stroke(230f, 126f, 34f)
                    g.cylinder(50,
                            sweep.sweepDataProvider.tracker.maxDelta.toFloat(),
                            sweep.sweepDataProvider.tracker.maxDelta.toFloat(),
                            10f)
                }

                // sparsing
                if (showSparsing) {
                    g.strokeWeight(0.5f)
                    g.noFill()
                    g.stroke(241f, 196f, 15f)
                    g.cylinder(50,
                            sweep.sweepDataProvider.tracker.sparsing.toFloat(),
                            sweep.sweepDataProvider.tracker.sparsing.toFloat(),
                            10f)
                }
            }
        }

        // visualise cones
        g.noStroke()
        g.noFill()
        g.strokeWeight(2f)
        g.stroke(0f, 255f, 0f, 20f)
        g.stackMatrix {
            it.cylinder(50,
                    sweep.outerCone.toFloat(),
                    sweep.outerCone.toFloat(),
                    5f)
        }

        g.noStroke()
        g.noFill()
        g.strokeWeight(2f)
        g.stroke(255f, 0f, 0f, 20f)
        g.stackMatrix {
            it.cylinder(50,
                    sweep.innerCone.toFloat(),
                    sweep.innerCone.toFloat(),
                    6f)

        }
    }
}