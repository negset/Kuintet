package com.negset.kuintet.play

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.negset.kuintet.play.TextureTag.*
import ktx.assets.getAsset
import ktx.assets.load

enum class Textures(val tag: TextureTag = OTHER)
{
    NOTE_R(PLAY),
    NOTE_G(PLAY),
    NOTE_B(PLAY),
    NOTE_P(PLAY),
    NOTE_Y(PLAY),
    BUTTONS(PLAY),
    COURSE_BG(PLAY),
    COURSE_BAR(PLAY),
    MSR_LINE(PLAY);

    private val path = "${name.toLowerCase()}.png"

    fun load() = manager.load<Texture>(path)
    fun unload() = manager.unload(path)

    operator fun invoke() = manager.getAsset<Texture>(path)

    companion object
    {
        lateinit var manager: AssetManager

        fun loadWithTag(tag: TextureTag)
        {
            values().filter { it.tag == tag }.forEach { it.load() }
        }

        fun unloadWithTag(tag: TextureTag)
        {
            values().filter { it.tag == tag }.forEach { it.unload() }
        }
    }
}

enum class TextureTag
{
    SELECT, PLAY, OTHER;
}
