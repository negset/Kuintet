package com.negset.kuintet.play

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import ktx.assets.getAsset
import ktx.assets.load

enum class Textures
{
    NOTE_R,
    NOTE_G,
    NOTE_B,
    NOTE_P,
    NOTE_Y,
    BUTTONS,
    COURSE_BG,
    COURSE_BAR,
    MSR_LINE;

    private val path = "${name.toLowerCase()}.png"

    fun load() = manager.load<Texture>(path)
    fun unload() = manager.unload(path)

    operator fun invoke() = manager.getAsset<Texture>(path)

    companion object
    {
        lateinit var manager: AssetManager
    }
}
