package com.example.schoolmanagement.Domain.UseCase

import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.MaterialData
import com.example.schoolmanagement.Domain.Repository.MaterialRepository
import kotlinx.coroutines.flow.first

class GetMaterialsUseCase (
    private val materialRepository: MaterialRepository,
    private val prefsManager: PrefsManager
) {
    suspend operator fun invoke(): Result<List<MaterialData>> {
        return try {
            val token = prefsManager.getAuthToken.first() ?: ""

            if (token.isEmpty()) {
                return Result.failure(Exception("Sesi berakhir, silakan login ulang"))
            }

            return materialRepository.getMaterials(token)
        } catch (e : Exception) {
            Result.failure(e)
        }
    }
}