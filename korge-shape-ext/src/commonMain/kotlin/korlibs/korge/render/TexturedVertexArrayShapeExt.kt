package korlibs.korge.render

import korlibs.image.color.*
import korlibs.math.geom.*
import korlibs.math.geom.triangle.*
import korlibs.math.geom.vector.*
import korlibs.math.triangle.poly2tri.*

fun TexturedVertexArray.Companion.fromPath(
    path: VectorPath,
    colorMul: RGBA = Colors.WHITE,
    matrix: Matrix = Matrix.IDENTITY,
    doClipper: Boolean = true
): TexturedVertexArray {
    //return fromTriangles(path.triangulateEarCut(), colorMul, matrix)
    //return fromTriangles(path.triangulatePoly2tri(), colorMul, matrix)
    return TexturedVertexArray.fromTriangles(path.triangulateSafe(doClipper), colorMul, matrix)
}

fun TexturedVertexArray.Companion.fromTriangles(
    triangles: TriangleList,
    colorMul: RGBA = Colors.WHITE,
    matrix: Matrix = Matrix.IDENTITY
): TexturedVertexArray {
    val tva = TexturedVertexArray(triangles.pointCount, triangles.indices, triangles.numIndices)
    tva.setSimplePoints(triangles.points, matrix, colorMul)
    return tva
}

