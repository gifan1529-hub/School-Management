package com.example.schoolmanagement.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schoolmanagement.Data.Remote.HomeWorkResponse
import com.example.schoolmanagement.Domain.UseCase.DeleteHomeWork
import com.example.schoolmanagement.Domain.UseCase.GetHomeWorkDetailUseCase
import com.example.schoolmanagement.Domain.UseCase.GetHomeWorkUseCase
import com.example.schoolmanagement.Domain.UseCase.PostHomeWorkUseCase
import com.example.schoolmanagement.Domain.UseCase.SubmitHomeworkUseCase
import com.example.schoolmanagement.Utils.HandleException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeWorkViewModel (
    private val submitHomeworkUseCase: SubmitHomeworkUseCase,
    private val getHomeWorkUseCase: GetHomeWorkUseCase,
    private val postHomeWorkUseCase: PostHomeWorkUseCase,
    private val deleteHomeWorkUseCase: DeleteHomeWork,
    private val getHomeWorkDetailUseCase: GetHomeWorkDetailUseCase
): ViewModel() {
    private val _homeworkList = MutableStateFlow<List<HomeWorkResponse>>(emptyList())
    val homeworkList: StateFlow<List<HomeWorkResponse>> = _homeworkList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _isAddSuccess = MutableStateFlow(false)
    val isAddSuccess: StateFlow<Boolean> = _isAddSuccess

    private val _isSubmitSuccess = MutableStateFlow(false)
    val isSubmitSuccess: StateFlow<Boolean> = _isSubmitSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _errorLoadHomeworks = MutableStateFlow<String?>(null)
    val errorLoadHomeworks: StateFlow<String?> = _errorLoadHomeworks

    private val _errorAddHomework = MutableStateFlow<String?>(null)
    val errorAddHomeworks: StateFlow<String?> = _errorAddHomework

    private val _errorSubmitHomework = MutableStateFlow<String?>(null)
    val errorSubmitHomeworks: StateFlow<String?> = _errorSubmitHomework

    private val _errorDeleteHomework = MutableStateFlow<String?>(null)
    val errorDeleteHomeworks: StateFlow<String?> = _errorDeleteHomework

    private val _selectedHomeworkDetail = MutableStateFlow<HomeWorkResponse?>(null)
    val selectedHomeworkDetail: StateFlow<HomeWorkResponse?> = _selectedHomeworkDetail

    private val exceptionHandler = HandleException()


    init {
        loadHomeworks()
    }

    fun loadHomeworks(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = getHomeWorkUseCase()
                result.onSuccess { data ->
                    println("DEBUG GURU: Berhasil ambil ${data.size} tugas")
                    _homeworkList.value = data
                }.onFailure { e ->
                    val handled = exceptionHandler.handleException(e as Exception)
                    _errorLoadHomeworks.value = handled.message
                }
            } catch (e: Exception) {
            _errorLoadHomeworks.value = "Terjadi kesalahan sistem ${e.message}"
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
                _errorAddHomework.value = "Harap isi semua kolom wajib"
                return@launch
            }

            _isLoading.value = true
            val result = postHomeWorkUseCase(subject, targetClass, title, description, deadline)

            result.onSuccess {
                _isAddSuccess.value = true
                loadHomeworks()
            }.onFailure { e ->
                val handled = exceptionHandler.handleException(e as Exception)
                _errorAddHomework.value = handled.message
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
                _errorDeleteHomework.value = handled.message
            }
            _isLoading.value = false
        }
    }

    fun submitHomework(
        homeworkId: Int,
        fileBytes: ByteArray,
        fileName: String
    ){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = submitHomeworkUseCase(homeworkId, fileBytes, fileName)
                result.onSuccess {
                    _isSubmitSuccess.value = true
                    loadHomeworks()
                }.onFailure { e ->
                    val handled = exceptionHandler.handleException(e as Exception)
                    _errorSubmitHomework.value = handled.message
                }
            } catch (e: Exception) {
                _errorSubmitHomework.value = "Gagal mengunggah file"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun loadHomeworkDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            getHomeWorkDetailUseCase(id).onSuccess {
                _selectedHomeworkDetail.value = it
            }.onFailure { e ->
                _errorMessage.value = e.message
            }
            _isLoading.value = false
        }
    }

    fun resetState() {
        _isSuccess.value = false
        _isAddSuccess.value = false
        _isSubmitSuccess.value = false
        _errorMessage.value = null
        _errorLoadHomeworks.value = null
        _errorAddHomework.value = null
        _errorSubmitHomework.value = null
        _errorDeleteHomework.value = null
    }
}