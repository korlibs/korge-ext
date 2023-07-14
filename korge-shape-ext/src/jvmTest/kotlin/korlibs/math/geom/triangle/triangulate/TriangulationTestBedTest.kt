package korlibs.math.geom.triangle.triangulate

import korlibs.io.async.*
import korlibs.io.file.*
import korlibs.io.file.std.*
import korlibs.math.geom.*
import korlibs.math.triangle.poly2tri.*
import kotlinx.coroutines.*
import org.junit.*
import org.junit.runner.*
import org.junit.runners.*
import java.io.*


@RunWith(org.junit.runners.Parameterized::class)
class TriangulationTestBedTest(val name: String, val file: VfsFile) {

    @Test
    fun testTriangulation() = suspendTest {
        val pointsList = listPointListFromString(file.readString())
        val sweep = Poly2TriNew.Sweep()
        val sweepContext = Poly2TriNew.SweepContext()
        for (points in pointsList) {
            sweepContext.addHole(points.points.map { Poly2TriNew.Point(it.x, it.y) })
        }
        sweep.triangulate(sweepContext)
        val tris = sweepContext.getTriangles()
        //val sweep = Poly2Tri.SweepContextExt(points)
        //val tris = sweep.triangulate().getTriangles()
        for (tri in tris) {
        }
    }

    companion object {
        @Parameterized.Parameters(name = "{0}")
        @JvmStatic
        fun data(): Collection<Array<Any>> {
            return runBlocking {
                resourcesVfs["data"]
                    .listSimple()
                    .filter { it.extensionLC == "dat" }
                    .filter { it.baseName != "sketchup.dat" }
                    .map {
                        arrayOf(it.baseName, it)
                    }
            }
        }
    }
}
