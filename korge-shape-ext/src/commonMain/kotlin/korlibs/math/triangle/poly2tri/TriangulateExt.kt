package korlibs.math.triangle.poly2tri

import korlibs.datastructure.IntArrayList
import korlibs.datastructure.iterators.fastForEach
import korlibs.math.geom.PointArrayList
import korlibs.math.geom.shape.*
import korlibs.math.geom.shape.ops.internal.Clipper
import korlibs.math.geom.shape.ops.internal.DefaultClipper
import korlibs.math.geom.shape.ops.internal.executePaths
import korlibs.math.geom.shape.ops.internal.toClipperPaths
import korlibs.math.geom.shape.ops.internal.toPathList
import korlibs.math.geom.triangle.TriangleList
import korlibs.math.geom.vector.VectorPath

fun VectorPath.triangulateSafe(doClipper: Boolean = true): TriangleList {
    val pathList = if (doClipper) {
        val clipper = DefaultClipper()
        val path = this
        clipper.addPaths(path.toClipperPaths(), Clipper.PolyType.SUBJECT, true)
        clipper.executePaths(Clipper.ClipType.UNION).toPathList()
    } else {
        this.toPathPointList()
    }

    val sweep = Poly2Tri.SweepContext()
    sweep.addHoles(pathList)
    sweep.triangulate()
    val triangles = sweep.getTriangles()
    val points = PointArrayList(triangles.size * 3)
    val indices = IntArrayList(triangles.size * 3)
    triangles.fastForEach {
        indices.add(points.size + 0)
        indices.add(points.size + 1)
        indices.add(points.size + 2)
        points.add(it.p0.x, it.p0.y)
        points.add(it.p1.x, it.p1.y)
        points.add(it.p2.x, it.p2.y)
    }
    return TriangleList(points, indices.toShortArray())
}
