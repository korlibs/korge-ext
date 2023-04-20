package korlibs.korge.render

import korlibs.image.color.RGBA
import korlibs.math.geom.triangle.TriangleList
import korlibs.math.geom.vector.VectorPath

fun RenderContext2D.pathFast(path: VectorPath, color: RGBA = this.multiplyColor, filtering: Boolean = this.filtering) {
    texturedVertexArrayNoTransform(TexturedVertexArray.fromPath(path, color, matrix = m, doClipper = false), filtering)
}

fun RenderContext2D.path(path: VectorPath, color: RGBA = this.multiplyColor, filtering: Boolean = this.filtering, doClipper: Boolean = true) {
    texturedVertexArrayNoTransform(TexturedVertexArray.fromPath(path, color, matrix = m, doClipper = doClipper), filtering)
}

fun RenderContext2D.triangles(triangles: TriangleList, color: RGBA = this.multiplyColor, filtering: Boolean = this.filtering) {
    texturedVertexArrayNoTransform(TexturedVertexArray.fromTriangles(triangles, color, matrix = m), filtering)
}

