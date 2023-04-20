package korlibs.korge.view.qview

import korlibs.datastructure.iterators.fastForEach
import korlibs.time.*
import korlibs.korge.input.EventsDslMarker
import korlibs.korge.input.MouseEvents
import korlibs.korge.input.onClick
import korlibs.korge.tween.*
import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.io.async.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import kotlin.reflect.KMutableProperty1

class QView(val views: List<View>) : List<View> by views, BView {
    val firstOrNull: View? = views.firstOrNull()
    val first: View by lazy { firstOrNull ?: DummyView() }
    override val bview: View get() = first
    override val bviewAll: List<View> get() = views

    constructor(view: View?) : this(if (view != null) listOf(view) else emptyList())

    operator fun get(name: String): QView = QView(views.mapNotNull { it.firstDescendantWith { it.name == name } })

    fun position(): Point = first.pos

    fun <T> setProperty(prop: KMutableProperty1<View, T>, value: T) {
        views.fastForEach { prop.set(it, value) }
    }

    inline fun fastForEach(callback: (View) -> Unit) {
        views.fastForEach { callback(it) }
    }

    var visible: Boolean
        get() = firstOrNull?.visible ?: false
        set(value) = fastForEach { it.visible = value }

    var alpha: Float
        get() = firstOrNull?.alpha ?: 1f
        set(value) = fastForEach { it.alpha = value }

    var scaleAvg: Float
        get() = firstOrNull?.scaleAvg ?: 1f
        set(value) = fastForEach { it.scaleAvg = value }

    var scaleX: Float
        get() = firstOrNull?.scaleX ?: 1f
        set(value) = fastForEach { it.scaleX = value }

    var scaleY: Float
        get() = firstOrNull?.scaleY ?: 1f
        set(value) = fastForEach { it.scaleY = value }

    var x: Float
        get() = firstOrNull?.x ?: 0f
        set(value) = fastForEach { it.x = value }

    var y: Float
        get() = firstOrNull?.y ?: 0f
        set(value) = fastForEach { it.y = value }

    var rotation: Angle
        get() = firstOrNull?.rotation ?: 0.degrees
        set(value) = fastForEach { it.rotation = value }

    var skewX: Angle
        get() = firstOrNull?.skewX ?: 0.degrees
        set(value) = fastForEach { it.skewX = value }

    var skewY: Angle
        get() = firstOrNull?.skewY ?: 0.degrees
        set(value) = fastForEach { it.skewY = value }

    var colorMul: RGBA
        get() = firstOrNull?.colorMul ?: Colors.WHITE
        set(value) = fastForEach { it.colorMul = value }
}

fun QView.visible(value: Boolean) { visible = value }
fun QView.alpha(value: Float) { alpha = value }
fun QView.onClick(handler: @EventsDslMarker suspend (MouseEvents) -> Unit) = fastForEach { it.onClick(handler) }
inline fun <reified T : View> QView.castTo(): T? = firstOrNull as? T?

/** Indexer that allows to get a descendant marked with the name [name]. */
operator fun View?.get(name: String): QView = QView(this)[name]

@PublishedApi
internal val DEFAULT_EASING = Easing.EASE_IN_OUT_QUAD

@PublishedApi
internal val DEFAULT_TIME = 1.seconds

suspend fun QView.tween(
    vararg vs: V2<*>,
    time: TimeSpan = DEFAULT_TIME,
    easing: Easing = DEFAULT_EASING,
    waitTime: TimeSpan = TimeSpan.NIL,
    callback: (Float) -> Unit = { }
) {
    if (isEmpty()) {
        // @TODO: Do this?
        delay(time)
    } else {
        fastForEach {
            it.tween(*vs, time = time, easing = easing, waitTime = waitTime, callback = callback)
        }
    }
}
