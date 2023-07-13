package korlibs.math.geom.triangle.triangulate

import korlibs.math.geom.*
import korlibs.math.triangle.poly2tri.*
import korlibs.math.triangle.triangulate.triangulate
import kotlin.test.Test
import kotlin.test.assertEquals

class TriangulateTest {
    //@Test
    //fun test() {
    //    assertEquals(
    //        "[Triangle((0, 100), (100, 0), (100, 100)), Triangle((0, 100), (0, 0), (100, 0)), Triangle((300, 100), (400, 0), (400, 100)), Triangle((300, 100), (300, 0), (400, 0))]",
    //        VectorPath {
    //            rect(0, 0, 100, 100)
    //            rect(300, 0, 100, 100)
    //        }.triangulate().toString()
    //    )
    //}

    @Test
    fun testEdgeTriangulation() {
        fun getTriangles(addInnerConstrainedEdge: Boolean): List<Poly2Tri.Triangle> {
            val sc = Poly2Tri.SweepContextExt()
            sc.addEdge(Point(0, 0), Point(100, 0))
            sc.addEdge(Point(100, 0), Point(100, 100))
            sc.addEdge(Point(100, 100), Point(0, 100))
            sc.addEdge(Point(0, 100), Point(0, 0))
            if (addInnerConstrainedEdge) sc.addEdge(Point(40, 40), Point(40, 60))
            sc.triangulate()
            return sc.getTriangles()
        }
        assertEquals(2, getTriangles(addInnerConstrainedEdge = false).size)
        assertEquals(6, getTriangles(addInnerConstrainedEdge = true).size)
    }

    @Test
    fun test2() {
        val points = listOf(
            MPoint(3, 10),
            MPoint(1, 5),
            MPoint(3, 1),
            MPoint(4, 0),
            MPoint(6, 0)
        )

        assertEquals(
            "[Triangle((3, 10), (1, 5), (6, 0)), Triangle((3, 1), (6, 0), (1, 5)), Triangle((6, 0), (3, 1), (4, 0))]",
            points.triangulate().toString()
        )
    }
}
