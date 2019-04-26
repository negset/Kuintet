package com.negset.kuintet

import ktx.app.KtxScreen

abstract class KuintetScreen(protected val game: Kuintet) : KtxScreen
{
    private var loading = true

    override fun render(delta: Float)
    {
        if (loading)
        {
            drawLoading()
            updateLoading()
            if (game.assetMgr.update())
            {
                onFinishLoading()
                loading = false
            }
        }
        else
        {
            draw()
            update(delta)
        }
    }

    abstract fun drawLoading()

    abstract fun updateLoading()

    abstract fun onFinishLoading()

    abstract fun draw()

    abstract fun update(delta: Float)
}
