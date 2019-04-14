package com.negset.kuintet.play.scene2d

import com.negset.kuintet.play.NoteProp
import com.negset.kuintet.play.Textures.*

class Note : GameObject()
{
    companion object
    {
        val posX = floatArrayOf(128f, 244f, 360f, 476f, 592f)
    }

    fun init(prop: NoteProp)
    {
        texture = when (prop.pos)
        {
            1 -> NOTE_Y()
            2 -> NOTE_P()
            3 -> NOTE_R()
            4 -> NOTE_B()
            5 -> NOTE_G()
            else -> null
        }

        x = posX[prop.pos - 1]
        y = 2560f
    }

    override fun act(delta: Float)
    {
        y -= 20
        if (y < -height) remove()
    }

    override fun reset()
    {
        texture = null
    }
}
