package com.soywiz.korge.view.qview

import com.soywiz.kds.iterators.fastForEach
import com.soywiz.klock.*
import com.soywiz.korge.input.EventsDslMarker
import com.soywiz.korge.input.MouseEvents
import com.soywiz.korge.input.onClick
import com.soywiz.korge.tween.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korio.async.*
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.IPoint
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.*
import kotlin.reflect.KMutableProperty1

class QView(val views: List<View>) : List<View> by views, BView {
    val firstOrNull: View? = views.firstOrNull()
    val first: View by lazy { firstOrNull ?: DummyView() }
    override val bview: View get() = first
    override val bviewAll: List<View> get() = views

    constructor(view: View?) : this(if (view != null) listOf(view) else emptyList())

    operator fun get(name: String): QView = QView(views.mapNotNull { it.firstDescendantWith { it.name == name } })

    fun position(): IPoint = first.ipos

    fun <T> setProperty(prop: KMutableProperty1<View, T>, value: T) {
        views.fastForEach { prop.set(it, value) }
    }

    inline fun fastForEach(callback: (View) -> Unit) {
        views.fastForEach { callback(it) }
    }

    var visible: Boolean
        get() = firstOrNull?.visible ?: false
        set(value) = fastForEach { it.visible = value }

    var alpha: Double
        get() = firstOrNull?.alpha ?: 1.0
        set(value) = fastForEach { it.alpha = value }

    var scale: Double
        get() = firstOrNull?.scale ?: 1.0
        set(value) = fastForEach { it.scale = value }

    var scaleX: Double
        get() = firstOrNull?.scaleX ?: 1.0
        set(value) = fastForEach { it.scaleX = value }

    var scaleY: Double
        get() = firstOrNull?.scaleY ?: 1.0
        set(value) = fastForEach { it.scaleY = value }

    var x: Double
        get() = firstOrNull?.x ?: 0.0
        set(value) = fastForEach { it.x = value }

    var y: Double
        get() = firstOrNull?.y ?: 0.0
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
fun QView.alpha(value: Double) { alpha = value }
fun QView.onClick(handler: @EventsDslMarker suspend (MouseEvents) -> Unit) = fastForEach { it.onClick(handler) }
inline fun <reified T : View> QView.castTo(): T? = firstOrNull as? T?

/** Indexer that allows to get a descendant marked with the name [name]. */
operator fun View?.get(name: String): QView = QView(this)[name]

suspend fun QView.tween(
    vararg vs: V2<*>,
    time: TimeSpan = DEFAULT_TIME,
    easing: Easing = DEFAULT_EASING,
    waitTime: TimeSpan = TimeSpan.NIL,
    callback: (Double) -> Unit = { }
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
