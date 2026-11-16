package com.Rirsefu3.Degmed.data.sample

import com.Rirsefu3.Degmed.R
import com.Rirsefu3.Degmed.core.config.GameConfig
import com.Rirsefu3.Degmed.domain.model.BoardState
import com.Rirsefu3.Degmed.domain.model.LevelDefinition
import com.Rirsefu3.Degmed.domain.model.Objective
import com.Rirsefu3.Degmed.domain.model.SpectrumBand
import com.Rirsefu3.Degmed.domain.model.Tile
import com.Rirsefu3.Degmed.domain.model.TileModifier

object SampleData {

    private fun band(code: Char): SpectrumBand = when (code) {
        'V' -> SpectrumBand.VIOLET
        'C' -> SpectrumBand.CYAN
        'B' -> SpectrumBand.BRASS
        else -> SpectrumBand.JADE
    }

    private fun modifier(code: Char): TileModifier = when (code) {
        'P' -> TileModifier.PRISM
        'G' -> TileModifier.COG
        'A' -> TileModifier.ANCHOR
        'F' -> TileModifier.FLUX
        else -> TileModifier.NONE
    }

    private fun decode(values: String, bands: String, mods: String): BoardState {
        val tiles = values.indices.map { i ->
            Tile(values[i] - '0', band(bands[i]), modifier(mods[i]))
        }
        return BoardState(GameConfig.BOARD_COLS, GameConfig.BOARD_ROWS, tiles)
    }

    val sectors: List<LevelDefinition> = listOf(
        LevelDefinition(
            id = 1,
            titleRes = R.string.sector_1,
            expert = false,
            moveLimit = 18,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 4)),
            board = decode(
                "1375971" +
                "3139953" +
                "1349613" +
                "1212523" +
                "3785216",
                "BVCBBVJ" +
                "BVCCVJC" +
                "JVVJJBB" +
                "CCBCJBB" +
                "VJJCCBB",
                "......." +
                "......." +
                "......." +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 2,
            titleRes = R.string.sector_2,
            expert = false,
            moveLimit = 17,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.VIOLET, 4)),
            board = decode(
                "6826916" +
                "5985651" +
                "5321243" +
                "8825192" +
                "3779788",
                "BJVVJBJ" +
                "BJBCVVJ" +
                "CBVBVVJ" +
                "JBBJBBJ" +
                "VBCCCCB",
                "......." +
                "......." +
                "......." +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 3,
            titleRes = R.string.sector_3,
            expert = false,
            moveLimit = 16,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.VIOLET, 3), Objective.TripleMatch(7)),
            board = decode(
                "1242424" +
                "7979377" +
                "1757445" +
                "3212967" +
                "3418161",
                "BVCCVVB" +
                "JBBJVVJ" +
                "JVVJBBV" +
                "CVBJVBV" +
                "BCVCBVV",
                "......." +
                "......." +
                "......." +
                "......." +
                ".....P."
            )
        ),
        LevelDefinition(
            id = 4,
            titleRes = R.string.sector_4,
            expert = false,
            moveLimit = 16,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 3), Objective.TripleMatch(2)),
            board = decode(
                "7738425" +
                "6984456" +
                "2182265" +
                "3396239" +
                "7349913",
                "JBJCJBJ" +
                "BCVVBJJ" +
                "BCBJBCV" +
                "BCBCCVV" +
                "BCJBVBJ",
                "......." +
                "......P" +
                "......." +
                "....A.." +
                "......."
            )
        ),
        LevelDefinition(
            id = 5,
            titleRes = R.string.sector_5,
            expert = false,
            moveLimit = 15,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.JADE, 5), Objective.TripleMatch(9)),
            board = decode(
                "4377927" +
                "8435799" +
                "6679853" +
                "8826987" +
                "5136472",
                "BVBJJJB" +
                "JCJVCJJ" +
                "VBVBCVC" +
                "VJCVBCB" +
                "VJCVJJB",
                ".....G." +
                "......." +
                "......P" +
                "......." +
                "...A..."
            )
        ),
        LevelDefinition(
            id = 6,
            titleRes = R.string.sector_6,
            expert = false,
            moveLimit = 15,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 4), Objective.TripleMatch(3)),
            board = decode(
                "1393726" +
                "3654416" +
                "6698517" +
                "3831693" +
                "6874889",
                "CBCVBVB" +
                "JBCBBVC" +
                "CCCVBBC" +
                "JBVJBJC" +
                "CJBBBCB",
                "F......" +
                "......." +
                "......." +
                ".....A." +
                ".P....."
            )
        ),
        LevelDefinition(
            id = 7,
            titleRes = R.string.sector_7,
            expert = false,
            moveLimit = 14,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 3), Objective.TripleMatch(7), Objective.SumLine(39)),
            board = decode(
                "3394692" +
                "7744844" +
                "5519732" +
                "7657279" +
                "1936273",
                "VJBJJVB" +
                "BBCCJCB" +
                "BVBBVJC" +
                "CVBCJCJ" +
                "CJVCCBB",
                "F......" +
                ".P....." +
                "..G...." +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 8,
            titleRes = R.string.sector_8,
            expert = false,
            moveLimit = 14,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 3), Objective.TripleMatch(7), Objective.SumLine(23)),
            board = decode(
                "5867361" +
                "2373713" +
                "3316826" +
                "7885858" +
                "2437192",
                "BBCCBJV" +
                "CCJJCBC" +
                "BVCCVBJ" +
                "JJCJBJB" +
                "BJVJVVB",
                "A.G...." +
                "......." +
                "......F" +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 9,
            titleRes = R.string.sector_9,
            expert = false,
            moveLimit = 13,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 3), Objective.TripleMatch(3), Objective.SumLine(41)),
            board = decode(
                "5163461" +
                "4639326" +
                "2374258" +
                "4558932" +
                "5447789",
                "BBVJJVC" +
                "BCJJVVJ" +
                "VBVJVBB" +
                "CJBVJVC" +
                "BVBJJCV",
                "......." +
                ".G....." +
                ".F....." +
                "......." +
                ".P...A."
            )
        ),
        LevelDefinition(
            id = 10,
            titleRes = R.string.sector_10,
            expert = false,
            moveLimit = 13,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.CYAN, 3), Objective.TripleMatch(5), Objective.SumLine(35)),
            board = decode(
                "2831555" +
                "7517638" +
                "4531738" +
                "4566633" +
                "7669469",
                "JBCCJBV" +
                "JBVJBBJ" +
                "JBCVBBJ" +
                "JCJVVJC" +
                "BCBVCCC",
                "......." +
                "......." +
                "......G" +
                "....A.." +
                "......F"
            )
        ),
        LevelDefinition(
            id = 11,
            titleRes = R.string.sector_11,
            expert = false,
            moveLimit = 12,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.CYAN, 4), Objective.TripleMatch(9), Objective.SumLine(45)),
            board = decode(
                "8399333" +
                "9237755" +
                "4897999" +
                "9397954" +
                "7819959",
                "CCVCCCB" +
                "JCBBBVB" +
                "BVCCJCJ" +
                "VBCBVVC" +
                "CCBVCCJ",
                "......." +
                "......." +
                "..G...." +
                ".P....A" +
                "......F"
            )
        ),
        LevelDefinition(
            id = 12,
            titleRes = R.string.sector_12,
            expert = false,
            moveLimit = 12,
            driftLimit = 3,
            allowRewind = true,
            starsRequired = 0,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.VIOLET, 4), Objective.TripleMatch(7), Objective.SumLine(37)),
            board = decode(
                "9887213" +
                "8923761" +
                "7576785" +
                "8842883" +
                "4325888",
                "VJVCJJB" +
                "VCVCBJC" +
                "JBBCVBJ" +
                "CVVCCVV" +
                "BCVVCCJ",
                "....F.." +
                "...A..." +
                "......." +
                "P......" +
                "....G.."
            )
        )
    )

    val expertFallbacks: List<LevelDefinition> = listOf(
        LevelDefinition(
            id = 13,
            titleRes = R.string.expert_1,
            expert = true,
            moveLimit = 11,
            driftLimit = 2,
            allowRewind = false,
            starsRequired = 6,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 4), Objective.TripleMatch(7), Objective.SumLine(38)),
            board = decode(
                "5997591" +
                "5748345" +
                "1271796" +
                "3156788" +
                "1727454",
                "BVBBVJV" +
                "CJCCCCC" +
                "BBVBCJJ" +
                "CCVCCCJ" +
                "BBVJJBB",
                "......." +
                "......." +
                "..P...." +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 14,
            titleRes = R.string.expert_2,
            expert = true,
            moveLimit = 11,
            driftLimit = 2,
            allowRewind = false,
            starsRequired = 12,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.CYAN, 3), Objective.TripleMatch(3), Objective.SumLine(36)),
            board = decode(
                "4914558" +
                "9935821" +
                "4348662" +
                "9573496" +
                "8938592",
                "JCBJCCV" +
                "CBJJVBC" +
                "JBCJBCC" +
                "BVCBJBV" +
                "CBJCBCJ",
                "......." +
                "......." +
                "......." +
                ".P....." +
                "......."
            )
        ),
        LevelDefinition(
            id = 15,
            titleRes = R.string.expert_3,
            expert = true,
            moveLimit = 10,
            driftLimit = 2,
            allowRewind = false,
            starsRequired = 18,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.CYAN, 3), Objective.TripleMatch(6), Objective.SumLine(30)),
            board = decode(
                "9985719" +
                "8415687" +
                "1267515" +
                "6692679" +
                "2358645",
                "CVBCCJJ" +
                "JCCJCBJ" +
                "CCJBBCV" +
                "VBVCJVV" +
                "VVCCBVC",
                "......." +
                "......." +
                ".....P." +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 16,
            titleRes = R.string.expert_4,
            expert = true,
            moveLimit = 10,
            driftLimit = 2,
            allowRewind = false,
            starsRequired = 24,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.CYAN, 4), Objective.TripleMatch(7), Objective.SumLine(53)),
            board = decode(
                "8917987" +
                "6428952" +
                "8325192" +
                "5779467" +
                "4716687",
                "JVVJVVV" +
                "VBVCCCB" +
                "VVBBCVB" +
                "VCJVCJC" +
                "CVCVJJJ",
                "......." +
                ".P....." +
                "......." +
                "......." +
                "......."
            )
        ),
        LevelDefinition(
            id = 17,
            titleRes = R.string.expert_5,
            expert = true,
            moveLimit = 9,
            driftLimit = 2,
            allowRewind = false,
            starsRequired = 30,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.BRASS, 4), Objective.TripleMatch(7), Objective.SumLine(49)),
            board = decode(
                "7739157" +
                "2717732" +
                "7996232" +
                "7779181" +
                "4422968",
                "JJBBJJC" +
                "BJBBCJJ" +
                "CBBBCJV" +
                "BBBCVJJ" +
                "VVBCCBB",
                "......." +
                "......." +
                "......." +
                "......." +
                "......P"
            )
        ),
        LevelDefinition(
            id = 18,
            titleRes = R.string.expert_6,
            expert = true,
            moveLimit = 9,
            driftLimit = 2,
            allowRewind = false,
            starsRequired = 33,
            objectives = listOf(Objective.SpectrumRun(SpectrumBand.CYAN, 4), Objective.TripleMatch(2), Objective.SumLine(31)),
            board = decode(
                "3652887" +
                "4718278" +
                "1763876" +
                "6765468" +
                "6323211",
                "JCBCBCC" +
                "JCJVJVJ" +
                "VVCCBBJ" +
                "BVBVBBC" +
                "VBCCBCJ",
                "......." +
                "P......" +
                "......." +
                "......." +
                "......."
            )
        )
    )
}
