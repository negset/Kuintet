package com.negset.kuintet

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.FitViewport
import com.negset.kuintet.play.*
import com.negset.kuintet.play.Textures.*
import com.negset.kuintet.play.scene2d.Course
import com.negset.kuintet.play.scene2d.MsrLine
import com.negset.kuintet.play.scene2d.Note
import ktx.actors.isShown
import ktx.actors.plusAssign
import ktx.assets.file
import ktx.assets.getValue
import ktx.assets.invoke
import ktx.assets.pool
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
    private lateinit var buttons: Image

    private val beatmap = Beatmap(file("beatmap.toml"))
    private val props = beatmap.parse(Difficulty.EASY).toMutableList()

    private val activeNotes = mutableListOf<Note>()
    private val notePool = pool { Note() }
    private val activeMsrLine = mutableListOf<MsrLine>()
    private val msrLinePool = pool { MsrLine() }

    private val sw = Stopwatch()

    init
    {
        Textures.manager = game.assetMgr
        NOTE_B.load()
        NOTE_G.load()
        NOTE_P.load()
        NOTE_R.load()
        NOTE_Y.load()
        BUTTONS.load()
        COURSE_BAR.load()
        COURSE_BG.load()
        MSR_LINE.load()
    }

    override fun onFinishLoading()
    {
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
            width = 700f
        }

        course += courseBg
        course += courseBar
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
            when (props[0])
            {
                is MsrLineProp -> msrLinePool().run {
                    init(props[0] as MsrLineProp)
                    activeMsrLine.add(this)
                    course += this
                }

                is NoteProp -> notePool().run {
                    init(props[0] as NoteProp)
                    activeNotes.add(this)
                    course += this
                }
            }
            props.removeAt(0)
        }

        removeDisables()

        stage.act(delta)

        if (Gdx.input.justTouched() &&
                Gdx.input.x < 100 && Gdx.input.y < 100)
            game.setScreen<SelectScreen>()
    }

    private fun removeDisables()
    {
        for (i in activeNotes.size - 1 downTo 0)
        {
            val item = activeNotes[i]
            if (!item.isShown())
            {
                activeNotes.removeAt(i)
                notePool(item)
            }
        }
        for (i in activeMsrLine.size - 1 downTo 0)
        {
            val item = activeMsrLine[i]
            if (!item.isShown())
            {
                activeMsrLine.removeAt(i)
                msrLinePool(item)
            }
        }
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
        MSR_LINE.unload()
    }
}
