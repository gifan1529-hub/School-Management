package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.GradeDetailData
import com.example.schoolmanagement.Domain.Repository.GradeRepository
import kotlinx.coroutines.flow.first

class GetMyGradesUseCase (
    private val repository: GradeRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(subject: String? = null): Result<List<GradeDetailData>>{
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getMyGrades(token, subject)
    }
}