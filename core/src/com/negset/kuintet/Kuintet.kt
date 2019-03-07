package com.negset.kuintet

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import ktx.app.KtxGame
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

const val WIDTH = 720f
const val HEIGHT = 1280f

class Kuintet : KtxGame<Screen>()
{
    val assetMgr = AssetManager()

    init
    {
        assetMgr.run {
            registerFreeTypeFontLoaders()
            loadFreeTypeFont("font.ttf") {
                size = 40
                color = Color.WHITE
                borderColor = Color.DARK_GRAY
                borderWidth = 3f
                magFilter = Texture.TextureFilter.Linear
                minFilter = Texture.TextureFilter.Linear
                incremental = true
            }
        }
    }

    override fun create()
    {
        addScreen(SelectScreen(this))
        addScreen(PlayScreen(this))
        setScreen<SelectScreen>()
    }
}

fun Texture.filtered(): Texture = apply {
    setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
}
