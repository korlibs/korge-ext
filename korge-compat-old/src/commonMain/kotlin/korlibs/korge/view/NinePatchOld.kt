package korlibs.korge.view

import korlibs.graphics.shader.*
import korlibs.korge.render.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.math.geom.*
import kotlin.math.*

inline fun Container.ninePatchOld(
    tex: BmpSlice, width: Double, height: Double, left: Double, top: Double, right: Double, bottom: Double,
    callback: @ViewDslMarker NinePatchOld.() -> Unit = {}
) = NinePatchOld(tex, width, height, left, top, right, bottom).addTo(this, callback)

class NinePatchOld(
    var tex: BmpSlice,
    width: Double,
    height: Double,
    var left: Double,
    var top: Double,
    var right: Double,
    var bottom: Double
) : View() {
    init {
        size(width, height)
    }

    var smoothing = true

    private val sLeft = 0.0
    private val sTop = 0.0

    val posCuts = arrayOf(
        Point(0, 0),
        Point(left, top),
        Point(1.0 - right, 1.0 - bottom),
        Point(1.0, 1.0)
    )

    val texCuts = arrayOf(
        Point(0, 0),
        Point(left, top),
        Point(1.0 - right, 1.0 - bottom),
        Point(1.0, 1.0)
    )

    override fun renderInternal(ctx: RenderContext) {
        if (!visible) return
        // Precalculate points to avoid matrix multiplication per vertex on each frame

        //for (n in 0 until 4) posCuts[n].setTo(posCutsRatios[n].x * width, posCutsRatios[n].y * height)

        val texLeftWidth = tex.width * left
        val texTopHeight = tex.height * top

        val texRighttWidth = tex.width * right
        val texBottomHeight = tex.height * bottom

        val ratioX = if (width < tex.width) width / tex.width else 1f
        val ratioY = if (height < tex.height) height / tex.height else 1f

        val actualRatioX = min(ratioX, ratioY)
        val actualRatioY = min(ratioX, ratioY)

        //val ratioX = 1.0
        //val ratioY = 1.0

        posCuts[1] = Point(texLeftWidth * actualRatioX / width, texTopHeight * actualRatioY / height)
        posCuts[2] = Point(1.0 - texRighttWidth * actualRatioX / width, 1.0 - texBottomHeight * actualRatioY / height)

        ctx.useBatcher { batch ->
            batch.drawNinePatch(
                ctx.getTex(tex),
                sLeft.toFloat(), sTop.toFloat(),
                width.toFloat(), height.toFloat(),
                posCuts = posCuts,
                texCuts = texCuts,
                m = globalMatrix.mutable,
                colorMul = renderColorMul,
                filtering = smoothing,
                blendMode = renderBlendMode,
            )
        }
    }

    override fun getLocalBoundsInternal(): Rectangle {
        return Rectangle(sLeft.toFloat(), sTop.toFloat(), width, height)
    }

    /*
	override fun hitTest(x: Double, y: Double): View? {
		val sRight = sLeft + width
		val sBottom = sTop + height
		return if (checkGlobalBounds(x, y, sLeft, sTop, sRight, sBottom)) this else null
	}
     */
}

/**
 * Draws/buffers a 9-patch image with the texture [tex] at [x], [y] with the total size of [width] and [height].
 * [posCuts] and [texCuts] are [Point] an array of 4 points describing ratios (values between 0 and 1) inside the width/height of the area to be drawn,
 * and the positions inside the texture.
 *
 * The 9-patch looks like this (dividing the image in 9 parts).
 *
 * 0--+-----+--+
 * |  |     |  |
 * |--1-----|--|
 * |  |SSSSS|  |
 * |  |SSSSS|  |
 * |  |SSSSS|  |
 * |--|-----2--|
 * |  |     |  |
 * +--+-----+--3
 *
 * 0: Top-left of the 9-patch
 * 1: Top-left part where scales starts
 * 2: Bottom-right part where scales ends
 * 3: Bottom-right of the 9-patch
 *
 * S: Is the part that is scaled. The other regions are not scaled.
 *
 * It uses the transform [m] matrix, with an optional [filtering] and [colorMul]/[colorAdd], [blendMode] and [program]
 */
fun BatchBuilder2D.drawNinePatch(
    tex: TextureCoords,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    posCuts: Array<Point>,
    texCuts: Array<Point>,
    m: MMatrix = MMatrix(),
    filtering: Boolean = true,
    colorMul: RGBA = Colors.WHITE,
    blendMode: BlendMode = BlendMode.NORMAL,
    program: Program? = null,
) {
    setStateFast(tex.base, filtering, blendMode, program, icount = 6 * 9, vcount = 4 * 4)
    //val texIndex: Int = currentTexIndex
    val texIndex: Int = 0 // @TODO: Restore currentTexIndex, once available in 4.0.0 final

    val ptt1 = MPoint()
    val ptt2 = MPoint()

    val pt1 = MPoint()
    val pt2 = MPoint()
    val pt3 = MPoint()
    val pt4 = MPoint()
    val pt5 = MPoint()

    val pt6 = MPoint()
    val pt7 = MPoint()
    val pt8 = MPoint()

    val p_o = pt1.setToTransform(m, ptt1.setTo(x, y))
    val p_dU = pt2.setToSub(ptt1.setToTransform(m, ptt1.setTo(x + width, y)), p_o)
    val p_dV = pt3.setToSub(ptt1.setToTransform(m, ptt1.setTo(x, y + height)), p_o)

    val t_o = pt4.setTo(tex.tlX, tex.tlY)
    val t_dU = pt5.setToSub(ptt1.setTo(tex.trX, tex.trY), t_o)
    val t_dV = pt6.setToSub(ptt1.setTo(tex.blX, tex.blY), t_o)

    val start = vertexCount

    for (cy in 0 until 4) {
        val posCutY = posCuts[cy].y
        val texCutY = texCuts[cy].y
        for (cx in 0 until 4) {
            val posCutX = posCuts[cx].x
            val texCutX = texCuts[cx].x

            val p = pt7.setToAdd(
                p_o,
                ptt1.setToAdd(
                    ptt1.setToMul(p_dU, posCutX),
                    ptt2.setToMul(p_dV, posCutY)
                )
            )

            val t = pt8.setToAdd(
                t_o,
                ptt1.setToAdd(
                    ptt1.setToMul(t_dU, texCutX),
                    ptt2.setToMul(t_dV, texCutY)
                )
            )

            addVertex(
                p.x.toFloat(), p.y.toFloat(),
                t.x.toFloat(), t.y.toFloat(),
                colorMul, texIndex
            )
        }
    }

    for (cy in 0 until 3) {
        for (cx in 0 until 3) {
            // v0...v1
            // .    .
            // v2...v3

            val v0 = start + cy * 4 + cx
            val v1 = v0 + 1
            val v2 = v0 + 4
            val v3 = v0 + 5

            addIndex(v0)
            addIndex(v1)
            addIndex(v2)
            addIndex(v2)
            addIndex(v1)
            addIndex(v3)
        }
    }
}
