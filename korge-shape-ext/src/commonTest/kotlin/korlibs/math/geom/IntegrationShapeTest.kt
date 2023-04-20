package korlibs.math.geom

import korlibs.math.geom.shape.*
import korlibs.math.geom.shape.ops.*
import korlibs.math.geom.shape.ops.internal.*
import korlibs.math.geom.vector.*
import korlibs.math.triangle.*
import korlibs.math.triangle.pathfind.*
import korlibs.math.triangle.triangulate.*
import kotlin.math.*
import kotlin.test.*

class IntegrationShapeTest {
    @Test
    @Ignore // @TODO: Fixme!
    fun vectorPathToShape2d() {
        val exactArea = Circle(Point(0, 0), 100f).area
        val vp = VectorPath().apply { circle(Point(0, 0), 100f) }
        val shape = vp.toShape2d()
        assertEquals(true, shape.closed)
        assertTrue(abs(exactArea - shape.area) / exactArea < 0.01)
        assertEquals(81, shape.paths.totalVertices)
    }

    @Test
    @Ignore // @TODO: Fixme!
    fun triangulate() {
        val shape = Rectangle(0, 0, 100, 100)
        //println(shape)
        //println(shape.getAllPoints())
        //println(shape.getAllPoints().toPoints())
        assertEquals(
            "[Triangle((0, 100), (100, 0), (100, 100)), Triangle((0, 100), (0, 0), (100, 0))]",
            shape.triangulateFlat().toString()
        )
    }

    @Test
    @Ignore // @TODO: Fixme!
    fun pathFind() {
        assertEquals(
            "[(10, 10), (90, 90)]",
            Rectangle(0, 0, 100, 100).pathFind(MPoint(10, 10), MPoint(90, 90)).toString()
        )
        assertEquals(
            "[(10, 10), (100, 50), (120, 52)]",
            (Rectangle(0, 0, 100, 100) union Rectangle(100, 50, 50, 50)).pathFind(
                MPoint(10, 10),
                MPoint(120, 52)
            ).toString()
        )
    }
}
