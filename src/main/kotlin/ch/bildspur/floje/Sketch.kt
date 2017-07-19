package ch.bildspur.floje

import ch.bildspur.floje.controller.*
import ch.bildspur.floje.controller.timer.TimerTask
import ch.bildspur.floje.model.Mirror
import ch.bildspur.floje.model.grid.Grid
import ch.bildspur.floje.util.draw
import ch.bildspur.floje.util.format
import ch.bildspur.floje.view.MirrorVisualiser
import ch.bildspur.floje.view.OscOutput
import ch.bildspur.floje.view.StatusView
import org.opencv.core.Core
import oscP5.OscMessage
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

    val peasy = PeasyController(this)

    val osc = OscController(this)

    val config = ConfigurationController(this)

    val timer = TimerController(this)

    val ping = PingController(this)

    val sweep = SweepController(this)

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

        // update all time relevant methods
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

            oscOutput = OscOutput(osc.osc, grid)
            visualiser = MirrorVisualiser(canvas, grid, sweep)
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

    fun applySettings() {
        sweep.sweepPort = config.settings.sweep.port

        sweep.sweepDataProvider.tracker.sparsing = config.settings.sweep.sparsing
        sweep.sweepDataProvider.tracker.maxDelta = config.settings.sweep.maxDelta

        sweep.innerCone = config.settings.sweep.innerCone
        sweep.outerCone = config.settings.sweep.outerCone
        sweep.minLifeTime = config.settings.sweep.minLifeTime
    }

    override fun keyPressed() {
        when (key) {
            'i' -> isStatusViewShown = !isStatusViewShown
            'u' -> isUIShown = !isUIShown
            'm' -> {

                val xpos = random(45f, 135f).toInt()
                val ypos = random(45f, 135f).toInt()

                // move all to random position
                grid.columns.forEachIndexed { y, fields ->
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
                grid.columns.forEachIndexed { y, fields ->
                    fields.forEachIndexed { x, field ->
                        if (!field.isEmpty()) {
                            val m = field as Mirror
                            println("${m.name}.local")
                        }
                    }
                }
            }
        }

        fun oscEvent(msg: OscMessage) {

            if (msg.addrPattern().contains("/floje/status")) {
                PApplet.print("Startup: ")
                PApplet.print(msg.addrPattern())

                var i = 0
                val version = msg.arguments()[i++] as String
                val reason = msg.arguments()[i++] as Int
                val excause = msg.arguments()[i++] as Int
                val epc1 = msg.arguments()[i++] as Int
                val epc2 = msg.arguments()[i++] as Int
                val epc3 = msg.arguments()[i++] as Int
                val excvaddr = msg.arguments()[i++] as Int
                val depc = msg.arguments()[i++] as Int

                val restartReason = if (reason == 0) "DEFAULT"
                else if (reason == 1) "WDT"
                else if (reason == 2) "EXCEPTION"
                else if (reason == 3) "SOFT_WDT"
                else if (reason == 4) "SOFT_RESTART"
                else if (reason == 5) "DEEP_SLEEP_AWAKE"
                else if (reason == 6) "EXT_SYS_RST" else "???"

                PApplet.print("\tVersion: $version")
                PApplet.print("\tReason: ")
                PApplet.println(restartReason)

                if (reason == 2) {
                    PApplet.print("EXCEPTION:")
                    PApplet.print("\tExcuse: " + excause)
                    PApplet.print("\tEPC1: " + epc1)
                    PApplet.print("\tEPC2: " + epc2)
                    PApplet.print("\tEPC3: " + epc3)
                    PApplet.print("\tEXCVADDR: " + excvaddr)
                    PApplet.println("\tDEPC: " + depc)
                }
            }
        }
    }
}