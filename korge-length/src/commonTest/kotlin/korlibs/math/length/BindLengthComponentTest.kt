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
        assertEquals(0.0, rect.x)
        assertEquals(0.0, rect.y)
        delayFrame()
        assertEquals(150.0, rect.x)
        assertEquals(250.0, rect.y)
        rect.x = 0.0
        rect.y = 0.0
        assertEquals(0.0, rect.x)
        assertEquals(0.0, rect.y)
        delayFrame() // The length component will override the previously set values
        assertEquals(150.0, rect.x)
        assertEquals(250.0, rect.y)
        rect.lengths {
            x = null
            y = null
        }
        delayFrame()
        assertEquals(150.0, rect.x)
        assertEquals(250.0, rect.y)
        rect.x = 0.0
        rect.y = 0.0
        delayFrame() // The length updater component shouldn't exist anymore, so the property changes are kept
        assertEquals(0.0, rect.x)
        assertEquals(0.0, rect.y)
    }
}
