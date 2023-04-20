package com.soywiz.korge.view

import com.soywiz.korge.tests.*
import korlibs.math.geom.*
import korlibs.korge.view.TextOld
import kotlin.test.*

class TextViewsJvmTest : ViewsForTesting(log = true) {
    @Test
    fun textGetBounds1() = viewsTest {
        val font = views.debugBmpFont
        assertEquals(MRectangle(0, 0, 77, 8), TextOld("Hello World", font = font, textSize = 8.0).globalBounds)
    }

    @Test
    fun textGetBounds2() = viewsTest {
        val font = views.debugBmpFont
        assertEquals(MRectangle(0, 0, 154, 16), TextOld("Hello World", font = font, textSize = 16.0).globalBounds)
    }
}
