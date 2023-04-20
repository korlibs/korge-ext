package com.soywiz.korge.view.ktree

import korlibs.korge.tests.*
import korlibs.korge.view.*
import korlibs.io.file.std.*
import korlibs.korge.view.Ellipse
import korlibs.korge.view.ktree.*
import korlibs.math.geom.*
import kotlin.test.*

class KTreeTest : ViewsForTesting() {
    @Test
    fun test() = viewsTest {
        val ktree = resourcesVfs["restitution.ktree"].readKTree()
        assertIs<Container>(ktree)
        val child = ktree.children[1]
        assertIs<Ellipse>(child)
        assertEquals(Anchor.CENTER, child.anchor)
    }
}
