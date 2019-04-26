package com.negset.kuintet.play

import com.badlogic.gdx.scenes.scene2d.Group
import com.negset.kuintet.play.scene2d.GameObject
import com.negset.kuintet.play.scene2d.MsrLine
import com.negset.kuintet.play.scene2d.Note
import ktx.assets.invoke
import ktx.assets.pool

class ObjectsMgr(parent: Group)
{
    private val notesGroup = Group()
    private val notesPool = pool { Note() }

    private val msrLineGroup = Group()
    private val msrLinePool = pool { MsrLine() }

    init
    {
        parent.addActor(msrLineGroup)
        parent.addActor(notesGroup)
    }

    fun createFromProp(prop: ElmProp)
    {
        when (prop)
        {
            is NoteProp -> notesPool().let {
                it.objectsMgr = this
                it.init(prop)
                notesGroup.addActor(it)
            }

            is MsrLineProp -> msrLinePool().let {
                it.objectsMgr = this
                it.init(prop)
                msrLineGroup.addActor(it)
            }
        }
    }

    fun remove(obj: GameObject): Boolean
    {
        return when (obj)
        {
            is Note ->
            {
                notesPool(obj)
                notesGroup.removeActor(obj, true)
            }
            is MsrLine ->
            {
                msrLinePool(obj)
                msrLineGroup.removeActor(obj, true)
            }
            else -> false
        }
    }
}
