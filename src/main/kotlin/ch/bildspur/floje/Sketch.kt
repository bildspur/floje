package ch.bildspur.floje

import ch.bildspur.floje.controller.*
import ch.bildspur.floje.controller.timer.TimerTask
import ch.bildspur.floje.model.DataModel
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.util.draw
import ch.bildspur.floje.util.format
import ch.bildspur.floje.view.MirrorVisualiser
import ch.bildspur.floje.view.OscOutput
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

    var isUIShown = true

    var enableLights = false

    var isInteractionOn = DataModel(true)

    val peasy = PeasyController(this)

    val osc = OscController(this)

    val config = ConfigurationController(this)

    val timer = TimerController(this)

    val ping = PingController(this)

    val sweep = SweepController(this)

    val remote = RemoteController(this)

    val grid = Grid(6, 4)

    val ui = UIController(this)

    lateinit var visualiser: MirrorVisualiser

    lateinit var statusView: StatusView

    lateinit var oscOutput: OscOutput

    lateinit var canvas: PGraphics

    init {
    }

    override fun settings() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P3D)
        PJOGL.profile = 1
        
        // retina screen
        pixelDensity = 2
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

        // update all time relevant
        if (isInteractionOn.value)
            sweep.analyseSweep()

        updateServos()
        timer.update()

        canvas.draw {
            it.background(255f)

            visualiser.render()

            peasy.applyTo(canvas)
        }

        // add hud
        peasy.hud {
            // output image
            image(canvas, 0f, 0f)

            if (isUIShown)
                ui.render()

            // overlay status
            if (isStatusViewShown)
                statusView.render()

            drawFPS(g)
        }
    }

    fun updateServos() {
        grid.forEachMirror { mirror, c, r ->
            mirror.xAxis.update()
            mirror.yAxis.update()
        }
    }

    fun initControllers(): Boolean {
        if (!osc.isSetup) {
            osc.setup()

            canvas = createGraphics(WINDOW_WIDTH, WINDOW_HEIGHT, PConstants.P3D)
            canvas.smooth(8)

            config.setup()
            config.loadConfiguration()

            oscOutput = OscOutput(osc.osc, grid)
            visualiser = MirrorVisualiser(this, canvas, grid, sweep)
            statusView = StatusView(g, grid)

            applySettings()

            timer.setup()

            prepareExitHandler()

            setupMirrors()

            // add ping task
            timer.addTask(TimerTask(PingController.PING_INTERVAL, {
                ping.pingMirrors(grid)
            }))

            // add osc output task
            timer.addTask(TimerTask(OscOutput.OUTPUT_INTERVAL, {
                oscOutput.updateMirrors()
            }))

            // run mirror check
            ping.pingMirrors(grid)

            sweep.setup(grid)

            ui.setup()

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
            sweep.close()
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

            // register mirror for osc update
            oscOutput.registerMirror(it)
        }
    }

    fun resyncSettings() {
        config.settings.sweep.port = sweep.sweepPort

        config.settings.sweep.sparsing = sweep.sweepDataProvider.tracker.sparsing
        config.settings.sweep.maxDelta = sweep.sweepDataProvider.tracker.maxDelta

        config.settings.sweep.innerCone = sweep.innerCone
        config.settings.sweep.outerCone = sweep.outerCone
        config.settings.sweep.minLifeTime = sweep.minLifeTime
        config.settings.sweep.rotation = sweep.rotation
        config.settings.sweep.minRegionSize = sweep.minRegionSize
        config.settings.sweep.minSignalStrength = sweep.sweepDataProvider.minimalSignalStrength

        config.settings.interaction.viewAngle = sweep.sweepInteraction.viewAngle.toDouble()
        config.settings.interaction.servoLimit = sweep.sweepInteraction.servoLimit.toDouble()
    }

    fun applySettings() {
        sweep.sweepPort = config.settings.sweep.port

        sweep.sweepDataProvider.tracker.sparsing = config.settings.sweep.sparsing
        sweep.sweepDataProvider.tracker.maxDelta = config.settings.sweep.maxDelta

        sweep.innerCone = config.settings.sweep.innerCone
        sweep.outerCone = config.settings.sweep.outerCone
        sweep.minLifeTime = config.settings.sweep.minLifeTime
        sweep.rotation = config.settings.sweep.rotation
        sweep.minRegionSize = config.settings.sweep.minRegionSize
        sweep.sweepDataProvider.minimalSignalStrength = config.settings.sweep.minSignalStrength

        sweep.sweepInteraction.viewAngle = config.settings.interaction.viewAngle.toFloat()
        sweep.sweepInteraction.servoLimit = config.settings.interaction.servoLimit.toFloat()
    }

    override fun keyPressed() {
        remote.processCommand(key)
    }
}