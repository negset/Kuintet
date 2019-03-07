package com.negset.kuintet.play

import com.badlogic.gdx.files.FileHandle
import com.moandjiezana.toml.Toml
import com.negset.kuintet.play.Cmd.Key.*

class Beatmap(file: FileHandle)
{
    val data = Toml().read(file.read()).to(BeatmapData::class.java)

    fun parse(dif: Difficulty): List<ChartElm>
    {
        val lines = data.getChart(dif)
                .split(Regex("\\n"))
                .map { it.split("#")[0].trim() }
                .filter { it.isNotBlank() }

        val elms = mutableListOf<ChartElm>()
        var msrIdx = 0
        var unitIdx = 0
        val unitPerMsr = mutableListOf<Int>()
        for (line in lines)
        {
            when
            {
                // 命令行
                line.matches(Regex("\\\$.+")) ->
                {
                    val prop = ElmProp(msrIdx, unitIdx)
                    elms.add(Cmd.from(prop, line))
                }

                // 小節行
                line.matches(Regex(".*[,;]")) ->
                {
                    val units = line.dropLast(1).split(Regex("\\s*,\\s*"))
                    for (unit in units)
                    {
                        val prop = ElmProp(msrIdx, unitIdx)
                        if (unit.isEmpty())
                            elms.add(Rest(prop))
                        else
                            elms.add(Note.from(prop, unit))
                        unitIdx++
                    }
                    if (line.endsWith(';'))
                    {
                        unitPerMsr.add(unitIdx)
                        unitIdx = 0
                        msrIdx++
                    }
                }
            }
        }

        calcTiming(elms, unitPerMsr)

        return elms.filter { it !is Rest }
    }

    private fun calcTiming(elms: MutableList<ChartElm>,
                           unitPerMsr: MutableList<Int>)
    {
        var bpm = data.bpm
        var timer = 0f          /* [ms] */
        for (elm in elms)
        {
            when (elm)
            {
                is Cmd ->
                {
                    when (elm.key)
                    {
                        BPM -> bpm = elm.value.toFloat()
                        DELAY -> timer += elm.value.toFloat() * 1000
                    }
                }
            }

            elm.prop.timing = timer.toLong()
            timer += 240000 / bpm / unitPerMsr[elm.prop.msrIdx]
        }
    }
}

enum class Difficulty
{
    EASY, NORMAL, HARD, LUNATIC
}

data class BeatmapData(val title: String,
                       val artist: String,
                       val track: String,
                       val bpm: Float,
                       val offset: Float,
                       val demo: List<Float>,
                       val level: List<Int>,
                       val easy: String,
                       val normal: String,
                       val hard: String,
                       val lunatic: String)
{
    fun getChart(dif: Difficulty): String
    {
        return when (dif)
        {
            Difficulty.EASY -> easy
            Difficulty.NORMAL -> normal
            Difficulty.HARD -> hard
            Difficulty.LUNATIC -> lunatic
        }
    }
}

interface ChartElm
{
    val prop: ElmProp
}

data class ElmProp(val msrIdx: Int,
                   val unitIdx: Int,
                   var timing: Long = 0L)

class Rest(override val prop: ElmProp) : ChartElm

data class Note(override val prop: ElmProp,
                val type: Type,
                val pos: Int,
                val color: Color,
                val color2: Color? = null) : ChartElm
{
    enum class Type
    {
        TAP, FLICK, HOLD, SLIDE;
    }

    enum class Color
    {
        B, G, P, R, Y;
    }

    companion object
    {
        fun from(prop: ElmProp, s: String): Note
        {
            val sp = s.chunked(1)
            return when
            {
                // TAP
                s.matches(Regex("[bgpry]\\d")) ->
                {
                    val c1 = Color.valueOf(sp[0].toUpperCase())
                    Note(prop, Type.TAP, sp[1].toInt(), c1)
                }
                // FLICK
                s.matches(Regex("[bgpry][bgpry]\\d")) ->
                {
                    val c1 = Color.valueOf(sp[0].toUpperCase())
                    val c2 = Color.valueOf(sp[1].toUpperCase())
                    Note(prop, Type.FLICK, sp[2].toInt(), c1, c2)
                }
                else -> throw IllegalArgumentException("不正なノーツ文字列: $s")
            }
        }
    }
}

data class Cmd(override val prop: ElmProp,
               val key: Key,
               val value: String) : ChartElm
{
    enum class Key
    {
        BPM, DELAY;
    }

    companion object
    {
        fun from(prop: ElmProp, s: String): Cmd
        {
            val sp = s.drop(1).split(Regex("\\s*=\\s*"))
            return Cmd(prop, valueOf(sp[0].toUpperCase()), sp[1])
        }
    }
}
