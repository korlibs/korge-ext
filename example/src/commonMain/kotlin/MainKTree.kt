import korlibs.korge.scene.Scene
import korlibs.korge.view.SContainer
import korlibs.korge.view.ktree.readKTree
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.view.*

class MainKTree : Scene() {
    override suspend fun SContainer.sceneMain() {
        addChild(resourcesVfs["scene.ktree"].readKTree(views))
    }
}
