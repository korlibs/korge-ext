package korlibs.math.raycasting

import korlibs.datastructure.*
import korlibs.math.geom.*
import kotlin.math.*

private fun sq(v: Double): Double = v * v
private fun signumNonZero(v: Double): Double = if (v < 0) -1.0 else +1.0

data class RayResult(val ray: Ray, val point: Point, val normal: Vector2D) : Extra by Extra.Mixin() {
    val reflected: Vector2D get() = ray.direction.reflected(normal)
}

// https://www.youtube.com/watch?v=NbSee-XM7WA
fun Ray.firstCollisionInTileMap(
    cellSize: Size = Size(1.0, 1.0),
    maxTiles: Int = 10,
    collides: (tilePos: PointInt) -> Boolean
): RayResult? {
    val ray = this
    val rayStart = this.point / cellSize
    val rayDir = ray.direction.normalized
    val rayUnitStepSize = Vector2D(
        sqrt(1.0 + sq(rayDir.y / rayDir.x)),
        sqrt(1.0 + sq(rayDir.x / rayDir.y)),
    )
    //println("vRayUnitStepSize=$vRayUnitStepSize")
    var mapCheckX = rayStart.x.toInt()
    var mapCheckY = rayStart.y.toInt()
    val stepX = signumNonZero(rayDir.x).toInt()
    val stepY = signumNonZero(rayDir.y).toInt()
    var rayLength1Dx = when {
        rayDir.x < 0 -> (rayStart.x - (mapCheckX)) * rayUnitStepSize.x
        else -> ((mapCheckX + 1) - rayStart.x) * rayUnitStepSize.x
    }
    var rayLength1Dy = when {
        rayDir.y < 0 -> (rayStart.y - (mapCheckY)) * rayUnitStepSize.y
        else -> ((mapCheckY + 1) - rayStart.y) * rayUnitStepSize.y
    }

    // Perform "Walk" until collision or range check
    var bTileFound = false
    val fMaxDistance = hypot(cellSize.width, cellSize.height) * maxTiles
    var fDistance = 0.0
    var dx = 0
    while (fDistance < fMaxDistance) {
        // Walk along shortest path
        if (rayLength1Dx < rayLength1Dy) {
            mapCheckX += stepX
            fDistance = rayLength1Dx
            rayLength1Dx += rayUnitStepSize.x
            dx = 0
        } else {
            mapCheckY += stepY
            fDistance = rayLength1Dy
            rayLength1Dy += rayUnitStepSize.y
            dx = 1
        }

        // Test tile at new test point
        if (collides(PointInt(mapCheckX, mapCheckY))) {
            bTileFound = true
            break
        }
    }

    // Calculate intersection location
    if (bTileFound) {
        //println("vRayStart=$vRayStart: vRayDir=$vRayDir, fDistance=$fDistance")
        return RayResult(
            this,
            (rayStart + rayDir * fDistance) * cellSize,
            if (dx == 0) Vector2D(-1.0 * rayDir.x.sign, 0.0) else Vector2D(0.0, -1.0 * rayDir.y.sign)
        )
    }
    return null
}

fun IStackedIntArray2.raycast(
    ray: Ray,
    cellSize: Size = Size(1, 1),
    maxTiles: Int = 10,
    collides: IStackedIntArray2.(tilePos: PointInt) -> Boolean
): RayResult? {
    return ray.firstCollisionInTileMap(cellSize, maxTiles) { pos -> collides(this, pos) }
}

fun IntIArray2.raycast(
    ray: Ray,
    cellSize: Size = Size(1, 1),
    maxTiles: Int = 10,
    collides: IntIArray2.(tilePos: PointInt) -> Boolean
): RayResult? {
    return ray.firstCollisionInTileMap(cellSize, maxTiles) { pos -> collides(this, pos) }
}

// https://math.stackexchange.com/questions/13261/how-to-get-a-reflection-vector
//𝑟=𝑑−2(𝑑⋅𝑛)𝑛
// @TODO: This will be available soon
//private operator fun Double.times(v: Vector3D): Vector3D = Vector3D(v.x * this, v.y * this, v.z * this)
// @TODO: This will be available soon
//private operator fun Double.times(v: Vector2D): Vector2D = v * this
