package ch.bildspur.floje

import ch.bildspur.floje.controller.ConfigurationController
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

        @JvmStatic val WINDOW_WIDTH = 768
        @JvmStatic val WINDOW_HEIGHT = 576

        @JvmStatic val NAME = "FLØJE Installation"

        @JvmStatic lateinit var instance: PApplet

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

    val config = ConfigurationController(this)

    val grid = Grid(6, 4)

    lateinit var visualiser: MirrorVisualiser

    lateinit var canvas: PGraphics

    init {
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
        canvas.smooth(8)

        config.setup()
        config.loadConfiguration()

        visualiser = MirrorVisualiser(canvas, grid)

        peasy.setup()

        prepareExitHandler()

        setupMirrors()
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
            it.background(255f)

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
        pg.fill(0)
        pg.text("FPS: ${frameRate.format(2)}\nFOT: ${averageFPS.format(2)}", 10f, height - 5f)
    }

    fun prepareExitHandler() {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("shutting down...")
            osc.osc.stop()
        })
    }

    fun setupMirrors() {

        // set limits and add to position
        config.settings.mirrors.forEach {
            grid[it.position.column, it.position.row] = it

            it.xAxis.maxAcceleration = config.settings.limits.maxAcceleration.toFloat()
            it.yAxis.maxAcceleration = config.settings.limits.maxAcceleration.toFloat()

            it.xAxis.maxVelocity = config.settings.limits.maxVelocity.toFloat()
            it.yAxis.maxVelocity = config.settings.limits.maxVelocity.toFloat()
        }
    }

    override fun keyPressed() {
        val m = grid[1, 4] as Mirror
        m.xAxis.moveTo(30)
        m.yAxis.moveTo(130)
    }
}