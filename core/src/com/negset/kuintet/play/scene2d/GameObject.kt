package com.negset.kuintet.play.scene2d

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Pool

open class GameObject : Actor(), Pool.Poolable
{
    var texture: Texture? = null
        set(value)
        {
            field = value
            field?.let {
                width = it.width.toFloat()
                height = it.height.toFloat()
            }
        }

    override fun draw(batch: Batch?, parentAlpha: Float)
    {
        batch?.setColor(color.r, color.g, color.b, color.a * parentAlpha)
        batch?.draw(texture, x - width / 2, y - height / 2, width, height)
    }

    override fun reset()
    {
    }
}
