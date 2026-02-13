package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.DiscussionData
import com.example.schoolmanagement.Domain.Repository.DiscussionRepository
import kotlinx.coroutines.flow.first

class GetDiscussionUseCase(
    private val repository: DiscussionRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(homeworkId: Int): Result<List<DiscussionData>> {
        val token = prefs.getAuthToken.first() ?: ""
        return repository.getDiscussions(token, homeworkId)
    }
}