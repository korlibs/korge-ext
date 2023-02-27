package com.soywiz.korim.bitmap

import com.soywiz.korma.geom.*
import com.soywiz.korma.geom.slice.*

val RectSlice<out Bitmap>.bmpWidth: Int get() = this.baseWidth
val RectSlice<out Bitmap>.bmpHeight: Int get() = this.baseHeight

// http://pixijs.download/dev/docs/PIXI.Texture.html#Texture
fun BitmapSliceCompat(
    bmp: Bitmap,
    frame: MRectangle,
    orig: MRectangle,
    trim: MRectangle,
    rotated: Boolean,
    name: String = "unknown"
) = bmp.slice(frame.toInt(), name, if (rotated) ImageOrientation.ROTATE_90 else ImageOrientation.ROTATE_0)
