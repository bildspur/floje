package ch.bildspur.floje.controller

import ch.bildspur.floje.Sketch
import ch.bildspur.floje.simulation.OscSimulation
import ch.bildspur.floje.util.toFloat
import netP5.NetAddress
import oscP5.OscMessage
import oscP5.OscP5
import processing.core.PApplet
import java.io.IOException
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import kotlin.concurrent.thread


/**
 * Created by cansik on 09.07.17.
 */
class OscController(internal var sketch: Sketch) {
    companion object {
        @JvmStatic val INCOMING_PORT = 9000
        @JvmStatic val OUTGOING_PORT = 8000
        @JvmStatic val VEZER_PORT = 1234
    }

    @Volatile
    var isSetup = false

    lateinit var apps: NetAddress
    lateinit var vezer: NetAddress
    lateinit var jmdns: JmDNS

    lateinit var osc: OscP5

    val simulator = OscSimulation(sketch)

    fun setup() {
        osc = OscP5(this, INCOMING_PORT)
        apps = NetAddress("255.255.255.255", OUTGOING_PORT)
        vezer = NetAddress("127.0.0.1", VEZER_PORT)

        thread {
            setupZeroConf()
        }

        isSetup = true
    }

    fun stop() {
        jmdns.unregisterAllServices()
    }

    private fun setupZeroConf() {
        try {
            println("setting up zero conf...")
            val address = InetAddress.getLocalHost()
            jmdns = JmDNS.create(address)
            jmdns.registerService(ServiceInfo.create("_osc._udp.", Sketch.NAME, INCOMING_PORT, ""))
            println("zero conf running!")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun oscEvent(msg: OscMessage) {
        if (msg.addrPattern().startsWith("/floje/show"))
            simulator.simulate(msg)

        checkIfIsStatusMessage(msg)

        when (msg.addrPattern()) {
            "/floje/remote/interaction" -> {
                sketch.remote.processCommand('x')
            }
            "/floje/remote/random" -> {
                sketch.remote.processCommand('m')
            }
            "/floje/remote/position/initial" -> {
                sketch.remote.processCommand('1')
            }
            "/floje/remote/position/rain" -> {
                sketch.remote.processCommand('2')
            }
            "/floje/remote/position/access" -> {
                sketch.remote.processCommand('3')
            }
            "/floje/remote/show/play" -> {
                sendMessage(vezer, "/vezer/mainshow/play", 1.0f)
            }
            "/floje/remote/show/pause" -> {
                sendMessage(vezer, "/vezer/mainshow/play", 0.0f)
            }
            "/floje/remote/show/rewind" -> {
                sendMessage(vezer, "/vezer/mainshow/rewind", 1.0f)
            }
        }

        updateOSCApp()
    }

    fun updateOSCApp() {
        sendMessage("/floje/remote/interaction", sketch.isInteractionOn.value.toFloat())
    }

    fun sendMessage(ip: NetAddress, address: String, value: Float) {
        val m = OscMessage(address)
        m.add(value)
        osc.send(m, ip)
    }

    fun sendMessage(address: String, value: Float) {
        val m = OscMessage(address)
        m.add(value)
        osc.send(m, apps)
    }

    fun sendMessage(address: String, value: String) {
        val m = OscMessage(address)
        m.add(value)
        osc.send(m, apps)
    }

    internal fun checkIfIsStatusMessage(msg: OscMessage) {
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