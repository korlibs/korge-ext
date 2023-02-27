package com.soywiz.korge.view

import com.soywiz.kds.*
import com.soywiz.korge.render.MaskStates
import com.soywiz.korge.render.RenderContext

class MaskedView : Container() {
    var mask: View? = null
        set(value) {
            if (field != null) {
                removeChild(value)
            }
            field = value
            if (value != null) {
                addChild(value)
            }
        }

    override fun renderInternal(ctx: RenderContext) {
        if (!visible) return
        val sctx = ctx.maskViewRenderContext
        val useMask = mask != null
        try {
            if (useMask) {
                ctx.flush()
                sctx.stencilIndex++
                setMaskState(sctx, ctx, MaskStates.STATE_SHAPE)
                mask?.render(ctx)
                ctx.flush()
                setMaskState(sctx, ctx, MaskStates.STATE_CONTENT)
            }

            forEachChild { child: View ->
                if (child != mask) {
                    child.render(ctx)
                }
            }
        } finally {
            if (useMask) {
                ctx.flush()
                sctx.stencilIndex--

                if (sctx.stencilIndex <= 0) {
                    setMaskState(sctx, ctx, MaskStates.STATE_NONE)
                    //println("ctx.stencilIndex: ${ctx.stencilIndex}")
                    sctx.stencilIndex = 0
                    ctx.clear(clearColor = false, clearDepth = false, clearStencil = true, stencil = sctx.stencilIndex)
                }
            }
        }
    }

    private fun setMaskState(sctx: MaskViewRenderContext, ctx: RenderContext, state: MaskStates.RenderState) {
        state.set(ctx, sctx.stencilIndex)
    }

    companion object {
        private val RenderContext.maskViewRenderContext: MaskViewRenderContext by Extra.Property { MaskViewRenderContext() }
        private class MaskViewRenderContext {
            var stencilIndex: Int = 0
        }
    }
}
