package korlibs.math.triangle.triangulate

import korlibs.math.geom.*
import korlibs.math.geom.shape.*
import korlibs.math.geom.shape.ops.internal.*
import korlibs.math.geom.triangle.Triangle
import korlibs.math.geom.vector.VectorPath
import korlibs.math.triangle.poly2tri.Poly2Tri
import kotlin.jvm.JvmName

@Deprecated("")
fun List<MPoint>.triangulate(): List<Triangle> = listOf(PointArrayList(this)).triangulate()
fun PointList.triangulate(): List<Triangle> = listOf(this).triangulate()

@JvmName("triangulateListPointArrayList")
fun List<PointList>.triangulate(): List<Triangle> {
    val sc = Poly2Tri.SweepContext()
    sc.addHoles(this)
    sc.triangulate()
    return sc.getTriangles().toList()
}

fun Shape2D.triangulate(): List<List<Triangle>> = this.paths.map { it.toPoints().triangulate() }
fun Shape2D.triangulateFlat(): List<Triangle> = triangulate().flatMap { it }

//fun VectorPath.triangulate(): List<List<Triangle>> = this.toPathList().triangulate()
fun VectorPath.triangulate(): List<Triangle> = this.toPathPointList().triangulate()
