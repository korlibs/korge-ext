import korlibs.datastructure.*
import korlibs.image.bitmap.*
import korlibs.image.format.*
import korlibs.image.tiles.*
import korlibs.io.file.std.*
import korlibs.korge.*
import korlibs.korge.view.tiles.*
import korlibs.math.geom.*
import korlibs.math.geom.slice.*

suspend fun main() = Korge(windowSize = Size(256 * 2, 196 * 2)) {
    val tilesIDC = resourcesVfs["tiles.ase"].readImageDataContainer(ASE)
    val tiles = tilesIDC.mainBitmap.slice()
    val tileSet = TileSet(tiles.splitInRows(16, 16).mapIndexed { index, slice -> TileSetTileInfo(index, slice) })
    val tileMap = tileMap(TileMapData(32, 24, tileSet = tileSet))
    val snakeMap = tileMap(TileMapData(32, 24, tileSet = tileSet))
    val rules = CombinedRuleMatcher(WallsProvider, AppleProvider)
    val ints = IntArray2(tileMap.map.width, tileMap.map.height, GROUND).observe { rect ->
        IntGridToTileGrid(this.base as IntArray2, rules, tileMap.map, rect)
    }
    ints.lock {
        ints[RectangleInt(0, 0, ints.width, 1)] = WALL
        ints[RectangleInt(0, 0, 1, ints.height)] = WALL
        ints[RectangleInt(0, ints.height - 1, ints.width, 1)] = WALL
        ints[RectangleInt(ints.width - 1, 0, 1, ints.height)] = WALL
        ints[RectangleInt(4, 4, ints.width / 2, 1)] = WALL
    }
}


object SnakeProvider : ISimpleTileProvider by (SimpleTileProvider(value = 3).also {
    it.rule(SimpleRule(Tile(1), right = true))
    it.rule(SimpleRule(Tile(2), left = true, right = true))
    it.rule(SimpleRule(Tile(3), left = true, down = true))
})

object SnakeHeadProvider : ISimpleTileProvider by (SimpleTileProvider(value = 3).also {
    it.rule(SimpleRule(Tile(0)))
    it.rule(SimpleRule(Tile(4), right = true))
})

object AppleProvider : ISimpleTileProvider by (SimpleTileProvider(value = 2).also {
    it.rule(SimpleRule(Tile(12)))
})

object WallsProvider : ISimpleTileProvider by (SimpleTileProvider(value = 1).also {
    it.rule(SimpleRule(Tile(16)))
    it.rule(SimpleRule(Tile(17), right = true))
    it.rule(SimpleRule(Tile(18), left = true, right = true))
    it.rule(SimpleRule(Tile(19), left = true, down = true))
    it.rule(SimpleRule(Tile(20), up = true, left = true, down = true))
    it.rule(SimpleRule(Tile(21), up = true, left = true, right = true, down = true))
})

val GROUND = 0
val WALL = 1
val APPLE = 2
