package korlibs.korge.i18n

import korlibs.datastructure.*
import korlibs.korge.view.*
import korlibs.io.util.i18n.*

private var Views._language: Language? by extraProperty { null }

private val I18N_TEXT_PROVIDER = "textProvider"

var Views.language: Language?
    get() = _language
    set(value) {
        if (_language == value) return
        _language = value
        this.stage.foreachDescendant { view ->
            view.getExtraTyped<TextProvider>(I18N_TEXT_PROVIDER)?.let {
                (view as IText).text = it.invoke(_language)
            }
        }
    }

typealias TextProvider = (language: Language?) -> String

fun <T : Text> T.textProvider(get: TextProvider): T {
    this.setExtra(I18N_TEXT_PROVIDER, get)
    text = get(stage?.views?.language)
    return this
}
