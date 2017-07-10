package ch.bildspur.floje

import ch.bildspur.floje.controller.OscController
import ch.bildspur.floje.controller.PeasyController
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.view.MirrorVisualiser
import org.opencv.core.Core
import processing.core.PApplet
import processing.core.PConstants
import processing.core.PGraphics
import processing.opengl.PJOGL

/**
 * Created by cansik on 04.02.17.
 */
class Sketch : PApplet() {
    companion object {
        @JvmStatic val FRAME_RATE = 30f

        @JvmStatic val WINDOW_WIDTH = 640
        @JvmStatic val WINDOW_HEIGHT = 480

        @JvmStatic val NAME = "FLØJE Installation"

        @JvmStatic var instance = PApplet()

        @JvmStatic fun map(value: Double, start1: Double, stop1: Double, start2: Double, stop2: Double): Double {
            return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1))
        }

        @JvmStatic fun currentMillis(): Int {
            return instance.millis()
        }
    }

    var fpsOverTime = 0f

    val peasy = PeasyController(this)

    val osc = OscController(this)

    val grid = Grid(5, 5)

    lateinit var visualiser: MirrorVisualiser

    lateinit var canvas: PGraphics

    init {
        // add test flojes
        grid[2, 2] = Mirror("Test Mirror")
        val m = Mirror("Test Mirror")
        m.xAxis.moveTo(110)
        grid[1, 4] = m
    }

    override fun settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P3D)
        PJOGL.profile = 1
    }

    override fun setup() {
        Sketch.instance = this

        smooth()
        frameRate(FRAME_RATE)

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        surface.setTitle(NAME)

        canvas = createGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P3D)

        visualiser = MirrorVisualiser(canvas, grid)

        peasy.setup()

        prepareExitHandler()
    }

    override fun draw() {
        // setup long loading controllers
        if (initControllers()) {
            peasy.hud {
                textAlign(CENTER, CENTER)
                text("loading controllers...", width / 2f, height / 2f)
            }
            return
        }

        updateServos()

        canvas.draw {
            it.background(15f)

            visualiser.render()

            peasy.applyTo(canvas)
        }

        // add hud
        peasy.hud {
            // output image
            image(canvas, 0f, 0f)
            drawFPS(g)
        }
    }

    fun updateServos() {
        grid.columns.forEachIndexed { y, fields ->
            fields.forEachIndexed { x, field ->
                if (!field.isEmpty()) {
                    val mirror = (field as Mirror)
                    mirror.xAxis.update()
                    mirror.yAxis.update()
                }
            }
        }
    }

    fun initControllers(): Boolean {
        if (!osc.isSetup) {
            osc.setup()
            return true
        }

        return false
    }

    fun drawFPS(pg: PGraphics) {
        // draw fps
        fpsOverTime += frameRate
        val averageFPS = fpsOverTime / frameCount.toFloat()

        pg.textAlign(PApplet.LEFT, PApplet.BOTTOM)
        pg.fill(255)
        pg.text("FPS: ${frameRate.format(2)}\nFOT: ${averageFPS.format(2)}", 10f, height - 5f)
    }

    fun prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("shutting down...")
            osc.osc.stop()
        })
    }

    override fun keyPressed() {
        val m = grid[1, 4] as Mirror
        m.xAxis.moveTo(30)
        m.yAxis.moveTo(130)
    }
}