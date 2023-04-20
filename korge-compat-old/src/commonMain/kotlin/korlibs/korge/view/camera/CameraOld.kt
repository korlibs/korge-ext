package korlibs.korge.view.camera

import korlibs.datastructure.*
import korlibs.io.lang.*
import korlibs.korge.annotations.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlin.math.*

@KorgeExperimental
inline fun Container.cameraContainerOld(
    width: Double,
    height: Double,
    camera: CameraOld = CameraOld(0.0, 0.0, width, height),
    noinline decoration: @ViewDslMarker CameraContainerOld.() -> Unit = {},
    content: @ViewDslMarker Container.() -> Unit = {}
) = CameraContainerOld(width, height, camera, decoration).addTo(this).also { content(it.content) }

@KorgeExperimental
class CameraContainerOld(
    width: Double = 100.0,
    height: Double = 100.0,
    camera: CameraOld = CameraOld(0.0, 0.0, width, height),
    decoration: @ViewDslMarker CameraContainerOld.() -> Unit = {}
) : FixedSizeContainer(Size(width, height), true), View.Reference {

    private val contentContainer = Container()

    val content: Container = object : Container(), Reference {
        override fun getLocalBoundsInternal(): Rectangle {
            return Rectangle(Point.ZERO, this@CameraContainerOld.size)
        }
    }

    var camera: CameraOld = camera
        set(newCamera) {
            if (newCamera.container != null) throw IllegalStateException("You can't use Camera that is already attached")
            camera.detach()
            newCamera.attach(this)
            setTo(newCamera)
            field = newCamera
        }

    init {
        decoration(this)
        contentContainer.addTo(this)
        content.addTo(contentContainer)
    }

    inline fun updateContent(action: Container.() -> Unit) {
        content.action()
    }

    internal fun setTo(
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        zoom: Double,
        angle: Angle,
        anchorX: Double,
        anchorY: Double
    ) {
        val realScaleX = (content.unscaledWidth / width) * zoom
        val realScaleY = (content.unscaledHeight / height) * zoom

        val contentContainerX = width * anchorX
        val contentContainerY = height * anchorY

        content.x = -x.toFloat()
        content.y = -y.toFloat()
        contentContainer.x = contentContainerX.toFloat()
        contentContainer.y = contentContainerY.toFloat()
        contentContainer.rotation = angle
        contentContainer.scaleX = realScaleX.toFloat()
        contentContainer.scaleY = realScaleY.toFloat()
    }

    internal fun setTo(camera: CameraOld) = setTo(
        camera.x,
        camera.y,
        camera.width,
        camera.height,
        camera.zoom,
        camera.angle,
        camera.anchor.doubleX,
        camera.anchor.doubleY
    )
}

@KorgeExperimental
class CameraOld(
    x: Double = 0.0,
    y: Double = 0.0,
    width: Double = 100.0,
    height: Double = 100.0,
    zoom: Double = 1.0,
    angle: Angle = 0.degrees,
    anchorX: Double = 0.0,
    anchorY: Double = 0.0
) : MutableInterpolable<CameraOld>, Interpolable<CameraOld>, Anchorable {
    override var anchor: Anchor = Anchor(anchorX, anchorY)

    var anchorX: Double
        get() = anchor.doubleX
        set(value) { anchor = anchor}

    init {
        this.anchor = Anchor(anchorX, anchorY)
    }

    var x: Double by Observable(x, before = { if (withUpdate) setTo(x = it) })
    var y: Double by Observable(y, before = { if (withUpdate) setTo(y = it) })
    var width: Double by Observable(width, before = { if (withUpdate) setTo(width = it) })
    var height: Double by Observable(height, before = { if (withUpdate) setTo(height = it) })
    var zoom: Double by Observable(zoom, before = { if (withUpdate) setTo(zoom = it) })
    var angle: Angle by Observable(angle, before = { if (withUpdate) setTo(angle = it) })

    internal var container: CameraContainerOld? = null
    private var cancelFollowing: Cancellable? = null
    private var withUpdate: Boolean = true

    internal fun attach(container: CameraContainerOld) {
        this.container = container
    }

    internal fun detach() {
        container = null
    }

    // when we don't want to update CameraContainer
    private inline fun withoutUpdate(callback: CameraOld.() -> Unit) {
        val prev = withUpdate
        withUpdate = false
        callback()
        withUpdate = prev
    }

    fun setTo(other: CameraOld) = setTo(
        other.x,
        other.y,
        other.width,
        other.height,
        other.zoom,
        other.angle,
        other.anchor.sx.toDouble(),
        other.anchor.sy.toDouble()
    )

    fun setTo(
        x: Double = this.x,
        y: Double = this.y,
        width: Double = this.width,
        height: Double = this.height,
        zoom: Double = this.zoom,
        angle: Angle = this.angle,
        anchorX: Double = this.anchor.doubleX,
        anchorY: Double = this.anchor.doubleY
    ): CameraOld {
        if (withUpdate) {
            container?.setTo(x, y, width, height, zoom, angle, anchorX, anchorY)
        }
        withoutUpdate {
            this.x = x
            this.y = y
            this.width = width
            this.height = height
            this.zoom = zoom
            this.angle = angle
            this.anchor = Anchor(anchorX, anchorY)
        }
        return this
    }

    suspend fun tweenTo(other: CameraOld, time: TimeSpan, easing: Easing = Easing.LINEAR) = tweenTo(
        x = other.x,
        y = other.y,
        width = other.width,
        height = other.height,
        zoom = other.zoom,
        angle = other.angle,
        anchorX = other.anchor.doubleX,
        anchorY = other.anchor.doubleY,
        time = time,
        easing = easing
    )

    suspend fun tweenTo(
        x: Double = this.x,
        y: Double = this.y,
        width: Double = this.width,
        height: Double = this.height,
        zoom: Double = this.zoom,
        angle: Angle = this.angle,
        anchorX: Double = this.anchor.doubleX,
        anchorY: Double = this.anchor.doubleY,
        time: TimeSpan,
        easing: Easing = Easing.LINEAR
    ) {
        val initialX = this.x
        val initialY = this.y
        val initialWidth = this.width
        val initialHeight = this.height
        val initialZoom = this.zoom
        val initialAngle = this.angle
        val initialAnchorX = this.anchor.doubleX
        val initialAnchorY = this.anchor.doubleY
        container?.tween(time = time, easing = easing) { ratio ->
            setTo(
                ratio.toRatio().interpolate(initialX, x),
                ratio.toRatio().interpolate(initialY, y),
                ratio.toRatio().interpolate(initialWidth, width),
                ratio.toRatio().interpolate(initialHeight, height),
                ratio.toRatio().interpolate(initialZoom, zoom),
                ratio.toRatio().interpolateAngleDenormalized(initialAngle, angle),
                ratio.toRatio().interpolate(initialAnchorX, anchorX),
                ratio.toRatio().interpolate(initialAnchorY, anchorY)
            )
        }
    }

    fun follow(view: View, threshold: Double = 0.0) {
        val inside = container?.content?.let { view.hasAncestor(it) } ?: false
        if (!inside) throw IllegalStateException("Can't follow view that is not in the content of CameraContainer")
        cancelFollowing?.cancel()
        cancelFollowing = view.addUpdater {
            val camera = this@CameraOld
            val x = view.x + view.width / 2
            val y = view.y + view.height / 2
            val dx = x - camera.x
            val dy = y - camera.y
            if (abs(dx) > threshold || abs(dy) > threshold) {
                camera.xy(x - dx.sign * threshold, y - dy.sign * threshold)
            }
        }
    }

    fun unfollow() {
        cancelFollowing?.cancel()
    }

    fun copy(
        x: Double = this.x,
        y: Double = this.y,
        width: Double = this.width,
        height: Double = this.height,
        zoom: Double = this.zoom,
        angle: Angle = this.angle,
        anchorX: Double = this.anchor.doubleX,
        anchorY: Double = this.anchor.doubleY
    ) = CameraOld(
        x = x,
        y = y,
        width = width,
        height = height,
        zoom = zoom,
        angle = angle,
        anchorX = anchorX,
        anchorY = anchorY
    )

    override fun setToInterpolated(ratio: Ratio, l: CameraOld, r: CameraOld): CameraOld = setTo(
        x = ratio.interpolate(l.x, r.x),
        y = ratio.interpolate(l.y, r.y),
        width = ratio.interpolate(l.width, r.width),
        height = ratio.interpolate(l.height, r.height),
        zoom = ratio.interpolate(l.zoom, r.zoom),
        angle = ratio.interpolateAngleDenormalized(l.angle, r.angle),
        anchorX = ratio.interpolate(l.anchor.doubleX, r.anchor.doubleX),
        anchorY = ratio.interpolate(l.anchor.doubleY, r.anchor.doubleY)
    )

    override fun interpolateWith(ratio: Ratio, other: CameraOld) = CameraOld().setToInterpolated(ratio, this, other)

    override fun toString() =
        "Camera(x=$x, y=$y, width=$width, height=$height, zoom=$zoom, angle=$angle, anchor=${anchor})"
}

fun CameraOld.xy(x: Double, y: Double): CameraOld {
    return setTo(x = x, y = y)
}

fun CameraOld.size(width: Double, height: Double): CameraOld {
    return setTo(width = width, height = height)
}

fun CameraOld.anchor(anchorX: Double, anchorY: Double): CameraOld {
    return setTo(anchorX = anchorX, anchorY = anchorY)
}

suspend fun CameraOld.moveTo(x: Double, y: Double, time: TimeSpan, easing: Easing = Easing.LINEAR) {
    tweenTo(x = x, y = y, time = time, easing = easing)
}

suspend fun CameraOld.moveBy(dx: Double, dy: Double, time: TimeSpan, easing: Easing = Easing.LINEAR) {
    tweenTo(x = x + dx, y = y + dy, time = time, easing = easing)
}

suspend fun CameraOld.resizeTo(width: Double, height: Double, time: TimeSpan, easing: Easing = Easing.LINEAR) {
    tweenTo(width = width, height = height, time = time, easing = easing)
}

suspend fun CameraOld.rotate(angle: Angle, time: TimeSpan, easing: Easing = Easing.LINEAR) {
    tweenTo(angle = this.angle + angle, time = time, easing = easing)
}

suspend fun CameraOld.zoom(value: Double, time: TimeSpan, easing: Easing = Easing.LINEAR) {
    tweenTo(zoom = value, time = time, easing = easing)
}
