package korlibs.math.triangle.pathfind

import korlibs.math.geom.*
import korlibs.math.geom.shape.Shape2d
import korlibs.math.geom.triangle.Triangle
import korlibs.math.geom.vector.VectorPath
import korlibs.math.triangle.poly2tri.triangulateSafe
import korlibs.math.triangle.triangulate.triangulateFlat

fun Iterable<Triangle>.toSpatialMesh(): SpatialMesh = SpatialMesh(this)
fun Iterable<Triangle>.pathFind(): SpatialMeshFind = SpatialMeshFind(this.toSpatialMesh())

fun SpatialMeshFind.funnel(p0: Point, p1: Point): PointList = find(p0, p1)
fun SpatialMeshFind.funnel(p0: MPoint, p1: MPoint): PointList = find(p0.point, p1.point)

fun Iterable<Triangle>.funnel(p0: MPoint, p1: MPoint): PointList = this.pathFind().funnel(p0, p1)
fun Iterable<Triangle>.pathFind(p0: MPoint, p1: MPoint): PointList = this.pathFind().funnel(p0, p1)

fun Shape2d.toSpatialMesh(): SpatialMesh = SpatialMesh(this.triangulateFlat())
fun Shape2d.pathFind(): SpatialMeshFind = this.triangulateFlat().pathFind()
fun Shape2d.pathFind(p0: MPoint, p1: MPoint): PointList = this.triangulateFlat().pathFind().funnel(p0, p1)

fun VectorPath.toSpatialMesh(): SpatialMesh = SpatialMesh(this.triangulateSafe())
fun VectorPath.pathFind(): SpatialMeshFind = this.triangulateSafe().pathFind()
fun VectorPath.pathFind(p0: MPoint, p1: MPoint): PointList = this.triangulateSafe().pathFind().funnel(p0, p1)
