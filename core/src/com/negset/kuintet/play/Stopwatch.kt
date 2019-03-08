package com.negset.kuintet.play

import com.badlogic.gdx.utils.TimeUtils

class Stopwatch
{
    private var now = 0L
    private var start = 0L
    var isRunning = false
        private set

    var elapsed = 0L
        get()
        {
            if (isRunning) now = TimeUtils.millis()
            return now - start
        }
        private set

    fun start()
    {
        start = TimeUtils.millis()
        isRunning = true
    }
}
