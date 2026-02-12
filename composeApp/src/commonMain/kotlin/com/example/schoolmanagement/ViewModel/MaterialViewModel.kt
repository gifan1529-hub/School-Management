package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Local.PrefsManager
import com.example.schoolmanagement.Domain.Model.MaterialData
import com.example.schoolmanagement.Domain.Repository.MaterialRepository
import com.example.schoolmanagement.Domain.UseCase.GetMaterialUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MaterialViewModel (
    private val getMaterialUseCase: GetMaterialUseCase
): ViewModel() {
    private val _materials = MutableStateFlow<List<MaterialData>>(emptyList())
    val materials: StateFlow<List<MaterialData>> = _materials.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val exceptionHandler = HandleException()

    init {
        loadMaterials()
    }

    fun loadMaterials() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

                val result = getMaterialUseCase.invoke()

                result.onSuccess { data ->
                    _materials.value = data
                }.onFailure { e ->
                    val handled = exceptionHandler.handleException(e as Exception)
                    _errorMessage.value = handled.message
                }
            _isLoading.value = false
        }
    }
}