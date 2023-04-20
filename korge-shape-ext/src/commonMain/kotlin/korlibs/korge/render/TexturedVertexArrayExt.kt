package korlibs.korge.render

import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.math.geom.MMatrix
import korlibs.math.geom.triangle.TriangleList
import korlibs.math.geom.vector.VectorPath
import korlibs.math.triangle.poly2tri.triangulateSafe

fun TexturedVertexArray.fromPath(path: VectorPath, colorMul: RGBA = Colors.WHITE, matrix: MMatrix? = null, doClipper: Boolean = true): TexturedVertexArray {
    //return fromTriangles(path.triangulateEarCut(), colorMul, matrix)
    //return fromTriangles(path.triangulatePoly2tri(), colorMul, matrix)
    return TexturedVertexArray.fromTriangles(path.triangulateSafe(doClipper), colorMul, matrix)
}

fun TexturedVertexArray.fromTriangles(triangles: TriangleList, colorMul: RGBA = Colors.WHITE, matrix: MMatrix? = null): TexturedVertexArray {
    val tva = TexturedVertexArray(triangles.pointCount, triangles.indices, triangles.numIndices)
    tva.setSimplePoints(triangles.points, matrix, colorMul)
    return tva
}

