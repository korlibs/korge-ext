package korlibs.korge.view.tween

import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*

@PublishedApi
internal val DEFAULT_EASING = Easing.EASE_IN_OUT_QUAD

@PublishedApi
internal val DEFAULT_TIME = 1.seconds

@Deprecated("Use animator instead")
suspend fun View.show(time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) =
    tween(this::alpha[1.0], time = time, easing = easing) { this.visible = true }

@Deprecated("Use animator instead")
suspend fun View.hide(time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) =
    tween(this::alpha[0.0], time = time, easing = easing)

@Deprecated("Use animator instead")
suspend inline fun View.moveTo(x: Double, y: Double, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::x[x], this::y[y], time = time, easing = easing)
@Deprecated("Use animator instead")
suspend inline fun View.moveTo(x: Float, y: Float, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::x[x], this::y[y], time = time, easing = easing)
@Deprecated("Use animator instead")
suspend inline fun View.moveTo(x: Int, y: Int, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::x[x], this::y[y], time = time, easing = easing)

@Deprecated("Use animator instead")
suspend inline fun View.moveBy(dx: Double, dy: Double, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::x[this.x + dx], this::y[this.y + dy], time = time, easing = easing)
@Deprecated("Use animator instead")
suspend inline fun View.moveBy(dx: Float, dy: Float, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::x[this.x + dx], this::y[this.y + dy], time = time, easing = easing)
@Deprecated("Use animator instead")
suspend inline fun View.moveBy(dx: Int, dy: Int, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::x[this.x + dx], this::y[this.y + dy], time = time, easing = easing)

@Deprecated("Use animator instead")
suspend inline fun View.scaleTo(sx: Double, sy: Double, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::scaleX[sx], this::scaleY[sy], time = time, easing = easing)
@Deprecated("Use animator instead")
suspend inline fun View.scaleTo(sx: Float, sy: Float, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::scaleX[sx], this::scaleY[sy], time = time, easing = easing)
@Deprecated("Use animator instead")
suspend inline fun View.scaleTo(sx: Int, sy: Int, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) = tween(this::scaleX[sx], this::scaleY[sy], time = time, easing = easing)

@Deprecated("Use animator instead")
suspend inline fun View.rotateTo(angle: Angle, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) =
    tween(this::rotation[angle], time = time, easing = easing)

@Deprecated("Use animator instead")
suspend inline fun View.rotateBy(deltaAngle: Angle, time: TimeSpan = DEFAULT_TIME, easing: Easing = DEFAULT_EASING) =
    tween(this::rotation[rotation + deltaAngle], time = time, easing = easing)
