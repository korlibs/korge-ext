package korlibs.math.triangle

import korlibs.math.geom.shape.Shape2d
import korlibs.math.geom.triangle.area
import korlibs.math.triangle.triangulate.triangulateFlat

val Shape2d.area: Double
    get() = when (this) {
        is Shape2d.Complex -> this.items.sumByDouble { it.area }
        is Shape2d.WithArea -> this.area
        else -> this.triangulateFlat().sumByDouble { it.area }
    }
