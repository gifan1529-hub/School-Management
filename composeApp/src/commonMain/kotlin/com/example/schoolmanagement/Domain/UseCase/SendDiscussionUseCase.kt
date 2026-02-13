package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Repository.DiscussionRepository
import kotlinx.coroutines.flow.first

class SendDiscussionUseCase (
    private val repository: DiscussionRepository,
    private val prefs: PrefsManager
) {
    suspend operator fun invoke(homeworkId: Int, message: String): Result<Boolean> {
        val token = prefs.getAuthToken.first() ?: ""
        return repository.sendDiscussion(token, homeworkId, message)
    }
}