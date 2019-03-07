package com.negset.kuintet.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.negset.kuintet.Kuintet

object DesktopLauncher
{
    @JvmStatic
    fun main(arg: Array<String>)
    {
        val config = LwjglApplicationConfiguration().apply {
            width = 540
            height = 960
        }
        LwjglApplication(Kuintet(), config)
    }
}
