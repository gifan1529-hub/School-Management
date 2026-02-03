package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.PermitData
import com.example.schoolmanagement.Domain.Repository.PermitRepository
import kotlinx.coroutines.flow.first

class GetPermitHistoryUseCase (
    private val repository: PermitRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(all: Boolean = false): Result<List<PermitData>> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getPermits(token, all)
    }
}