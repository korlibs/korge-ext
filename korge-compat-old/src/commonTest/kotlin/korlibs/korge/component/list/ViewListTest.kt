package korlibs.korge.component.list

import korlibs.korge.tests.ViewsForTesting
import korlibs.korge.view.SolidRect
import korlibs.korge.view.xy
import korlibs.image.color.Colors
import korlibs.math.geom.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ViewListTest : ViewsForTesting() {
	@Test
	fun createList() {
		val item0 = SolidRect(10, 10, Colors.RED).apply { xy(0, 0) }
		val item1 = SolidRect(10, 10, Colors.RED).apply { xy(20, 0) }
		views.stage.addChild(item0)
		views.stage.addChild(item1)
		val itemList = ViewList(item0, item1, 3)
		assertEquals(3, itemList.length)
		assertEquals(Rectangle(40, 0, 10, 10), itemList[2]?.globalBounds)
	}
}
