package korlibs.math.raycasting

import korlibs.datastructure.*
import korlibs.math.geom.*
import kotlin.math.*

// @TODO: Move to KorGE and add companion to Ray, so this can be an static method
fun RayFromTwoPoints(start: Point, end: Point): Ray = Ray(start, Angle.between(start, end))

private fun sq(f: Float): Float = f * f

// https://www.youtube.com/watch?v=NbSee-XM7WA
fun Ray.firstCollisionInTileMap(
    cellSize: Size = Size(1f, 1f),
    maxTiles: Int = 10,
    collides: (tilePos: PointInt) -> Boolean
): Point? {
    val ray = this
    val vRayStart = this.point / cellSize
    val vRayDir = ray.direction.normalized
    val vRayUnitStepSize = Vector2(
        sqrt(1f + sq(vRayDir.y / vRayDir.x)),
        sqrt(1f + sq(vRayDir.x / vRayDir.y)),
    )
    //println("vRayUnitStepSize=$vRayUnitStepSize")
    var vMapCheckx = vRayStart.x.toInt()
    var vMapChecky = vRayStart.y.toInt()
    var vStepx = 0
    var vStepy = 0
    var vRayLength1Dx = 0f
    var vRayLength1Dy = 0f
    if (vRayDir.x < 0) {
        vStepx = -1
        vRayLength1Dx = (vRayStart.x - (vMapCheckx)) * vRayUnitStepSize.x
    } else {
        vStepx = +1
        vRayLength1Dx = ((vMapCheckx + 1) - vRayStart.x) * vRayUnitStepSize.x
    }

    if (vRayDir.y < 0) {
        vStepy = -1
        vRayLength1Dy = (vRayStart.y - (vMapChecky)) * vRayUnitStepSize.y
    } else {
        vStepy = +1
        vRayLength1Dy = ((vMapChecky + 1) - vRayStart.y) * vRayUnitStepSize.y
    }

    // Perform "Walk" until collision or range check
    var bTileFound = false
    val fMaxDistance = hypot(cellSize.width.toFloat(), cellSize.height.toFloat()) * maxTiles
    var fDistance = 0.0f
    while (fDistance < fMaxDistance) {
        // Walk along shortest path
        if (vRayLength1Dx < vRayLength1Dy) {
            vMapCheckx += vStepx
            fDistance = vRayLength1Dx
            vRayLength1Dx += vRayUnitStepSize.x
        } else {
            vMapChecky += vStepy
            fDistance = vRayLength1Dy
            vRayLength1Dy += vRayUnitStepSize.y
        }

        // Test tile at new test point
        if (collides(PointInt(vMapCheckx, vMapChecky))) {
            bTileFound = true
            break
        }
    }

    // Calculate intersection location
    if (bTileFound) {
        //println("vRayStart=$vRayStart: vRayDir=$vRayDir, fDistance=$fDistance")
        return (vRayStart + vRayDir * fDistance) * cellSize
    }
    return null
}

fun IStackedIntArray2.raycast(
    ray: Ray,
    cellSize: Size = Size(1f, 1f),
    maxTiles: Int = 10,
    collides: IStackedIntArray2.(tilePos: PointInt) -> Boolean
): Point? {
    return ray.firstCollisionInTileMap(cellSize, maxTiles) { pos -> collides(this, pos) }
}
