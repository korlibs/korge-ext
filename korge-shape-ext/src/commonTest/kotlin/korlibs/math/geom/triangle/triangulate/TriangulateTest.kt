package korlibs.math.geom.triangle.triangulate

import korlibs.io.async.*
import korlibs.io.file.*
import korlibs.io.file.std.*
import korlibs.math.geom.*
import korlibs.math.triangle.poly2tri.*
import korlibs.math.triangle.triangulate.triangulate
import kotlin.test.*

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
    fun testEdgeTriangulation2() {
        val sc = Poly2Tri.SweepContextExt()
        sc.addHole(listOf(MPoint(0, 0), MPoint(0, 100), MPoint(100, 100), MPoint(100, 0)), closed = true)
        sc.addHole(listOf(MPoint(40, 40), MPoint(40, 60), MPoint(60, 40)), closed = false)
        sc.triangulate()
        println(sc.getTriangles())
    }

    @Test
    @Ignore
    fun testEdgeTriangulation3() {
        val sc = Poly2Tri.SweepContextExt()
        sc.addEdge(Point(0, 0), Point(0, 100))
        sc.addEdge(Point(0, 100), Point(100, 100))
        sc.addEdge(Point(100, 100), Point(0, 100))
        sc.addEdge(Point(0, 100), Point(0, 0))
        sc.addEdge(Point(40, 40), Point(40, 60))
        sc.addEdge(Point(40, 60), Point(60, 40))
        sc.triangulate()
        println(sc.getTriangles())
    }

    @Test
    fun testEdgeTriangulation3New() {
        val sweep = Poly2TriNew.Sweep()
        val sc = Poly2TriNew.SweepContextExt()
        sc.addEdge(Point(0, 0), Point(0, 100))
        sc.addEdge(Point(0, 100), Point(100, 100))
        sc.addEdge(Point(100, 100), Point(0, 100))
        sc.addEdge(Point(0, 100), Point(0, 0))
        sc.addEdge(Point(40, 40), Point(40, 60))
        sc.addEdge(Point(40, 60), Point(60, 40))
        sweep.triangulate(sc)
        println(sc.getTriangles())
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

    @Test
    @Ignore
    fun testBed() = suspendTest {
        for (file in resourcesVfs["data"].listSimple()) {
            if (file.extensionLC != "dat") continue
            println("file: $file")
            val points = listPointListFromString(file.readString())
            try {
                val sweep = Poly2Tri.SweepContextExt(points)
                for (tri in sweep.triangulate().getTriangles()) {
                    println("${tri.a}, ${tri.b}, ${tri.c}")
                }
            } catch (e: Throwable) {
                println(points)
                throw e
            }
            //println(sweep.triangulate().getTriangles())
        }
        //println("listPointList=$listPointList")
    }

    fun listPointListFromString(string: String): List<List<MPoint>> {
        val holes = arrayListOf<List<MPoint>>()
        var currentPoints = arrayListOf<MPoint>()

        fun split() {
            if (currentPoints.isNotEmpty()) {
                holes += currentPoints
                currentPoints = arrayListOf()
            }
        }

        for (str in string.split("\n").map { it.trim() }) {
            if (str.isEmpty()) continue

            if (str == "HOLE") {
                split()
            } else {
                val (p1, p2) = str.trim().split(" ")
                currentPoints.add(MPoint(p1.toDouble(), p2.toDouble()))
            }
        }
        split()

        return holes
    }
}
