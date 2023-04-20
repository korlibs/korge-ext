package korlibs.korge.html

import korlibs.image.color.Colors
import korlibs.image.font.SystemFont
import korlibs.image.text.TextAlignment
import korlibs.math.geom.MRectangle
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals

class HtmlTest {
	@Test
	fun name() {
		val doc = Html.parse(
            """<p align="center"><font face="Times New Roman" size="33" color="#ffffff" letterSpacing="0.00" kerning="1">50%</font></p>""",
            Html.FontsCatalog(null, EmptyCoroutineContext)
        )
		assertEquals("50%", doc.text)
		doc.doPositioning(Html.MetricsProvider.Identity, MRectangle(0, 0, 100, 100))
		assertEquals(MRectangle(48.5, 0.0, 3.0, 1.0), doc.bounds)
		assertEquals(listOf(MRectangle(48.5, 0.0, 3.0, 1.0)), doc.allSpans.map { it.bounds })
		val format = doc.firstFormat
		assertEquals(SystemFont("Times New Roman", EmptyCoroutineContext), format.computedFace)
		assertEquals(33, format.computedSize)
		assertEquals(Colors.WHITE, format.computedColor)
		assertEquals(1, format.computedKerning)
		assertEquals(0.0, format.computedLetterSpacing)
		assertEquals(TextAlignment.CENTER, format.computedAlign)
	}
}
