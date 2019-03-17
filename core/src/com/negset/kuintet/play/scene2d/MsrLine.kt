package com.negset.kuintet.play.scene2d

import com.negset.kuintet.WIDTH
import com.negset.kuintet.play.MsrLineProp
import com.negset.kuintet.play.Textures.*

class MsrLine : GameObject()
{
    init
    {
        texture = MSR_LINE()
        width = 700f
    }

    fun init(prop: MsrLineProp)
    {
        x = WIDTH / 2
        y = 2560f
    }

    override fun act(delta: Float)
    {
        y -= 20
        if (y < -height) remove()
    }
}
