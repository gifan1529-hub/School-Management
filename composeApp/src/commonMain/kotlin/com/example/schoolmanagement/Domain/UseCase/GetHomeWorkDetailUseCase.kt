package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse
import com.example.schoolmanagement.Domain.Repository.HomeWorkRepository
import kotlinx.coroutines.flow.first

class GetHomeWorkDetailUseCase (
    private val repository: HomeWorkRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(homeworkId: Int): Result<HomeWorkResponse> {
        val token = prefsManager.getAuthToken.first() ?: ""
        return repository.getHomeworkDetail(token, homeworkId)
    }
}