import korlibs.image.bitmap.*
import korlibs.image.bitmap.effect.*
import korlibs.image.color.*
import korlibs.io.resources.*
import korlibs.korge.input.*
import korlibs.korge.resources.*
import korlibs.korge.scene.*
import korlibs.korge.view.*

class MainOldMask : ScaledScene(512, 512) {
    val ResourcesContainer.korgePng by resourceBitmap("korge.png")

    override suspend fun SContainer.sceneMain() {
        //val bitmap = korgePng.get().extract().toBMP32()
        val bitmap = korgePng.get().extract().toBMP32()
        val bitmap2 = bitmap.applyEffect(BitmapEffect(dropShadowColor = Colors.BLUE, dropShadowX = 10, dropShadowY = 0, dropShadowRadius = 6))
        val maskedView = MaskedView()
        addChild(maskedView)
        maskedView.text("HELLO WORLD!", textSize = 64f)
        val img = maskedView.image(bitmap2).scale(3, 3)
        val mask = Circle(256.0).centered
        maskedView.mask = mask
        maskedView.mask!!.position(0, 0)
        mouse {
            onMoveAnywhere {
                val mouse = localMousePos(views)
                maskedView.mask!!.position(mouse.x.coerceIn(0.0, 512.0), mouse.y.coerceIn(0.0, 512.0))
            }
        }
    }
}
