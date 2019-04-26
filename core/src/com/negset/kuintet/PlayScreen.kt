package com.negset.kuintet

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.FitViewport
import com.negset.kuintet.play.*
import com.negset.kuintet.play.Textures.*
import com.negset.kuintet.play.scene2d.Course
import ktx.actors.plusAssign
import ktx.assets.file
import ktx.assets.getValue
import ktx.freetype.loadFreeTypeFont
import ktx.graphics.use

class PlayScreen(game: Kuintet) : KuintetScreen(game)
{
    private val defaultFont = BitmapFont()
    private val font by game.assetMgr.loadFreeTypeFont("font.ttf")

    private val stage = Stage(FitViewport(WIDTH, HEIGHT))

    private val course = Course()

    private lateinit var buttons: Image

    private val beatmap = Beatmap(file("beatmap.toml"))
    private val props = beatmap.parse(Difficulty.EASY).toMutableList()

    private val objectsMgr = ObjectsMgr(course)

    private val sw = Stopwatch()

    init
    {
        Textures.manager = game.assetMgr
        Textures.loadWithTag(TextureTag.PLAY)
    }

    override fun drawLoading()
    {
        stage.batch.use {
            defaultFont.draw(it, "drawLoading...", 100f, 100f)
        }
    }

    override fun updateLoading()
    {
    }

    override fun onFinishLoading()
    {
        course.onFinishLoading()

        buttons = Image(BUTTONS().filtered()).apply {
            x = (WIDTH - 640) / 2
        }

        stage += course
        stage += buttons

        sw.start()
    }

    override fun draw()
    {
        stage.draw()
        stage.batch.use {
            font.draw(it, "プレイ画面", 10f, 50f)
        }
    }

    override fun update(delta: Float)
    {
        while (props.isNotEmpty() && sw.elapsed >= props[0].timing)
        {
            objectsMgr.createFromProp(props[0])
            props.removeAt(0)
        }

        stage.act(delta)

        if (Gdx.input.justTouched() &&
                Gdx.input.x < 100 && Gdx.input.y < 100)
            game.setScreen<SelectScreen>()
    }

    override fun resize(width: Int, height: Int)
    {
        stage.viewport.update(width, height)
    }

    override fun dispose()
    {
        stage.dispose()
        defaultFont.dispose()
        Textures.unloadWithTag(TextureTag.PLAY)
    }
}
