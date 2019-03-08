package com.negset.kuintet.play.scene2d

import com.negset.kuintet.play.NoteProp
import com.negset.kuintet.play.Textures.*

class Note : GameObject()
{
    companion object
    {
        val posX = floatArrayOf(111f, 207f, 302f, 398f, 493f, 589f)
    }

    fun init(prop: NoteProp)
    {
        texture = when (prop.color)
        {
            NoteProp.Color.B -> NOTE_B()
            NoteProp.Color.G -> NOTE_G()
            NoteProp.Color.P -> NOTE_P()
            NoteProp.Color.R -> NOTE_R()
            NoteProp.Color.Y -> NOTE_Y()
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
