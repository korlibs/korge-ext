import korlibs.korge.Korge
import korlibs.korge.scene.sceneContainer
import korlibs.image.color.Colors

suspend fun main() = Korge(backgroundColor = Colors.DARKGREY).start {
    //sceneContainer().changeTo({ MainOldMask() })
    sceneContainer().changeTo({ MainKTree() })
    //sceneContainer().changeTo({ MainCharts() })
}
