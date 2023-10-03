package korlibs.math.triangle

import korlibs.datastructure.*
import korlibs.math.geom.*
import korlibs.math.geom.shape.*
import korlibs.math.geom.triangle.*
import korlibs.math.geom.vector.*
import mapbox.earcut.*

object EarCutTriangulator {
    fun triangulate(points: PointArrayList, holeIndices: IntArray?): TriangleList {
        val doubles = DoubleArray(points.size * 2)
        for (n in 0 until points.size) {
            val p = points[n]
            doubles[n * 2 + 0] = p.x
            doubles[n * 2 + 1] = p.y
        }
        val result = EarCut.earcut(doubles, holeIndices, 2)
        return TriangleList(points, result.toShortArray())
    }
}

fun VectorPath.triangulateEarCut(up: Vector2D = Vector2D.UP): TriangleList = this.toPathPointList().triangulateEarCut(up)

fun List<PointList>.triangulateEarCut(up: Vector2D = Vector2D.UP): TriangleList {
    val allPoints = PointArrayList(this.sumOf { it.size })
    val holeIndices = IntArrayList()
    //var lastClockWise = true
    for (path in this) {
        //val clockWise = path.orientation() != Orientation.COUNTER_CLOCK_WISE
        if (path.orientationWithUp(up) == Orientation.COUNTER_CLOCK_WISE) {
            //if (lastClockWise != clockWise) {
            //lastClockWise = clockWise
            holeIndices.add(allPoints.size)
        }
        allPoints.add(path)
    }
    return EarCutTriangulator.triangulate(allPoints, holeIndices.toIntArray())
}
