import korlibs.datastructure.*
import korlibs.image.atlas.*
import korlibs.image.bitmap.*
import korlibs.image.color.*
import korlibs.image.tiles.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.view.*
import korlibs.korge.view.tiles.*
import korlibs.math.geom.*
import korlibs.math.geom.Circle
import korlibs.math.geom.vector.*
import korlibs.math.raycasting.*

suspend fun main() = Korge {
    sceneContainer().changeTo({ RaycastingExampleScene()})
}

class RaycastingExampleScene : Scene() {
    @KeepOnReload
    var startPoint: Point = Point(300, 300)

    @KeepOnReload
    var endPoint: Point = Point(100, 100)

    @KeepOnReload
    var tileMap: IntArray2 = IntArray2(100, 100, 0).also {
        for (n in 0 until 10) {
            it[10 + n, 10] = 1
            it[10, 10 + n] = 1

            it[25, 5 + (n * 1.5).toInt()] = 1
            it[28, 5 + n * 3] = 1
        }
    }

    override suspend fun SContainer.sceneMain() {
        val atlas = MutableAtlasUnit()
        val bmp0 = atlas.add(Bitmap32(16, 16, Colors.TRANSPARENT.premultiplied)).slice
        val bmp1 = atlas.add(Bitmap32(16, 16, Colors.BLUE.premultiplied)).slice
        val tileSet = TileSet(TileSetTileInfo(0, bmp0), TileSetTileInfo(0, bmp1))
        val tileMap = tileMap(tileMap, tileSet)
        val cellSize = Size(bmp0.width, bmp0.height)
        val overlay = graphics(renderer = GraphicsRenderer.SYSTEM) {  }

        text("""
            mouse down: put blocks
            shift+mouse down: remove blocks
            ctrl+mouse down: to change the starting point
        """.trimIndent())

        fun updateOverlay(
            startPoint: Point,
            endPoint: Point,
            result: RayResult?
        ) {
            overlay.updateShape {
                clear()
                stroke(Colors.YELLOW) { line(startPoint, endPoint) }
                stroke(Colors.GREEN) { circle(Circle(startPoint, 3f)) }
                //stroke(Colors.WHITE) { circle(Circle(endPoint, 3f)) }

                if (result != null) {
                    stroke(Colors.RED) {
                        circle(Circle(result.point, 3f))
                    }
                    stroke(Colors.RED) {
                        arrow(result.point, result.point + result.normal * 30f)
                    }
                    stroke(Colors.YELLOW) {
                        arrow(result.point, result.point + result.reflected * 30f)
                    }
                }
            }

        }

        fun downOnTileMapPos(mousePos: Point, add: Boolean, setStartPos: Boolean) {
            if (setStartPos) {
                startPoint = mousePos
                return
            }
            val cell = (mousePos / cellSize).toInt()
            tileMap.lock {
                tileMap.stackedIntMap.setFirst(cell.x, cell.y, if (add) 1 else 0)
            }
        }
        fun checkTileMap(mousePos: Point) {
            val result = tileMap.stackedIntMap.raycast(Ray.fromTwoPoints(startPoint, mousePos), cellSize) {
                //println("this.getLast(it.x, it.y): $it")
                if (!this.inside(it.x, it.y)) return@raycast false
                this.getLast(it.x, it.y) != 0
            }
            updateOverlay(startPoint, mousePos, result)
        }

        tileMap.mouse {
            onDown {
                downOnTileMapPos(it.currentPosLocal, !it.isShiftDown, it.isCtrlDown)
                checkTileMap(it.currentPosLocal)
            }
            onMove {
                if (it.pressing) downOnTileMapPos(it.currentPosLocal, !it.isShiftDown, it.isCtrlDown)
                checkTileMap(it.currentPosLocal)
            }
        }
    }
}
