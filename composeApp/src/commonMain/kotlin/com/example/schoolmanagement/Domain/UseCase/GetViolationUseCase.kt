package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.ViolationData
import com.example.schoolmanagement.Domain.Repository.ViolationRepository
import kotlinx.coroutines.flow.first

class GetViolationUseCase (
    private val repository: ViolationRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<List<ViolationData>>{
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getViolations(token)
    }
}