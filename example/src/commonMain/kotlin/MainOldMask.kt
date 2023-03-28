/*
class MainOldMask : ScaledScene(512, 512) {
    val ResourcesContainer.korgePng by resourceBitmap("korge.png")

    override suspend fun SContainer.sceneMain() {
        //val bitmap = korgePng.get().extract().toBMP32()
        val bitmap = korgePng.get().extract().toBMP32()
        val bitmap2 = bitmap.applyEffect(BitmapEffect(dropShadowColor = Colors.BLUE, dropShadowX = 10, dropShadowY = 0, dropShadowRadius = 6))
        val maskedView = MaskedView()
        addChild(maskedView)
        maskedView.text2("HELLO WORLD!", textSize = 64.0)
        val img = maskedView.image(bitmap2).scale(3, 3)
        val mask = Circle(256.0).centered
        maskedView.mask = mask
        maskedView.mask!!.position(0, 0)
        mouse {
            onMoveAnywhere {
                maskedView.mask!!.position(localMouseX(views).coerceIn(0.0, 512.0), localMouseY(views).coerceIn(0.0, 512.0))
            }
        }
    }
}
*/
