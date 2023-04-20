package korlibs.math.length

import korlibs.image.color.*
import korlibs.korge.component.length.*
import korlibs.korge.testing.*
import korlibs.korge.time.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import kotlin.test.*

class BindLengthComponentJvmTest {
    @Test
    @Ignore("This is not working. We should fix this") // @TODO: Fixme!
    fun testPercent() = korgeScreenshotTest(Size(512, 512)) {
        val container = fixedSizeContainer(Size(300.0, 500.0))
        container.solidRect(container.width, container.height, Colors.BLUE)
        val rect = container.solidRect(100, 100)
        rect.bindLength(View::x) { 50.percent }
        rect.bindLength(View::y) { 50.percent }
        assertEquals(0f, rect.x)
        assertEquals(0f, rect.y)
        assertScreenshot()
        delayFrame()
        assertScreenshot()
        assertEquals(150f, rect.x)
        assertEquals(250f, rect.y)
    }
}
