package com.soywiz.korge.render

import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korma.geom.MMatrix
import com.soywiz.korma.geom.triangle.TriangleList
import com.soywiz.korma.geom.vector.VectorPath
import com.soywiz.korma.triangle.poly2tri.triangulateSafe

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

