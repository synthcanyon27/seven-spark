package com.Rirsefu3.Degmed.domain.model

sealed interface Objective {

    val reward: Int

    data class SumLine(val target: Int) : Objective {
        override val reward: Int get() = 60
    }

    data class TripleMatch(val value: Int) : Objective {
        override val reward: Int get() = 30
    }

    data class SpectrumRun(val band: SpectrumBand, val length: Int) : Objective {
        override val reward: Int get() = 40
    }
}

data class ObjectiveProgress(
    val objective: Objective,
    val current: Int,
    val required: Int,
    val completed: Boolean
)
