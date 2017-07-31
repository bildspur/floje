package ch.bildspur.floje.data

import com.google.gson.annotations.Expose

class Sweep {
    @Expose var port = ""
    @Expose var innerCone = 0.0
    @Expose var outerCone = 0.0
    @Expose var sparsing = 0.0
    @Expose var maxDelta = 25.0
    @Expose var minLifeTime = 0
    @Expose var rotation = 0.0
    @Expose var minSignalStrength = 0
    @Expose var minRegionSize = 0
}