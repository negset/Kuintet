package com.negset.kuintet.play

import com.badlogic.gdx.files.FileHandle
import com.moandjiezana.toml.Toml
import com.negset.kuintet.play.CmdProp.Key.*

class Beatmap(file: FileHandle)
{
    val data = Toml().read(file.read()).to(BeatmapData::class.java)

    fun parse(dif: Difficulty): List<ElmProp>
    {
        val lines = data.getChart(dif)
                .split(Regex("\\n"))
                .map { it.split("#")[0].trim() }
                .filter { it.isNotBlank() }

        val props = mutableListOf<ElmProp?>()
        val indices = mutableListOf<ElmIdx>()
        val unitCounts = mutableListOf<Int>()
        var msrIdx = 0
        var unitIdx = 0
        for (line in lines)
        {
            when
            {
                // 命令行
                line.matches(Regex("\\\$.+")) ->
                {
                    props.add(CmdProp.from(line))
                    indices.add(ElmIdx(msrIdx, unitIdx))
                }

                // 小節行
                line.matches(Regex(".*[,;]")) ->
                {
                    // 小節線
                    if (unitIdx == 0)
                    {
                        props.add(MsrLineProp())
                        indices.add(ElmIdx(msrIdx, unitIdx))
                    }

                    val units = line.dropLast(1).split(Regex("\\s*,\\s*"))
                    for (unit in units)
                    {
                        if (unit.isEmpty())
                            props.add(null)
                        else
                            props.add(NoteProp.from(unit))
                        indices.add(ElmIdx(msrIdx, unitIdx))
                        unitIdx++
                    }
                    if (line.endsWith(';'))
                    {
                        unitCounts.add(unitIdx)
                        unitIdx = 0
                        msrIdx++
                    }
                }
            }
        }

        calcTiming(props, indices, unitCounts)

        return props.filterNotNull()
    }

    private fun calcTiming(props: List<ElmProp?>,
                           indices: List<ElmIdx>,
                           unitCounts: List<Int>)
    {
        var bpm = data.bpm
        var timer = 0f          /* [ms] */
        var lastIdx = indices[0]
        for ((i, prop) in props.withIndex())
        {
            if (indices[i] != lastIdx)
            {
                timer += 240000 / bpm / unitCounts[indices[i].msrIdx]
            }

            when (prop)
            {
                is CmdProp -> when (prop.key)
                {
                    BPM -> bpm = prop.value.toFloat()
                    DELAY -> timer += prop.value.toFloat() * 1000
                }
            }

            prop?.timing = timer.toLong()
            lastIdx = indices[i]
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

interface ElmProp
{
    var timing: Long
}

data class ElmIdx(val msrIdx: Int,
                  val unitIdx: Int)

data class MsrLineProp(override var timing: Long = 0L) : ElmProp

data class NoteProp(val type: Type,
                    val pos: Int,
                    val color: Color,
                    val color2: Color? = null,
                    override var timing: Long = 0L) : ElmProp
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
        fun from(s: String): NoteProp
        {
            val sp = s.chunked(1)
            return when
            {
                // TAP
                s.matches(Regex("[bgpry]\\d")) ->
                {
                    val c1 = Color.valueOf(sp[0].toUpperCase())
                    NoteProp(Type.TAP, sp[1].toInt(), c1)
                }
                // FLICK
                s.matches(Regex("[bgpry][bgpry]\\d")) ->
                {
                    val c1 = Color.valueOf(sp[0].toUpperCase())
                    val c2 = Color.valueOf(sp[1].toUpperCase())
                    NoteProp(Type.FLICK, sp[2].toInt(), c1, c2)
                }
                else -> throw IllegalArgumentException("不正なノーツ文字列: $s")
            }
        }
    }
}

data class CmdProp(val key: Key,
                   val value: String,
                   override var timing: Long = 0L) : ElmProp
{
    enum class Key
    {
        BPM, DELAY;
    }

    companion object
    {
        fun from(s: String): CmdProp
        {
            val sp = s.drop(1).split(Regex("\\s*=\\s*"))
            return CmdProp(valueOf(sp[0].toUpperCase()), sp[1])
        }
    }
}
