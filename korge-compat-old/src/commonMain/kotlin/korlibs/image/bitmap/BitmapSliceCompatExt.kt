package korlibs.image.bitmap

import korlibs.math.geom.*
import korlibs.math.geom.slice.*

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
) = bmp.slice(frame.immutable.toInt(), name, if (rotated) ImageOrientation.ROTATE_90 else ImageOrientation.ROTATE_0)
