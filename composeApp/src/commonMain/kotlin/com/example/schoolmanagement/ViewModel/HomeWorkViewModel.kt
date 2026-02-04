package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse
import com.example.schoolmanagement.Domain.UseCase.DeleteHomeWork
import com.example.schoolmanagement.Domain.UseCase.GetHomeWorkUseCase
import com.example.schoolmanagement.Domain.UseCase.PostHomeWorkUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeWorkViewModel (
    private val getHomeWorkUseCase: GetHomeWorkUseCase,
    private val postHomeWorkUseCase: PostHomeWorkUseCase,
    private val deleteHomeWorkUseCase: DeleteHomeWork
): ViewModel() {
    private val _homeworkList = MutableStateFlow<List<HomeWorkResponse>>(emptyList())
    val homeworkList: StateFlow<List<HomeWorkResponse>> = _homeworkList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val exceptionHandler = HandleException()


    init {
        loadHomeworks()
    }

    fun loadHomeworks(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = getHomeWorkUseCase()
                result.onSuccess {
                    _homeworkList.value = it
                }.onFailure { e ->
                    val handled = exceptionHandler.handleException(e as Exception)
                    _errorMessage.value = handled.message
                }
            } catch (e: Exception) {
            _errorMessage.value = "Terjadi kesalahan sistem"
        } finally {
            _isLoading.value = false
        }
        }
    }

    fun addHomework(
        subject: String,
        targetClass: String,
        title: String,
        description: String,
        deadline: String
    ) {
        viewModelScope.launch {
            if (subject.isBlank() || title.isBlank() || deadline.isBlank()) {
                _errorMessage.value = "Harap isi semua kolom wajib"
                return@launch
            }

            _isLoading.value = true
            val result = postHomeWorkUseCase(subject, targetClass, title, description, deadline)

            result.onSuccess {
                _isSuccess.value = true
                loadHomeworks()
            }.onFailure { e ->
                val handled = exceptionHandler.handleException(e as Exception)
                _errorMessage.value = handled.message
            }
            _isLoading.value = false
        }
    }

    fun deletePR(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = deleteHomeWorkUseCase(id)

            result.onSuccess {
                loadHomeworks()
            }.onFailure { e ->
                val handled = exceptionHandler.handleException(e as Exception)
                _errorMessage.value = handled.message
            }
            _isLoading.value = false
        }
    }

    fun resetState() {
        _isSuccess.value = false
        _errorMessage.value = null
    }
}