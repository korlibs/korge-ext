package korlibs.korge.component.docking

import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*

fun <T : View> T.dockedTo(anchor: Anchor, scaleMode: ScaleMode = ScaleMode.NO_SCALE, offset: MPoint = MPoint(), hook: (View) -> Unit = {}): T {
    val view = this

    val initialViewSize = Size(view.width, view.height)
    var actualVirtualSize = Size(0, 0)

    view.onStageResized(firstTrigger = true) { width, height ->
        //println(views.actualVirtualWidth)
        view.position(
            anchor.ratioX.interpolate(views.virtualLeft, views.virtualRight) + offset.x,
            anchor.ratioY.interpolate(views.virtualTop, views.virtualBottom) + offset.y,
            //views.actualVirtualBounds.getAnchoredPosition(anchor) + offset
        )
        // @TODO: This is not working? why?
        //view.alignX(views.stage, anchor.sx, true)
        //view.alignY(views.stage, anchor.sy, true)
        if (scaleMode != ScaleMode.NO_SCALE) {
            actualVirtualSize = Size(views.actualVirtualWidth, views.actualVirtualHeight)
            val size = scaleMode(initialViewSize, actualVirtualSize)
            view.size(size)
        }
        view.invalidate()
        view.parent?.invalidate()
        hook(view)

    }
    return this
}
