package com.Rirsefu3.Degmed.data.repository

import com.Rirsefu3.Degmed.data.sample.SampleData
import com.Rirsefu3.Degmed.domain.model.LevelDefinition
import com.Rirsefu3.Degmed.domain.repository.LevelRepository
import com.Rirsefu3.Degmed.domain.usecase.GenerateExpertLevelUseCase

class LevelRepositoryImpl(
    private val generateExpert: GenerateExpertLevelUseCase
) : LevelRepository {

    override fun sectors(): List<LevelDefinition> = SampleData.sectors

    override fun expertContracts(): List<LevelDefinition> = SampleData.expertFallbacks

    override fun all(): List<LevelDefinition> = SampleData.sectors + SampleData.expertFallbacks

    override fun level(id: Int, attempt: Int): LevelDefinition {
        val base = all().firstOrNull { it.id == id } ?: SampleData.sectors.first()
        return if (base.expert) generateExpert(base, attempt) else base
    }

    override fun nextLevelId(id: Int): Int? {
        val ordered = all()
        val index = ordered.indexOfFirst { it.id == id }
        if (index < 0 || index + 1 >= ordered.size) return null
        return ordered[index + 1].id
    }
}
