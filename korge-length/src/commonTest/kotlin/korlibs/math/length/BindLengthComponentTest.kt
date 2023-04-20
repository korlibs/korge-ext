package korlibs.math.length

import korlibs.korge.annotations.*
import korlibs.korge.component.length.*
import korlibs.korge.tests.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import kotlin.test.*

@KorgeExperimental
class BindLengthComponentTest : ViewsForTesting(log = true) {
    @Test
    fun testPercent2() = viewsTest {
        val container = fixedSizeContainer(Size(300.0, 500.0))
        val rect = container.solidRect(100, 100)
        rect.lengths {
            x = 50.percent
            y = 50.percent
        }
        assertEquals(0f, rect.x)
        assertEquals(0f, rect.y)
        delayFrame()
        assertEquals(150f, rect.x)
        assertEquals(250f, rect.y)
        rect.x = 0f
        rect.y = 0f
        assertEquals(0f, rect.x)
        assertEquals(0f, rect.y)
        delayFrame() // The length component will override the previously set values
        assertEquals(150f, rect.x)
        assertEquals(250f, rect.y)
        rect.lengths {
            x = null
            y = null
        }
        delayFrame()
        assertEquals(150f, rect.x)
        assertEquals(250f, rect.y)
        rect.x = 0f
        rect.y = 0f
        delayFrame() // The length updater component shouldn't exist anymore, so the property changes are kept
        assertEquals(0f, rect.x)
        assertEquals(0f, rect.y)
    }
}
