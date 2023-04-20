package korlibs.korge.time

import korlibs.korge.tests.*
import korlibs.time.*
import kotlin.test.*

class FrameBlockTest : ViewsForTesting() {
    @Test
    fun test() = viewsTest {
        assertEquals(2000.milliseconds, views.timeProvider.measure {
            frameBlock(fps = 5.timesPerSecond) {
                for (n in 0 until 10) {
                    frame()
                }
            }
        })
        assertEquals(1000.milliseconds, views.timeProvider.measure {
            frameBlock(fps = 10.timesPerSecond) {
                for (n in 0 until 10) {
                    frame()
                }
            }
        })
        assertEquals(500.milliseconds, views.timeProvider.measure {
            frameBlock(fps = 20.timesPerSecond) {
                for (n in 0 until 10) {
                    frame()
                }
            }
        })
        assertEquals(250.milliseconds, views.timeProvider.measure {
            frameBlock(fps = 40.timesPerSecond) {
                for (n in 0 until 10) {
                    frame()
                }
            }
        })
    }
}
