import korlibs.io.util.i18n.*
import korlibs.korge.i18n.*
import korlibs.korge.scene.*
import korlibs.korge.ui.*
import korlibs.korge.view.*

class MainI18n : Scene() {
    override suspend fun SContainer.sceneMain() {
        val languages = listOf(Language.SPANISH, Language.ENGLISH)
        views.language = Language.SPANISH
        uiVerticalStack(adjustSize = false) {
            uiComboBox(items = languages).onSelectionUpdate {
                views.language = it.selectedItem
            }
            text("").textProvider {
                when (it) {
                    Language.SPANISH -> "¡Hola Mundo!"
                    Language.ENGLISH -> "Hello world!"
                    else -> "Unknown"
                }
            }
            text("").textProvider {
                when (it) {
                    Language.SPANISH -> "Esto es una prueba"
                    Language.ENGLISH -> "This is a test"
                    else -> "Unknown"
                }
            }
        }
    }
}
