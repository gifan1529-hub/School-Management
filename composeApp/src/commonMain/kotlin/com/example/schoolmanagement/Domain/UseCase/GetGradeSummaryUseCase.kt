package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.GradeSummaryData
import com.example.schoolmanagement.Domain.Repository.GradeRepository
import kotlinx.coroutines.flow.first

class GetGradeSummaryUseCase (
    private val repository: GradeRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(): Result<GradeSummaryData> {
        val token = prefs.getAuthToken.first() ?: ""
        return repository.getGradeSummary(token)
    }
}