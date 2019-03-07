package com.negset.kuintet

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.assets.getValue
import ktx.freetype.loadFreeTypeFont
import ktx.graphics.use

class SelectScreen(game: Kuintet) : KuintetScreen(game)
{
    private val defaultFont = BitmapFont()

    private val font by game.assetMgr.loadFreeTypeFont("font.ttf")

    private val stage = Stage(FitViewport(WIDTH, HEIGHT))

    override fun draw()
    {
        stage.draw()
        stage.batch.use {
            font.draw(it, "選曲画面", 10f, 50f)
        }
    }

    override fun update(delta: Float)
    {
        stage.act(delta)
        if (Gdx.input.justTouched())
            game.setScreen<PlayScreen>()
    }

    override fun loading()
    {
        stage.batch.use {
            defaultFont.draw(it, "loading...", 100f, 100f)
        }
    }

    override fun onFinishLoading()
    {
    }

    override fun resize(width: Int, height: Int)
    {
        stage.viewport.update(width, height)
    }

    override fun dispose()
    {
        stage.dispose()
        defaultFont.dispose()
    }
}
