package ch.bildspur.floje.controller

import ch.bildspur.floje.data.Settings
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import processing.core.PApplet
import java.nio.file.Files
import java.nio.file.Paths


/**
 * Created by cansik on 11.07.17.
 */
class ConfigurationController(internal var sketch: PApplet) {
    companion object {
        @JvmStatic val CONFIGURATION_FILE = "floe.json"
    }

    lateinit var gson: Gson

    lateinit var settings: Settings

    fun setup() {
        gson = Gson()
        settings = Settings()
    }

    fun loadConfiguration() {
        val content = String(Files.readAllBytes(Paths.get(sketch.dataPath(CONFIGURATION_FILE))))
        settings = gson.fromJson<Settings>(content)
    }

    fun saveConfiguration() {
        val content = gson.toJson(settings)
        Files.write(Paths.get(sketch.dataPath(CONFIGURATION_FILE)), content.toByteArray())
    }
}