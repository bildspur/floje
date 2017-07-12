package ch.bildspur.floje

import ch.bildspur.floje.controller.*
import ch.bildspur.floje.controller.timer.TimerTask
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.view.MirrorVisualiser
import ch.bildspur.floje.view.StatusView
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

    var isStatusViewShown = false

    val peasy = PeasyController(this)

    val osc = OscController(this)

    val config = ConfigurationController(this)

    val timer = TimerController(this)

    val ping = PingController(this)

    val grid = Grid(6, 4)

    lateinit var visualiser: MirrorVisualiser

    lateinit var statusView: StatusView

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

        peasy.setup()
    }

    override fun draw() {
        background(255)

        // skip first two frames
        if (frameCount < 2) {
            peasy.hud {
                textAlign(CENTER, CENTER)
                fill(0)
                textSize(20f)
                text("FLØJE is loading...", width / 2f, height / 2f)
            }
            return
        }

        // setup long loading controllers
        if (initControllers()) {
            return
        }

        timer.update()
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

            // overlay status
            if (isStatusViewShown)
                statusView.render()

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

            canvas = createGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P3D)
            canvas.smooth(8)

            config.setup()
            config.loadConfiguration()

            visualiser = MirrorVisualiser(canvas, grid)
            statusView = StatusView(g, grid)

            timer.setup()

            prepareExitHandler()

            setupMirrors()

            // add ping task
            timer.addTask(TimerTask(PingController.PING_INTERVAL, {
                ping.pingMirrors(grid)
            }))

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
        pg.textSize(12f)
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

            it.setup()
        }
    }

    override fun keyPressed() {
        when (key) {
            'i' -> isStatusViewShown = !isStatusViewShown
            'm' -> {
                val m = grid[0, 0] as Mirror
                m.xAxis.moveTo(30)
                m.yAxis.moveTo(130)
            }
        }
    }
}