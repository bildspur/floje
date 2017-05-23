package ch.bildspur.floje.controller

import codeanticode.syphon.SyphonServer
import processing.core.PApplet
import processing.core.PImage

/**
 * Created by cansik on 04.02.17.
 */
class SyphonController(internal var sketch: PApplet) {

    internal var syphon: SyphonServer? = null

    fun setupSyphon(name: String) {
        syphon = SyphonServer(sketch, name)
    }

    fun sendScreen()
    {
        sketch.g.endRaw()
        sendImageToSyphon(sketch.g)
        sketch.g.beginDraw()
    }

    fun sendImageToSyphon(p: PImage) {
        syphon!!.sendImage(p)
    }
}