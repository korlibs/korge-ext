package korlibs.math.raycasting

import korlibs.datastructure.*
import korlibs.math.geom.*
import kotlin.test.*

class RaycastExtTest {
    @Test
    fun test() {
        fun chunk(pos: PointInt, data: String): StackedIntArray2 =
            StackedIntArray2(IntArray2(data, gen = { c, x, y -> if (c == '.') 0 else 1 }), startX = pos.x, startY = pos.y)

        val fullMap = SparseChunkedStackedIntArray2()
        fullMap.putChunk(chunk(PointInt(0, 0), """
            .......
            .......
            .####..
            .......
        """.trimIndent()))

        val result = fullMap.raycast(Ray.fromTwoPoints(Point(12, 12), Point(120, 80)), Size(8, 8)) { this.getLast(it.x, it.y) != 0 }
        assertEquals(PointInt(18, 16), result?.point?.toInt())
    }
}
