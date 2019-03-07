package com.negset.kuintet

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.FitViewport
import com.negset.kuintet.play.Beatmap
import com.negset.kuintet.play.Course
import com.negset.kuintet.play.Difficulty
import com.negset.kuintet.play.Textures
import com.negset.kuintet.play.Textures.*
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
    private lateinit var courseBg: Image
    private lateinit var courseBar: Image
    private lateinit var noteR: Image
    private lateinit var buttons: Image

    private val beatmap = Beatmap(file("beatmap.toml"))

    init
    {
        Textures.manager = game.assetMgr
        NOTE_R.load()
        BUTTONS.load()
        COURSE_BAR.load()
        COURSE_BG.load()

        beatmap.parse(Difficulty.EASY).forEach {
            println(it)
        }
    }

    override fun onFinishLoading()
    {
        noteR = Image(NOTE_R().filtered()).apply {
            x = 300f
        }
        buttons = Image(BUTTONS().filtered()).apply {
            x = (WIDTH - 640) / 2
        }
        courseBg = Image(COURSE_BG().filtered()).apply {
            scaleY = 2560 / 16f
            x = 10f
        }
        courseBar = Image(COURSE_BAR().filtered()).apply {
            x = 10f
            y = 50f
        }

        course += courseBg
        course += courseBar
        course += noteR
        stage += course
        stage += buttons
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
        noteR.y -= 15
        if (noteR.y < -580) noteR.y = 2560f

        stage.act(delta)

        if (Gdx.input.justTouched() &&
                Gdx.input.x < 100 && Gdx.input.y < 100)
            game.setScreen<SelectScreen>()
    }

    override fun loading()
    {
        stage.batch.use {
            defaultFont.draw(it, "loading...", 100f, 100f)
        }
    }

    override fun resize(width: Int, height: Int)
    {
        stage.viewport.update(width, height)
    }

    override fun dispose()
    {
        stage.dispose()
        defaultFont.dispose()
        NOTE_R.unload()
        BUTTONS.unload()
        COURSE_BAR.unload()
        COURSE_BG.unload()
    }
}
