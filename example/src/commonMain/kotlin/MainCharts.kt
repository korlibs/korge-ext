import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.vectorImage
import com.soywiz.korim.vector.toShape
import korlibs.image.vector.chart.ChartBars

class MainCharts : Scene() {
    override suspend fun SContainer.sceneMain() {
        this.vectorImage(ChartBars("hello" to 10, "world" to 20).toShape(512, 512))
    }
}