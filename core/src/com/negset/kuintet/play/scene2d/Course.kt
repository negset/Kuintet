package com.negset.kuintet.play.scene2d

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.negset.kuintet.filtered
import com.negset.kuintet.play.Textures.*

class Course : Group()
{
    private val projectionMatrix = Matrix4()

    private val xAxisRotation = -0.7f
    private val perspective = -0.1f

    private lateinit var courseBg: Image
    private lateinit var courseBar: Image

    fun onFinishLoading()
    {
        courseBg = Image(COURSE_BG().filtered()).apply {
            scaleY = 2560 / 16f
            x = 10f
        }
        courseBar = Image(COURSE_BAR().filtered()).apply {
            x = 10f
            y = 50f
            width = 700f
        }
        addActorAt(0, courseBg)
        addActorAt(1, courseBar)
    }

    override fun draw(batch: Batch, parentAlpha: Float)
    {
        projectionMatrix.set(batch.projectionMatrix)

        batch.projectionMatrix.`val`[Matrix4.M32] = perspective
        batch.projectionMatrix.translate(0f, 640f, 0f)
        batch.projectionMatrix.rotate(1f, 0f, 0f, xAxisRotation)

        super.draw(batch, parentAlpha)

        batch.projectionMatrix = projectionMatrix
    }
}
