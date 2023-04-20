package korlibs.korge.component.docking

import korlibs.io.async.*
import korlibs.korge.input.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.math.interpolation.*
import korlibs.time.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

class JellyButton(val view: View?, val coroutineContext: CoroutineContext, var targetScale: Float = 1.5f) {
	val hitTest = view["hitTest"].firstOrNull ?: view
	val content = view["content"].firstOrNull ?: view
	val initialScale: Float = content?.scaleAvg ?: 1f
	var down = false
	var over = false

	//val thread = AsyncThread()

	init {
		if (hitTest != content) {
			hitTest?.alpha = 0f
		}
		//println("----------------")
		//println(hitTest?.globalBounds)
		//println(content?.globalBounds)
		//println(view?.globalBounds)

		hitTest?.onOver {
			over = true
			updateState()
		}
		hitTest?.onOut {
			over = false
			updateState()
		}
		hitTest?.onDown {
			down = true
			updateState()
		}
		hitTest?.onUpAnywhere {
			down = false
			updateState()
		}
	}

	private fun updateState() {
		if (content == null) return
		val scale: Float = when {
			down -> 1f / targetScale
			over -> targetScale
			else -> 1f
		}
		CoroutineScope(coroutineContext).launchImmediately {
			content.tween(content::scaleAvg[initialScale * scale], time = 200.milliseconds, easing = Easing.EASE_OUT_ELASTIC)
		}
	}

	suspend fun onClick(callback: suspend () -> Unit) {
		hitTest?.mouse?.click?.addSuspend { callback() }
	}
}

suspend fun View?.jellyButton(targetScale: Float = 1.5f) = JellyButton(this, coroutineContext, targetScale)
