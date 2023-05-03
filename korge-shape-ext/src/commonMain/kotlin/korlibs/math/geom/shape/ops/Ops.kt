package korlibs.math.geom.shape.ops

import korlibs.math.geom.shape.*
import korlibs.math.geom.shape.ops.internal.*
import korlibs.math.geom.vector.*

infix fun Shape2D.collidesWith(other: Shape2D): Boolean =
    this.clipperOp(other, Clipper.ClipType.INTERSECTION) != EmptyShape2D

infix fun Shape2D.intersection(other: Shape2D): Shape2D = this.clipperOp(other, Clipper.ClipType.INTERSECTION)
infix fun Shape2D.union(other: Shape2D): Shape2D = this.clipperOp(other, Clipper.ClipType.UNION)
infix fun Shape2D.xor(other: Shape2D): Shape2D = this.clipperOp(other, Clipper.ClipType.XOR)
infix fun Shape2D.difference(other: Shape2D): Shape2D = this.clipperOp(other, Clipper.ClipType.DIFFERENCE)

fun Shape2D.extend(size: Double, cap: LineCap = LineCap.ROUND): Shape2D {
    val clipper = ClipperOffset()
    val solution = Paths()
    clipper.addPaths(
        this.paths.toClipperPaths(), Clipper.JoinType.MITER,
        if (this.closed) Clipper.EndType.CLOSED_POLYGON else cap.toClipper()
    )
    clipper.execute(solution, size)
    return solution.toShape2d()
}

fun Shape2D.extendLine(size: Double, join: LineJoin = LineJoin.BEVEL, cap: LineCap = LineCap.SQUARE): Shape2D {
    val clipper = ClipperOffset()
    val solution = Paths()
    clipper.addPaths(
        this.paths.toClipperPaths(), join.toClipper(),
        if (this.closed) Clipper.EndType.CLOSED_LINE else cap.toClipper()
    )
    clipper.execute(solution, size)
    return solution.toShape2d()
}
