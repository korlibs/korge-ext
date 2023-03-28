import com.soywiz.korge.Korge
import com.soywiz.korge.scene.sceneContainer
import com.soywiz.korim.color.Colors

suspend fun main() = Korge(bgcolor = Colors.DARKGREY) {
    //sceneContainer().changeTo({ MainOldMask() })
    //sceneContainer().changeTo({ MainKTree() })
    sceneContainer().changeTo({ MainCharts() })
}
