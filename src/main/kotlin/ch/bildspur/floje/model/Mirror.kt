package ch.bildspur.floje.model

import ch.bildspur.floje.model.grid.GridField

/**
 * Created by cansik on 08.06.17.
 */
class Mirror(var name: String) : GridField() {
    var xAxis = DataModel(90)
    var yAxis = DataModel(90)

}