package korlibs.image.triangulate

import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.math.geom.*
import korlibs.math.geom.shape.*
import korlibs.math.geom.triangle.*
import korlibs.math.geom.vector.*
import korlibs.math.triangle.poly2tri.*
import kotlin.test.*

class TriangulateTest {
    @Test
    fun testTriangulateSafe() {
        buildVectorPath(VectorPath()) {
            rect(0, 0, 100, 100)
            rect(20, 20, 60, 60)
        }.also { path ->
            val triangles = path.triangulateSafe()
            //suspendTest { outputTriangles(triangles) }
            assertEquals(8, triangles.size)
            assertEquals(
                "[Triangle((0, 100), (20, 80), (100, 100)), Triangle((0, 100), (0, 0), (20, 80)), Triangle((0, 0), (20, 20), (20, 80)), Triangle((20, 20), (0, 0), (100, 0)), Triangle((80, 20), (20, 20), (100, 0)), Triangle((80, 80), (80, 20), (100, 0)), Triangle((80, 80), (100, 0), (100, 100)), Triangle((20, 80), (80, 80), (100, 100))]",
                triangles.getTriangles().toString()
            )
        }

        buildVectorPath(VectorPath()) {
            rect(0, 0, 100, 100)
            rect(20, 20, 120, 60)
        }.also { path ->
            val triangles = path.triangulateSafe()
            //suspendTest { outputTriangles(triangles) }
            assertEquals(10, triangles.size)
            assertEquals(
                "[Triangle((0, 100), (20, 80), (100, 100)), Triangle((0, 100), (0, 0), (20, 80)), Triangle((0, 0), (20, 20), (20, 80)), Triangle((20, 20), (0, 0), (100, 0)), Triangle((20, 20), (100, 0), (100, 20)), Triangle((20, 80), (100, 80), (100, 100)), Triangle((20, 80), (100, 80), (100, 80)), Triangle((100, 80), (100, 80), (140, 20)), Triangle((100, 80), (140, 20), (140, 80)), Triangle((100, 80), (100, 20), (140, 20))]",
                triangles.getTriangles().toString()
            )
        }
    }

    @Suppress("unused")
    private suspend fun outputTriangles(triangles: TriangleList) {
        val image = NativeImage(512, 512).context2d {
            for (triangle in triangles.getTriangles()) {
                fillStroke(Colors.RED, Colors.BLUE) {
                    triangle(triangle.p0, triangle.p1, triangle.p2)
                }
            }
            //path(out.toVectorPath())
        }
        image.writeTo(localCurrentDirVfs["demo.png"], PNG)
    }
}

fun VectorBuilder.triangle(x1: Double, y1: Double, x2: Double, y2: Double, x3: Double, y3: Double) {
    //this.rect()
    this.moveTo(Point(x1, y1))
    this.lineTo(Point(x2, y2))
    this.lineTo(Point(x3, y3))
    this.lineTo(Point(x1, y1))
    //this.close() // @TODO: Is this a Bug? But still we have to handle strange cases like this one to be consistent with other rasterizers.
    //println("TRIANGLE: ($x1,$y1)-($x2,$y2)-($x3,$y3)")
}

fun VectorBuilder.triangle(p1: MPoint, p2: MPoint, p3: MPoint) = triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
fun VectorBuilder.triangle(p1: Point, p2: Point, p3: Point) = triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
